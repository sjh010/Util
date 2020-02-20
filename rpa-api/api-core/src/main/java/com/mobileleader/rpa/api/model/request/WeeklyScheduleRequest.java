package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;

public class WeeklyScheduleRequest extends TaskQueueScheduleRequest {

    @NotNull
    private Integer[] daysOfWeek;

    private int hour;

    private int minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Integer[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Integer[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
