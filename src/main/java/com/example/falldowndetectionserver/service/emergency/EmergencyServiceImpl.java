package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.aligo.AligoSendSMSResponseDTO;
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



        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", smsUtil.getKey())
                .addFormDataPart("user_id", smsUtil.getUser_id())
                .addFormDataPart("sender", smsUtil.getSender())
                .addFormDataPart("receiver", receiver)
                .addFormDataPart("msg", "테스트 메시지")
                .addFormDataPart("msg_type", "SMS")
                // 테스트 할 때는 주석 해제 해야 함
                .addFormDataPart("testmode_yn", "Y")
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

    }
}
