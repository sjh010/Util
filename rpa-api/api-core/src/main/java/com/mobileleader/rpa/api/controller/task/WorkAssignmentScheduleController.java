package com.mobileleader.rpa.api.controller.task;

import com.mobileleader.rpa.api.model.request.DailyScheduleRequest;
import com.mobileleader.rpa.api.model.request.HourlyScheduleRequest;
import com.mobileleader.rpa.api.model.request.TaskQueueControlRequest;
import com.mobileleader.rpa.api.model.request.WeeklyScheduleRequest;
import com.mobileleader.rpa.api.service.task.TaskQueueScheduleService;
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
@RequestMapping("/schedule/work")
public class WorkAssignmentScheduleController {

    @Autowired
    private TaskQueueScheduleService taskQueueScheduleService;


    @PostMapping("/hourly")
    public ResponseEntity<String> createHourlyWorkAssignmentSchedule(@RequestBody @Valid HourlyScheduleRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<String>(taskQueueScheduleService.createHourlyWorkAssignmentSchedule(request),
                HttpStatus.OK);
    }

    @PostMapping("/daily")
    public ResponseEntity<String> createDailyWorkAssignmentSchedule(@RequestBody @Valid DailyScheduleRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<String>(taskQueueScheduleService.createDailyWorkAssignmentSchedule(request),
                HttpStatus.OK);
    }

    @PostMapping("/weekly")
    public ResponseEntity<String> createWeeklyWorkAssignmentSchedule(@RequestBody @Valid WeeklyScheduleRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<String>(taskQueueScheduleService.createWeeklyWorkAssignmentSchedule(request),
                HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Boolean> removeWorkAssignment(@RequestBody @Valid TaskQueueControlRequest request,
            BindingResult bindingResult) {
        return new ResponseEntity<Boolean>(taskQueueScheduleService.removeWorkAssignmentSchedule(request),
                HttpStatus.OK);
    }
}
