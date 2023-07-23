package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zrq
 * @ClassName CourseMapper
 * @date 2023/1/13 11:28
 * @Description TODO
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
