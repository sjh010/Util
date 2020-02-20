package com.mobileleader.rpa.api.model.response;

import com.mobileleader.rpa.auth.model.response.AuthenticationResponse;

public class StudioAuthenticationResponse extends AuthenticationResponse {

    private String userName;

    /**
     * Constructor.
     *
     * @param builder {@link Builder}
     */
    public StudioAuthenticationResponse(Builder builder) {
        super(new AuthenticationResponse.Builder().authenticationToken(builder.authenticationToken));
        userName = builder.userName;
    }

    public String getUserName() {
        return userName;
    }

    public static class Builder {
        private String authenticationToken;

        private String userName;

        public Builder authenticationToken(String authenticationToken) {
            this.authenticationToken = authenticationToken;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public StudioAuthenticationResponse build() {
            return new StudioAuthenticationResponse(this);
        }
    }
}
