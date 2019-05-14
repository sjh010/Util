package com.mobileleader.rpa.view.rest.type;

import com.mobileleader.rpa.utils.rest.type.RestApiType;
import org.springframework.http.HttpMethod;

public enum RepositoryReloadApi implements RestApiType {
    // @formatter:off
    RELOAD_USER_REPOSITORY("/common/repository/user/reload", HttpMethod.POST, Void.class),
    RELOAD_CODE_AND_CONFIG_REPOSITORY("/common/repository/code/reload", HttpMethod.POST, Void.class),
    UNKNOWN("", null, null);
    // @formatter:on

    private final String url;

    private final HttpMethod httpMethod;

    private final Class<?> responseClass;

    private RepositoryReloadApi(String url, HttpMethod httpMethod, Class<?> responseClass) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.responseClass = responseClass;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Class<?> getResponseClass() {
        return responseClass;
    }
}
