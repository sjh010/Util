package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;

public class RobotStatusRequest {

    @NotNull
    private String robotStatusCode;

    @NotNull
    private String healthCheckInterval;

    public String getRobotStatusCode() {
        return robotStatusCode;
    }

    public void setRobotStatusCode(String robotStatusCode) {
        this.robotStatusCode = robotStatusCode;
    }

    public String getHealthCheckInterval() {
        return healthCheckInterval;
    }

    public void setHealthCheckInterval(String healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RobotStatusRequest [robotStatusCode=").append(robotStatusCode).append(", healthCheckInterval=")
                .append(healthCheckInterval).append("]");
        return builder.toString();
    }
}
