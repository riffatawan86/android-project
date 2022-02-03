package com.test.project.samplelocation.room_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.project.samplelocation.models.ReminderModel;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insertSingle(ReminderModel model);

    @Query("DELETE FROM ReminderModel WHERE id=:id")
    void deleteSingle(int id);

    @Update
    void updateSingle(ReminderModel model);

    @Query("SELECT* FROM ReminderModel WHERE id=:id")
    ReminderModel getByPackageName(int id);

    @Query("SELECT* FROM ReminderModel")
    List<ReminderModel> getAll();

    @Query("DELETE FROM ReminderModel")
    void deleteAll();
}
