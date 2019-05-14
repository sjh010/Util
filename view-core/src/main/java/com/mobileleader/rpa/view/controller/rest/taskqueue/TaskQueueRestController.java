package com.mobileleader.rpa.view.controller.rest.taskqueue;

import com.mobileleader.rpa.view.model.form.TaskQueueSearchForm;
import com.mobileleader.rpa.view.model.response.TaskQueueFileterResponse;
import com.mobileleader.rpa.view.model.response.TaskQueueListResponse;
import com.mobileleader.rpa.view.service.taksqueue.TaskQueueWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/taskqueue")
public class TaskQueueRestController {

    @Autowired
    private TaskQueueWebService taskQueueWebService;

    /**
     * 작업대기열 조회.
     *
     * @param form 검색폼
     * @return
     */
    @PostMapping(value = "/list")
    public ResponseEntity<TaskQueueListResponse> getTaksQueueList(@RequestBody TaskQueueSearchForm form) {
        TaskQueueListResponse response = taskQueueWebService.getTaskQueueList(form);
        return new ResponseEntity<TaskQueueListResponse>(response, HttpStatus.OK);
    }

    /**
     * 작업대기열 조회.
     *
     * @param workSequence 업무 시퀀스
     * @return
     */
    @PostMapping(value = "/list/filter")
    public ResponseEntity<TaskQueueFileterResponse> getTaksQueueListFilter(
            @RequestParam(required = true, name = "workSequence") Integer workSequence) {


        TaskQueueFileterResponse response = taskQueueWebService.getTaskQueueFilterList(workSequence);
        return new ResponseEntity<TaskQueueFileterResponse>(response, HttpStatus.OK);

    }

}
