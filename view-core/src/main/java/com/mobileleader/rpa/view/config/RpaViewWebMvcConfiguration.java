package com.mobileleader.rpa.view.config;

import com.mobileleader.rpa.utils.beans.ConfigurationBeanFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.mobileleader.rpa"}, useDefaultFilters = false,
        includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {Controller.class, RestController.class})})
public class RpaViewWebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
        registry.addRedirectViewController("", "/login");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(ConfigurationBeanFactory.createMessageConverters());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views", ".jsp");
    }

    /**
     * MultipartResolver Bean create.
     *
     * @return {@link MultipartResolver}
     */
    @Bean
    public MultipartResolver multipartResolver(
            @Value("#{viewProperties['common.file.maxUploadSizePerFile']}") Long maxUploadSizePerFile) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1); // not limit
        multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
        return multipartResolver;
    }

    /**
     * MessageSource Bean create.
     *
     * @return {@link ResourceBundleMessageSource}
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("message");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
