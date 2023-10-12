package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/UPToken/")
public class UPTokenController {
    private final UPTokenDao uPTokenDao;

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
