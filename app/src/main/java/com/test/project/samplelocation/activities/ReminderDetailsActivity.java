package com.test.project.samplelocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.models.ReminderModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.ReminderDao;
import com.test.project.samplelocation.utils.MyConstants;


public class ReminderDetailsActivity extends AppCompatActivity {
    private ReminderModel reminderModel;
    TextView tvTitle;
    TextView address;
    TextView valueLatitude;
    TextView valueLongitude;
    ImageView ivEdit;
    TextView tvEdit;
    ImageView ivDelete;
    TextView tvDelete;
    TextView dateTv;
    TextView detailsTv;
    TextView setDateTv;
    ReminderDao reminderDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);
        reminderDao = LocationAppDatabase.getINSTANCE(this).getReminderDao();
        tvTitle = findViewById(R.id.tvTitle);
        address = findViewById(R.id.address);
        valueLatitude = findViewById(R.id.valueLatitude);
        valueLongitude = findViewById(R.id.valueLongitude);
        ivEdit = findViewById(R.id.ivEdit);
        tvEdit = findViewById(R.id.tvEdit);
        ivDelete = findViewById(R.id.ivDelete);
        tvDelete = findViewById(R.id.tvDelete);
        dateTv = findViewById(R.id.date);
        detailsTv = findViewById(R.id.detailsTv);
        setDateTv = findViewById(R.id.setDateTv);
        reminderModel = getIntent().getParcelableExtra("DetailsNew");
        if (reminderModel != null) {
            Log.d("DETAILS", ": " + reminderModel.toString());
            dateTv.setText(reminderModel.getDateTime());
            tvTitle.setText(reminderModel.getTitle());
            detailsTv.setText(reminderModel.getNote());
            address.setText(reminderModel.getAddress());
            setDateTv.setText(reminderModel.getDateOfSet());
            valueLatitude.setText(reminderModel.getLatitude() + "");
            valueLongitude.setText(reminderModel.getLongitude() + "");
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openEditorActivity(reminderModel);
                }
            });
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openEditorActivity(reminderModel);
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reminderDao.deleteSingle(reminderModel.getId());
                    onBackPressed();
                    Toast.makeText(ReminderDetailsActivity.this, "Reminder Deleted.", Toast.LENGTH_SHORT).show();
                }
            });
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reminderDao.deleteSingle(reminderModel.getId());
                    Toast.makeText(ReminderDetailsActivity.this, "Reminder Deleted.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openEditorActivity(ReminderModel model) {
        Intent intent = new Intent(this, EditReminderActivity.class);
        intent.putExtra(MyConstants.Companion.getREMINDER_EDIT_MODEL_INTENT(), model);
        this.startActivity(intent);
    }
}
