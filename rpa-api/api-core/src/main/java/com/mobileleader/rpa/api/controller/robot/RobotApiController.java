package com.mobileleader.rpa.api.controller.robot;

import com.mobileleader.rpa.api.model.request.RobotStatusRequest;
import com.mobileleader.rpa.api.model.response.RpaApiBaseResponse;
import com.mobileleader.rpa.api.service.robot.RobotApiService;
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
@RequestMapping("/robot")
public class RobotApiController {

    @Autowired
    private RobotApiService robotService;

    /**
     * 로봇 상태 변경.
     *
     * @param robotStatusRequest 로봇 상태 코드, 폴링 주기
     * @return {@link RpaApiBaseResponse}
     */
    @PostMapping("/status")
    public ResponseEntity<RpaApiBaseResponse> updateRobotStatus(
            @RequestBody @Valid RobotStatusRequest robotStatusRequest, BindingResult bindingResult) {
        robotService.updateRobotStatusCode(robotStatusRequest);
        return new ResponseEntity<RpaApiBaseResponse>(new RpaApiBaseResponse(), HttpStatus.OK);
    }
}
