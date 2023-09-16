import cv2
import asyncio
import websockets
import json


CONNECTION_INFO = json.dumps({
    "camera_id": "cam01",
    "identifier": "sender"
})

EXTRA_HEADER = {
    "camera_id": "cam01"
}


# WebSocket을 통해 서버로 연결하고, Opencv를 이용해 웹캠으로 찍은 영상을 프레임 단위로 전송
async def send_video_and_position():
    # localhost:10000/video 로 연결한 WebSocket을 websocket이라는 이름으로 사용
    async with websockets.connect("ws://localhost:10000/video", extra_headers=EXTRA_HEADER) as websocket:
        # 연결되면 연결 정보를 TextMessage로 보냄
        await websocket.send(CONNECTION_INFO)
        # 웹캡으로 영상 캡쳐
        cap = cv2.VideoCapture(0)
        # 웹캡이 켜져 있는동안 반복
        while cap.isOpened():
            # 프레임 단위로 영상 읽음
            ret, frame = cap.read()
            if not ret:
                break
            # jpg 이미지 형태로 인코딩
            _, encoded_image = cv2.imencode('.jpg', frame)
            # 이미지를 byte 형식으로 변환
            encoded_image = encoded_image.tobytes()
            # 서버로 전송
            await websocket.send(encoded_image)


if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(send_video_and_position())
