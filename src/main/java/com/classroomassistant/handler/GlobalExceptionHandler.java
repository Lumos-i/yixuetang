package com.classroomassistant.handler;

import com.classroomassistant.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author zrq
 * @ClassName GlobalExceptionController
 * @date 2022/10/15 8:10
 * @Description 全局异常处理
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception ex,HandlerMethod hm) {
        log.info("异常信息被处理了"+","+"错误信息："+ex);
        log.info(ex.getMessage());
        //异常信息
        log.info("类所在地址:{}",hm.getBean().getClass());
        //哪个类下
        log.info("类中的方法:{}",hm.getMethod().getName());
        //在哪个方法的
        JsonResult jsonResult = new JsonResult(500, ex.getMessage(), ex.getCause());
        return jsonResult;

    }

    /** 空指针异常 */
    @ExceptionHandler(NullPointerException.class)
    public JsonResult nullPointerExceptionHandler(NullPointerException ex) {
        log.error("空指针异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("空指针异常");
    }

    /** 类型转换异常 */
    @ExceptionHandler(ClassCastException.class)
    public JsonResult classCastExceptionHandler(ClassCastException ex) {
        log.error("类型转换异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("类型转换异常");
    }
    /** 文件未找到异常 */
    @ExceptionHandler(FileNotFoundException.class)
    public JsonResult FileNotFoundException(FileNotFoundException ex) {
        log.error("文件未找到异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("文件未找到异常");
    }
    /** 数字格式异常 */
    @ExceptionHandler(NumberFormatException.class)
    public JsonResult NumberFormatException(NumberFormatException ex) {
        log.error("数字格式异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("数字格式异常");
    }
    /** 安全异常 */
    @ExceptionHandler(SecurityException.class)
    public JsonResult SecurityException(SecurityException ex) {
        log.error("安全异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("安全异常");
    }
    /** sql异常 */
    @Order(0)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public JsonResult SQLException(SQLIntegrityConstraintViolationException ex) {
        log.error("sql重复插入数据异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("插入重复数据");
    }
    /** 类型不存在异常 */
    @ExceptionHandler(TypeNotPresentException.class)
    public JsonResult TypeNotPresentException(TypeNotPresentException ex) {
        log.error("类型不存在异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("类型不存在异常");
    }

    /** IO异常 */
    @ExceptionHandler(IOException.class)
    public JsonResult iOExceptionHandler(IOException ex) {
        log.error("IO异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("IO异常");
    }


    /** 数组越界异常 */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public JsonResult indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        log.error("数组越界异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("数组越界异常");
    }
    /** sql语法错误异常 */
    @ExceptionHandler(BadSqlGrammarException.class)
    public JsonResult BadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("sql语法错误异常：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("sql语法错误异常");
    }

    /** 无法注入bean异常 */
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public JsonResult NoSuchBeanDefinitionException(NoSuchBeanDefinitionException ex) {
        log.error("无法注入bean异常 ：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("无法注入bean");
    }

    /** Http消息不可读异常 */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public JsonResult requestNotReadable(HttpMessageNotReadableException ex) {
        log.error("400错误..requestNotReadable：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("Http消息不可读");
    }

    /** 除数不能为0 */
    @ExceptionHandler({ArithmeticException.class})
    public JsonResult arithmeticException(ArithmeticException ex) {
        log.error("除数不能为0：{} ", ex.getMessage(), ex);
        return JsonResult.errorMsg("除数不能为0异常");
    }
    @ExceptionHandler(value = RuntimeException.class)
    public JsonResult tokenException(RuntimeException ru,HandlerMethod hm){
        log.info(ru.getMessage());
        CharSequence charSequence="MySQLIntegrityConstraintViolationException";
        if (StringUtils.isEmpty(ru.getMessage())) {
            return JsonResult.errorMsg("数据操作错误");
        }
        if (ru.getMessage().contains(charSequence)){
            return JsonResult.errorMsg("插入重复数据");
        }
        log.info("在那个类的错误:{}",hm.getMethod());
        return JsonResult.errorMsg("数据操作错误");
    }
}