package com.mobileleader.rpa.api.service.task;

import com.mobileleader.rpa.api.model.request.DailyScheduleRequest;
import com.mobileleader.rpa.api.model.request.HourlyScheduleRequest;
import com.mobileleader.rpa.api.model.request.TaskQueueControlRequest;
import com.mobileleader.rpa.api.model.request.WeeklyScheduleRequest;

public interface TaskQueueScheduleService {

    public String createHourlyWorkAssignmentSchedule(HourlyScheduleRequest request);

    public String createDailyWorkAssignmentSchedule(DailyScheduleRequest request);

    public String createWeeklyWorkAssignmentSchedule(WeeklyScheduleRequest request);

    public boolean removeWorkAssignmentSchedule(TaskQueueControlRequest request);

}
