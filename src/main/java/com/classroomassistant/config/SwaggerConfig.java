package com.classroomassistant.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2022年06月26日 8:01
 */

@Configuration  //spring的配置文件
@EnableSwagger2  //开启swagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webapi")
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                //需要扫描的控制类包
                .apis(RequestHandlerSelectors.basePackage("com.classroomassistant.controller"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }
    @Bean
    public Docket web_api_admin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.classroomassistant.controller"))
                .paths(PathSelectors.ant("/superAdmin/**"))
                .build()
                .groupName("管理员:web-admin-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket web_api_bm() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("bm-api", "关于关注", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*Focus*"))
                .build()
                .groupName("关注:web-bm-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket web_api_bo() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("bo-api", "用户", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*User*"))
                .build()
                .groupName("用户:web-bo-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket web_api_sm() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("sm-api", "专辑", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*Album*"))
                .build()
                .groupName("专辑:web-sm-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket web_api_so() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("so-api", "轮播图", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*Carousel*"))
                .build()
                .groupName("轮播图:web-so-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public Docket xcx_api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("xcx-api", "动态", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*Dynamic*"))
                .build()
                .groupName("动态:xcx-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }
    @Bean
    public Docket xc_api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("xc-api", "评论", "1.0"))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**/*Comment*"))
                .build()
                .groupName("评论:xcx-接口文档V1.0")
                .pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("api接口文档")
                .description("controller层接口")
                .version("1.0")
                .license("堆图接口文档")
                .build();
    }
    private ApiInfo apiInfo(String title,String description,String version){
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .license("堆糖")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("token", authorizationScopes));
        return securityReferences;
    }
}