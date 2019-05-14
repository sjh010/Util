package com.mobileleader.rpa.view.service.work;

import com.mobileleader.rpa.auth.type.RpaAuthority;
import com.mobileleader.rpa.data.dto.biz.ProcessVersion;
import com.mobileleader.rpa.data.dto.biz.Work;
import com.mobileleader.rpa.utils.rest.RestClientExecutorException;
import com.mobileleader.rpa.view.data.dto.ProcessNameVersion;
import com.mobileleader.rpa.view.data.dto.RobotName;
import com.mobileleader.rpa.view.data.dto.WorkAssignedRobot;
import com.mobileleader.rpa.view.data.dto.WorkModify;
import com.mobileleader.rpa.view.data.dto.WorkRegist;
import com.mobileleader.rpa.view.data.mapper.biz.ProcessVersionMapper;
import com.mobileleader.rpa.view.data.mapper.biz.RobotMapper;
import com.mobileleader.rpa.view.data.mapper.biz.WorkMapper;
import com.mobileleader.rpa.view.exception.RpaViewRestError;
import com.mobileleader.rpa.view.exception.RpaViewRestException;
import com.mobileleader.rpa.view.model.form.WorkAssignmentAddForm;
import com.mobileleader.rpa.view.model.form.WorkAssignmentModifyForm;
import com.mobileleader.rpa.view.model.form.WorkAssignmentSearchForm;
import com.mobileleader.rpa.view.model.request.WorkAssignmentDeleteRequest;
import com.mobileleader.rpa.view.model.response.WorkAssignmentActivationYnResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentActivationYnResponse.WorkAssignmentActivationYnErrorCode;
import com.mobileleader.rpa.view.model.response.WorkAssignmentActivationYnResponse.WorkAssignmentActivationYnResultCode;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddCheckResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddCheckResponse.WorkAssignmentAddCheckResultCode;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddPopupResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentAddResponse.WorkAssignmentAddErrorCode;
import com.mobileleader.rpa.view.model.response.WorkAssignmentDeleteResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentDeleteResponse.WorkAssignmentDeleteResultCode;
import com.mobileleader.rpa.view.model.response.WorkAssignmentListResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentModifyPopupResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentModifyResponse;
import com.mobileleader.rpa.view.model.response.WorkAssignmentModifyResponse.WorkAssignmentModifyErrorCode;
import com.mobileleader.rpa.view.support.UserDetailsSupport;
import com.mobileleader.rpa.view.support.WorkAssignmentScheduleSupport;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class WorkAssignmentWebServiceImpl implements WorkAssignmentWebService {

    @Autowired
    private ProcessVersionMapper processVersionMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkAssignmentScheduleSupport workAssignmentScheduleSupport;


    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    @Transactional
    public WorkAssignmentListResponse getWorkAssignmentList(WorkAssignmentSearchForm form) {
        WorkAssignmentListResponse response = new WorkAssignmentListResponse();
        int count = workMapper.selectCount(form);
        response.setTotalCount(count);
        form.resetPageNoIfNotValid(count);

        if (count > 0) {
            response.setList(workMapper.selectWorkAssignmentList(form));
        }
        response.setForm(form);
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentAddPopupResponse getWorkAssignmentAddPopup() {

        List<RobotName> robotNameList = getWorkAssignmentTargetRobot(null);
        List<ProcessNameVersion> processVersionList = getWorkAssignmentTargetProcessName(null);

        WorkAssignmentAddPopupResponse response = new WorkAssignmentAddPopupResponse();

        response.setRobotNameList(robotNameList);
        response.setProcessVersionList(processVersionList);

        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_READ)
    @Transactional
    public WorkAssignmentModifyPopupResponse getWorkAssignmentModifyPopup(Integer workSequence) {

        List<RobotName> robotNameList = getWorkAssignmentTargetRobot(workSequence);
        List<ProcessNameVersion> processVersionList = getWorkAssignmentTargetProcessName(workSequence);

        WorkAssignmentModifyPopupResponse response = new WorkAssignmentModifyPopupResponse();

        response.setRobotNameList(robotNameList);
        response.setProcessVersionList(processVersionList);

        // 수정의 경우 기존 정보를 추가로 조회함.
        if (workSequence != null) {
            // 기작성 업무
            Work work = workMapper.selectByPrimaryKey(workSequence);
            Assert.notNull(work, "등록한 업무가 없음.");
            response.setWork(work);

            // 기등록 타켓 프로세스/버전
            Integer processVersionSequence = work.getProcessVersionSequence();

            ProcessNameVersion processNameVersion =
                    processVersionMapper.selectProcessNameVersionByPrimaryKey(processVersionSequence);
            Assert.notNull(processNameVersion, "프로세스가  없음.");
            response.setProcessNameVersion(processNameVersion);

            // 기등록 타켓 로봇
            List<WorkAssignedRobot> workAssignedRobotList = workMapper.selectWorkAssignedRobot(workSequence);
            response.setWorkAssignedRobotList(workAssignedRobotList);
        }
        return response;
    }



    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentAddResponse addRegist(WorkAssignmentAddForm form) {

        WorkAssignmentAddResponse response = new WorkAssignmentAddResponse();

        // 로봇이 다른 업무할당에 있는지 확인.
        List<String> otherWorkAssignedRobotNameList =
                robotMapper.selectOtherWorkAssignedRobotName(null, form.getRobotSequenceList());
        if (otherWorkAssignedRobotNameList.isEmpty() == false) {
            response.setErrorCode(WorkAssignmentAddErrorCode.ROBOT_IS_ASSIGNED);
            response.setOtherWorkAssignedRobotNameList(otherWorkAssignedRobotNameList);
            return response;
        }

        // 활성화 프로세스 여부 확인.
        ProcessVersion processVersion = processVersionMapper.selectByPrimaryKey(form.getProcessVersionSequence());
        if (processVersion == null || processVersion.getActivationYn().equals("Y") == false) {
            response.setErrorCode(WorkAssignmentAddErrorCode.PROCESS_IS_INACTIVE);
            return response;
        }

        // 업무는 하나만 생성..
        WorkRegist workRegist = new WorkRegist();
        workRegist.setProcessVersionSequence(form.getProcessVersionSequence());
        workRegist.setRepeatCycleUnitCode(form.getRepeatCycleUnitCd());
        workRegist.setRepeatCycle(1); // 매년, 매월, 매일의 step = 1.
        workRegist.setExecuteStandardValue(form.getExecuteStandardValue());
        workRegist.setExecuteHourminute(form.getExecHourMinute());
        workRegist.setActivationYn(form.getWorkActiveYn());
        workRegist.setRegisterId(UserDetailsSupport.getUserId());

        workMapper.registWork(workRegist);
        workAssignmentScheduleSupport.addWorkAssignmentSchedule(form, workRegist.getWorkSequence());
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentDeleteResponse deleteWorkAssignment(WorkAssignmentDeleteRequest workDeleteRequest) {

        List<Integer> workSequenceList = workDeleteRequest.getWorkSequenceList();

        WorkAssignmentDeleteResponse response = new WorkAssignmentDeleteResponse();
        // 로봇이 실행중이면 삭제 불가.
        int workingRobotCount = robotMapper.selectWorkingRobotCount(workSequenceList);
        if (workingRobotCount > 0) {
            response.setResultCode(WorkAssignmentDeleteResultCode.ROBOT_IS_WORKING);
            return response;
        }

        for (Integer workSequence : workSequenceList) {
            Work work = workMapper.selectByPrimaryKey(workSequence);

            work.setRemoveId(UserDetailsSupport.getUserId());
            work.setRemoveYn("Y");

            workMapper.updateRemoveStatus(work);
            workAssignmentScheduleSupport.deleteWorkSchedule(work.getWorkSequence(), "Y");
        }
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentActivationYnResponse changeWorkActivationYn(String activationYn,
            List<Integer> workSequenceList) {

        WorkAssignmentActivationYnResponse response = new WorkAssignmentActivationYnResponse();
        // 실행중인 프로세스(로봇) 인지 체크
        if ("N".equals(activationYn)) {
            int processWorkingYCount = robotMapper.selectWorkingRobotCount(workSequenceList);
            if (processWorkingYCount > 0) {
                response.setErrorCode(WorkAssignmentActivationYnErrorCode.ROBOT_IS_WORKING);
                return response;
            }
        } else if ("Y".equals(activationYn)) {
            // 로봇이 다른 업무할당에 있는지 확인.
            List<Integer> workAssignedRobotList = robotMapper.selectOtherWorkAssignedRobotList(workSequenceList);
            if (workAssignedRobotList.isEmpty() == false) {
                response.setErrorCode(WorkAssignmentActivationYnErrorCode.ROBOT_IS_ASSIGNED);
                return response;
            }
        }

        int workActivationYCount = 0;
        int workActivationNCount = 0;
        int changedCount = 0;
        for (Integer workSequence : workSequenceList) {
            Work work = workMapper.selectByPrimaryKey(workSequence);
            if (work != null) {
                if (("Y".equals(activationYn) && "N".equals(work.getActivationYn()))
                        || ("N".equals(activationYn) && "Y".equals(work.getActivationYn()))) {
                    // 상태 변경 가능 업무
                    String modifyId = UserDetailsSupport.getUserId();
                    changedCount += workMapper.updateActivationYn(workSequence, activationYn, modifyId);
                    try {
                        if ("Y".equalsIgnoreCase(activationYn)) {
                            workAssignmentScheduleSupport.addWorkAssignmentSchedule(work);
                        } else {
                            workAssignmentScheduleSupport.deleteWorkSchedule(work.getWorkSequence(), "N");
                        }
                    } catch (RestClientExecutorException e) {
                        throw new RpaViewRestException(RpaViewRestError.INTERNAL_SERVER_ERROR);
                    }
                } else if ("Y".equals(activationYn) && "Y".equals(work.getActivationYn())) {
                    // 이미 활성화된 업무
                    workActivationYCount++;
                } else if ("N".equals(activationYn) && "N".equals(work.getActivationYn())) {
                    // 이미 비활성화된 업무
                    workActivationNCount++;
                }
            }
        }
        if (changedCount > 0) {
            if (workActivationYCount > 0) {
                response.setResultCode(WorkAssignmentActivationYnResultCode.ACTIVATE_ONLY_INACTIVED);
            } else if (workActivationNCount > 0) {
                response.setResultCode(WorkAssignmentActivationYnResultCode.INACTIVATE_ONLY_ACTIVED);
            }
        }


        response.setChangedCount(changedCount);
        response.setWorkActivationNCount(workActivationNCount);
        response.setWorkActivationYCount(workActivationYCount);
        return response;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentModifyResponse modifyWorkAssignment(WorkAssignmentModifyForm form) {
        WorkAssignmentModifyResponse response = new WorkAssignmentModifyResponse();

        // 실행중인 로봇이 있으면 업무를 수정 불가.
        List<Integer> robotSequenceList = form.getRobotSequenceAddList();
        List<Integer> workingRobotSequenceList = robotMapper.selectWorkingRobotSequence(robotSequenceList);
        if (workingRobotSequenceList.size() > 0) {
            response.setErrorCode(WorkAssignmentModifyErrorCode.ROBOT_IS_WORKING);
            return response;
        }

        // 로봇이 다른 업무할당에 있는지 확인.
        List<String> otherWorkAssignedRobotNameList =
                robotMapper.selectOtherWorkAssignedRobotName(form.getWorkSequence(), form.getRobotSequenceList());
        if (otherWorkAssignedRobotNameList.isEmpty() == false) {
            response.setErrorCode(WorkAssignmentModifyErrorCode.ROBOT_IS_ASSIGNED);
            response.setOtherWorkAssignedRobotNameList(otherWorkAssignedRobotNameList);
            return response;
        }

        int workSequence = form.getWorkSequence();
        Work work = workMapper.selectByPrimaryKey(workSequence);
        Assert.notNull(work, "존재하지 않는 업무입니다.");

        // 업무 수정.
        WorkModify workModify = new WorkModify();
        workModify.setWorkSequence(workSequence);
        workModify.setRepeatCycleUnitCode(form.getRepeatCycleUnitCd());
        workModify.setRepeatCycle(1); // 매년, 매월, 매일의 step = 1.
        workModify.setExecuteStandardValue(form.getExecuteStandardValue());
        workModify.setExecuteHourminute(form.getExecHourMinute());
        workModify.setActivationYn(form.getWorkActiveYn());
        workModify.setModifyId(UserDetailsSupport.getUserId());

        workMapper.updateWork(workModify);
        workAssignmentScheduleSupport.deleteWorkSchedule(workSequence, form.getRobotSequenceRemoveList(), "Y");
        workAssignmentScheduleSupport.addWorkAssignmentSchedule(form);
        return response;
    }

    /**
     * 선택가능한 대상 로봇을 조회한다.
     *
     * @param workSequence 작성중인 업무순번(신규 추가는 null)
     * @return
     */
    private List<RobotName> getWorkAssignmentTargetRobot(Integer workSequence) {

        List<RobotName> workAssignTargetRobotList = robotMapper.selectAvailableTargetRobot(workSequence);
        return workAssignTargetRobotList;
    }

    /**
     * 선택가능한 대상 프로세스를 조회한다.
     *
     * @param workSequence 작성중인 업무순번(신규 추가는 null)
     * @return
     */
    private List<ProcessNameVersion> getWorkAssignmentTargetProcessName(Integer workSequence) {
        List<ProcessNameVersion> processVersionList = processVersionMapper.selectActiveProcess(workSequence);
        return processVersionList;
    }

    @Override
    @Secured(RpaAuthority.SecuredRole.MANAGER_WORK_ASSIGNMENT_MODIFY)
    @Transactional
    public WorkAssignmentAddCheckResponse addCheck() {
        WorkAssignmentAddCheckResponse response = new WorkAssignmentAddCheckResponse();
        int cnt = processVersionMapper.selectActiveProcessCount();
        if (cnt == 0) {
            response.setResultCode(WorkAssignmentAddCheckResultCode.NO_ASSIGNALBE_PROCESS);
        }

        return response;
    }
}
