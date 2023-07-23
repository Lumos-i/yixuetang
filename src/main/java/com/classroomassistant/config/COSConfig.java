package com.classroomassistant.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;


@Configuration
@ConditionalOnProperty(prefix = "cos.tengxun",name = "enable",havingValue = "true")
@ConfigurationProperties(prefix = "cos.tengxun")
@Data
public class COSConfig implements Serializable {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
    private String path;
    private Boolean enable;

    @Bean
    public COSClient cosClient(){
        //初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(this.secretId, this.secretKey);
        //设置 bucket 的区域
        Region region = new Region(this.region);
        ClientConfig clientConfig = new ClientConfig(region);
        //生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
