package com.example.weighttracker;

import android.content.Intent;
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
    //setup the top and bottom bar
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
    String curUnit = MainActivity.user.getUnit();

    TextView userName;
    TextView goalWeight;
    TextView currHeight;

    ImageButton editSett;
    ImageButton backToSett;

    Button unitswap;

    EditText nameedit;
    EditText goaledit;
    EditText heightedit;

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
        currHeight = (TextView) findViewById(R.id.currHeight);

        editsettingsBack = (ConstraintLayout) findViewById(R.id.editSettings);

        editSett = findViewById(R.id.editSett);
        editSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditWindow();
            }
        });

        //edit texts for the change setting menu popup
        nameedit = (EditText) findViewById(R.id.nameEditTxt);
        goaledit = (EditText) findViewById(R.id.goalEditTxt);
        heightedit = (EditText) findViewById(R.id.heightEditTxt);

        //clicking the arrow in the settings popup saves those values to the user
        backToSett = findViewById(R.id.backToSett);
        backToSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = MainActivity.user.getName();;
                float  weight = MainActivity.user.getWeight();
                float height = MainActivity.user.getHeight();

                if (String.valueOf(nameedit.getText()) != name) {
                    name = String.valueOf(nameedit.getText());
                }

                if (Float.parseFloat(String.valueOf(goaledit.getText())) != weight) {
                    weight = Float.parseFloat(String.valueOf(goaledit.getText()));
                }

                if (Float.parseFloat(String.valueOf(heightedit.getText())) != height) {
                    height = Float.parseFloat(String.valueOf(heightedit.getText()));
                }

                updateUser(name, weight, height);
                updateView();
                editsettingsBack.setVisibility(View.INVISIBLE);
            }
        });

        //swapping the unit used in the app
        unitswap = findViewById(R.id.buttonUnit);
        unitswap.setText(MainActivity.user.getUnit());
        unitswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //if the user is new, goes straight to the edit page
        if (MainActivity.user.isNew()){
            showEditWindow();
            MainActivity.user.setNew(false);
        }
    }

    private void showEditWindow() {
            String name = MainActivity.user.getName();;
            double  weight = MainActivity.user.getWeight();
            float height = MainActivity.user.getHeight();

            editsettingsBack.setVisibility(View.VISIBLE);
            nameedit.setText(name);
            goaledit.setText(""+weight);
            heightedit.setText(""+height);
    }

    public void updateUser(String s, float w, float h) {
        MainActivity.user.setName(s);
        MainActivity.user.setWeight(w);
        MainActivity.user.setHeight(h);

        MainActivity.user.setPrefs(this);
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
        double temp = MainActivity.user.getWeight();
        if (units) {
            MainActivity.user.setUnits("lbs");
            MainActivity.user.setWeight((float) (temp/2.205));
            curUnit = "lbs";
        } else {
            MainActivity.user.setUnits("kg");
            MainActivity.user.setWeight((float) (temp*2.205));
            curUnit = "kg";
        }

    }

    public void updateView() {
        nameTxt.setText(MainActivity.user.getName());
        goalTxt.setText("Goal: "+MainActivity.user.getWeight()+MainActivity.user.getUnit());
        userName.setText(MainActivity.user.getName());
        goalWeight.setText(""+MainActivity.user.getWeight()+curUnit);
        currHeight.setText(""+MainActivity.user.getHeight()+"m");
    }

}