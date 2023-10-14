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

model = hub.load(f"./movenet_singlepose_thunder_4")
movenet = model.signatures['serving_default']

# 웹캡으로 영상 캡쳐
cap = cv2.VideoCapture(0)


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

    pose_image = cv2.resize(frame, (256, 256))
    pose_image = tf.expand_dims(pose_image, axis=0)
    pose_image = tf.cast(tf.image.resize_with_pad(pose_image, 256, 256), dtype=tf.int32)
    outputs = movenet(pose_image)['output_0']

    for keypoint in outputs.numpy()[0][0]:
        for key in keypoint:
            key = float(key)
        if keypoint[2] <= 0.4:
            continue
        if keypoint[0] <= min_y:
            min_y = keypoint[0]
        if keypoint[0] >= max_y:
            max_y = keypoint[0]
        if keypoint[1] <= min_x:
            min_x = keypoint[1]
        if keypoint[1] >= max_x:
            max_x = keypoint[1]

    temp = {
        "position": [
            [1*outputs.numpy()[0][0][0][0], 1*outputs.numpy()[0][0][0][1], 1*outputs.numpy()[0][0][0][2]],
            [1*outputs.numpy()[0][0][1][0], 1*outputs.numpy()[0][0][1][1], 1*outputs.numpy()[0][0][1][2]],
            [1*outputs.numpy()[0][0][2][0], 1*outputs.numpy()[0][0][2][1], 1*outputs.numpy()[0][0][2][2]],
            [1*outputs.numpy()[0][0][3][0], 1*outputs.numpy()[0][0][3][1], 1*outputs.numpy()[0][0][3][2]],
            [1*outputs.numpy()[0][0][4][0], 1*outputs.numpy()[0][0][4][1], 1*outputs.numpy()[0][0][4][2]],
            [1*outputs.numpy()[0][0][5][0], 1*outputs.numpy()[0][0][5][1], 1*outputs.numpy()[0][0][5][2]],
            [1*outputs.numpy()[0][0][6][0], 1*outputs.numpy()[0][0][6][1], 1*outputs.numpy()[0][0][6][2]],
            [1*outputs.numpy()[0][0][7][0], 1*outputs.numpy()[0][0][7][1], 1*outputs.numpy()[0][0][7][2]],
            [1*outputs.numpy()[0][0][8][0], 1*outputs.numpy()[0][0][8][1], 1*outputs.numpy()[0][0][8][2]],
            [1*outputs.numpy()[0][0][9][0], 1*outputs.numpy()[0][0][9][1], 1*outputs.numpy()[0][0][9][2]],
            [1*outputs.numpy()[0][0][10][0], 1*outputs.numpy()[0][0][10][1], 1*outputs.numpy()[0][0][10][2]],
            [1*outputs.numpy()[0][0][11][0], 1*outputs.numpy()[0][0][11][1], 1*outputs.numpy()[0][0][11][2]],
            [1*outputs.numpy()[0][0][12][0], 1*outputs.numpy()[0][0][12][1], 1*outputs.numpy()[0][0][12][2]],
            [1*outputs.numpy()[0][0][13][0], 1*outputs.numpy()[0][0][13][1], 1*outputs.numpy()[0][0][13][2]],
            [1*outputs.numpy()[0][0][14][0], 1*outputs.numpy()[0][0][14][1], 1*outputs.numpy()[0][0][14][2]],
            [1*outputs.numpy()[0][0][15][0], 1*outputs.numpy()[0][0][15][1], 1*outputs.numpy()[0][0][15][2]],
            [1*outputs.numpy()[0][0][16][0], 1*outputs.numpy()[0][0][16][1], 1*outputs.numpy()[0][0][16][2]]
        ],
        "min_x": int(round(min_x, 2) * 100) if min_x != 99999 else -1,
        "max_x": int(round(max_x, 2) * 100) if max_x != 0 else -2,
        "min_y": int(round(min_y, 2) * 100) if min_y != 99999 else -1,
        "max_y": int(round(max_y, 2) * 100) if max_y != 0 else -2
    }

    json_message = json.dumps(temp)
    print(json_message)
    await position_socket.send(json_message)


async def encode_image_and_send(frame, video_socket):
    _, encoded_image = cv2.imencode('.jpg', frame)
    encoded_image = encoded_image.tobytes()
    await video_socket.send(encoded_image)


if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(main())
