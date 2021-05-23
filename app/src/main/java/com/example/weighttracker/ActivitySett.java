package com.example.weighttracker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivitySett extends AppCompatActivity {
    //setup the top and bottom bar
    int prime = Color.parseColor("#BAFFD9");
    ImageView profPic;

    DecimalFormat decimal = new DecimalFormat("#.#");

    Button entrybtn;
    ImageButton mainbtn;
    ImageButton settbtn;
    ImageButton histbtn;

    TextView nameTxt;
    TextView goalTxt;
    TextView dateTxt;

    //text fields for the settings
    //true = kgs | false = lbs
    boolean units = true;

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

    Button camButton;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);

        //setup the top and bottom bar
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
                boolean curUnit = MainActivity.user.getUnit();

                if (String.valueOf(nameedit.getText()) != name) {
                    name = String.valueOf(nameedit.getText());
                }

                if (Float.parseFloat(String.valueOf(goaledit.getText())) != weight) {
                    weight = Float.parseFloat(String.valueOf(goaledit.getText()));
                }

                if (Float.parseFloat(String.valueOf(heightedit.getText())) != height) {
                    height = Float.parseFloat(String.valueOf(heightedit.getText()));
                }

                updateUser(name, weight, height, curUnit);
                updateView();
                editsettingsBack.setVisibility(View.INVISIBLE);
            }
        });

        //swapping the unit used in the app
        unitswap = findViewById(R.id.buttonUnit);

        unitswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = MainActivity.user.getName();;
                float  weight = MainActivity.user.getWeight();
                float height = MainActivity.user.getHeight();
                boolean curUnit = MainActivity.user.getUnit();
                if (curUnit) {
                    curUnit = false;
                    weight = (float) (weight*2.205);
                    height = (float) (height*3.281);
                } else if (!curUnit) {
                    curUnit = true;
                    weight = (float) (weight/2.205);
                    height = (float) (height/3.281);
                }
                updateUser(name, weight, height, curUnit);
                updateView();
            }
        });

        //camera button
        PackageManager manager = this.getPackageManager();

        camButton = findViewById(R.id.camButton);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePic, 1);
                }
            }
        });

        //if the user is new, goes straight to the edit page
        if (MainActivity.user.isNew()){
            showEditWindow();
            MainActivity.user.setNew(false);
        }
    }

    protected void onActivityResult (int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == 1 && result == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get ("data");
            saveProfPic();
        }
    }

    private void saveProfPic() {
        Log.d("Prof pic", "entered save pic class");

        try {
            MainActivity.photo.writeProfilePic(this, bitmap);
            Toast.makeText(this, "saved pic", Toast.LENGTH_LONG).show();
        } catch (IOException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showEditWindow() {
            String name = MainActivity.user.getName();;
            double  weight = MainActivity.user.getWeight();
            double height = MainActivity.user.getHeight();

            editsettingsBack.setVisibility(View.VISIBLE);
            nameedit.setText(name);
            goaledit.setText(""+weight);
            heightedit.setText(""+height);
    }

    public void updateUser(String s, float w, float h, boolean u) {
        MainActivity.user.setName(s);
        MainActivity.user.setWeight(w);
        MainActivity.user.setHeight(h);
        MainActivity.user.setUnits(u);

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
        String unitH = MainActivity.user.getHeightString();
        String height = decimal.format(MainActivity.user.getHeight());

        nameTxt.setText(MainActivity.user.getName());
        goalTxt.setText("Goal: "+weight+units);
        userName.setText(MainActivity.user.getName());
        goalWeight.setText(""+weight+units);

        currHeight.setText(""+height+unitH);

        if (MainActivity.user.getUnit()) {
            unitswap.setText("Kgs/M");
        } else if (!MainActivity.user.getUnit()) {
            unitswap.setText("Lbs/Ft");
        }
    }

}