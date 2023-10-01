package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Mapper
@Component
public interface UserMapper {
    /**
     *
     * @param userVO uno, regdate, updatedate 제외하고, 모두 넘겨줘야 한다.
     */
    public int insert(UserVO userVO);

    /**
     *
     * @param cameraId primary key인 uno만 넘겨주면 됨
     * @return UserVO형태로 리턴함
     */
    public Optional<UserVO> select(String cameraId);

    /**
     *
     * @param cameraId primary key인 uno만 넘겨주면 됨
     */
    public int delete(String cameraId);

    /**
     *
     * @param userVO userName, userPassword, userAddress, userPhone 이 네개는 꼭 필요함
     */
    public int update(UserVO userVO);
}
