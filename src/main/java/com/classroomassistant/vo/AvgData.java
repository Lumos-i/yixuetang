package com.classroomassistant.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.classroomassistant.pojo.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author DELL
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgData {
    private BigDecimal avg;
    private Integer gradeCount;
    private Grade grade;
}
