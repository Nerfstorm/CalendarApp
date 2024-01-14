package com.example.fkandroidstudio;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalendarDao {
    @Query("SELECT * FROM CalendarTableRow")
    List<CalendarTableRow> getAll();
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
