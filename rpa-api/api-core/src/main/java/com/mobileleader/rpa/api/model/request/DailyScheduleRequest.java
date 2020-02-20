package com.mobileleader.rpa.api.model.request;

public class DailyScheduleRequest extends TaskQueueScheduleRequest {

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
}
