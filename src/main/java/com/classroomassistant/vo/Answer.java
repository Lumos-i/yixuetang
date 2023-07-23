package com.classroomassistant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private Integer type;
    private String answer;
}
