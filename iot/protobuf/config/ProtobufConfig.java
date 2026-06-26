package com.lp.iot.protobuf;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ProtobufConfig implements WebMvcConfigurer {

//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 添加 Protobuf 消息转换器，放在第一位以优先处理
//        converters.add(0, new CustomProtobufHttpMessageConverter());
//    }

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        // 1. 添加自定义转换器 (将自定义转换器添加到所有默认转换器之前。)
        builder.addCustomConverter(new CustomProtobufHttpMessageConverter());

        // 2. (可选) 如果需要将自定义转换器置于最高优先级，可以使用定制方法
//         builder.configureMessageConvertersList(list -> list.addFirst(new CustomProtobufHttpMessageConverter()));
    }



    /**
     * 自定义 Protobuf HttpMessageConverter
     */
    public static class CustomProtobufHttpMessageConverter extends ProtobufHttpMessageConverter {
        public CustomProtobufHttpMessageConverter() {
            super();
            // 设置支持的媒体类型
            setSupportedMediaTypes(List.of(
                    MediaType.parseMediaType("application/x-protobuf"),
                    MediaType.parseMediaType("application/octet-stream")
            ));
        }
    }
}
