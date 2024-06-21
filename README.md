# FallDetectionSystem

### [개발 보고서](https://drive.google.com/file/d/1J0N4tWgM3rrh6MndQsfRDTTGw-xNEgjW/view?usp=sharing)

### [ICTAES 제출 논문](https://drive.google.com/file/d/13QC6ZKJvAbbr3zint8Oid9gCTuIBX9a7/view?usp=sharing)

## 개요
대한민국의 1인가구 수는 매년 늘어나고 있으며, 그에 따라 고독사의 수 또한 꾸준히 늘어나고 있습니다.
독거인에게 발생하는 실신 등의 위급상황에 빠르게 대처하기 위하여 시스템을 개발했습니다.

시스템은 사용자의 가정에 설치되며, 카메라를 통해 사용자를 촬영합니다.
촬영된 영상은 영상서버로 전송되며, 영상서버는 수신한 영상에 자세 감지 모델을 적용하여 얻은 신체부위 좌표를 주 서버에 전송합니다.
주 서버는 좌표값을 통해 사용자의 실신을 판단하며, 실신을 감지한 경우, 위급상황 판단 로직을 통해 위급상황 여부를 판단합니다.

실신 발생 시, 사용자의 핸드폰에 큰 소리의 알림이 울립니다. 사용자가 일정시간 내에 이 알림을 종료하지 않으면 보호자에게 알림이 감과 동시에 119에 신고됩니다.
이 경우, 보호자의 핸드폰에도 큰 소리의 알림이 울리게 되며, 보호자는 사용자의 실신 장면이 담긴 영상을 확인하여 빠른 상황파악이 가능합니다.

## 개발 기간
- 2023.04 ~ 2024.01

## 사용 기술 및 개발 환경
- Java / Python / HTML / Javascript
- SpringBoot / Tensorflow
- MySQL / Redis / Mybatis / JWT
- Firebase / Aligo 문자 API
- MacOS / Ubuntu / Raspberry Pi OS
- Git / Github
- Android Studio / IntelliJ IDEA / PyCharm
- MoveNet 자세감지모델

## 주요 기능
- 사용자 앱을 통해 촬영되는 영상 실시간 모니터링
- 개인정보 및 보호자 등록
- 신체 비율을 통한 실신 감지
- 실신 발생 시, 사용자와 보호자에게 알림 전송
- 실신 발생 시, 보호자는 사용자의 실신 장면이 담긴 영상 확인 가능

## 성과
- 교내 해커톤 우수상 수상
- ICT-AES에 논문 제출하여 12th ICAEIC-2024에서 발표 진행 / Best Paper Award 수상

### 네트워크 구조도
![네트워크 구조도](https://github.com/PIGMONGKEY/ChurchProject/assets/113700356/30e4de36-2068-4c53-8339-eadffdffd15d)

### ERD
![ERD](https://github.com/PIGMONGKEY/ChurchProject/assets/113700356/f7fb6c32-e8fe-43f3-abe6-3c59a9530cf5)

### UI 네비게이션
![UI네비게이션](https://github.com/PIGMONGKEY/ChurchProject/assets/113700356/d7037c65-dd46-4b91-b1ad-ea2aa3bcbf54)

### 위급상황 판단 로직 Sequence Diagram
![실신감지Sequence](https://github.com/PIGMONGKEY/ChurchProject/assets/113700356/ead5bdc6-45bd-40ee-bc15-47b2c1bf857f)
