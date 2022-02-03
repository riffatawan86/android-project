package com.test.project.samplelocation.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.project.samplelocation.R;
import com.test.project.samplelocation.interfaces.ContactCallback;
import com.test.project.samplelocation.models.ContactModel;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private ArrayList<ContactModel> list;
    private Context context;
    private ContactCallback contactCallback;

    public ContactsAdapter(ArrayList<ContactModel> list, Context context, ContactCallback contactCallback) {
        this.list = list;
        this.contactCallback = contactCallback;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_user_contact, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ContactModel model = list.get(i);
        myViewHolder.userNameTv.setText(model.name);
        myViewHolder.contactNum.setText(model.mobileNumber);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactCallback.setContact(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTv;
        TextView contactNum;
        ImageView userIv;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.userNameTv);
            contactNum = itemView.findViewById(R.id.contactNum);
            userIv = itemView.findViewById(R.id.userIv);
        }
    }
}
