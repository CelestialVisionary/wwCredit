package com.wwfinance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    /**
     * 消息转换器配置
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 配置JSON转换器
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
                // 生成JSON时,将所有Long转换成String
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);

                // 时间格式序列化
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
                simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
                // 时间格式序列化
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
                simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
                simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

                objectMapper.registerModule(simpleModule);
                // 设置JSON媒体类型
                jacksonConverter.setSupportedMediaTypes(List.of(
                        MediaType.APPLICATION_JSON, 
                        MediaType.APPLICATION_JSON_UTF8
                ));
                break;
            }
        }
        
        // 检查并配置StringHttpMessageConverter，只让它处理TEXT_PLAIN和TEXT_HTML
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringConverter = (StringHttpMessageConverter) converter;
                stringConverter.setSupportedMediaTypes(List.of(
                        MediaType.TEXT_PLAIN, 
                        MediaType.TEXT_HTML
                ));
                break;
            }
        }
    }
}