package com.example.weighttracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //static values to be used across screens
    public static databaseHandler dbhandle;
    public static User user;
    public static PhotoHandler photo;

    //setup top and bottom bar
    int prime = Color.parseColor("#BAFFD9");
    ImageView profPic;
    DecimalFormat decimal = new DecimalFormat("#.#");

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

    TextView currGoalDate;

    //Line graph
    GraphView graphView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(this);
        setContentView(R.layout.main_scrn);
        dbhandle = new databaseHandler(this);

        //setup top and bottom bar
        profPic = (ImageView) findViewById(R.id.profPic);
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

        currGoalDate =(TextView) findViewById(R.id.goalDate);


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

        //Line graph
        graphView = findViewById(R.id.idGraphView);
        graphView.setTitle("Weight Loss History");

        updateView();

    }

    protected void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        //set the profile picture
        try {
            File dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String filename = "ProfilePic";
            Drawable drawProfile = Drawable.createFromPath(dir+filename);
            profPic.setImageDrawable(drawProfile);
            profPic.setBackgroundColor(prime);
        } catch (Exception e) {
            profPic.setImageBitmap(null);
        }


        //get user values as strings to input into text views
        String weight = decimal.format(MainActivity.user.getWeight());
        String units = MainActivity.user.getWeightString();

        nameTxt.setText(user.getName());
        goalTxt.setText("Goal: "+weight+units);

        //Main info box
        String CURR_WEIGHT = "Current Weight: ";
        String GOAL_MAIN = "Goal Weight: ";
        String BMI = "Current BMI: ";
        String WEEK_AVG = "Weekly Average Lost: ";
        String GOAL_DATE = "Goal Date: ";

        double bmi;
        double curweight;

        try {
            if (MainActivity.user.getUnit()){
                bmi = dbhandle.mostRecent().getKg() / (MainActivity.user.getHeight()*MainActivity.user.getHeight());
            } else {
                double temp = MainActivity.user.getHeight()/3.281;
                bmi = dbhandle.mostRecent().getKg() / (temp*temp);
            }
        } catch (Exception e) {
            bmi = 0;
        }

        try {
            if (MainActivity.user.getUnit()) {
                curweight = dbhandle.mostRecent().getKg();
            } else {
                curweight = dbhandle.mostRecent().getLbs();
            }
        } catch (Exception e) {
            curweight = 0.0;
        }

        double average = 0;
        try {
            ArrayList<Entry> lastWeek = MainActivity.dbhandle.selectPastWeek();
            int pastWkEntries = lastWeek.size();
            double runTotal;
            if (MainActivity.user.getUnit()) {
                runTotal = lastWeek.get(0).getKg()-lastWeek.get(lastWeek.size()-1).getKg();
            } else {
                runTotal = lastWeek.get(0).getLbs()-lastWeek.get(lastWeek.size()-1).getLbs();
            }
            average = runTotal/pastWkEntries;
        } catch (Exception e) {
            average = 0;
        }

        curWeight.setText(CURR_WEIGHT+decimal.format(curweight)+units);
        goalWeightMain.setText(GOAL_MAIN+weight+units);
        bmicur.setText(BMI+decimal.format(bmi));
        weekAvg.setText(WEEK_AVG+decimal.format(average)+units);

        final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        String sDate = sdf.format(MainActivity.user.getDate());
        currGoalDate.setText(GOAL_DATE+sDate);

        //create the series to add to the graph
        try {
            ArrayList<Entry> entries = MainActivity.dbhandle.selectAllReverse();
            Entry[] entriesList = entries.toArray(new Entry[entries.size()]);
            LineGraphSeries<Entry> series = new LineGraphSeries<Entry>(entriesList);
            graphView.addSeries(series);

            graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            graphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
            // set manual x bounds to have nice steps
            graphView.getViewport().setMinX(entriesList[0].getX());
            graphView.getViewport().setMaxX(entriesList[entries.size() - 1].getX());
            graphView.getViewport().setXAxisBoundsManual(true);

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            graphView.getGridLabelRenderer().setHumanRounding(false);
        } catch (Exception e) {

        }

    }
}

