package com.saneforce.milksales.Model_Class;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventCaptureDao {

    @Query("SELECT * FROM eventcapture")
    List<EventCapture> getAll();

    @Insert
    void insert(EventCapture task);

    @Delete
    void delete(EventCapture task);


    @Update
    void update(EventCapture task);

}
