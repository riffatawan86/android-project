package com.test.project.samplelocation.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.adapters.MainItemAdapter;
import com.test.project.samplelocation.interfaces.MainItemsCallback;
import com.test.project.samplelocation.models.MainItemModel;

import java.util.ArrayList;

public class ReminderMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_menu);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ArrayList<MainItemModel> list = new ArrayList<MainItemModel>();
        list.add(new MainItemModel("Set Reminder", R.drawable.ic_set_reminder_icon));
        list.add(new MainItemModel("View Reminders", R.drawable.ic_list_reminder_icon));
        MainItemAdapter adapter = new MainItemAdapter(this, list, new MainItemsCallback() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(ReminderMenuActivity.this, AddReminderActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(ReminderMenuActivity.this, SavedRemindersActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

}
