package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.UserVO;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    public void insert(UserVO userVO);
    public UserVO select(int uno);
    public void delete(int uno);

//    이름, 비밀번호, 전화번호, 주소, 업데이트 시간
    public void update(UserVO userVO);
}
