package com.mobileleader.rpa.view.config;

import com.mobileleader.rpa.repository.file.FileReadWriteSupport;
import com.mobileleader.rpa.utils.beans.ConfigurationBeanFactory;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import com.mobileleader.rpa.utils.rest.RestClientManager;
import com.mobileleader.rpa.view.config.datasource.DataSourceConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = {"com.mobileleader.rpa"}, excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class, Controller.class, RestController.class})})
@EnableAspectJAutoProxy
@Import(DataSourceConfiguration.class)
public class RpaViewConfiguration extends AnnotationConfigApplicationContext {
    /**
     * Create Properties Bean.
     *
     * @return {@link Properties}
     * @throws IOException if file loading failed
     */
    @Bean(name = "viewProperties")
    public Properties viewProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/view-properties.xml"));
    }

    @Bean(name = "passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedirectStrategy defaultRedirectStrategy() {
        return new DefaultRedirectStrategy();
    }

    @Bean
    public FileReadWriteSupport fileReadWriteSupport(
            @Value("#{viewProperties['common.file.repository']}") String repositoryPath) {
        return new FileReadWriteSupport(repositoryPath);
    }

    @Bean(name = "rpaRsaCipher")
    public RpaRsaCipher rsaCipher() {
        return new RpaRsaCipher();
    }

    /**
     * Create RestClientManager.
     *
     * @param hostAddress API-Server HostAddress
     * @param port API-Server Port
     * @param rootContext API-Server Context
     * @return {@link RestClientManager}
     */
    @Bean(name = "restClientManager")
    public RestClientManager createRestClientManager(
            @Value("#{viewProperties['api.server.hostAddress']}") String hostAddress,
            @Value("#{viewProperties['api.server.port']}") int port,
            @Value("#{viewProperties['api.server.rootContext']}") String rootContext) {
        RestClientManager restClientManager = new RestClientManager();
        restClientManager.setRestTemplate(restTemplate());
        restClientManager.setHostAddress(hostAddress);
        restClientManager.setPort(port);
        restClientManager.setRootContext(rootContext);
        return restClientManager;
    }

    /**
     * Create RestTemplate Bean.
     *
     * @return {@link RestTemplate}
     */
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(ConfigurationBeanFactory.createGsonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    /**
     * Create HttpRequestFactory Bean. Use RestTemplate.
     *
     * @return {@link HttpComponentsClientHttpRequestFactory}
     */
    @Bean(name = "httpRequestFactory")
    public HttpComponentsClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(10000);
        httpComponentsClientHttpRequestFactory.setReadTimeout(60000);
        return httpComponentsClientHttpRequestFactory;
    }
}
