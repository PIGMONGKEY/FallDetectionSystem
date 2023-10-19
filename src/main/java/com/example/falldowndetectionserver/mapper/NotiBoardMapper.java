package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.NotiBoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotiBoardMapper {
    public int insert(NotiBoardVO notiBoardVO);
    public List<NotiBoardVO> selectAll();
    public int update(NotiBoardVO notiBoardVO);
    public int delete(int bno);
}
