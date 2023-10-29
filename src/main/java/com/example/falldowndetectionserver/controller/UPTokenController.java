package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * FCM 발송을 위한 사용자 핸드폰 고유 token을 처리한다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/UPToken/")
public class UPTokenController {
    private final UPTokenDao uPTokenDao;
    // TODO: 필요한 지 확인하고 없어도 되면 삭제

    /**
     * Database에 사용자 핸드폰 Token을 등록함
     * @param userPhoneTokenVO
     * @return 성공시 "Success", 실패시 "Fail"
     */
    @PostMapping("register")
    public ResponseEntity registerUserPhoneToken(@RequestBody UserPhoneTokenVO userPhoneTokenVO) {
        if (uPTokenDao.insert(userPhoneTokenVO) == 1) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.ok("Fail");
        }
    }

    /**
     * Database에 저장된 사용자 핸드폰 Token을 업데이트 함
     * @param userPhoneTokenVO
     * @return 성공시 "Success", 실패시 "Fail"
     */
    @PutMapping("update")
    public ResponseEntity updateUserPhoneToken(@RequestBody UserPhoneTokenVO userPhoneTokenVO) {
        if (uPTokenDao.update(userPhoneTokenVO) == 1) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.ok("Fail");
        }
    }
}
