package com.mobileleader.rpa.api.service.authetication;

import com.mobileleader.rpa.api.model.request.RobotAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RobotAuthenticationResponse;

public interface RobotAuthenticationService {
    public RobotAuthenticationResponse getAuthentication(RobotAuthenticationRequest request);
}
