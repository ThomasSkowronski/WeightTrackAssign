package com.example.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scrn);
    }

    public void screenChange(View v){
        switch (v.getId()){
            case (R.id.buttonEntry):
                setContentView(R.layout.entry_scrn);
                break;
            case (R.id.buttonMain):
                setContentView(R.layout.main_scrn);
                break;
            case (R.id.buttonHistory):
                setContentView(R.layout.hist_scrn);
                break;
            case (R.id.buttonSett):
                setContentView(R.layout.sett_scrn);
                break;



            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}