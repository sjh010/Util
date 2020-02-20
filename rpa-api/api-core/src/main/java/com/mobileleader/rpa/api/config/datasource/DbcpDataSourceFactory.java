package com.mobileleader.rpa.api.config.datasource;

import com.mobileleader.rpa.api.exception.RpaApiError;
import com.mobileleader.rpa.api.exception.RpaApiException;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DbcpDataSourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DbcpDataSourceFactory.class);

    public static BasicDataSource getDbcpDataSource() {
        return new DbcpDataSourceFactory().createDbcp2DataSource();
    }

    private BasicDataSource createDbcp2DataSource() {
        BasicDataSource dataSource = null;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(getDbcpProperties());
        } catch (Exception e) {
            logger.info("[createDbcp2DataSource exception]", e);
            throw new RpaApiException(RpaApiError.INTERNAL_SERVER_ERROR, "[createDbcp2DataSource exception]");
        }
        return dataSource;
    }

    private Properties getDbcpProperties() {
        Properties properties = null;
        try {
            Resource resource = new ClassPathResource("properties/dbcp-properties.xml");
            properties = PropertiesLoaderUtils.loadProperties(resource);
            String encryptType = (String) properties.get("encryptType");
            properties.put("username", decrypt(encryptType, properties.getProperty("username")));
            properties.put("password", decrypt(encryptType, properties.getProperty("password")));
        } catch (IOException e) {
            logger.error("[getDbcpProperties exception]", e);
            throw new RpaApiException(RpaApiError.INTERNAL_SERVER_ERROR, "[getDbcpProperties exception]");
        }
        return properties;
    }

    private String decrypt(String encryptType, String source) {
        String decryptedValue = null;
        if (DbcpEncryptType.PLAIN.name().equalsIgnoreCase(encryptType)) {
            decryptedValue = source;
        } else if (DbcpEncryptType.BASE64.name().equalsIgnoreCase(encryptType)) {
            // decryptedValue = MessageDigestSupport.decodeBase64String(source);
        }
        return decryptedValue;
    }

    protected enum DbcpEncryptType {
        PLAIN, BASE64;
    }
}
