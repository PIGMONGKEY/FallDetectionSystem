# FallDownDetectionServer

넘어짐 감지 시스템의 서버입니다.

SpringBoot를 사용하여 Java기반으로 개발되었으며, redis, mysql을 사용하여 데이터를 처리합니다.

Android 앱을 위한 REST API 및, 영상 송수신, 넘어짐 감지 처리, 등의 역할을 합니다.

문자 전송을 위해 Aligo 문자전송 서비스를 사용합니다.

PUSH 알림 전송을 위해 FirebaseCloudMessaging (FCM)을 사용합니다.

보안을 위한 인증수단으로서, JWT를 사용했습니다.
