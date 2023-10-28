package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.aligo.AligoSendSMSRequestDTO;
import com.example.falldowndetectionserver.domain.dto.aligo.AligoSendSMSResponseDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import com.example.falldowndetectionserver.utils.AligoSmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyServiceImpl implements EmergencyService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final FallDownDetector fallDownDetector;
    private final AligoSmsUtil smsUtil;

    @Override
    public ResponseEntity<AligoSendSMSResponseDTO> sendEmergencySMS(String cameraId) {
        String requestURI = smsUtil.getSendRequestURI();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = makeMessgage(cameraId);

        Request request = new Request.Builder()
                .url(requestURI)
                .method("POST", body)
                .build();

        try {
            Response response = client.newCall(request).execute();
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

    private RequestBody makeMessgage(String cameraId) {
        String receiver;

        List<String> nokphones = nokPhoneDao.selectAll(cameraId);
        receiver = nokphones.get(0);

        if (nokphones.size() > 1) {
            receiver = receiver + "," + nokphones.get(1);
        }

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", smsUtil.getKey())
                .addFormDataPart("user_id", smsUtil.getUser_id())
                .addFormDataPart("sender", smsUtil.getSender())
                .addFormDataPart("receiver", receiver)
                .addFormDataPart("msg", "테스트 메시지")
                .addFormDataPart("msg_type", "SMS")
                // 테스트 할 때는 주석 해제 해야 함
//                .addFormDataPart("testmode_yn", "Y")
                .build();

        return body;
    }
}
