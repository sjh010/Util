package com.mobileleader.rpa.view.controller.rest.process;

import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.view.model.form.ProcessHistorySearchForm;
import com.mobileleader.rpa.view.model.form.ProcessSearchForm;
import com.mobileleader.rpa.view.model.request.ProcessDeleteRequest;
import com.mobileleader.rpa.view.model.response.ProcessHistoryListResponse;
import com.mobileleader.rpa.view.model.response.ProcessListResponse;
import com.mobileleader.rpa.view.service.process.ProcessWebService;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessRestController.class);

    @Autowired
    private ProcessWebService processService;

    /**
     * 프로세스 조회.
     *
     * @param processVersionSequence 프로세스 버전 순번
     * @return {@link ProcessVersion}
     */
    @PostMapping
    public ResponseEntity<ProcessVersion> getProcess(
            @RequestParam(required = true, name = "processVersionSequence") Integer processVersionSequence) {
        return new ResponseEntity<ProcessVersion>(processService.getProcessVersion(processVersionSequence),
                HttpStatus.OK);
    }

    /**
     * 프로세스 목록 요청.
     *
     * @param processSearchForm 검색폼
     * @return {@link ProcessListResponse}
     */
    @PostMapping(value = "/list")
    public ResponseEntity<ProcessListResponse> getProcessList(@RequestBody @Valid ProcessSearchForm processSearchForm,
            BindingResult bindingResult) {
        logger.info("** Get Process List ** sortKey : {}, sortOrder : {}", processSearchForm.getSortKey(),
                processSearchForm.getSortOrder());

        return new ResponseEntity<ProcessListResponse>(processService.getProcessList(processSearchForm), HttpStatus.OK);
    }

    /**
     * 프로세스 이력 요청.
     *
     * @param processHistorySearchForm 검색폼
     * @return {@link ProcessHistoryListResponse}
     */
    @PostMapping(value = "/history")
    public ResponseEntity<ProcessHistoryListResponse> getProcessHistory(
            @RequestBody @Valid ProcessHistorySearchForm processHistorySearchForm, BindingResult bindingResult) {
        logger.info("** Get Process History ** : {}", processHistorySearchForm.toString());

        return new ResponseEntity<ProcessHistoryListResponse>(
                processService.getProcessHistory(processHistorySearchForm), HttpStatus.OK);
    }

    /**
     * 프로세스 체크인.
     *
     * @param processVersionSequenceList 프로세스 버전 순번 리스트
     * @return 체크인 프로세스 수
     */
    @PostMapping(value = "/modify/checkIn")
    public ResponseEntity<Integer> checkInProcess(@RequestBody List<Integer> processVersionSequenceList) {
        logger.info("** Check In Process ** {}", Arrays.toString(processVersionSequenceList.toArray()));

        return new ResponseEntity<Integer>(processService.checkInProcess(processVersionSequenceList), HttpStatus.OK);
    }

    /**
     * 프로세스 사용 안함 상태 변경.
     *
     * @param processVersionSequence 프로세스 버전 순번
     * @return 상태 변경 프로세스 수
     */
    @PostMapping(value = "/status/off")
    public ResponseEntity<Integer> setProcessStatusOff(
            @RequestParam(required = true, name = "processVersionSequence") Integer processVersionSequence) {
        logger.info("** Off Process Status ** {}", processVersionSequence);

        return new ResponseEntity<Integer>(processService.setProcessStatusOff(processVersionSequence), HttpStatus.OK);
    }

    /**
     * 프로세스 사용.
     *
     * @param processVersionSequence 프로세스 버전 순번
     * @return 사용 프로세스 수
     */
    @PostMapping(value = "/status/on")
    public ResponseEntity<Integer> setProcessStatusOn(
            @RequestParam(required = true, name = "processVersionSequence") Integer processVersionSequence) {
        logger.info("** On Process Status ** {}", processVersionSequence);

        return new ResponseEntity<Integer>(processService.setProcessStatusOn(processVersionSequence), HttpStatus.OK);
    }

    /**
     * 프로세스 삭제.
     *
     * @param processDeleteReuqest 프로세스 버전 순번 리스트 및 프로세스 순번 리스트
     * @return 삭제 프로세스 수
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<Integer> deleteProcess(@RequestBody @Valid ProcessDeleteRequest processDeleteReuqest,
            BindingResult bindingResult) {
        logger.info("** Delete Process Version Sequence ** {}", processDeleteReuqest.toString());

        return new ResponseEntity<Integer>(processService.deleteProcess(processDeleteReuqest), HttpStatus.OK);
    }

}
