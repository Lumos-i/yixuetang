package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}
