package com.classroomassistant.vo;

import com.classroomassistant.pojo.Discuss;
import com.classroomassistant.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussTree implements Serializable {
    private User user;
    private Discuss discuss;
    private List<DiscussTree> discussList;
}
