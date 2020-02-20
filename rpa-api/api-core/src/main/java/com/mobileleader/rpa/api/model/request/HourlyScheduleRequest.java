package com.mobileleader.rpa.api.model.request;

public class HourlyScheduleRequest extends TaskQueueScheduleRequest {

    private int minute;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
