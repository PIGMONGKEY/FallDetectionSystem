package com.example.falldowndetectionserver.fallDownDetect;

import com.example.falldowndetectionserver.domain.vo.PositionVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
@Slf4j
public class FallDownDetector {
    private String cameraId;
    private Queue<PositionVO> positionVOQueue;
    private final int FALL_DOWN = 1;
    private final int NORMAL = 0;
    private final int ERROR = -1;

    public static FallDownDetector getNewDetector(String cameraId) {
        FallDownDetector detector = new FallDownDetector();
        detector.setCameraId(cameraId);
        detector.setPositionVOQueue(new LinkedList<>());

        return detector;
    }

    public void pushPosition(PositionVO positionVO) {
        int checkCode = checkFallDown(positionVO);
        if (checkCode == NORMAL) {
            if (this.positionVOQueue.size() >= 60) {
                this.positionVOQueue.remove();
                this.positionVOQueue.add(positionVO);
            } else {
                this.positionVOQueue.add(positionVO);
            }
            log.info("normal");
        } else if (checkCode == FALL_DOWN){
            // 넘어졌다고 인식한 경우임 ---------------------------------------------------------------
            log.info("fall down");
        } else {
            // 한 개의 Keypoint만 인식해서 0으로 나눗셈을 실행하는 에러가 뜨는 경우
            log.info("not enough detected keypoints");
        }
    }

    private int checkFallDown(PositionVO positionVO) {
        float ratio;
        try {
            ratio = (positionVO.getMax_x() - positionVO.getMin_x()) / (positionVO.getMax_y() - positionVO.getMin_y());
            if (ratio >= 1.0) {
                return FALL_DOWN;
            } else {
                return NORMAL;
            }
        } catch (ArithmeticException e) {
            return ERROR;
        }
    }
}
