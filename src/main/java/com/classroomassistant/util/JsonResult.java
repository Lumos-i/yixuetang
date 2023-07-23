package com.classroomassistant.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.utils.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @Title: LeeJSONResult.java
 * @Package com.lee.utils
 * @Description: 自定义响应数据结构
 *                 200：表示成功
 *                 500：表示错误，错误信息在msg字段中
 *                 501：bean验证错误，不管多少个错误都以map形式返回
 *                 502：拦截器拦截到用户token出错
 *                 555：异常抛出信息
 * Copyright: Copyright (c) 2016
 * Company:Nathan.Lee.Salvatore
 *
 * @author leechenxiang
 * @date 2016年4月22日 下午8:33:36
 * @version V1.0
 */

/**
 * 结果集主要包含三段内容
 * 1：响应状态：即业务码；200，500之类的
 * 2:响应消息：主要是提示的内容
 * 3：响应数据：数据的主体部分
 */
public class JsonResult {


    private static final ObjectMapper MAPPER = new ObjectMapper();
    // 定义jackson对象

    private Integer status;
    // 响应业务状态


    private String msg;
    // 响应消息


    private Object data;
    // 响应中的数据

    private String ok;
    // 不使用

    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }
    public static JsonResult  ok(String msg) {return new JsonResult(msg);}
    public static JsonResult error() {
        return new JsonResult(500, "error", null);
    }
    public static JsonResult errorMsg(String msg) {
        return new JsonResult(500, msg, null);
    }

    public static JsonResult errorMap(Object data) {
        return new JsonResult(501, "error", data);
    }

    public static JsonResult errorTokenMsg(String msg) {
        return new JsonResult(502, msg, null);
    }

    public static JsonResult errorException(String msg) {
        return new JsonResult(555, msg, null);
    }
    public static JsonResult nullException(String msg) {
        return new JsonResult(500, msg, null);
    }
    public static Boolean checkSpace(String data){
        if (StringUtils.isNullOrEmpty(data.trim())||!data.trim().equals(data)){
            return true;
        }
        return false;
    }

    public JsonResult() {

    }

    public JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     *
     * @Description: 将json结果集转化为LeeJSONResult对象
     *                 需要转换的对象是一个类
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:34:58
     */
    public static JsonResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, JsonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @Description: 没有object对象的转化
     * @param json
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:21
     */
    public static JsonResult format(String json) {
        try {
            return MAPPER.readValue(json, JsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @Description: Object是集合转化
     *                 需要转换的对象是一个list
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:31
     */

    public static JsonResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public static Boolean checkSpace(String... data){
        for (String date:data){
            if (StringUtils.isNullOrEmpty(date.trim())||!date.trim().equals(date)
                    ||!date.replaceAll(" ","").equals(date)
                    ||!date.replaceAll(" ","").equals(date)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkSpaceContainNull(String... data){
        Set<Boolean> booleans=new HashSet<>();
        for (String date:data){
            if (StringUtils.isNullOrEmpty(date)){
                continue;
            }
            if (StringUtils.isNullOrEmpty(date.trim())||!date.trim().equals(date)
                    ||!date.replaceAll(" ","").equals(date)
                    ||!date.replaceAll(" ","").equals(date)){
                booleans.add(true);
            }
        }
        return booleans.isEmpty()?false:true;
    }

    public static String createCode(Integer n){
        String code = "";
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int type = r.nextInt(3);
            switch (type) {
                case 0:
                    //大写字符（A 65-Z 65+25）
                    char ch = (char) (r.nextInt(26) + 65);
                    code += ch;
                    break;
                case 1:
                    //小写字符（a 97-z 97+25）
                    char ch1 = (char) (r.nextInt(26) + 97);
                    code += ch1;
                    break;
                case 2:
                    code += r.nextInt(10);
                    break;
            }
        }
        return code;
    }

}