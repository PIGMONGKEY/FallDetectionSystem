package com.example.falldowndetectionserver.fallDownDetect;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.vo.PositionVO;
import com.example.falldowndetectionserver.service.FirebaseMessageService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;

@Getter
@RequiredArgsConstructor
@Component
@Slf4j
public class FallDownDetector {
    private final FirebaseMessageService firebaseMessageService;
    private final UPTokenDao uPTokenDao;

    private final HashMap<String, Queue<PositionVO>> positionHash = new HashMap<>();

    public void checkFallDown(String cameraId, @NotNull PositionVO positionVO) {
        float ratio;
        try {
            ratio = (float) (positionVO.getMax_x() - positionVO.getMin_x()) / (positionVO.getMax_y() - positionVO.getMin_y());
            if (ratio >= 1.0) {
                log.info("Body radio : " + ratio + "fall down");
                checkEmergency(cameraId, positionVO);
            } else {
                log.info("Body radio : " + ratio);
                positionHash.get(cameraId).clear();
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

    private void checkEmergency(String cameraId, PositionVO positionVO) {
        positionHash.get(cameraId).add(positionVO);

        // 25 프레임 정도 나옴
        if (positionHash.get(cameraId).size() == 180) {
            try {
                firebaseMessageService.sendMessageTo(uPTokenDao.select(cameraId).get(), cameraId, "dangerous");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
