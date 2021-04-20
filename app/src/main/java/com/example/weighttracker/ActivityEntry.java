package com.example.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityEntry extends AppCompatActivity {

    //setup top and bottom bar
    String name = User.getInstance().getName();
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
        setContentView(R.layout.activity_entry);

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

        //more below

    }
}