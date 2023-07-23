package com.classroomassistant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOutCome {
    private Integer allCount;
    private Integer manCount;
    private Integer womanCount;
    private Map<String,Integer> map;
}
