package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.aligo.AligoSendSMSResponseDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import com.example.falldowndetectionserver.utils.AligoSmsUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyServiceImpl implements EmergencyService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final FallDownDetector fallDownDetector;
    private final AligoSmsUtil smsUtil;
    private final Gson gson;

    @Override
    public ResponseEntity<AligoSendSMSResponseDTO> sendEmergencySMS(String cameraId) {
        String requestURI = smsUtil.getSendRequestURI();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = makeRequestBody(cameraId);
        AligoSendSMSResponseDTO responseDTO;

        Request request = new Request.Builder()
                .url(requestURI)
                .method("POST", body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            responseDTO = gson.fromJson(response.body().string(), AligoSendSMSResponseDTO.class);
            log.info(responseDTO.getSuccess_cnt() + "");
            // TODO: 문자 여유가 된다면, 전송 실패 시 재전송 로직 추가
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void emergencyRelease(String cameraId) {
        fallDownDetector.getFallDownTimeHash().remove(cameraId);
        fallDownDetector.getPositionHash().get(cameraId).clear();
        fallDownDetector.getEmergencyFlagHash().replace(cameraId, false);
        fallDownDetector.getFallDownFlagHash().replace(cameraId, false);

        // TODO: 라즈베리 파이 소리 끄기
    }

    private RequestBody makeRequestBody(String cameraId) {
        String receiver = getNokphones(cameraId);
        String message = makeMessage(cameraId);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", smsUtil.getKey())
                .addFormDataPart("user_id", smsUtil.getUser_id())
                .addFormDataPart("sender", smsUtil.getSender())
                .addFormDataPart("receiver", receiver)
                .addFormDataPart("msg", message)
                .addFormDataPart("msg_type", "LMS")
                // 테스트 할 때는 주석 해제 해야 함
//                .addFormDataPart("testmode_yn", "Y")
                .build();

        return body;
    }

    private String getNokphones(String cameraId) {
        String receiver;

        List<String> nokphones = nokPhoneDao.selectAll(cameraId);
        receiver = nokphones.get(0);

        if (nokphones.size() > 1) {
            receiver = receiver + "," + nokphones.get(1);
        }

        return receiver;
    }

    private String makeMessage(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElseThrow();

        String message = "[ 긴급상황 감지 시스템 ]" + "\n(" +
                userVO.getUserName() + " / " + userVO.getUserGender() + " / " + userVO.getUserAge() + "세) 위급상황이 발생했습니다." +
                "아래 링크에 접속하셔서 " + cameraId + "를 입력하시고, 상황을 확인하세요." +
                "링크 넣어야 함";
        // TODO: 넘어지는 영상 재생하는 화면 만들어야 함

        return message;
    }
}
