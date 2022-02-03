package com.test.project.samplelocation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.test.project.samplelocation.R;
import com.test.project.samplelocation.models.ContactModel;
import com.test.project.samplelocation.models.MessageModel;
import com.test.project.samplelocation.models.MyLocationModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.MessageDao;
import com.test.project.samplelocation.services.AppLocationService;
import com.test.project.samplelocation.utils.MyConstants;
import com.test.project.samplelocation.utils.MyGeocoderUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMessageActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextContact;
    CardView addContactCv;
    EditText editTextTitle;
    ImageView clearBtn;
    EditText editTextDetails;
    EditText editTextLocation;
    CardView addLocCv;
    TextView dateTimeTv;
    CardView dateTimeCv;
    CardView saveCv;
    String dateTime = null;
    boolean haveGotLocation = false;
    String TAG = "REMINDER-NEW";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String address = null;
    String contactNum = null;
    private boolean haveGotFromPhonebook = false;
    private RadioButton radioButton;
    String setAlarmDate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        init();
    }

    private void init() {
        editTextContact = findViewById(R.id.editTextContact);
        addContactCv = findViewById(R.id.addContactCv);
        editTextTitle = findViewById(R.id.editTextTitle);
        clearBtn = findViewById(R.id.clearBtn);
        editTextDetails = findViewById(R.id.editTextDetails);
        editTextLocation = findViewById(R.id.editTextLocation);
        addLocCv = findViewById(R.id.addLocCv);
        dateTimeTv = findViewById(R.id.dateTimeTv);
        dateTimeCv = findViewById(R.id.dateTimeCv);
        saveCv = findViewById(R.id.saveCv);
        addContactCv.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        addLocCv.setOnClickListener(this);
        dateTimeCv.setOnClickListener(this);
        saveCv.setOnClickListener(this);
        radioButton = findViewById(R.id.checkbox);
        findViewById(R.id.tvCheck).setOnClickListener(this);
        dateTimeCv.setClickable(false);
        YoYo.with(Techniques.Shake).repeat(YoYo.INFINITE).duration(1200).playOn(addLocCv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCheck:
                if (radioButton.isChecked()) {
                    radioButton.setChecked(false);
                    dateTimeCv.setAlpha(0.8F);
                    dateTimeCv.setClickable(false);
                } else {
                    radioButton.setChecked(true);
                    dateTimeCv.setAlpha(1);
                    dateTimeCv.setClickable(true);
                }
                break;
            case R.id.clearBtn:
                editTextTitle.setText("");
                break;
            case R.id.addLocCv:
                Intent intent = new Intent(this, AddLocationActivity.class);
                startActivityForResult(intent, MyConstants.Companion.getADD_LOCATION_CODE());
                break;
            case R.id.dateTimeCv:
                openDateTimeDialog();
                break;
            case R.id.saveCv:
                saveData();
                break;
            case R.id.addContactCv:
                Intent intentContact = new Intent(this, AddContactActivity.class);
                startActivityForResult(intentContact, MyConstants.Companion.getADD_CONTACT_CODE());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.Companion.getADD_LOCATION_CODE()) {
            if (resultCode == RESULT_OK && data != null) {
                haveGotLocation = true;
                MyLocationModel myLocationModel = data.getParcelableExtra(MyConstants.Companion.getDATA_FROM_LOCATION());
                Log.d(TAG, myLocationModel.toString());
                latitude = myLocationModel.getMyLatitude();
                longitude = myLocationModel.getMyLongitude();
                address = MyGeocoderUtils.Companion.getAddressFromLocation(this, latitude, longitude);
                editTextLocation.setText(address);
            }
        } else if (requestCode == MyConstants.Companion.getADD_CONTACT_CODE()) {
            if (resultCode == RESULT_OK && data != null) {
                haveGotFromPhonebook = true;
                ContactModel model = data.getParcelableExtra(MyConstants.Companion.getDATA_FROM_CONTACT());
                contactNum = model.mobileNumber;
                editTextContact.setText(model.mobileNumber.trim());
            }

        }
    }



    private void openDateTimeDialog() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                    }
                })
                .mainColor(getResources().getColor(R.color.colorPrimary))
                .titleTextColor(getResources().getColor(R.color.colorWhite))
                .backgroundColor(getResources().getColor(R.color.colorWhite))
                .title("Date & Time")
                .mustBeOnFuture()
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm zzzz yyyy");
                        dateTime = formatter.format(date);
                        dateTimeTv.setText(dateTime);
                        Date date1 = new Date();
                        setAlarmDate = formatter.format(date1);
                        Log.d("LOGGER:", "Today Date: " + setAlarmDate);
                    }
                }).display();
    }

    private void saveData() {

        if (haveGotLocation && !editTextContact.getText().toString().equals("") && !editTextTitle.getText().toString().equals("") && !editTextDetails.getText().toString().equals("") && !editTextLocation.getText().equals("") && editTextContact.getText().toString().trim().length() >= 11) {
            contactNum = editTextContact.getText().toString().trim();
            MessageModel model = new MessageModel();
            model.setTitle(editTextTitle.getText().toString().trim());
            model.setMessage(editTextDetails.getText().toString().trim());
            model.setAddress(address);
            model.setDateTime(dateTime);
            model.setDateOfSet(setAlarmDate);
            model.setLatitude(latitude);
            model.setLongitude(longitude);
            model.setContactNo(contactNum);
            MessageDao messageDao = LocationAppDatabase.getINSTANCE(this).getMessageDao();
            messageDao.insertSingle(model);
            Toast.makeText(this, "Message Saved Successfully!", Toast.LENGTH_SHORT).show();
            editTextTitle.setText("");
            editTextDetails.setText("");
            editTextLocation.setText("");
            editTextContact.setText("");
            dateTimeTv.setText("");
            AppLocationService.resetLists();
        } else {
            Toast.makeText(this, "Please fill all the fields and contact number should be of at least 11 numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
