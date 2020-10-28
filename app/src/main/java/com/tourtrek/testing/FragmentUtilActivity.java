package com.tourtrek.testing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

public class FragmentUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout view = new LinearLayout(this);
        view.setId(1);
        setContentView(view);
    }
}
