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
}
