package com.classroomassistant.vo;

import com.classroomassistant.pojo.Topic;
import com.classroomassistant.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DELL
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopicAndUser {
    private User user;
    private Topic topic;
}
