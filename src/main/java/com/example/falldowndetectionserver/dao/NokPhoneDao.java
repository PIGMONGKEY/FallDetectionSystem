package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import com.example.falldowndetectionserver.mapper.NokPhoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@RequiredArgsConstructor
public class NokPhoneDao {
    private final NokPhoneMapper nokPhoneMapper;

    /**
     * 보호자 연락저를 저장한다.
     */
    public int insert(NokPhoneVO nokPhoneVO) {
        try {
            return nokPhoneMapper.insert(nokPhoneVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public int updateToken(NokPhoneVO nokPhoneVO) {
        try {
            return nokPhoneMapper.updateToken(nokPhoneVO);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 같은 cameraId를 가진 보호자 연락처를 모두 삭제한다.
     */
    public int delete(String cameraId) {
        try {
            return nokPhoneMapper.delete(cameraId);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 같은 카메라 아이디를 가진 보호자 연락처를 모두 조회한다.
     */
    public List<NokPhoneVO> selectAll(String cameraId) {
        return nokPhoneMapper.selectAll(cameraId);
    }
}
