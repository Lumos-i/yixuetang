package com.classroomassistant.service;

import com.classroomassistant.pojo.Discuss;
import com.classroomassistant.pojo.Topic;
import com.classroomassistant.util.JsonResult;

public interface DiscussService {
    JsonResult publishTopic(Topic topic);

    JsonResult getTopic(Integer courseId, Integer beginIndex, Integer size);

    JsonResult deleteTopic(Integer topicId);

    JsonResult publishDiscuss(Discuss discuss);

    JsonResult getAllDiscuss(Integer topicId, Integer beginIndex, Integer size);

    JsonResult deleteDiscuss(Integer discussId);
}
