# FallDetectionLaspberryPi

위급상황 감지 시스템 - 라즈베리파이 python 부분

라즈베리파이에서 rtsp 프로토콜을 사용하여 스트리밍 하는 영상을 OpenCV로 읽어옵니다.
이후 MoveNet SinglePose Thunder 모델을 사용하여 영상 속 사람의 자세를 추정합니다.

WebSocket을 통하여 서버로 영상과 모델 적용 결과를 전송합니다.
