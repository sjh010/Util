package com.mobileleader.rpa.api.controller.process;

import com.mobileleader.rpa.api.model.request.ProcessCheckInRequest;
import com.mobileleader.rpa.api.model.request.ProcessCheckOutRequest;
import com.mobileleader.rpa.api.model.request.ProcessRegistRequest;
import com.mobileleader.rpa.api.model.response.ProcessCheckInResponse;
import com.mobileleader.rpa.api.model.response.ProcessCheckOutResponse;
import com.mobileleader.rpa.api.model.response.ProcessHistoryResponse;
import com.mobileleader.rpa.api.model.response.ProcessListResponse;
import com.mobileleader.rpa.api.service.process.ProcessCheckInOutService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessCheckInOutController {

    @Autowired
    private ProcessCheckInOutService processCheckInOutService;

    @GetMapping("/list")
    public ResponseEntity<ProcessListResponse> getProcessInfoList() {
        return new ResponseEntity<ProcessListResponse>(processCheckInOutService.getProcessInfoList(), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<ProcessHistoryResponse> getProcessHistory(
            @RequestParam(name = "processSequence", required = true) Integer processSequence) {
        return new ResponseEntity<ProcessHistoryResponse>(processCheckInOutService.getProcessHistory(processSequence),
                HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<ProcessCheckInResponse> registProcess(@Valid ProcessRegistRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<ProcessCheckInResponse>(processCheckInOutService.registProcess(request),
                HttpStatus.OK);
    }

    @PostMapping("/checkin")
    public ResponseEntity<ProcessCheckInResponse> checkInProcess(@Valid ProcessCheckInRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<ProcessCheckInResponse>(processCheckInOutService.checkInProcess(request),
                HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<ProcessCheckOutResponse> checkOutProcess(@RequestBody @Valid ProcessCheckOutRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<ProcessCheckOutResponse>(processCheckInOutService.checkOutProcess(request),
                HttpStatus.OK);
    }

    @PostMapping("/checkout/cancel")
    public ResponseEntity<ProcessCheckOutResponse> cancelCheckOutProcess(
            @RequestBody @Valid ProcessCheckOutRequest request, BindingResult bindingResult) {
        return new ResponseEntity<ProcessCheckOutResponse>(processCheckInOutService.cancelCheckOutProcess(request),
                HttpStatus.OK);
    }
}
