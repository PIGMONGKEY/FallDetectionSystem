package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.vo.NotiBoardVO;
import com.example.falldowndetectionserver.mapper.NotiBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@RequiredArgsConstructor
public class NotiBoardDao {
    private final NotiBoardMapper notiBoardMapper;

    public int insert(NotiBoardVO notiBoardVO) {
        try {
            return notiBoardMapper.insert(notiBoardVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public NotiBoardVO select(int bno) {
        return notiBoardMapper.select(bno);
    }

    public List<NotiBoardVO> selectAll() {
        return notiBoardMapper.selectAll();
    }

    public int update(NotiBoardVO notiBoardVO) {
        try {
            return notiBoardMapper.update(notiBoardVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete(int bno) {
        try {
            return notiBoardMapper.delete(bno);
        } catch (Exception e) {
            return -1;
        }
    }
}
