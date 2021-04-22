package com.example.weighttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivitySett extends AppCompatActivity {

    private SharedPreferences mPrefs;

    //setup the top and bottom bar
    String name = User.getInstance().getName();;
    double  weight = User.getInstance().getWeight();

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
    String curUnit = User.getInstance().getUnit();

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

        //setup the top and bottom bar
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

        //settings text views
        userName = (TextView) findViewById(R.id.userName);
        goalWeight = (TextView) findViewById(R.id.goalWeight);

        editsettingsBack = (ConstraintLayout) findViewById(R.id.editSettings);

        editSett = findViewById(R.id.editSett);
        editSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsettingsBack.setVisibility(View.VISIBLE);
                nameedit.setText(name);
                goaledit.setText(""+weight);
            }
        });

        nameedit = (EditText) findViewById(R.id.nameEditTxt);
        goaledit = (EditText) findViewById(R.id.goalEditTxt);

        //clicking the arrow in the settings popup saves those values to the user
        backToSett = findViewById(R.id.backToSett);
        backToSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(nameedit.getText()) != name) {
                    name = String.valueOf(nameedit.getText());
                }

                if (Double.parseDouble(String.valueOf(goaledit.getText())) != weight) {
                    weight = Double.parseDouble(String.valueOf(goaledit.getText()));
                }

                updateUser(name, weight);
                updateView();
                editsettingsBack.setVisibility(View.INVISIBLE);
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
                unitswap.setText(curUnit);
                goaledit.setText(""+User.getInstance().getWeight());
                updateView();
            }
        });

        updateView();
    }

    public void updateUser(String s, double d) {
        User.getInstance().setName(s);
        User.getInstance().setWeight(d);
    }

    protected void onStart() {
        super.onStart();
        updateView();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
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

    public void updateView() {
        nameTxt.setText(User.getInstance().getName());
        goalTxt.setText("Goal: "+User.getInstance().getWeight()+User.getInstance().getUnit());
        userName.setText(name);
        goalWeight.setText(""+weight+curUnit);
    }

}