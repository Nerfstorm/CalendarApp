package com.example.fkandroidstudio;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import biweekly.component.VEvent;

@Dao
public interface CalendarDao {
    @Query("SELECT * FROM CalendarTableRow")
    List<CalendarTableRow> getAll();

    @Query("SELECT DISTINCT fileName FROM CalendarTableRow")
    List<String> DistinctFileNames();

    @Query("SELECT event FROM calendarTableRow WHERE fileName = :specFileName")
    List<String> getEventsFromSpecFile(String specFileName);

    @Query("DELETE FROM sqlite_sequence WHERE name = 'calendarTableRow'")
    void resetPrimaryKey();

    @Query("SELECT COUNT(*) FROM CalendarTableRow")
    int getRowCount();

    @Delete
    void delete(CalendarTableRow calendarTableRow);

    @Query("DELETE FROM CalendarTableRow")
    void deleteAllRows();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CalendarTableRow calendarTableRow);

}
