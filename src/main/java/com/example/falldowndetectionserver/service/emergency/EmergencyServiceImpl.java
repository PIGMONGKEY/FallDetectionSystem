package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSRequestDTO;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSRequestMessageInfo;
import com.example.falldowndetectionserver.domain.dto.naver.NaverSMSResponseDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.utils.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyServiceImpl implements EmergencyService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final SmsUtil smsUtil;

    /**
     * 카메라 아이디를 받아서, 보호자 핸드폰으로 문자를 전송한다.
     * 카메라 아이디로 NokPhoneDao를 통해 보호자 핸드폰 번호 명단을 불러온 후,
     * 문자를 전송한다.
     * @param cameraId 카메라 아이디를 파라미터로 받는다
     */
    @Override
    public void sendSMS(String cameraId) {
        NaverSMSRequestDTO body = makeMessage(cameraId);
        URI requestURI = smsUtil.getSendMessageURI();
        HttpHeaders headers = makeHeader();

        RequestEntity<NaverSMSRequestDTO> requestEntity = RequestEntity
                .post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(body);

        ResponseEntity<NaverSMSResponseDTO> response = new RestTemplate().exchange(requestEntity, NaverSMSResponseDTO.class);

        log.info(response.getBody().toString());
    }

    /**
     * 카메라 아이디로 보호자 핸드폰 번호를 불러온 후,
     * NaverSMSApi에 요청을 보낼 Body를 생성한다.
     * @param cameraId 카메라 아이디를 파라미터로 받는다.
     * @return NaverSMSRequestDTO 형태로 requestBody를 리턴한다.
     */
    private NaverSMSRequestDTO makeMessage(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElseThrow();
        List<String> nokPhones = nokPhoneDao.selectAll(cameraId);
        List<NaverSMSRequestMessageInfo> messages = new ArrayList<>();

        messages.add(NaverSMSRequestMessageInfo.builder().to(nokPhones.get(0)).build());
        // 등록된 보호자 연락처가 2개인 경우, 2명 다 추가한다.
        if (nokPhones.size() > 1) {
            messages.add(NaverSMSRequestMessageInfo.builder().to(nokPhones.get(1)).build());
        }

        String address = userVO.getUserAddress();
        String phone = userVO.getUserPhone();
        String name = userVO.getUserName();

        return NaverSMSRequestDTO.builder()
                .type("SMS")
                .from(smsUtil.getSender())
                .content(name + "님의 자택에 설치된 위급상황 감지 시스템이 위급상황을 감지했습니다.\n" +
                        "자택 주소 : " + address + "\n" +
                        name + "님의 전화번호 : " + phone + "\n" +
                        "감지된 영상을 보려면 링크를 클릭해주세요.\n" +
                        "링크 : ")
                .messages(messages)
                .build();
    }

    /**
     * NaverSMSApi에 요청을 보내기 위한 헤더를 생성하여 리턴한다.
     * SmsUtil에서 AccessKey와 Signature를 생성하여 헤더에 넣는다.
     * @return 완성된 HttpHeaders를 리턴한다.
     */
    private HttpHeaders makeHeader() {
        long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-ncp-apigw-timestamp", time + "");
        headers.add("x-ncp-iam-access-key", smsUtil.getAccessKey());
        headers.add("x-ncp-apigw-signature-v2", smsUtil.getSignature(time));

        return headers;
    }
}
