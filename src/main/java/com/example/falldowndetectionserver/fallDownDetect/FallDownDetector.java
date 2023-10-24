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

    private boolean fallDownFlag = false;
    private boolean emergencyFlag = false;

    public void checkFallDown(String cameraId, @NotNull PositionVO positionVO) {
        float ratio;

        try {
            ratio = (float) (positionVO.getMax_x() - positionVO.getMin_x()) / (positionVO.getMax_y() - positionVO.getMin_y());
            positionVO.setRatio(ratio);
            if (ratio >= 2.0) {
                log.info("ratio : " + ratio);
//                checkEmergency(cameraId, positionVO);
                fallDownFlag = true;
            } else {
                log.info("ratio : " + ratio);
                positionHash.get(cameraId).clear();
                fallDownFlag = false;
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }

        if (fallDownFlag) {
            checkEmergency(cameraId, positionVO);
        }
    }

    private void checkEmergency(String cameraId, PositionVO positionVO) {
        positionHash.get(cameraId).add(positionVO);

        if (positionHash.get(cameraId).size() >= 30) {
            try {
                firebaseMessageService.sendMessageTo(uPTokenDao.select(cameraId).get(), cameraId, "dangerous");
                emergencyFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 비율이 0.5 이상 변했을 경우
            if (positionHash.get(cameraId).peek().getRatio() - positionVO.getRatio() > 0.5 ||
                    positionVO.getRatio() - positionHash.get(cameraId).peek().getRatio() < -0.5) {
                positionHash.get(cameraId).clear();
                fallDownFlag = false;
                emergencyFlag = false;
            }
        }
    }
}
