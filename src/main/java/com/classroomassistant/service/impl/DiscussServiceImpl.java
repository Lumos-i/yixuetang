package com.classroomassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.mapper.DiscussMapper;
import com.classroomassistant.mapper.TopicMapper;
import com.classroomassistant.mapper.UserMapper;
import com.classroomassistant.pojo.Discuss;
import com.classroomassistant.pojo.Topic;
import com.classroomassistant.service.DiscussService;
import com.classroomassistant.util.JsonResult;
import com.classroomassistant.util.PageUtil;
import com.classroomassistant.util.UserUtils;
import com.classroomassistant.vo.DiscussTree;
import com.classroomassistant.vo.TopicAndUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DELL
 **/
@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonResult publishTopic(Topic topic) {
        topic.setUserId(UserUtils.getUserId());
        int i = topicMapper.insert(topic);
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getTopic(Integer courseId, Integer beginIndex, Integer size) {
        QueryWrapper<Topic> topicQueryWrapper=new QueryWrapper<>();
        topicQueryWrapper.eq("course_id",courseId).eq("deleted",false);
        List<TopicAndUser> topicPage=new ArrayList<>();
        topicMapper.selectList(topicQueryWrapper).forEach(topic -> {
            topicPage.add(new TopicAndUser(userMapper.selectById(topic.getUserId()),topic));
        });
        PageUtil pageUtil=new PageUtil<>();
        PageUtil count = pageUtil.getCount(topicPage, beginIndex, size);
        return JsonResult.ok(count);
    }

    @Override
    public JsonResult deleteTopic(Integer topicId) {
        int i = topicMapper.updateById(new Topic(topicId, true));
        return i>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult publishDiscuss(Discuss discuss) {
        discuss.setSendId(UserUtils.getUserId());
        return discussMapper.insert(discuss)>0?JsonResult.ok():JsonResult.error();
    }

    @Override
    public JsonResult getAllDiscuss(Integer topicId, Integer beginIndex, Integer size) {
        QueryWrapper<Discuss> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_id",topicId).eq("level",1);
        List<Discuss> discussList = discussMapper.selectList(queryWrapper);

        List<DiscussTree> discussTreeList=new ArrayList<>();
        DiscussTree discussTree=new DiscussTree(null,null,discussTreeList);

        discussList.forEach(discuss -> {

            DiscussTree discussTree1=new DiscussTree(userMapper.selectById(discuss.getSendId()),discuss,null);
            discussTreeList.add(discussTree1);
            getAllDiscussMethod(topicId,1,discuss.getId(),discussTree1);
        });

        PageUtil pageUtil=new PageUtil<>();
        PageUtil count = pageUtil.getCount(discussTreeList, beginIndex, size);

        return JsonResult.ok(count);
    }

    @Override
    public JsonResult deleteDiscuss(Integer discussId) {
        return discussMapper.deleteById(discussId)>0?JsonResult.ok():JsonResult.error();
    }

    private void getAllDiscussMethod(Integer topicId,Integer level,Integer superId,DiscussTree discussTree){
        List<DiscussTree> discussTreeList=new ArrayList<>();
        discussTree.setDiscussList(discussTreeList);
        QueryWrapper<Discuss> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_id",topicId)
                .eq("level",level+1)
                .eq("super_id",superId);
        List<Discuss> discusses = discussMapper.selectList(queryWrapper);

        discusses.forEach(discuss -> {
            DiscussTree discussTree1=new DiscussTree(userMapper.selectById(discuss.getSendId()),discuss,null);
            discussTreeList.add(discussTree1);
            getAllDiscussMethod(topicId,level+1,discuss.getId(),discussTree1);
        });

    }
}
