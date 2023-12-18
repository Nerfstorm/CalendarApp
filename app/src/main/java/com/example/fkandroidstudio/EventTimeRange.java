package com.example.fkandroidstudio;

import java.util.Date;

public class EventTimeRange {
    private Date startTime;
    private Date endTime;

    public EventTimeRange(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
