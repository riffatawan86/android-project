package com.test.project.samplelocation.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.adapters.SavedRemindersAdapter;
import com.test.project.samplelocation.models.ReminderModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.ReminderDao;

import java.util.ArrayList;

public class SavedRemindersActivity extends AppCompatActivity implements TextWatcher {
    ReminderDao reminderDao;
    private ArrayList<ReminderModel> list = new ArrayList<>();
    private ArrayList<ReminderModel> listBackup = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText editText;
    private CardView cardViewSearch;
    private ConstraintLayout statusLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_reminders);
        initViews();
    }

    private void initViews() {
        cardViewSearch = findViewById(R.id.cardViewSearch);
        statusLayout = findViewById(R.id.statusLayout);
        editText = findViewById(R.id.editText);
        findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        editText.addTextChangedListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        reminderDao = LocationAppDatabase.getINSTANCE(this).getReminderDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = (ArrayList<ReminderModel>) reminderDao.getAll();
        if (list.size() == 0) {
            statusLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            cardViewSearch.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            cardViewSearch.setVisibility(View.VISIBLE);
            statusLayout.setVisibility(View.GONE);
            SavedRemindersAdapter adapter = new SavedRemindersAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listBackup.clear();
        String query = charSequence.toString().toLowerCase().trim();
        Log.d("Querynew", ": " + query);
        if (!query.equalsIgnoreCase("")) {
            for (ReminderModel model : list) {
                if (model.getTitle().toLowerCase().trim().contains(query)) {
                    listBackup.add(model);
                }
            }
        } else {
            listBackup.addAll(list);
        }

        SavedRemindersAdapter adapter = new SavedRemindersAdapter(listBackup, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
