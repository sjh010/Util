package com.mobileleader.rpa.api.controller.authentication;

import com.mobileleader.rpa.api.model.request.StudioAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RpaApiBaseResponse;
import com.mobileleader.rpa.api.model.response.RsaPublicKeyResponse;
import com.mobileleader.rpa.api.model.response.StudioAuthenticationResponse;
import com.mobileleader.rpa.api.service.authetication.StudioAuthenticationService;
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
public class StudioAuthenticationController {
    @Autowired
    private StudioAuthenticationService studioAuthenticationService;

    @PostMapping("/studio")
    public ResponseEntity<StudioAuthenticationResponse> createAuthentication(
            @RequestBody(required = true) @Valid StudioAuthenticationRequest request, BindingResult bindingResult) {
        return new ResponseEntity<StudioAuthenticationResponse>(studioAuthenticationService.create(request),
                HttpStatus.OK);
    }

    @PostMapping("/studio/logout")
    public ResponseEntity<RpaApiBaseResponse> deleteAuthentication() {
        studioAuthenticationService.delete();
        return new ResponseEntity<RpaApiBaseResponse>(new RpaApiBaseResponse(), HttpStatus.OK);
    }

    @PostMapping("/publickey")
    public ResponseEntity<RsaPublicKeyResponse> createPublicKey(
            @RequestBody(required = true) @Valid StudioAuthenticationRequest request, BindingResult bindingResult) {
        return new ResponseEntity<RsaPublicKeyResponse>(studioAuthenticationService.createPublicKey(request),
                HttpStatus.OK);
    }
}
