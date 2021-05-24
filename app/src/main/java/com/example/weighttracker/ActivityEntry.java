package com.example.weighttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.CalendarView;
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

public class ActivityEntry extends AppCompatActivity {

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

    //elements specific to the entry page
    static TextView dateview;
    EditText weightEntertxt;

    Button submit;
    ImageButton cam;

    ImageView entryPic;
    private Bitmap bitmap;

    private static long entryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

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

        dateview = (TextView) findViewById(R.id.dateView);
        dateview.setText("Date");

        //camera button
        PackageManager manager = this.getPackageManager();

        cam = (ImageButton) findViewById(R.id.camButton);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePic, 1);
                }
            }
        });

        //input fields and submit button
        weightEntertxt = (EditText) findViewById(R.id.weightEntertxt);

        entryPic = (ImageView) findViewById(R.id.entryPic);

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence message = "Must enter valid values for the weight and date.";
                int dur = Toast.LENGTH_SHORT;
                Date todays = new Date();
                try {
                    //checks for valid input in the date field, if not clears the fields to reset the input
                    if (entryDate == 0 | entryDate > todays.getTime()){ weightEntertxt.setText(""); }

                    double weightIn = Double.parseDouble(String.valueOf(weightEntertxt.getText()));
                    double kg = 0.0;
                    double lb = 0.0;

                    //depending on unit, also creates the mirrored value for the other unit
                    if (MainActivity.user.getUnit()){
                        kg = weightIn;
                        lb = weightIn*2.205;
                    } else {
                        kg = weightIn/2.205;
                        lb = weightIn;
                    }

                    //created the entry and adds to the db
                    Entry entry = new Entry(0, entryDate, kg, lb);
                    MainActivity.dbhandle.insert(entry);

                    if (entryPic.getDrawable() != null) {
                        saveEntryPic ();
                        entryPic.setImageBitmap(null);
                    }

                } catch (Exception e){
                    //if some input was left blank prompt the user
                    Toast.makeText(context,message,dur).show();
                }

                //reset the fields for another input
                dateview.setText("Date");
                weightEntertxt.setText("");
            }
        });

        updateView();
    }

    protected void onActivityResult (int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == 1 && result == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get ("data");
            entryPic.setImageBitmap(bitmap);
        }
    }

    private void saveEntryPic() {
        Log.d("entry pic", "entered save pic class");

        Entry recentEntry = MainActivity.dbhandle.mostRecent();
        try {
            MainActivity.photo.writePhoto(this, bitmap, recentEntry);
            Toast.makeText(this, "saved pic", Toast.LENGTH_LONG).show();
        } catch (IOException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

        final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        String sDate = sdf.format(entryDate);
        dateview.setText(sDate);

    }

    public void dateSelector(View v) {
        DialogFragment newFragment = new ActivityEntry.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static void setDate(long l) {
        entryDate = l;

        final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        String sDate = sdf.format(entryDate);
        ActivityEntry.dateview.setText(sDate);
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
            ActivityEntry.setDate(milli);
        }
    }
}