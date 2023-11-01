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

    /**
     * 위급상황 SMS를 전송한다.
     */
    @Override
    public void sendEmergencySMS(String cameraId) {
        String requestURI = smsUtil.getSendRequestURI();
        RequestBody body = makeRequestBody(cameraId);
        AligoSendSMSResponseDTO responseDTO;

        OkHttpClient client = new OkHttpClient().newBuilder().build();

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
    }

    /**
     * 위급상황을 해제 한다.
     * FallDownDetector에 설정된 Flag들을 수정하고, 일반 감지 상태로 되돌린다.
     * @param cameraId
     */
    @Override
    public void emergencyRelease(String cameraId) {
        fallDownDetector.getFallDownTimeHash().remove(cameraId);
        fallDownDetector.getPositionHash().get(cameraId).clear();
        fallDownDetector.getEmergencyFlagHash().replace(cameraId, false);
        fallDownDetector.getFallDownFlagHash().replace(cameraId, false);

        // TODO: 라즈베리 파이 소리 끄기 - 소리 못 낼듯
    }

    /**
     * Aligo SMS 전송 요청 Body를 생성하여 리턴한다.
     */
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
                .addFormDataPart("testmode_yn", "Y")
                .build();

        return body;
    }

    /**
     * 보호자 연락저를 조회하여 ,로 구분된 문자열로 만든 후 리턴한다.
     */
    private String getNokphones(String cameraId) {
        String receiver;

        List<String> nokphones = nokPhoneDao.selectAll(cameraId);
        receiver = nokphones.get(0);

        if (nokphones.size() > 1) {
            receiver = receiver + "," + nokphones.get(1);
        }

        return receiver;
    }

    /**
     * SMS로 전송할 메시지 본문을 생성하여 리턴한다.
     */
    private String makeMessage(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElseThrow();

        String message = "[ 긴급상황 감지 시스템 ]" + "\n(" +
                userVO.getUserName() + " / " + userVO.getUserGender() + " / " + userVO.getUserAge() + "세) 위급상황이 발생했습니다." +
                "아래 링크에 접속하셔서 " + cameraId + "를 입력하시고, 상황을 확인하세요.\n" +
                "링크 주소";

        return message;
    }
}
