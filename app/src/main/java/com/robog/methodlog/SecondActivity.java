package com.robog.methodlog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        firstMethod();
    }

    private void firstMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                secondMethod();
            }
        }).start();
    }

    private void secondMethod() {

    }
}
