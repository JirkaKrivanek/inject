package com.kk.test.inject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.test.inject.model.Service;

public class MainActivity extends AppCompatActivity {

    @Inject private Service mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Factory.injectInstance(this);
        final TextView textView = (TextView) findViewById(R.id.activity_main_greeting);
        final String greeting = mService.getGreeting();
        textView.setText(greeting);
    }
}
