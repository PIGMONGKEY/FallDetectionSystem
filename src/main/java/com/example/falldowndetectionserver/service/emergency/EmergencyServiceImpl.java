package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSRequestDTO;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSRequestMessageInfo;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSResponseDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public void emergencyRelease(String cameraId) {
        fallDownDetector.getFallDownTimeHash().remove(cameraId);
        fallDownDetector.getPositionHash().get(cameraId).clear();
        fallDownDetector.getEmergencyFlagHash().replace(cameraId, false);
        fallDownDetector.getFallDownFlagHash().replace(cameraId, false);

        // TODO: 라즈베리 파이 소리 끄기
    }
}
