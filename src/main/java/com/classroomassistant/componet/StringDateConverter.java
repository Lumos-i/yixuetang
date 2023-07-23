package com.classroomassistant.componet;

import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DELL
 */
public class StringDateConverter implements Converter<String,Date> {
    @Override
    @SneakyThrows
    public Date convert(String s) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(s);
    }
}
