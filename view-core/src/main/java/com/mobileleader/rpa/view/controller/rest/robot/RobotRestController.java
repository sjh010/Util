package com.mobileleader.rpa.view.controller.rest.robot;

import com.mobileleader.rpa.view.model.form.RobotAddForm;
import com.mobileleader.rpa.view.model.form.RobotLogSearchForm;
import com.mobileleader.rpa.view.model.form.RobotSearchForm;
import com.mobileleader.rpa.view.model.response.RobotAddCheckResponse;
import com.mobileleader.rpa.view.model.response.RobotListResponse;
import com.mobileleader.rpa.view.model.response.RobotLogFileterResponse;
import com.mobileleader.rpa.view.model.response.RobotLogListResponse;
import com.mobileleader.rpa.view.service.robot.RobotWebService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/robot")
public class RobotRestController {

    @Autowired
    private RobotWebService robotService;

    /**
     * 로봇 목록을 조회한다.
     *
     * @param form .
     * @return
     */
    @PostMapping(value = "/list")
    public ResponseEntity<RobotListResponse> getRobotList(@RequestBody RobotSearchForm form) {
        return new ResponseEntity<RobotListResponse>(robotService.getRobotList(form), HttpStatus.OK);
    }

    /**
     * 연결PC명 조회.
     *
     * @return
     */
    @PostMapping(value = "/list/pcName")
    public ResponseEntity<List<String>> getPcNameList() {
        return new ResponseEntity<List<String>>(robotService.getPcNameList(), HttpStatus.OK);
    }

    /**
     * 로봇 로그 리스트 조회.
     *
     * @param form 검색폼
     * @return
     */
    @PostMapping(value = "/log")
    public ResponseEntity<RobotLogListResponse> getRobotLogList(@RequestBody RobotLogSearchForm form) {
        return new ResponseEntity<RobotLogListResponse>(robotService.getRobotLogList(form), HttpStatus.OK);
    }

    /**
     * 로봇 로그 필터 조회.
     *
     * @param robotSequence 로봇 순번
     * @return
     */
    @PostMapping(value = "/log/filter")
    public ResponseEntity<RobotLogFileterResponse> getRobotLogFilterList(
            @RequestParam(required = true, name = "robotSequence") Integer robotSequence) {
        return new ResponseEntity<RobotLogFileterResponse>(robotService.getRobotLogFilterList(robotSequence),
                HttpStatus.OK);
    }

    /**
     * 로봇을 추가한다.
     *
     * @param form .
     * @return
     */
    @PostMapping(value = "/add/process")
    public ResponseEntity<Integer> addRobotProcess(@RequestBody RobotAddForm form) {
        return new ResponseEntity<Integer>(robotService.addRobot(form), HttpStatus.OK);
    }

    @PostMapping(value = "/add/check")
    public ResponseEntity<RobotAddCheckResponse> checkPcNameAndIp(@RequestBody RobotAddForm form) {
        return new ResponseEntity<RobotAddCheckResponse>(robotService.checkRobotAddValidity(form), HttpStatus.OK);
    }

    /**
     * 로봇을 삭제한다.
     *
     * @param robotSequenceList .
     * @return
     */
    @GetMapping(value = "/delete/process")
    public ResponseEntity<Integer> deleteRobotProcess(@RequestParam("robotSequence") List<Integer> robotSequenceList) {
        return new ResponseEntity<Integer>(robotService.deleteRobot(robotSequenceList), HttpStatus.OK);
    }

    /**
     * 로봇 로그 엑셀 파일 생성.
     *
     * @param form 검색폼
     * @return 엑셀 파일 아이디
     */
    @PostMapping(value = "/log/excel")
    public ResponseEntity<String> createRobotLogExcel(@RequestBody RobotLogSearchForm form) {
        return new ResponseEntity<String>(robotService.createRobotLogExcel(form), HttpStatus.OK);
    }

    /**
     * 로봇 로그 엑셀 다운로드.
     *
     * @param uuid 엑셀 파일 아이디
     * @param request request
     * @param response response
     */
    @GetMapping(value = "/log/excel/download")
    public void downloadRobotLogExcel(@RequestParam(required = true, name = "uuid") String uuid,
            HttpServletRequest request, HttpServletResponse response) {
        robotService.downloadExcel(uuid, request, response);
    }

    /**
     * 로봇 로그 오류 파일 다운로드.
     *
     * @param fileGroupSequence 파일 그룹 순번
     * @param robotName 로봇명
     * @param request request
     * @param response response
     */
    @GetMapping(value = "/log/error/download")
    public void downloadErrorLogFile(
            @RequestParam(required = true, name = "fileGroupSequence") Integer fileGroupSequence,
            @RequestParam(required = false, name = "robotName", defaultValue = "NoName") String robotName,
            HttpServletRequest request, HttpServletResponse response) {
        robotService.downloadErrorLogFile(fileGroupSequence, robotName, request, response);
    }
}
