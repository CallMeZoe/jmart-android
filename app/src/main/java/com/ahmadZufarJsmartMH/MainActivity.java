package com.ahmadZufarJsmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ahmadZufarJsmartMH.model.Account;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.txtMain);
        Account account = LoginActivity.getLoggedAccount();
        tv.setText("Hello" + account.name);
    }
}