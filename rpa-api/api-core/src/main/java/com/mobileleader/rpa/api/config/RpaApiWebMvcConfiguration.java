package com.mobileleader.rpa.api.config;

import com.mobileleader.rpa.utils.beans.ConfigurationBeanFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.mobileleader.rpa"}, useDefaultFilters = false,
        includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {Controller.class, RestController.class})})
public class RpaApiWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(ConfigurationBeanFactory.createMessageConverters());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Create multipartResolver.
     *
     * @return {@link CommonsMultipartResolver}
     */
    @Bean
    public MultipartResolver multipartResolver(
            @Value("#{apiProperties['common.file.maxUploadSizePerFile']}") Long maxUploadSizePerFile) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1); // no limit
        multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
        return multipartResolver;
    }
}
