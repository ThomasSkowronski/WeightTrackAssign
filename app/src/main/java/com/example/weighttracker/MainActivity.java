package com.example.weighttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static databaseHandler dbhandle;
    public static User user;

    //setup top and bottom bar
    ImageButton mainbtn;
    Button entrybtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //setup screen specific views
    TextView curWeight;
    TextView goalWeightMain;
    TextView bmicur;
    TextView weekAvg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(this);
        setContentView(R.layout.main_scrn);
        dbhandle = new databaseHandler(this);

        //setup top and bottom bar
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        dateTxt.setText(date_n);

        //dialogue box to prompt first timer to setting page
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New User?");
        builder.setPositiveButton("New User Setup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),ActivitySett.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();

        if (user.isNew()){
            dialog.show();
        }

        //Nav Buttons

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

        //Main Page Info
        curWeight = (TextView) findViewById(R.id.curWeight);
        goalWeightMain = (TextView) findViewById(R.id.goalWeightMain);
        bmicur = (TextView) findViewById(R.id.bmiCur);
        weekAvg = (TextView) findViewById(R.id.weekAvg);

        updateView();

    }

    protected void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        nameTxt.setText(user.getName());
        goalTxt.setText("Goal: "+user.getWeight()+user.getUnit());

        //Main info box
        String CURR_WEIGHT = "Current Weight: ";
        String GOAL_MAIN = "Goal Weight: ";
        String BMI = "Current BMI: ";
        String WEEK_AVG = "Weekly Average: ";
        double bmi;
        double curweight;

        try {
            bmi = dbhandle.mostRecent().getWeight() / (MainActivity.user.getHeight()*MainActivity.user.getHeight());
        } catch (Exception e) {
            bmi = 0;
        }

        try {
            curweight = dbhandle.mostRecent().getWeight();
        } catch (Exception e) {
            curweight = 0;
        }

        curWeight.setText(CURR_WEIGHT+curweight);
        goalWeightMain.setText(GOAL_MAIN+MainActivity.user.getWeight());
        bmicur.setText(BMI+bmi);
        weekAvg.setText(WEEK_AVG);
    }
}

