package com.mobileleader.rpa.api.controller.authentication;

import com.mobileleader.rpa.api.model.request.RobotAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RobotAuthenticationResponse;
import com.mobileleader.rpa.api.service.authetication.RobotAuthenticationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class RobotAuthenticationController {

    @Autowired
    private RobotAuthenticationService robotAuthenticationService;

    @PostMapping("/robot")
    public ResponseEntity<RobotAuthenticationResponse> getAuthentication(
            @RequestBody(required = true) @Valid RobotAuthenticationRequest request, BindingResult bindingResult) {
        return new ResponseEntity<RobotAuthenticationResponse>(robotAuthenticationService.getAuthentication(request),
                HttpStatus.OK);
    }
}
