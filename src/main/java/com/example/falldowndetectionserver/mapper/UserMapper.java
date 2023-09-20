package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    /**
     *
     * @param userVO uno, regdate, updatedate 제외하고, 모두 넘겨줘야 한다.
     */
    public void insert(UserVO userVO);

    /**
     *
     * @param uno primary key인 uno만 넘겨주면 됨
     * @return UserVO형태로 리턴함
     */
    public UserVO select(int uno);

    /**
     *
     * @param uno primary key인 uno만 넘겨주면 됨
     */
    public void delete(int uno);

    /**
     *
     * @param userVO userName, userPassword, userAddress, userPhone 이 네개는 꼭 필요함
     */
    public void update(UserVO userVO);
}
