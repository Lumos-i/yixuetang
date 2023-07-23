package com.classroomassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.pojo.Remind;
import com.classroomassistant.util.JsonResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zrq
 * @ClassName RemindMapper
 * @date 2023/2/1 11:38
 * @Description TODO
 */
@Mapper
public interface RemindMapper extends BaseMapper<Remind> {
    List<Remind> selectRemind(@Param("courseId")Integer courseId,
                             @Param("page") Page<Remind> page,
                             @Param("type") Integer type);
}
