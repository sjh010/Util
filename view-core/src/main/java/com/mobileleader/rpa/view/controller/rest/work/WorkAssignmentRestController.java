package com.mobileleader.rpa.view.controller.rest.work;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.view.model.form.WorkAssignmentAddForm;
import com.mobileleader.rpa.view.model.form.WorkAssignmentModifyForm;
import com.mobileleader.rpa.view.model.form.WorkAssignmentSearchForm;
import com.mobileleader.rpa.view.model.request.WorkAssignmentDeleteRequest;
import com.mobileleader.rpa.view.model.response.WorkAssignmentActivationYnResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddCheckResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddPopupResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentDeleteResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentListResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentModifyPopupResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentModifyResponse;
import com.mobileleader.rpa.view.service.work.WorkAssignmentWebService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work")
public class WorkAssignmentRestController {

    @Autowired
    private WorkAssignmentWebService workAssignmentWebService;

    /**
     * 업무할당 관리 목록을 가져온다.
     *
     * @param form .
     * @return
     */
    @PostMapping(value = "/list")
    public ResponseEntity<WorkAssignmentListResponse> getWorkAssignmentList(
            @RequestBody WorkAssignmentSearchForm form) {
        return new ResponseEntity<WorkAssignmentListResponse>(workAssignmentWebService.getWorkAssignmentList(form),
                HttpStatus.OK);
    }

    /**
     * 업무할당 추가 팝업 호출전 가능여부 체크.
     *
     * @return
     */
    @PostMapping("/add/check")
    public ResponseEntity<WorkAssignmentAddCheckResponse> addCheck() {

        WorkAssignmentAddCheckResponse response = workAssignmentWebService.addCheck();
        return new ResponseEntity<WorkAssignmentAddCheckResponse>(response, HttpStatus.OK);
    }

    /**
     * 업무 할당팝업에서 필요한 로봇, 프로세스 목록 조회.
     *
     * @return
     */
    @PostMapping(value = "/add/data")
    public ResponseEntity<WorkAssignmentAddPopupResponse> getAddWorkAssignmentData() {

        WorkAssignmentAddPopupResponse response = workAssignmentWebService.getWorkAssignmentAddPopup();

        return new ResponseEntity<WorkAssignmentAddPopupResponse>(response, HttpStatus.OK);
    }

    /**
     * 업무 할당팝업에서 업무를 추가.
     *
     * @param form 업무할당 입력 폼
     * @return
     */
    @PostMapping(value = "/add/regist")
    public ResponseEntity<WorkAssignmentAddResponse> addWorkAssignmentRegist(
            @RequestBody @Valid WorkAssignmentAddForm form, BindingResult bindingResult) {

        WorkAssignmentAddResponse body = workAssignmentWebService.addRegist(form);
        ResponseEntity<WorkAssignmentAddResponse> response =
                new ResponseEntity<WorkAssignmentAddResponse>(body, HttpStatus.OK);
        return response;
    }

    /**
     * 업무할당 관리에서 선택한 업무를 삭제 체크한다.
     *
     * @param workDeleteRequest 업무 순번.
     * @return
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<WorkAssignmentDeleteResponse> deleteWorkAssignment(
            @RequestBody WorkAssignmentDeleteRequest workDeleteRequest) {

        WorkAssignmentDeleteResponse response = workAssignmentWebService.deleteWorkAssignment(workDeleteRequest);

        return new ResponseEntity<WorkAssignmentDeleteResponse>(response, HttpStatus.OK);
    }

    /**
     * 업무 활성/비활성 상태를 변경한다.
     *
     * @param workSequenceList 업무 시퀀스
     * @return
     */
    @GetMapping(value = "/activation/{activationYn}")
    public ResponseEntity<WorkAssignmentActivationYnResponse> changeWorkActivationStatusProcess(
            @PathVariable String activationYn, @RequestParam("workSequence") List<Integer> workSequenceList) {
        return new ResponseEntity<WorkAssignmentActivationYnResponse>(
                workAssignmentWebService.changeWorkActivationYn(activationYn, workSequenceList), HttpStatus.OK);
    }

    /**
     * 업무 할당팝업에서 필요한 로봇, 프로세스 목록 조회.
     *
     * @return
     */
    @PostMapping(value = "/modify/data/{workSequence}")
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    public ResponseEntity<WorkAssignmentModifyPopupResponse> getModifyWorkAssignmentData(
            @PathVariable int workSequence) {

        WorkAssignmentModifyPopupResponse r = workAssignmentWebService.getWorkAssignmentModifyPopup(workSequence);

        return new ResponseEntity<WorkAssignmentModifyPopupResponse>(r, HttpStatus.OK);
    }

    /**
     * 업무할당 수정 요청.
     *
     * @param workAssignmentModifyForm 업무 할당 수정 요청
     * @return
     */
    @PostMapping(value = "/modify")
    public ResponseEntity<WorkAssignmentModifyResponse> modifyWorkAssignment(
            @RequestBody @Valid WorkAssignmentModifyForm workAssignmentModifyForm) {

        WorkAssignmentModifyResponse r = workAssignmentWebService.modifyWorkAssignment(workAssignmentModifyForm);

        return new ResponseEntity<WorkAssignmentModifyResponse>(r, HttpStatus.OK);
    }
}
