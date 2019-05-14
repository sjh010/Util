package com.mobileleader.rpa.view.rest.type;

import com.mobileleader.rpa.utils.rest.type.RestApiType;
import org.springframework.http.HttpMethod;

public enum WorkAssignmentScheduleApi implements RestApiType {
    // @formatter:off
    CREATE_HOURLY_SCHEDULE("/schedule/work/hourly", HttpMethod.POST, String.class),
    CREATE_DAILY_SCHEDULE("/schedule/work/daily", HttpMethod.POST, String.class),
    CREATE_WEEKLY_SCHEDULE("/schedule/work/weekly", HttpMethod.POST, String.class),
    REMOVE_SCHEDULE("/schedule/work/remove", HttpMethod.POST, Boolean.class),
    UNKNOWN("", null, null);
    // @formatter:on

    private final String url;

    private final HttpMethod httpMethod;

    private final Class<?> responseClass;

    private WorkAssignmentScheduleApi(String url, HttpMethod httpMethod, Class<?> responseClass) {
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
