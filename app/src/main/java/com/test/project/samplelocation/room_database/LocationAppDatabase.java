package com.test.project.samplelocation.room_database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.test.project.samplelocation.models.MessageModel;
import com.test.project.samplelocation.models.ReminderModel;

@Database(entities = {ReminderModel.class, MessageModel.class}, version =3, exportSchema = false)

public abstract class LocationAppDatabase extends RoomDatabase {

    private static final String DB_NAME = "loc_app_db";
    private static LocationAppDatabase INSTANCE;

    public abstract ReminderDao getReminderDao();

    public abstract MessageDao getMessageDao();

    public static LocationAppDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, LocationAppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
