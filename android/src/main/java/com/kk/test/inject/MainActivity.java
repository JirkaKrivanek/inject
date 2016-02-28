package com.kk.test.inject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kk.inject.Factory;
import com.kk.inject.testsdk.TestSdkContext;
import com.kk.inject.testsdk.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Factory.injectInstance(this);
        final TextView textView = (TextView) findViewById(R.id.activity_main_greeting);
        final User user = TestSdkContext.getFactory().get(User.class, "John Doe");
        final String introduction = user.introduce();
        textView.setText(introduction);
    }
}
