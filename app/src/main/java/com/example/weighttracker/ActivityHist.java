package com.example.weighttracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityHist extends AppCompatActivity {

    //setup top and bottom bar
    int prime = Color.parseColor("#BAFFD9");
    int elem = Color.parseColor("#58CC8D");

    ImageButton mainbtn;
    Button entrybtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //setup screen specific elements
    LinearLayout histScroll;

    int entries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);

        //setup top and bottom bar
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

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

        histScroll = (LinearLayout) findViewById(R.id.histScrollLayout);


    }

    protected void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        nameTxt.setText(MainActivity.user.getName());
        goalTxt.setText("Goal: "+MainActivity.user.getWeight()+MainActivity.user.getUnit());

        //adds the entries from the database onto the history screen
        histScroll.removeAllViews();

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        LinearLayout.LayoutParams params = new LayoutParams(0, 128);
        params.weight = 1;
        params.topMargin = 24;

        ArrayList<Entry> entries = MainActivity.dbhandle.selectAll();
        for (Entry entry : entries) {
            LinearLayout row = new LinearLayout(this);

            TextView tvDate = new TextView(this);
            tvDate.setId(entry.getId());
            tvDate.setText("Date: "+sdf.format(entry.getDate()));
            tvDate.setBackgroundColor(prime);
            tvDate.setLayoutParams(params);

            row.addView(tvDate);

            TextView tvWeight = new TextView(this);
            tvWeight.setId(entry.getId());
            tvWeight.setText(" Weight: "+entry.getWeight());
            tvWeight.setBackgroundColor(prime);
            tvWeight.setLayoutParams(params);

            row.addView(tvWeight);

            ImageButton edit = new ImageButton(this);
            edit.setImageResource(R.drawable.trash);
            edit.setBackgroundColor(prime);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.dbhandle.deleteId(entry.getId());
                    updateView();
                }
            });
            edit.setLayoutParams(params);

            row.addView(edit);


            histScroll.addView(row);
        }
    }
}