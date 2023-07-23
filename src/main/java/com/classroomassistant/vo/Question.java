package com.classroomassistant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {
    private Integer order;
    private Integer type;
    private BigDecimal score;
    private String questionContent;

}
