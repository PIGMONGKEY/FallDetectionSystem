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
import java.util.List;

@Getter
@RequiredArgsConstructor
@Component
@Slf4j
public class FallDownDetector {
    private final FirebaseMessageService firebaseMessageService;
    private final UPTokenDao uPTokenDao;

    private final HashMap<String, List<PositionVO>> positionHash = new HashMap<>();
    private final HashMap<String, Long> fallDownTimeHash = new HashMap<>();
    private final HashMap<String, Boolean> fallDownFlagHash = new HashMap<>();
    private final HashMap<String, Boolean> emergencyFlagHash = new HashMap<>();

    public void checkFallDown(String cameraId, @NotNull PositionVO positionVO) {
        try {
            positionVO.setRatio((float) (positionVO.getMax_x() - positionVO.getMin_x()) / (positionVO.getMax_y() - positionVO.getMin_y()));
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return;
        }

        String positionLog = "";
        int detectedCount = 0;

        for (int i=0; i<17; i++) {
            positionLog += PositionVO.keypoint[i] + " : " + positionVO.getPosition_x()[i] + "/" + positionVO.getPosition_y()[i] + "-" + positionVO.getPosition_trust()[i] + "\n";
            if (positionVO.getPosition_trust()[i] > 35) {
                detectedCount++;
            }
        }

        log.info("detected : " + detectedCount + "\n" + positionLog);

//        if (emergencyFlagHash.get(cameraId)) {
//            log.info("emergency situation");
//            return;
//        }
//
//        // 이전에 넘어짐을 감지하지 않았을 때
//        if (!fallDownFlagHash.get(cameraId)) {
//            log.info("not fall down ratio : " + positionVO.getRatio());
//
//            // 넘어졌을 때
//            if (positionVO.getRatio() >= 2.0) {
//                log.info("falldownfalldownfalldownfalldownfalldownfalldownfalldown");
//                log.info("falldownfalldownfalldownfalldownfalldownfalldownfalldown");
//                log.info("falldownfalldownfalldownfalldownfalldownfalldownfalldown");
//                log.info("falldownfalldownfalldownfalldownfalldownfalldownfalldown");
//                // FallDownFlag를 true로 전환
//                fallDownFlagHash.replace(cameraId, true);
//                // 넘어진 시간을 저장
//                fallDownTimeHash.put(cameraId, System.currentTimeMillis());
//                // positionHash에 position 저장 시작
//                positionHash.get(cameraId).add(positionVO);
//            }
//        }
//
//        // 이전에 넘어짐을 감지했을 때
//        else {
//            log.info("in falldown logic ratio : " + positionVO.getRatio());
//            if (positionVO.getRatio() >= 2.0) {
//                // 응급flag가 false라면 움직임 없음 감지
//                // true라면 알림 해제 대기
//                if (!emergencyFlagHash.get(cameraId)) {
//                    // 움직임 없음 감지
//                    falldownCheck(cameraId, positionVO);
//                }
//            } else {
//                // 넘어졌었는데 지금 안넘어짐
//                fallDownFlagHash.replace(cameraId, false);
//                emergencyFlagHash.replace(cameraId, false);
//                positionHash.get(cameraId).clear();
//                fallDownTimeHash.remove(cameraId);
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//                log.info("release falldown logic");
//            }
//        }
    }

    private void falldownCheck(String cameraId, PositionVO positionVO) {
        // 넘어진 지 30초가 됨
        if (System.currentTimeMillis() - fallDownTimeHash.get(cameraId) >= 30000) {
            try {
                // 사용자 핸드폰으로 알림을 울림
                firebaseMessageService.sendMessageTo(uPTokenDao.select(cameraId).get(), "위험!!!", "위험!!!");
                emergencyFlagHash.replace(cameraId, true);
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.info("EMERGENCY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            float lastRatio = positionHash.get(cameraId).get(positionHash.get(cameraId).size() - 1).getRatio();

            if (lastRatio - positionVO.getRatio() > 0.5 ||
                    positionVO.getRatio() - lastRatio < -0.5) {
                // 비율이 크게 변할 만큼 움직임
                // 넘어짐 감지 로직 해제
                fallDownFlagHash.replace(cameraId, false);
                emergencyFlagHash.replace(cameraId, false);
                positionHash.get(cameraId).clear();
                fallDownTimeHash.remove(cameraId);
                log.info("release falldown logic");
                log.info("release falldown logic");
                log.info("release falldown logic");
                log.info("release falldown logic");
                log.info("release falldown logic");
                log.info("release falldown logic");
                log.info("release falldown logic");
            } else {
                log.info("still falldown");
            }
        }
    }
}
