package com.example.fkandroidstudio;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class DateShenanigans { //dumb class name, I have no idea what it does
    public static Date SkipDays(Date recvDate, int numSkipped){
        Instant recvInstant = recvDate.toInstant();
        Instant newInstant = recvInstant.plus(Duration.ofDays(numSkipped));
        return Date.from(newInstant);
    }
}
