package com.test.project.samplelocation.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.test.project.samplelocation.R;
import com.test.project.samplelocation.interfaces.MainItemsCallback;
import com.test.project.samplelocation.models.MainItemModel;

import java.util.ArrayList;

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.MainViewHolder> {

    private Context context;
    private ArrayList<MainItemModel> list;
    private MainItemsCallback callback;

    public MainItemAdapter(Context context, ArrayList<MainItemModel> list, MainItemsCallback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        MainItemModel model = list.get(position);
        Glide.with(context).load(model.getItemIcon()).into(holder.ivMainItem);
        holder.tvMainItem.setText(model.getItemName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMainItem;
        TextView tvMainItem;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMainItem = itemView.findViewById(R.id.ivMainItem);
            tvMainItem = itemView.findViewById(R.id.tvMainItem);
        }
    }
}
