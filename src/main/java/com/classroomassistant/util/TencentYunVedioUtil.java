package com.classroomassistant.util;

/**
 * @author zrq
 * @ClassName TencentYunVedioUtil
 * @date 2023/1/27 15:53
 * @Description TODO
 */

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author zhushaojie
 * @Date 2020/6/15 18:15
 */
@Slf4j
public class TencentYunVedioUtil {

    //推流防盗链的key 你自己的推流K
    public static final String push_key = "db3fc0d6c676ebb4158d8f2b91852a85";
    //拉流防盗链的key 你自己的直播K，注意域名需要去解析CNAME，推流k和拉流k可能相同
    public static final String play_key = "yGPfKCXBtDWPRXQEWxrk";
    //推流地址
    public static final String pushUrl ="rtmp://179674.push.tlivecloud.com/ive/";
    //拉流地址 例子 rtmp  只需要将push 改成play即可
//自己申请的播放地址域名xx.xxx.cn或xx.xxx.com
    public static final String playUrl_rtmp ="rtmp://www.wall-e.icu/live/";
    public static final String playUrl_flv ="https://www.wall-e.icu/live/";
    public static final String playUrl_hls ="http://www.wall-e.icu/live/classassistant.m3u8";


    /**
     * 生成防盗链签名
     * @param key ：防盗链key
     * @param streamName ：直播码（或称作流ID）
     * @param txTime ：过期时间 16进制的unix时间戳
     * @return
     */
    public static String genSign(String key,String streamName, String txTime) {
        return MD5Encode.getMD5Str(key+streamName+txTime);
    }

    /**
     * 播放
     * @param key
     * @param txTime
     * @return
     */
    public static String pushSign(String key,String streamName, String txTime) {
        return MD5Encode.getMD5Str(key+streamName+txTime);
    }
    /**
     * 生成直播码
     * 直播码 也叫房间号，推荐用随机数字或者用户ID，注意一个合法的直播码需要拼接 BIZID 前缀。
     * @param bizid
     * @param userId 用户id
     * @return
     */
    public static String genLiveCode(String bizid,String userId) {
        return bizid+"_"+userId;
    }

    /**
     * 随机生成6位数字
     */
    public static String bizid(){
        int newNum = (int)((Math.random()*9+1)*100000000);
        return String.valueOf(newNum);
    }
    /**
     * 将传入的时间转换为 16进制
     * @param date
     * @return
     */
    public static String to16Hex(Date date) {
        Long ab = date.getTime()/1000;
        String a = Long.toHexString(ab);
        return a.toUpperCase();
    }

    /**
     * 将当前时间加1天
     */
    public static Date addOneDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 校验是否在线
     * @param urls 拉流地址
     * @return
     */
    public static boolean isPush(String urls) {
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
        }
        return false;
    }
}