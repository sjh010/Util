package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.auth.model.response.AuthenticationResponse;

public class RobotAuthenticationResponse extends AuthenticationResponse {

    private String robotName;

    private String healthCheckInterval = "10000";

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public RobotAuthenticationResponse(Builder builder) {
        super(new AuthenticationResponse.Builder().authenticationToken(builder.authenticationToken));
        robotName = builder.robotName;
        healthCheckInterval = builder.healthCheckInterval;
    }

    public String getRobotName() {
        return robotName;
    }

    public String getHealthCheckInterval() {
        return healthCheckInterval;
    }

    public static class Builder {
        private String authenticationToken;

        private String robotName;

        private String healthCheckInterval;

        public Builder authenticationToken(String authenticationToken) {
            this.authenticationToken = authenticationToken;
            return this;
        }

        public Builder robotName(String robotName) {
            this.robotName = robotName;
            return this;
        }

        public Builder healthCheckInterval(String healthCheckInterval) {
            this.healthCheckInterval = healthCheckInterval;
            return this;
        }

        public RobotAuthenticationResponse build() {
            return new RobotAuthenticationResponse(this);
        }
    }
}
