package com.example.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityEntry extends AppCompatActivity {

    //setup top and bottom bar
    ImageButton mainbtn;
    Button entrybtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //elements specific to the entry page
    ConstraintLayout calLayout;
    CalendarView cal;
    long dateMilli;
    Button calDone;
    TextView dateview;
    EditText weightEntertxt;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

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

        cal = (CalendarView) findViewById(R.id.calendar);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String sDate = sdf.format(calendar.getTime());
                dateMilli = calendar.getTimeInMillis();
                dateview.setText(sDate);
            }
        });



        dateview = (TextView) findViewById(R.id.dateView);
        dateview.setText("Date");

        calLayout = findViewById(R.id.calLayout);

        calDone = findViewById(R.id.calDone);
        calDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calLayout.setVisibility(View.INVISIBLE);
            }
        });

        weightEntertxt = (EditText) findViewById(R.id.weightEntertxt);

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence message = "Must enter values for the weight and date.";
                int dur = Toast.LENGTH_SHORT;
                try {
                    if (dateMilli == 0){ weightEntertxt.setText(""); }
                    double weight = Double.parseDouble(String.valueOf(weightEntertxt.getText()));
                    Entry entry = new Entry(0, dateMilli, weight);
                    MainActivity.dbhandle.insert(entry);
                } catch (Exception e){
                    Toast.makeText(context,message,dur).show();
                }
                dateview.setText("Date");
                weightEntertxt.setText("");
            }
        });

        updateView();
    }

    public void updateView() {
        nameTxt.setText(MainActivity.user.getName());
        goalTxt.setText("Goal: "+MainActivity.user.getWeight()+MainActivity.user.getUnit());
    }

    public void dateSelector(View view) {
        calLayout.setVisibility(View.VISIBLE);
    }
}