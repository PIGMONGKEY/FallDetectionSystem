import cv2
import asyncio
import websockets
import json
import tensorflow as tf
import tensorflow_hub as hub

# 파일에서 설정 정보 읽어옴
print("Loading settings...")
file = open("./setting.txt", 'r')
url = file.readline().strip()
video_save_path = file.readline()
file.close()
print("Load setting success")

# 비디오를 전송할 때 사용하는 소켓 연결 정보
CONNECTION_INFO = json.dumps({
    "camera_id": "cam01",
    "identifier": "sender",
    "streaming_url": url
})

# 비디오 또는 포지션 정보를 전송할 때 추가하는 헤더
EXTRA_HEADER = {
    "camera_id": "cam01"
}


# movenet 모델 로드
print("Loading model...")
model = hub.load(f"./movenet_singlepose_thunder_4")
movenet = model.signatures['serving_default']
print("Model load success")

# 영상 캡처
print("Loading streaming...")
cap = cv2.VideoCapture(url)
print("Streaming load success")


# 영상 저장
async def save_video(falldown_time, temp_queue):
    # 비디오 저장기? VideoWriter를 생성
    fps = cap.get(cv2.CAP_PROP_FPS)
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    width = round(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = round(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    # 영상 저장 경로, 파일명 설정
    # 파일명은 넘어진 시간
    video_name = f"{video_save_path}{falldown_time}.mp4"
    video_writer = cv2.VideoWriter(video_name, fourcc, fps, (width, height))

    # 비디오 저장
    for f in temp_queue:
        video_writer.write(f)

    # 메모리 해제
    video_writer.release()


class FallDown:
    def __init__(self):
        self.frame_queue = []
        self.temp_frames = []
        self.falldown_time_list = []
        self.save_flag = False

    # 기본 버퍼에 프레임 추가
    def append_frame(self, frame):
        if self.frame_queue.__len__() < 75:
            self.frame_queue.append(frame)
        else:
            self.frame_queue.pop(0)
            self.frame_queue.append(frame)

    # 저장을 위한 버퍼에 리스트 추가
    # 기본 버퍼를 복사하여 추가
    def append_new_temp_frame_list(self):
        temp_frame_list = self.frame_queue.copy()
        self.temp_frames.append(temp_frame_list)
        print("new temp list appended", self.temp_frames.__len__())

    # 새로운 비디오 저장기를 만들어서 추가
    def append_new_video_writer(self, falldown_time):
        # 비디오 저장기 queue에 넣기
        self.falldown_time_list.append(falldown_time)
        print("video writer appended", self.falldown_time_list)

    # 넘어짐 이후 5초 추가 녹화
    # 5초(75프레임) 경과하면 저장
    async def append_new_temp_frame(self, frame):
        if self.temp_frames.__len__() > 1:
            self.temp_frames.pop(0)
            self.falldown_time_list.pop(0)
            print("not used writer, list popped", self.temp_frames.__len__())

        for temp_list in self.temp_frames:
            if temp_list.__len__() < 150:
                temp_list.append(frame)
            else:
                self.save_flag = True

        if self.save_flag:
            await save_video(self.falldown_time_list.pop(0), self.temp_frames.pop(0))
            self.save_flag = False


# WebSocket을 통해 서버로 연결하고, Opencv를 이용해 웹캠으로 찍은 영상을 프레임 단위로 전송
async def send_video_position(falldown: FallDown):
    async with websockets.connect("ws://localhost:10000/position", extra_headers=EXTRA_HEADER) as position_socket:

        # 웹캡이 켜져 있는동안 반복
        while cap.isOpened():
            # 프레임 단위로 영상 읽음
            ret, frame = cap.read()
            if not ret:
                break

            falldown.append_frame(frame)

            # 저장을 위한 버퍼에 프레임 추가, 비디오 저장
            save_task = asyncio.create_task(falldown.append_new_temp_frame(frame))
            # Movenet 적용하여 포지션 정보 전송하는 Task 생성
            position_task = asyncio.create_task(get_position_and_send(frame, position_socket))

            # 두 개의 Task를 동시에 실행 - 둘 다 끝나길 기다림
            await save_task
            await position_task


# 서버에서 넘어짐을 알리는 메시지를 받는다.
async def receive_message(falldown: FallDown):
    async with websockets.connect("ws://localhost:10000/video", extra_headers=EXTRA_HEADER) as video_socket:

        # 소켓에 연결 정보 전송
        await video_socket.send(CONNECTION_INFO)

        while True:
            # 서버에서 오는 메시지 기다리기
            falldown_time = await video_socket.recv()

            falldown.append_new_video_writer(falldown_time=falldown_time)
            falldown.append_new_temp_frame_list()


# frame에 movenet을 적용하여 keypoint들의 좌표를 추출한 후 전송한다
async def get_position_and_send(frame, position_socket):
    min_x = 99999
    min_y = 99999
    max_x = 0
    max_y = 0

    # frame 차원 늘리기
    pose_image = tf.expand_dims(frame, axis=0)
    # 이미지 사이즈 조절
    pose_image = tf.cast(tf.image.resize_with_pad(pose_image, 256, 256), dtype=tf.int32)
    # pose_image = tf.cast(tf.image.resize_with_pad(pose_image, 192, 192), dtype=tf.int32)
    # 자세 추정 후 결과값 받기
    outputs = movenet(pose_image)['output_0']

    # 인식 기준 신뢰도 0.35로 설정
    # x, y 최대 최소 값 구함
    for keypoint in outputs.numpy()[0][0]:
        if keypoint[2] <= 0.5:
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

    # 모든 결과값 JSON 형식으로 리턴
    # float32 tensor가 JSON에 안들어가는 이유로 integer 형식으로 변환하여 전송
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


async def main():
    falldown = FallDown()

    main_task = asyncio.create_task(send_video_position(falldown=falldown))
    receive_task = asyncio.create_task(receive_message(falldown=falldown))

    await asyncio.gather(main_task, receive_task)


if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(main())
