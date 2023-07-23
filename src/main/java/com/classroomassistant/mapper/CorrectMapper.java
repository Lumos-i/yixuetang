package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.Correct;
import com.classroomassistant.vo.AvgData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CorrectMapper extends BaseMapper<Correct> {
    List<AvgData> getAvgData(Integer h_id);
}
