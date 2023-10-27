import cv2
import asyncio
import websockets
import json
import tensorflow as tf
import tensorflow_hub as hub

CONNECTION_INFO = json.dumps({
    "camera_id": "cam01",
    "identifier": "sender"
})

EXTRA_HEADER = {
    "camera_id": "cam01"
}

print("Loading model...")
model = hub.load(f"./movenet_singlepose_thunder_4")
# model = hub.load(f"./movenet_singlepose_lightning_4")
movenet = model.signatures['serving_default']
print("Model load success")

# 웹캡으로 영상 캡쳐
cap = cv2.VideoCapture("rtsp://{addr}/cam")


# WebSocket을 통해 서버로 연결하고, Opencv를 이용해 웹캠으로 찍은 영상을 프레임 단위로 전송
async def main():
    # localhost:10000/video 로 연결한 WebSocket을 websocket이라는 이름으로 사용
    async with websockets.connect("ws://localhost:10000/position", extra_headers=EXTRA_HEADER) as position_socket:
        async with websockets.connect("ws://localhost:10000/video", extra_headers=EXTRA_HEADER) as video_socket:

            # 연결되면 연결 정보를 TextMessage로 보냄
            # await position_socket.send(CONNECTION_INFO)
            await video_socket.send(CONNECTION_INFO)

            # 웹캡이 켜져 있는동안 반복
            while cap.isOpened():
                # 프레임 단위로 영상 읽음
                ret, frame = cap.read()
                if not ret:
                    break

                position_task = asyncio.create_task(get_position_and_send(frame, position_socket))
                video_task = asyncio.create_task(encode_image_and_send(frame, video_socket))
                await position_task
                await video_task


async def get_position_and_send(frame, position_socket):
    min_x = 99999
    min_y = 99999
    max_x = 0
    max_y = 0

    pose_image = tf.expand_dims(frame, axis=0)
    pose_image = tf.cast(tf.image.resize_with_pad(pose_image, 256, 256), dtype=tf.int32)
    # pose_image = tf.cast(tf.image.resize_with_pad(pose_image, 192, 192), dtype=tf.int32)
    outputs = movenet(pose_image)['output_0']

    for keypoint in outputs.numpy()[0][0]:
        if keypoint[2] <= 0.35:
            continue
        if keypoint[0] <= min_y:
            min_y = keypoint[0]
        if keypoint[0] >= max_y:
            max_y = keypoint[0]
        if keypoint[1] <= min_x:
            min_x = keypoint[1]
        if keypoint[1] >= max_x:
            max_x = keypoint[1]

    position = outputs.numpy()[0][0]

    temp = {
        "position_x": [
            int(position[0][1] * 100),
            int(position[1][1] * 100),
            int(position[2][1] * 100),
            int(position[3][1] * 100),
            int(position[4][1] * 100),
            int(position[5][1] * 100),
            int(position[6][1] * 100),
            int(position[7][1] * 100),
            int(position[8][1] * 100),
            int(position[9][1] * 100),
            int(position[10][1] * 100),
            int(position[11][1] * 100),
            int(position[12][1] * 100),
            int(position[13][1] * 100),
            int(position[14][1] * 100),
            int(position[15][1] * 100),
            int(position[16][1] * 100)
        ],
        "position_y": [
            int(position[0][0] * 100),
            int(position[1][0] * 100),
            int(position[2][0] * 100),
            int(position[3][0] * 100),
            int(position[4][0] * 100),
            int(position[5][0] * 100),
            int(position[6][0] * 100),
            int(position[7][0] * 100),
            int(position[8][0] * 100),
            int(position[9][0] * 100),
            int(position[10][0] * 100),
            int(position[11][0] * 100),
            int(position[12][0] * 100),
            int(position[13][0] * 100),
            int(position[14][0] * 100),
            int(position[15][0] * 100),
            int(position[16][0] * 100)
        ],
        "position_trust": [
            int(position[0][2] * 100),
            int(position[1][2] * 100),
            int(position[2][2] * 100),
            int(position[3][2] * 100),
            int(position[4][2] * 100),
            int(position[5][2] * 100),
            int(position[6][2] * 100),
            int(position[7][2] * 100),
            int(position[8][2] * 100),
            int(position[9][2] * 100),
            int(position[10][2] * 100),
            int(position[11][2] * 100),
            int(position[12][2] * 100),
            int(position[13][2] * 100),
            int(position[14][2] * 100),
            int(position[15][2] * 100),
            int(position[16][2] * 100)
        ],
        "min_x": int(round(min_x, 2) * 100) if min_x != 99999 else -1,
        "max_x": int(round(max_x, 2) * 100) if max_x != 0 else -2,
        "min_y": int(round(min_y, 2) * 100) if min_y != 99999 else -1,
        "max_y": int(round(max_y, 2) * 100) if max_y != 0 else -2
    }

    json_message = json.dumps(temp)
    await position_socket.send(json_message)


async def encode_image_and_send(frame, video_socket):
    _, encoded_image = cv2.imencode('.jpg', frame)
    encoded_image = encoded_image.tobytes()
    await video_socket.send(encoded_image)


if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(main())
