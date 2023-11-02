package com.example.falldowndetectionserver.fallDownDetect;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.vo.PositionVO;
import com.example.falldowndetectionserver.handler.VideoWebSocketHandler;
import com.example.falldowndetectionserver.service.FirebaseMessageService;
import com.mysql.cj.util.TimeUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * 넘어짐과 위급상황을 감지하고 판단하는 클래스
 */
@Getter
@RequiredArgsConstructor
@Component
@Slf4j
public class FallDownDetector {
    private final FirebaseMessageService firebaseMessageService;
    private final UPTokenDao uPTokenDao;
    private final VideoWebSocketHandler videoWebSocketHandler;

    // CameraId / List<PositionVO>
    private final HashMap<String, List<PositionVO>> positionHash = new HashMap<>();
    // CameraId / 넘어진 시간
    private final HashMap<String, Long> fallDownTimeHash = new HashMap<>();
    // CameraId / 넘어짐 Flag
    private final HashMap<String, Boolean> fallDownFlagHash = new HashMap<>();
    // CameraId / 긴급상황 Flag
    private final HashMap<String, Boolean> emergencyFlagHash = new HashMap<>();
    DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD_HH:mm:ss");

    /**
     * 넘어졌는지 확인한다.
     * @param cameraId 카메라 아이디
     * @param positionVO 인식한 각 keypoint 위치 정보
     */
    public void checkFallDown(String cameraId, @NotNull PositionVO positionVO) {
        try {
            // 세로 대비 가로 비율을 구한다.
            positionVO.setRatio((float) (positionVO.getMax_x() - positionVO.getMin_x()) / (positionVO.getMax_y() - positionVO.getMin_y()));
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return;
        }

//        String positionLog = "";
//        int detectedCount = 0;
//
//        for (int i=0; i<17; i++) {
//            positionLog += PositionVO.keypoint[i] + " : " + positionVO.getPosition_x()[i] + "/" + positionVO.getPosition_y()[i] + "-" + positionVO.getPosition_trust()[i] + "\n";
//            if (positionVO.getPosition_trust()[i] > 35) {
//                detectedCount++;
//            }
//        }
//
//        log.info("detected : " + detectedCount + "\n" + positionLog);

        // cameraId의 주인이 위급상황이라면 로직 스킵
        if (emergencyFlagHash.get(cameraId)) {
            log.info("emergency situation");
            return;
        }

        // 이전에 넘어짐을 감지하지 않았을 때
        if (!fallDownFlagHash.get(cameraId)) {
            log.info("not fall down ratio : " + positionVO.getRatio());


            // 넘어짐을 감지
            // 세로 대비 가로 비율이 2.0을 넘음
            if (positionVO.getRatio() >= 2.0) {
                log.info("falldown-----------------------------------------------------");

                // FallDownFlag를 true로 전환
                fallDownFlagHash.replace(cameraId, true);

                // 넘어진 시간을 저장
                fallDownTimeHash.put(cameraId, System.currentTimeMillis());

                // positionHash에 position 저장 시작
                positionHash.get(cameraId).add(positionVO);
                Date date = new Date(fallDownTimeHash.get(cameraId));
                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                // 넘어졌다고 비디오 전송지에 알려줌 - 넘어지는 영상 저장
                videoWebSocketHandler.sendFalldownMessageToWaiter(cameraId, formatter.format(date));
            }
        } else {
        // 이전에 넘어짐을 감지해서 움직임 없음 감지 중일 경우
            log.info("in falldown logic ratio : " + positionVO.getRatio());
            if (positionVO.getRatio() >= 2.0) {
                // 움직임 없음 감지
                moveDetect(cameraId, positionVO);
            } else {
                // 넘어졌었는데 지금 안넘어짐
                fallDownFlagHash.replace(cameraId, false);
                emergencyFlagHash.replace(cameraId, false);
                positionHash.get(cameraId).clear();
                fallDownTimeHash.remove(cameraId);
                log.info("release falldown logic-----------------------------------------------------");
            }
        }
    }

    private void moveDetect(String cameraId, PositionVO positionVO) {
        // 넘어진 지 30초가 됨
        if (System.currentTimeMillis() - fallDownTimeHash.get(cameraId) >= 30000) {
            try {
                // 사용자 핸드폰으로 알림을 울림
                firebaseMessageService.sendMessageTo(uPTokenDao.select(cameraId).get(), "위험!!!", "위험!!!");
                emergencyFlagHash.replace(cameraId, true);
                log.info("EMERGENCY-----------------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        // 넘어진 지 30초가 지나지 않음
            int lastIndex = positionHash.size() - 1;
            float lastRatio = positionHash.get(cameraId).get(lastIndex).getRatio();

            // 1프레임 전과 비교하여 비율이 0.5 이상 달라졌는지 확인
            if (lastRatio - positionVO.getRatio() > 0.5 ||
                    lastRatio - positionVO.getRatio() < -0.5) {
            // 비율이 크게 변할 만큼 움직임이 있다면
            // 넘어짐 감지 로직 해제
                fallDownFlagHash.replace(cameraId, false);
                emergencyFlagHash.replace(cameraId, false);
                positionHash.get(cameraId).clear();
                fallDownTimeHash.remove(cameraId);
                log.info("release falldown logic-----------------------------------------------------");
            }
        }
    }
}
