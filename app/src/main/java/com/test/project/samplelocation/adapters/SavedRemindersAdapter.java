package com.test.project.samplelocation.adapters;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.activities.EditReminderActivity;
import com.test.project.samplelocation.activities.ReminderDetailsActivity;
import com.test.project.samplelocation.activities.SplashActivity;
import com.test.project.samplelocation.models.ReminderModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.ReminderDao;
import com.test.project.samplelocation.utils.MyConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SavedRemindersAdapter extends RecyclerView.Adapter<SavedRemindersAdapter.SavedViewHolder> {

    private ArrayList<ReminderModel> list;
    private Context context;
    ReminderDao reminderDao;

    public SavedRemindersAdapter(ArrayList<ReminderModel> list, Context context) {
        this.list = list;
        this.context = context;
        reminderDao = LocationAppDatabase.getINSTANCE(context).getReminderDao();
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_saved_reminder, viewGroup, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder genericViewHolder, final int i) {
        final ReminderModel model = list.get(i);
       /* if (model.getDateTime() == null) {
            genericViewHolder.ivRepeat.setVisibility(View.GONE);
            genericViewHolder.tvRepeat.setVisibility(View.GONE);
        } else {
            genericViewHolder.ivRepeat.setVisibility(View.VISIBLE);
            genericViewHolder.tvRepeat.setVisibility(View.VISIBLE);
        }*/
        genericViewHolder.dateTv.setText(model.getDateTime());
        genericViewHolder.tvTitle.setText(model.getTitle());
        genericViewHolder.detailsTv.setText(model.getNote());
        genericViewHolder.address.setText(model.getAddress());
        genericViewHolder.valueLatitude.setText(model.getLatitude() + "");
        genericViewHolder.valueLongitude.setText(model.getLongitude() + "");
        genericViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditorActivity(model);
            }
        });
        genericViewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditorActivity(model);
            }
        });
        genericViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _myIntent = new Intent(context, SplashActivity.class);
                PendingIntent currentIntent = PendingIntent.getBroadcast(context, model.getId(), _myIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                if (alarmManager != null) {
                    try {
                        alarmManager.cancel(currentIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                reminderDao.deleteSingle(model.getId());
                list.clear();
                list = (ArrayList<ReminderModel>) reminderDao.getAll();
                notifyDataSetChanged();
                Toast.makeText(context, "Reminder Deleted.", Toast.LENGTH_SHORT).show();
            }
        });
        genericViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _myIntent = new Intent(context, SplashActivity.class);
                PendingIntent currentIntent = PendingIntent.getBroadcast(context, model.getId(), _myIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                if (alarmManager != null) {
                    try {
                        alarmManager.cancel(currentIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                reminderDao.deleteSingle(model.getId());
                list.clear();
                list = (ArrayList<ReminderModel>) reminderDao.getAll();
                notifyDataSetChanged();
                Toast.makeText(context, "Reminder Deleted.", Toast.LENGTH_SHORT).show();
            }
        });
        genericViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReminderDetailsActivity.class);
                intent.putExtra("DetailsNew", model);
                context.startActivity(intent);
            }
        });
        genericViewHolder.ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatAlarm(model);
            }
        });
        genericViewHolder.tvRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatAlarm(model);
            }
        });
    }

    private void openEditorActivity(ReminderModel model) {
        Intent intent = new Intent(context, EditReminderActivity.class);
        intent.putExtra(MyConstants.Companion.getREMINDER_EDIT_MODEL_INTENT(), model);
        context.startActivity(intent);
    }

    private void repeatAlarm(ReminderModel model) {
        String dateFormatted = model.getDateOfSet();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm zzzz yyyy");
        try {
            Date date = format.parse(dateFormatted);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, 1);
            Date dateNew = cal.getTime();
            Log.d("LOGGER:", "Date Old: " + date);
            Log.d("LOGGER:", "Date New: " + dateNew);
            Intent _myIntent = new Intent(context, SplashActivity.class);
            PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context, model.getId(), _myIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
            AlarmManager _myAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (_myAlarmManager != null) {
                _myAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), _myPendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "You will notify next month for this Saved Reminder.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder {
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

        ImageView ivRepeat;
        TextView tvRepeat;

        SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            address = itemView.findViewById(R.id.address);
            valueLatitude = itemView.findViewById(R.id.valueLatitude);
            valueLongitude = itemView.findViewById(R.id.valueLongitude);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            dateTv = itemView.findViewById(R.id.date);
            detailsTv = itemView.findViewById(R.id.detailsTv);
            ivRepeat = itemView.findViewById(R.id.ivRepeat);
            tvRepeat = itemView.findViewById(R.id.tvRepeat);
        }
    }
}
