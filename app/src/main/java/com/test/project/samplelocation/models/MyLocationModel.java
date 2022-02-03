package com.test.project.samplelocation.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


public class MyLocationModel implements Parcelable {

    private int id;
    private double myLatitude;
    private double myLongitude;

    public MyLocationModel(double myLatitude, double myLongitude) {
        this.myLatitude = myLatitude;
        this.myLongitude = myLongitude;
    }

    protected MyLocationModel(Parcel in) {
        id = in.readInt();
        myLatitude = in.readDouble();
        myLongitude = in.readDouble();
    }

    public static final Creator<MyLocationModel> CREATOR = new Creator<MyLocationModel>() {
        @Override
        public MyLocationModel createFromParcel(Parcel in) {
            return new MyLocationModel(in);
        }

        @Override
        public MyLocationModel[] newArray(int size) {
            return new MyLocationModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(myLatitude);
        dest.writeDouble(myLongitude);
    }

    @Override
    public String toString() {
        return "MyLocationModel{" +
                "id=" + id +
                ", myLatitude=" + myLatitude +
                ", myLongitude=" + myLongitude +
                '}';
    }
}
