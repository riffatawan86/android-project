package com.test.project.samplelocation.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ContactModel implements Parcelable {
    public String id;
    public String name;
    public String mobileNumber;
    public Bitmap photo;
    public Uri photoURI;

    public ContactModel() {
    }

    public ContactModel(String id, String name, String mobileNumber, Bitmap photo, Uri photoURI) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.photo = photo;
        this.photoURI = photoURI;
    }

    protected ContactModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobileNumber = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
        photoURI = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel in) {
            return new ContactModel(in);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(Uri photoURI) {
        this.photoURI = photoURI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(mobileNumber);
        parcel.writeParcelable(photo, i);
        parcel.writeParcelable(photoURI, i);
    }
}
