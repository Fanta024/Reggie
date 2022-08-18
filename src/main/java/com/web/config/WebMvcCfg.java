//package com.web.config;
//
//import com.web.common.JacksonObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.List;


//  @JsonFormat(shape = JsonFormat.Shape.STRING)   解决



//@Configuration
//@Slf4j
//public class WebMvcCfg extends WebMvcConfigurationSupport {
//
//    //静态资源映射
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
//        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
//
//    }
//
//    //自定义   mvc消息转化器
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("拓展消息转化器");
//        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
//        converters.add(0,mappingJackson2HttpMessageConverter);
//    }
//}


