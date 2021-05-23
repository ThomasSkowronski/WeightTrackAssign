package com.example.weighttracker;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityHist extends AppCompatActivity {

    //setup top and bottom bar
    ImageView profPic;

    int prime = Color.parseColor("#BAFFD9");
    int elem = Color.parseColor("#58CC8D");

    DecimalFormat decimal = new DecimalFormat("#.#");


    ImageButton mainbtn;
    Button entrybtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //setup screen specific elements
    LinearLayout histScroll;

    FrameLayout histViewer;
    ImageView histImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);

        //setup top and bottom bar
        profPic = (ImageView) findViewById(R.id.profPic);
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
        histViewer = (FrameLayout) findViewById(R.id.histViewer);
        histViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                histViewer.setVisibility(View.INVISIBLE);
            }
        });
        histImg = (ImageView) findViewById(R.id.histImg);


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
            Drawable draw = new Drawable() {
                @Override
                public void draw(@NonNull Canvas canvas) {

                }

                @Override
                public void setAlpha(int alpha) {

                }

                @Override
                public void setColorFilter(@Nullable ColorFilter colorFilter) {

                }

                @Override
                public int getOpacity() {
                    return PixelFormat.OPAQUE;
                }
            }.createFromPath(dir+filename);
            profPic.setImageDrawable(draw);
            profPic.setBackgroundColor(prime);
        } catch (Exception e) {
            profPic.setImageBitmap(null);
        }

        //get user values as strings to input into text views
        String weight = decimal.format(MainActivity.user.getWeight());
        String units = MainActivity.user.getWeightString();

        nameTxt.setText(MainActivity.user.getName());
        goalTxt.setText("Goal: "+weight+units);


        //adds the entries from the database onto the history screen
        histScroll.removeAllViews();

        final SimpleDateFormat sdf = new SimpleDateFormat("M.dd.YY");
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 128);
        params.weight = 1;
        params.topMargin = 24;

        File dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //loops thru the select all on the SQL to grab each entry in order by date
        try {
            ArrayList<Entry> entries = MainActivity.dbhandle.selectAll();
            for (Entry entry : entries) {
                LinearLayout row = new LinearLayout(this);

                //date text for entries
                TextView tvDate = new TextView(this);
                tvDate.setId(entry.getId());
                tvDate.setText(""+sdf.format(entry.getDate()));
                tvDate.setBackgroundColor(prime);
                tvDate.setLayoutParams(params);

                row.addView(tvDate);

                //weight text for entries
                TextView tvWeight = new TextView(this);
                tvWeight.setId(entry.getId());
                if (MainActivity.user.getUnit()){
                    tvWeight.setText(" Weight: "+entry.getKg());
                } else if (!MainActivity.user.getUnit()){
                    tvWeight.setText(" Weight: "+entry.getLbs());
                }
                tvWeight.setBackgroundColor(prime);
                tvWeight.setLayoutParams(params);

                row.addView(tvWeight);

                //thumbnail pic for entries
                ImageButton thumb = new ImageButton(this);
                String filename = ""+entry.getId();
                Drawable draw = new Drawable() {
                    @Override
                    public void draw(@NonNull Canvas canvas) {

                    }

                    @Override
                    public void setAlpha(int alpha) {

                    }

                    @Override
                    public void setColorFilter(@Nullable ColorFilter colorFilter) {

                    }

                    @Override
                    public int getOpacity() {
                        return PixelFormat.OPAQUE;
                    }
                }.createFromPath(dir+filename);
                thumb.setImageDrawable(draw);
                thumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
                thumb.setLayoutParams(params);
                thumb.setBackgroundColor(prime);
                thumb.setId(entry.getId());

                thumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        histViewer.setVisibility(View.VISIBLE);
                        histImg.setImageDrawable(draw);
                    }
                });

                row.addView(thumb);

                ImageButton edit = new ImageButton(this);
                edit.setImageResource(R.drawable.trash);
                edit.setBackgroundColor(prime);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.dbhandle.deleteId(entry.getId());
                        try {
                            String entryId = ""+entry.getId();
                            removePhoto(entryId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateView();
                    }
                });
                edit.setLayoutParams(params);

                row.addView(edit);


                histScroll.addView(row);
            }
            //end for loop
        } catch (Exception e) {
            LinearLayout row = new LinearLayout(this);

            TextView noEntry = new TextView(this);
            noEntry.setBackgroundColor(prime);
            noEntry.setLayoutParams(params);
            noEntry.setText("There's no entries yet. Go add some!");

            row.addView(noEntry);

            histScroll.addView(row);
        }
    }

    public void removePhoto(String entry) throws IOException {
        MainActivity.photo.deletePhoto(this, entry);
    }
}