package com.example.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;

    //setup top and bottom bar

    String name;
    String goal = "Goal Weight: "+User.getInstance().getWeight()+ User.getInstance().getUnit();

    ImageButton mainbtn;
    Button entrybtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scrn);

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("com.example.weighttracker_pref", 0);
        name = mPrefs.getString("user_name", "user");
        User.getInstance().setName(name);


        //setup top and bottom bar
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

        nameTxt.setText(""+name);
        goalTxt.setText(""+goal);

        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        dateTxt.setText(date_n);


        entrybtn = findViewById(R.id.buttonEntry);
        entrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityEntry.class);
                startActivity(i);
            }
        });

        settbtn = findViewById(R.id.buttonSett);
        settbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivitySett.class);
                startActivity(i);
            }
        });

        mainbtn = findViewById(R.id.buttonMain);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        histbtn = findViewById(R.id.buttonHistory);
        histbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ActivityHist.class);
                startActivity(i);
            }
        });

    }

    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString("user_name", name);
        ed.apply();
    }

    protected void onResume() {
        super.onResume();

        name = mPrefs.getString("user_name", "user");
        User.getInstance().setName(name);
    }
}