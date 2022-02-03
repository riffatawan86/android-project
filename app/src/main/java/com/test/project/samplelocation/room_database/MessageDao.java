package com.test.project.samplelocation.room_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.test.project.samplelocation.models.MessageModel;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSingle(MessageModel model);

    @Query("DELETE FROM MessageModel WHERE id=:id")
    void deleteSingle(int id);

    @Update
    void updateSingle(MessageModel model);

    @Query("SELECT* FROM MessageModel WHERE id=:id")
    MessageModel getByPackageName(int id);

    @Query("SELECT* FROM MessageModel")
    List<MessageModel> getAll();

    @Query("DELETE FROM MessageModel")
    void deleteAll();
}
