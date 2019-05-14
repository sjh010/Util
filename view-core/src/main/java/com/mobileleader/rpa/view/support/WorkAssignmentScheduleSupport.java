package com.mobileleader.rpa.view.support;

import com.mobileleader.rpa.data.dto.biz.Work;
import com.mobileleader.rpa.data.dto.biz.WorkAssignment;
import com.mobileleader.rpa.data.type.RepeatCycleUnitCode;
import com.mobileleader.rpa.utils.rest.RestClientExecutor;
import com.mobileleader.rpa.utils.rest.RestClientExecutorException;
import com.mobileleader.rpa.utils.rest.RestClientManager;
import com.mobileleader.rpa.view.data.mapper.biz.WorkAssignmentMapper;
import com.mobileleader.rpa.view.exception.RpaViewRestError;
import com.mobileleader.rpa.view.exception.RpaViewRestException;
import com.mobileleader.rpa.view.model.form.WorkAssignmentAddForm;
import com.mobileleader.rpa.view.model.form.WorkAssignmentModifyForm;
import com.mobileleader.rpa.view.rest.model.DailyScheduleRequest;
import com.mobileleader.rpa.view.rest.model.HourlyScheduleRequest;
import com.mobileleader.rpa.view.rest.model.TaskQueueControlRequest;
import com.mobileleader.rpa.view.rest.model.TaskQueueScheduleRequest;
import com.mobileleader.rpa.view.rest.model.WeeklyScheduleRequest;
import com.mobileleader.rpa.view.rest.type.WorkAssignmentScheduleApi;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class WorkAssignmentScheduleSupport {

    @Autowired
    private RestClientManager restClientManager;

    @Autowired
    private WorkAssignmentMapper workAssignmentMapper;

    /**
     * 업무할당 스케줄을 삭제한다.
     *
     * @param workSequence 업무순번
     */
    public void deleteWorkSchedule(Integer workSequence, String removeYn) {
        List<WorkAssignment> workAssignments = workAssignmentMapper.selectByWorkSequence(workSequence);
        for (WorkAssignment workAssignment : workAssignments) {
            deleteWorkSchedule(workSequence, workAssignment.getRobotSequence(), removeYn);
        }
    }

    /**
     * 업무할당 스케줄을 삭제한다.
     *
     * @param workSequence 업무순번
     * @param robotSequence 로봇순번
     */
    public void deleteWorkSchedule(Integer workSequence, Integer robotSequence, String removeYn) {
        try {
            boolean isSuccess = restClientManager.createExecutor().apiType(WorkAssignmentScheduleApi.REMOVE_SCHEDULE)
                    .request(new RestClientExecutor.JsonRequestBodyBuilder()
                            .addObject(new TaskQueueControlRequest.Builder().workSequence(workSequence)
                                    .robotSequence(robotSequence).build())
                            .build())
                    .token(UserDetailsSupport.getAuthenticationToken()).execute();
            if (!isSuccess) {
                throw new RpaViewRestException(RpaViewRestError.SCHEDULE_ERROR, "delete schedule failure");
            }
            insertOrUpdateWorkAssignment(workSequence, robotSequence, null, "N", removeYn);
        } catch (RestClientExecutorException e) {
            throw new RpaViewRestException(RpaViewRestError.SCHEDULE_ERROR);
        }
    }

    /**
     * 업무할당 스케줄을 삭제한다.
     *
     * @param workSequence 업무순번
     * @param robotSequences 로봇순번 리스트
     */
    public void deleteWorkSchedule(int workSequence, List<Integer> robotSequences, String removeYn) {
        for (Integer robotSequence : robotSequences) {
            deleteWorkSchedule(workSequence, robotSequence, removeYn);
        }
    }

    /**
     * 업무할당 상태변경에 의해 스케줄을 등록한다.<br>
     * WorkAssignment는 Update 진행.
     *
     * @param work {@link Work}
     */
    public void addWorkAssignmentSchedule(Work work) {
        List<WorkAssignment> workAssignments = workAssignmentMapper.selectByWorkSequence(work.getWorkSequence());
        for (WorkAssignment workAssignment : workAssignments) {
            addWorkAssignmentSchedule(RepeatCycleUnitCode.getByCode(work.getrepeatCycleUnitCode()),
                    work.getExecuteStandardValue(), work.getExecuteHourminute(), workAssignment.getWorkSequence(),
                    workAssignment.getRobotSequence(), work.getProcessVersionSequence());
        }
    }

    /**
     * 업무할당을 신규 추가하고 스케줄을 등록한다.<br>
     * 비활성인 경우 스케줄은 등록하지 않고 WorkAssignment Insert만 한다.
     *
     * @param form {@link WorkAssignmentAddForm}
     * @param workSequence 업무순번
     */
    public void addWorkAssignmentSchedule(WorkAssignmentAddForm form, Integer workSequence) {
        RepeatCycleUnitCode repeatCycleUnitCode = RepeatCycleUnitCode.getByCode(form.getRepeatCycleUnitCd());
        for (Integer robotSequence : form.getRobotSequenceList()) {
            if ("Y".equalsIgnoreCase(form.getWorkActiveYn())) {
                addWorkAssignmentSchedule(repeatCycleUnitCode, form.getExecuteStandardValue(), form.getExecHourMinute(),
                        workSequence, robotSequence, form.getProcessVersionSequence());
            } else {
                insertOrUpdateWorkAssignment(workSequence, robotSequence, null, "N", "N");
            }
        }
    }

    /**
     * 업무할당 수정에 의해 업무할당 스케줄을 등록/수정 또는 삭제한다.<br>
     *
     *
     * @param form {@link WorkAssignmentModifyForm}
     */
    public void addWorkAssignmentSchedule(WorkAssignmentModifyForm form) {
        RepeatCycleUnitCode repeatCycleUnitCode = RepeatCycleUnitCode.getByCode(form.getRepeatCycleUnitCd());
        for (Integer robotSequence : form.getRobotSequenceAddList()) {
            if ("Y".equalsIgnoreCase(form.getWorkActiveYn())) {
                addWorkAssignmentSchedule(repeatCycleUnitCode, form.getExecuteStandardValue(), form.getExecHourMinute(),
                        form.getWorkSequence(), robotSequence, form.getProcessVersionSequence());
            } else {
                deleteWorkSchedule(form.getWorkSequence(), robotSequence, "N");
            }
        }
    }

    /**
     * 업무할당 스케줄을 등록한다.
     *
     * @param repeatCycleUnitCode 반복주기단위코드
     * @param executeStandardValue 실행기준값
     * @param hourMinutes 시분
     * @param workSequence 업무순번
     * @param robotSequence 로봇순번
     * @param processVersionSequence 프로세스버전순번
     */
    private void addWorkAssignmentSchedule(RepeatCycleUnitCode repeatCycleUnitCode, String executeStandardValue,
            String hourMinutes, Integer workSequence, Integer robotSequence, Integer processVersionSequence) {
        TaskQueueScheduleRequest request = null;
        WorkAssignmentScheduleApi workAssignmentScheduleApi = WorkAssignmentScheduleApi.UNKNOWN;
        switch (repeatCycleUnitCode) {
            case TIME:
                workAssignmentScheduleApi = WorkAssignmentScheduleApi.CREATE_HOURLY_SCHEDULE;
                request = new HourlyScheduleRequest.Builder().workSequence(workSequence).robotSequence(robotSequence)
                        .processVersionSequence(processVersionSequence).minute(executeStandardValue).build();
                break;
            case DAY:
                workAssignmentScheduleApi = WorkAssignmentScheduleApi.CREATE_DAILY_SCHEDULE;
                request = new DailyScheduleRequest.Builder().workSequence(workSequence).robotSequence(robotSequence)
                        .processVersionSequence(processVersionSequence).hourMinutes(hourMinutes).build();
                break;
            case WEEK:
                workAssignmentScheduleApi = WorkAssignmentScheduleApi.CREATE_WEEKLY_SCHEDULE;
                request = new WeeklyScheduleRequest.Builder().workSequence(workSequence).robotSequence(robotSequence)
                        .processVersionSequence(processVersionSequence).hourMinutes(hourMinutes)
                        .daysOfWeek(createDaysOfWeekArray(executeStandardValue)).build();
                break;
            default:
                break;
        }
        Assert.notNull(request, "createWorkAssignmentScheduleRequest must no be null");
        Assert.isTrue(!WorkAssignmentScheduleApi.UNKNOWN.equals(workAssignmentScheduleApi),
                "Unknown repeatCycleUnitCode");
        String triggerName = null;
        try {
            triggerName = restClientManager.createExecutor().apiType(workAssignmentScheduleApi)
                    .request(new RestClientExecutor.JsonRequestBodyBuilder().addObject(request).build())
                    .token(UserDetailsSupport.getAuthenticationToken()).execute();
            if (triggerName == null) {
                throw new RpaViewRestException(RpaViewRestError.SCHEDULE_ERROR, "add schedule failure");
            }
        } catch (RestClientExecutorException e) {
            throw new RpaViewRestException(RpaViewRestError.SCHEDULE_ERROR);
        }
        String setSchedulerTriggerRegistYn = triggerName != null ? "Y" : "N";
        insertOrUpdateWorkAssignment(workSequence, robotSequence, triggerName, setSchedulerTriggerRegistYn, "N");
    }

    private Integer[] createDaysOfWeekArray(String executeStandardValue) {
        String[] daysOfWeekArray = executeStandardValue.split("(?!^)");
        List<Integer> daysOfWeek = new ArrayList<>();
        for (int i = 0; i < daysOfWeekArray.length; i++) {
            if ("1".equals(daysOfWeekArray[i])) {
                daysOfWeek.add(i + 1);
            }
        }
        return daysOfWeek.toArray(new Integer[daysOfWeek.size()]);
    }

    private int insertOrUpdateWorkAssignment(Integer workSequence, Integer robotSequence, String triggerName,
            String schedulerTriggerRegistYn, String removeYn) {
        int existsCount = workAssignmentMapper.countByPrimaryKey(workSequence, robotSequence);
        if (existsCount > 0) {
            return updateWorkAssignment(workSequence, robotSequence, triggerName, schedulerTriggerRegistYn, removeYn);
        } else {
            return insertWorkAssignment(workSequence, robotSequence, triggerName, schedulerTriggerRegistYn, removeYn);
        }
    }

    private int insertWorkAssignment(Integer workSequence, Integer robotSequence, String triggerName,
            String schedulerTriggerRegistYn, String removeYn) {
        WorkAssignment workAssignment = new WorkAssignment();
        workAssignment.setWorkSequence(workSequence);
        workAssignment.setRobotSequence(robotSequence);
        workAssignment.setSchedulerTriggerName(triggerName);
        workAssignment.setSchedulerTriggerRegistYn(schedulerTriggerRegistYn);
        workAssignment.setRemoveYn(removeYn);
        return workAssignmentMapper.insert(workAssignment);
    }

    private int updateWorkAssignment(Integer workSequence, Integer robotSequence, String triggerName,
            String schedulerTriggerRegistYn, String removeYn) {
        WorkAssignment workAssignment = new WorkAssignment();
        workAssignment.setWorkSequence(workSequence);
        workAssignment.setRobotSequence(robotSequence);
        workAssignment.setSchedulerTriggerName(triggerName);
        workAssignment.setSchedulerTriggerRegistYn(schedulerTriggerRegistYn);
        workAssignment.setRemoveYn(removeYn);
        return workAssignmentMapper.updateByPrimaryKey(workAssignment);
    }
}
