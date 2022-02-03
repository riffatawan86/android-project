package com.test.project.samplelocation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.test.project.samplelocation.R;

public class AppUsageDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public AppUsageDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_how_to_use);
        findViewById(R.id.cardViewYes).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewYes:
                dismissIt();
                break;

        }
    }

    private void dismissIt() {
        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
