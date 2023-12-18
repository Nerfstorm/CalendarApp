package com.example.fkandroidstudio;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class DateShenanigans {
    public static Date SkipDays(Date recvDate, int nrSkiped){
        Instant recvInstant = recvDate.toInstant();
        Instant newInstant = recvInstant.plus(Duration.ofDays(nrSkiped));
        Date newDate = Date.from(newInstant);
        return newDate;
    }
}
