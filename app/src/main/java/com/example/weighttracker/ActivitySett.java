package com.example.weighttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    TextView currGoalDate;

    ImageButton editSett;
    ImageButton backToSett;

    Button unitswap;

    EditText nameedit;
    EditText goaledit;
    EditText heightedit;

    ConstraintLayout editsettingsBack;

    Button camButton;
    private Bitmap bitmap;

    Button delProfPic;

    //date picker
    static TextView goalDatePick;
    private static long goalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);

        //setup the top and bottom bar
        profPic = (ImageView) findViewById(R.id.profPic);
        nameTxt = (TextView) findViewById(R.id.name);
        goalTxt = (TextView) findViewById(R.id.goal);
        dateTxt = (TextView) findViewById(R.id.date);

        currGoalDate = (TextView) findViewById(R.id.currGoalDate);


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

        delProfPic = findViewById(R.id.deleteProfPic);
        delProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removePhoto("ProfilePic");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateView();
            }
        });

        //date picker button
        goalDatePick = (TextView) findViewById(R.id.goalDateEdit);

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

    public void removePhoto(String s) throws IOException {
        MainActivity.photo.deletePhoto(this, s);
    }

    private void showEditWindow() {
            String name = MainActivity.user.getName();;
            double  weight = MainActivity.user.getWeight();
            double height = MainActivity.user.getHeight();

            editsettingsBack.setVisibility(View.VISIBLE);
            nameedit.setText(name);
            goaledit.setText(""+decimal.format(weight));
            heightedit.setText(""+decimal.format(height));
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
            Drawable drawProfile = Drawable.createFromPath(dir+filename);
            profPic.setImageDrawable(drawProfile);
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

        //date text handler
        final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        String sdate = sdf.format(MainActivity.user.getDate());
        goalDatePick.setText(sdate);

        currGoalDate.setText(sdate);

    }

    public void dateSelector(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static void setDate(long l) {
        goalDate = l;
        MainActivity.user.setDate(goalDate);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        long milli;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            milli = calendar.getTimeInMillis();
            ActivitySett.setDate(milli);

            final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
            String sDate = sdf.format(calendar.getTime());
            ActivitySett.goalDatePick.setText(sDate);
        }
    }
}