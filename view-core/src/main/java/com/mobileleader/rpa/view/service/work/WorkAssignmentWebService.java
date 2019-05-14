package com.mobileleader.rpa.view.service.work;

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
import java.util.List;

public interface WorkAssignmentWebService {

    public WorkAssignmentListResponse getWorkAssignmentList(WorkAssignmentSearchForm form);

    /**
     * 업무할당 추가 팝업에 필요한 정보 조회.
     *
     * @return
     */
    public WorkAssignmentAddPopupResponse getWorkAssignmentAddPopup();

    /**
     * 업무할당 수정 팝업에 필요한 정보 조회.
     *
     * @param workSequence 업무 순번.
     * @return
     */
    public WorkAssignmentModifyPopupResponse getWorkAssignmentModifyPopup(Integer workSequence);

    /**
     * 업무할당 신규 등록.
     *
     * @param form 입력 폼.
     * @return
     */
    public WorkAssignmentAddResponse addRegist(WorkAssignmentAddForm form);

    public WorkAssignmentDeleteResponse deleteWorkAssignment(WorkAssignmentDeleteRequest workDeleteRequest);

    public WorkAssignmentActivationYnResponse changeWorkActivationYn(String workActivationYn,
            List<Integer> processVersionSequenceList);

    /**
     * 업무할당 정보 수정.
     *
     * @param workAssignmentModifyForm 입력 폼.
     * @return
     */
    public WorkAssignmentModifyResponse modifyWorkAssignment(WorkAssignmentModifyForm workAssignmentModifyForm);

    /**
     * 업무관리 목록 화면에서 추가로 할당가능한 프로세스가 있는지 체크.
     *
     * @return
     */
    public WorkAssignmentAddCheckResponse addCheck();



}
