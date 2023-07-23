package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.classroomassistant.pojo.CourseAndUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zrq
 * @ClassName CourseAndUserMapper
 * @date 2023/1/14 13:40
 * @Description TODO
 */
@Mapper
public interface CourseAndUserMapper extends BaseMapper<CourseAndUser> {
}
