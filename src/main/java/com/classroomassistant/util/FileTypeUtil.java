package com.classroomassistant.util;

import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileTypeUtil {
    private static Pattern pattern = Pattern.compile("image/.*");
    private static Pattern pattern2 = Pattern.compile("application/pdf");
    private static Pattern pattern3 = Pattern.compile("application/msword");
    private static Pattern pattern4 = Pattern.compile("application/vnd.ms-excel");

    /**
     * 获取类型
     * @param file
     * @return
     */
    public static String getMimeType(MultipartFile file) {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());
        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        }catch (Exception e){
            throw new RuntimeException();
        }
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * 判断是否是图片
     * @param file
     * @return
     */
    public static boolean isImage(MultipartFile file){
        String type = getMimeType(file);
        System.out.println(type);
        //对比对应的文件类型的mime就好了至于不知道对应的是什么的话就百度,百度一定会知道
        Matcher m = pattern.matcher(type);
        return m.matches();
    }
    public static boolean isPdf(MultipartFile file){
        String type = getMimeType(file);
        System.out.println(type);
        //对比对应的文件类型的mime就好了至于不知道对应的是什么的话就百度,百度一定会知道
        Matcher m = pattern2.matcher(type);
        return m.matches();
    }
    public static boolean isWord(MultipartFile file) {
        String type = getMimeType(file);
        Matcher m = pattern3.matcher(type);
        return  m.matches();
    }
    public static boolean isExcel(MultipartFile file) {
        String type= getMimeType(file);
        Matcher m = pattern4.matcher(type);
        return  m.matches();
    }
}
