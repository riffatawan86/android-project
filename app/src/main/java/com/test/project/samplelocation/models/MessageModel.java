package com.test.project.samplelocation.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    String title;
    String message;
    String contactNo;
    String dateTime;
    double latitude;
    double longitude;
    String address;
    String dateOfSet;


    public MessageModel() {
    }

    public MessageModel(String title, String message, String contactNo, String dateTime, double latitude, double longitude, String address, String dateOfSet) {
        this.title = title;
        this.message = message;
        this.contactNo = contactNo;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.dateOfSet = dateOfSet;
    }

    protected MessageModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        message = in.readString();
        contactNo = in.readString();
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
        dest.writeString(message);
        dest.writeString(contactNo);
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

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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
