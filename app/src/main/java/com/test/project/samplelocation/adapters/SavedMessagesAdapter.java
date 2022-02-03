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
import com.test.project.samplelocation.activities.EditMessageActivity;
import com.test.project.samplelocation.activities.SplashActivity;
import com.test.project.samplelocation.models.MessageModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.MessageDao;
import com.test.project.samplelocation.utils.MyConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SavedMessagesAdapter extends RecyclerView.Adapter<SavedMessagesAdapter.SavedViewHolder> {

    private ArrayList<MessageModel> list;
    private Context context;
    MessageDao messageDao;

    public SavedMessagesAdapter(ArrayList<MessageModel> list, Context context) {
        this.list = list;
        this.context = context;
        messageDao = LocationAppDatabase.getINSTANCE(context).getMessageDao();
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_saved_message, viewGroup, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder genericViewHolder, final int i) {
        final MessageModel model = list.get(i);
        /*if (model.getDateTime() == null) {
            genericViewHolder.ivRepeat.setVisibility(View.GONE);
            genericViewHolder.tvRepeat.setVisibility(View.GONE);
        } else {
            genericViewHolder.ivRepeat.setVisibility(View.VISIBLE);
            genericViewHolder.tvRepeat.setVisibility(View.VISIBLE);
        }*/
        genericViewHolder.dateTv.setText(model.getDateTime());
        genericViewHolder.tvContact.setText("Contact Num: " + model.getContactNo());
        genericViewHolder.tvTitle.setText(model.getTitle());
        genericViewHolder.messageTv.setText(model.getMessage());
        genericViewHolder.address.setText(model.getAddress());
        genericViewHolder.valueLatitude.setText(model.getLatitude() + "");
        genericViewHolder.valueLongitude.setText(model.getLongitude() + "");
        genericViewHolder.setDateTv.setText(model.getDateOfSet());
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
                messageDao.deleteSingle(model.getId());
                list.clear();
                list = (ArrayList<MessageModel>) messageDao.getAll();
                notifyDataSetChanged();
                Toast.makeText(context, "Message Deleted.", Toast.LENGTH_SHORT).show();
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
                messageDao.deleteSingle(model.getId());
                list.clear();
                list = (ArrayList<MessageModel>) messageDao.getAll();
                notifyDataSetChanged();
                Toast.makeText(context, "Message Deleted.", Toast.LENGTH_SHORT).show();
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

    private void repeatAlarm(MessageModel model) {
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
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), _myPendingIntent);
                //Log.d("LOGGER:", "Next Alarm: " + alarmManager.getNextAlarmClock().getTriggerTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "You will notify next month for this Saved Message.", Toast.LENGTH_SHORT).show();
    }

    private void openEditorActivity(MessageModel model) {
        Intent intent = new Intent(context, EditMessageActivity.class);
        intent.putExtra(MyConstants.Companion.getMESSAGE_EDIT_MODEL_INTENT(), model);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder {
        TextView tvContact;
        TextView tvTitle;
        TextView address;
        TextView valueLatitude;
        TextView valueLongitude;
        ImageView ivEdit;
        TextView tvEdit;
        ImageView ivDelete;
        TextView tvDelete;
        TextView dateTv;
        TextView messageTv;
        TextView setDateTv;
        ImageView ivRepeat;
        TextView tvRepeat;

        SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            address = itemView.findViewById(R.id.address);
            valueLatitude = itemView.findViewById(R.id.valueLatitude);
            valueLongitude = itemView.findViewById(R.id.valueLongitude);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            dateTv = itemView.findViewById(R.id.date);
            messageTv = itemView.findViewById(R.id.messageTv);
            setDateTv = itemView.findViewById(R.id.setDateTv);
            ivRepeat = itemView.findViewById(R.id.ivRepeat);
            tvRepeat = itemView.findViewById(R.id.tvRepeat);
        }
    }
}
