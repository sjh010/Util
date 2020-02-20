package com.mobileleader.rpa.api.controller.log;

import com.mobileleader.rpa.api.model.request.LogUploadRequest;
import com.mobileleader.rpa.api.model.response.RpaApiBaseResponse;
import com.mobileleader.rpa.api.service.log.LogUploadService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogUploadController {

    @Autowired
    private LogUploadService logUploadService;

    @PostMapping("/studio")
    public ResponseEntity<RpaApiBaseResponse> uploadStudioLog(@Valid LogUploadRequest request,
            BindingResult bindingResult) {
        logUploadService.uploadStudioLog(request);
        return new ResponseEntity<RpaApiBaseResponse>(new RpaApiBaseResponse(), HttpStatus.OK);
    }

    @PostMapping("/robot")
    public ResponseEntity<RpaApiBaseResponse> uploadRobotLog(@Valid LogUploadRequest request,
            BindingResult bindingResult) {
        logUploadService.uploadRobotLog(request);
        return new ResponseEntity<RpaApiBaseResponse>(new RpaApiBaseResponse(), HttpStatus.OK);
    }
}
