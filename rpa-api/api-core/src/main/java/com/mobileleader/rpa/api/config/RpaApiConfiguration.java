package com.mobileleader.rpa.api.config;

import com.mobileleader.rpa.api.config.datasource.DataSourceConfiguration;
import com.mobileleader.rpa.repository.file.FileReadWriteSupport;
import com.mobileleader.rpa.utils.cipher.RpaRsaCipher;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = {"com.mobileleader.rpa"}, excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class, Controller.class, RestController.class})})
@EnableAspectJAutoProxy
@Import(value = DataSourceConfiguration.class)
public class RpaApiConfiguration {
    @Bean(name = "apiProperties")
    public static Properties apiProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/api-properties.xml"));
    }

    @Bean(name = "passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "fileReadWriteSupport")
    public FileReadWriteSupport fileReadWriteSupport(
            @Value("#{apiProperties['common.file.repository']}") String repositoryPath) {
        return new FileReadWriteSupport(repositoryPath);
    }

    @Bean(name = "rpaRsaCipher")
    public RpaRsaCipher rsaCipher() {
        return new RpaRsaCipher();
    }
}
