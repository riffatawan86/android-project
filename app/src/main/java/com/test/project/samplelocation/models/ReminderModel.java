package com.test.project.samplelocation.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class ReminderModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    String title;
    String note;
    String dateTime;
    double latitude;
    double longitude;
    String address;
    String dateOfSet;

    public ReminderModel() {
    }

    public ReminderModel(String title, String note, String dateTime, double latitude, double longitude, String address, String dateOfSet) {
        this.title = title;
        this.note = note;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.dateOfSet = dateOfSet;
    }

    protected ReminderModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        note = in.readString();
        dateTime = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        dateOfSet = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(note);
        dest.writeString(dateTime);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(dateOfSet);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReminderModel> CREATOR = new Creator<ReminderModel>() {
        @Override
        public ReminderModel createFromParcel(Parcel in) {
            return new ReminderModel(in);
        }

        @Override
        public ReminderModel[] newArray(int size) {
            return new ReminderModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfSet() {
        return dateOfSet;
    }

    public void setDateOfSet(String dateOfSet) {
        this.dateOfSet = dateOfSet;
    }
}