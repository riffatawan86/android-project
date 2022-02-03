package com.test.project.samplelocation.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.adapters.ContactsAdapter;
import com.test.project.samplelocation.dialogs.MyDownloadingDialog;
import com.test.project.samplelocation.interfaces.ContactCallback;
import com.test.project.samplelocation.models.ContactModel;
import com.test.project.samplelocation.utils.MyConstants;

import java.io.InputStream;
import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    MyDownloadingDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        init();
    }

    private void init() {
        progressBar = new MyDownloadingDialog(AddContactActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        new GetContantsTask().execute();
    }

    class GetContantsTask extends AsyncTask<Void, Integer, ArrayList<ContactModel>> {
        int currentProgress = 0;
        int totalContacts = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.show();
            progressBar.setCancelable(false);
        }

        @Override
        protected ArrayList<ContactModel> doInBackground(Void... voids) {
            ArrayList<ContactModel> list = new ArrayList<>();
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            totalContacts = cursor.getCount();
            Log.d("PROGRESSNEW", "Total " + totalContacts);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                        Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                        Bitmap photo = null;
                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                        }
                        while (cursorInfo.moveToNext()) {
                            ContactModel info = new ContactModel();
                            info.id = id;
                            info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            info.photo = photo;
                            info.photoURI = pURI;
                            list.add(info);
                        }
                        currentProgress += (int) (100f * (1.0f / totalContacts));
                        Log.d("PROGRESSNEW", "Current " + currentProgress);
                        publishProgress(currentProgress);
                        cursorInfo.close();
                    }
                }
                cursor.close();
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //progressBar.setProgressToViews(values[0]);
        }

        @Override
        protected void onPostExecute(final ArrayList<ContactModel> contactModels) {
            super.onPostExecute(contactModels);
            ContactsAdapter adapter = new ContactsAdapter(contactModels, AddContactActivity.this, new ContactCallback() {
                @Override
                public void setContact(ContactModel contactModel) {
                    Toast.makeText(AddContactActivity.this, "Contact Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(MyConstants.Companion.getDATA_FROM_CONTACT(), contactModel);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
            recyclerView.setAdapter(adapter);
            progressBar.dismissIt();
        }
    }
}
