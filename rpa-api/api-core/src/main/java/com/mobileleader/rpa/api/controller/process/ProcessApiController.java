package com.mobileleader.rpa.api.controller.process;

import com.mobileleader.rpa.api.model.request.ProcessStatusRequest;
import com.mobileleader.rpa.api.model.response.ProcessDownloadResponse;
import com.mobileleader.rpa.api.model.response.ProcessTaskResponse;
import com.mobileleader.rpa.api.model.response.RpaApiBaseResponse;
import com.mobileleader.rpa.api.service.process.ProcessApiService;
import com.mobileleader.rpa.api.support.AuthenticationTokenSupport;
import com.mobileleader.rpa.auth.type.AuthenticationType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessApiController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessApiController.class);

    @Autowired
    private ProcessApiService processService;

    /**
     * 프로세스 요청.
     *
     * @return 작업 정보
     */
    @GetMapping("")
    public ResponseEntity<ProcessTaskResponse> requestProcess() {
        return new ResponseEntity<ProcessTaskResponse>(processService.getProcessTask(
                AuthenticationTokenSupport.getAuthenticationSequence(AuthenticationType.ROBOT)), HttpStatus.OK);
    }

    /**
     * 작업 상태 변경.
     *
     * @param processStatusRequest 작업대기열 순번, 작업 로그 상태 코드
     * @return
     */
    @PostMapping("/status")
    public ResponseEntity<RpaApiBaseResponse> updateJobStatus(@Valid ProcessStatusRequest processStatusRequest,
            BindingResult bindingResult) {
        processService.addProcessTaskLog(processStatusRequest);
        return new ResponseEntity<RpaApiBaseResponse>(new RpaApiBaseResponse(), HttpStatus.OK);
    }

    /**
     * 프로세스 파일 다운로드.
     *
     * @param processVersionSequence 프로세스 버전 순번
     * @param fileSequence 프로세스 파일 순번
     * @return {@link ProcessDownloadResponse}
     */
    @GetMapping("/download")
    public ResponseEntity<ProcessDownloadResponse> downloadProcess(
            @RequestParam(name = "processVersionSequence", required = true) Integer processVersionSequence,
            @RequestParam(name = "fileSequence", required = true) Integer fileSequence, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("** Download Process File ** processVersionSequence : {}, fileSequence : {}",
                processVersionSequence, fileSequence);
        return new ResponseEntity<ProcessDownloadResponse>(
                processService.downloadProcessFile(processVersionSequence, fileSequence, request, response),
                HttpStatus.OK);
    }
}
