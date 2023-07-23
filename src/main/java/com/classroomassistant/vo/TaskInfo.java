package com.classroomassistant.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.classroomassistant.pojo.Correct;
import com.classroomassistant.pojo.Task;
import com.classroomassistant.pojo.UserAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {
    private Task task;
    private Correct correct;
    private UserAnswer userAnswer;
    private Integer allCount;
    private Integer finish;
}
