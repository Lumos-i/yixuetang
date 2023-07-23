package com.classroomassistant.vo;

import com.classroomassistant.pojo.Correct;
import com.classroomassistant.pojo.Homework;
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
public class UserHomework {
    private Homework homework;
    private UserAnswer userAnswer;
    private Correct correct;
}
