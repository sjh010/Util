package com.mobileleader.rpa.view.config.datasource;

import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

public class DataSourceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceInitializer.class);

    /**
     * api-properties.xml jdbc.dataSourceType에 따라 DBCP/JNDI DataSource를 리턴
     *
     * @return DataSource Bean
     * @throws Exception if an error occurs creating the data source
     */
    public static DataSource getDataSource(String dataSourceType, String jndiName) {
        DataSource dataSource = null;
        if (DataSourceType.JNDI.name().equalsIgnoreCase(dataSourceType)) {
            dataSource = getJndiObjectFactoryBean(jndiName);
        } else if (DataSourceType.DBCP2.name().equalsIgnoreCase(dataSourceType)) {
            dataSource = DbcpDataSourceFactory.getDbcpDataSource();
        } else {
            logger.error("[DataSourceInitializer exception] Invalid DataSourceType : {}", dataSourceType);
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR,
                    "[DataSourceInitializer exception] Invalid DataSourceType : " + dataSourceType);
        }
        return dataSource;
    }

    private static DataSource getJndiObjectFactoryBean(String jndiName) {
        JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
        return jndiDataSourceLookup.getDataSource(jndiName);
    }

    public enum DataSourceType {
        JNDI, DBCP2;
    }
}
