package com.mobileleader.rpa.view.config.datasource;

import java.io.IOException;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan(basePackages = {"com.mobileleader.rpa.**.data.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {

    @Value("#{viewProperties['jdbc.dataSourceType']}")
    private String dataSourceType;

    @Value("#{viewProperties['jdbc.jndiName']}")
    private String jndiName;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return DataSourceInitializer.getDataSource(dataSourceType, jndiName);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * SqlSessionFactory Bean 생성.
     *
     * @return {@link SqlSessionFactoryBean}
     * @throws IOException mapperLocations not found exception.
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean
                .setMapperLocations(resolver.getResources("classpath*:com/mobileleader/rpa/**/data/mapper/**/*.xml"));
        sqlSessionFactoryBean.setTypeHandlersPackage("com.mobileleader.rpa.data.typehandler");
        return sqlSessionFactoryBean;
    }
}
