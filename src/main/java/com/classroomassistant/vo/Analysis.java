package com.classroomassistant.vo;

import com.classroomassistant.pojo.Homework;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analysis {
    private Homework homework;
    private Integer personCount;
    private BigDecimal allAvg;
    private List<AvgData> avgData;
}
