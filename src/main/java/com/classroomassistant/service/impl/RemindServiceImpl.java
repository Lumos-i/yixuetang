package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.RemindMapper;
import com.classroomassistant.pojo.Remind;
import com.classroomassistant.service.RemindService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zrq
 * @ClassName RemindServiceImpl
 * @date 2023/2/1 11:48
 * @Description TODO
 */
@Service
public class RemindServiceImpl implements RemindService {
    @Autowired
    private RemindMapper remindMapper;
    @Override
    public JsonResult addMessage(Integer courseId, String content, Integer type) {
        if (courseId == null || type == null || StringUtils.isEmpty(content)) {
            return JsonResult.errorException("数据不能为空");
        }
        Integer userId = UserUtils.getUserId();
        int insert = remindMapper.insert(new Remind(null, userId, courseId, content, type, null,null));
        return insert > 0 ? JsonResult.ok() : JsonResult.errorException("服务器错误");
    }

    @Override
    public JsonResult getMessage(Integer courseId,Integer type, Integer nodePage, Integer pageSize) {
        if (courseId == null) {
            return JsonResult.errorException("数据不能为空");
        }
        if (nodePage <=0 || pageSize <= 0) {
            return JsonResult.errorException("数据不正确");
        }
        Page<Remind> page = new Page<>(nodePage,pageSize);
        List<Remind> reminds = remindMapper.selectRemind(courseId, page, type);
        List<Integer> ids = new ArrayList<>();
        for (Remind remind : reminds) {
            ids.add(remind.getId());
        }
        LambdaQueryWrapper<Remind> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(!ids.isEmpty(),Remind::getId,ids);
        Remind remind = new Remind();
        remind.setStatus(1);
        remindMapper.update(remind,wrapper);
        return reminds.isEmpty() ? JsonResult.errorException("无数据") : JsonResult.ok(reminds);
    }
}
