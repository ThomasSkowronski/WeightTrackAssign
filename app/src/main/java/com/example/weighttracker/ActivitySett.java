package com.example.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivitySett extends AppCompatActivity {

    private SharedPreferences mPrefs;

    //setup the top and bottom bar
    String name = User.getInstance().getName();
    String goal = "Goal Weight: "+User.getInstance().getWeight()+ User.getInstance().getUnit();

    Button entrybtn;
    ImageButton mainbtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //text fields for the settings
    //true = lbs | false = kg
    boolean units = true;
    String curUnit = "";

    TextView userName;
    TextView goalWeight;

    ImageButton editSett;
    ImageButton backToSett;

    Button unitswap;

    EditText nameedit;
    EditText goaledit;

    ConstraintLayout editsettingsBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("com.example.weighttracker_pref", 0);
        name = mPrefs.getString("user_name", "user");
        User.getInstance().setName(name);

        //setup the top and bottom bar
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

        nameTxt.setText(name);
        goalTxt.setText(goal);

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

        //settings text views
        userName = (TextView) findViewById(R.id.userName);

        editsettingsBack = (ConstraintLayout) findViewById(R.id.editSettings);

        editSett = findViewById(R.id.editSett);
        editSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsettingsBack.setVisibility(View.VISIBLE);
            }
        });

        nameedit = (EditText) findViewById(R.id.nameEditTxt);
        goaledit = (EditText) findViewById(R.id.goalEditTxt);

        //clicking the arrow in the settings popup saves those values to the user
        backToSett = findViewById(R.id.backToSett);
        backToSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsettingsBack.setVisibility(View.INVISIBLE);
                if (String.valueOf(nameedit.getText()) != "") {
                    User.getInstance().setName(String.valueOf(nameedit.getText()));
                }
                if (String.valueOf(goaledit.getText()) != "") {
                    User.getInstance().setWeight(Double.parseDouble(String.valueOf(goaledit.getText())));
                }
                refreshTopBar();
            }
        });

        //swapping the unit used in the app
        unitswap = findViewById(R.id.buttonUnit);
        unitswap.setText(User.getInstance().getUnit());
        unitswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                units = !units;
                unitSwapper();
                User.getInstance().setUnits(curUnit);
                unitswap.setText(curUnit);
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

    public void unitSwapper() {
        double temp = User.getInstance().getWeight();
        if (units) {
            User.getInstance().setUnits("lbs");
            User.getInstance().setWeight(temp/2.205);
            curUnit = "lbs";
        } else {
            User.getInstance().setUnits("kg");
            User.getInstance().setWeight(temp*2.205);
            curUnit = "kg";
        }

    }

    public void refreshTopBar(){
        name = User.getInstance().getName();
        goal = "Goal Weight: "+User.getInstance().getWeight()+ User.getInstance().getUnit();

        nameTxt.setText(name);
        goalTxt.setText(goal);
    }
}