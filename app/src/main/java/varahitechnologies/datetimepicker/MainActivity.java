package varahitechnologies.datetimepicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import varahitechnologies.datetimepicker.DateTimeWheel.DateWheel.DatePickerPopWin;
import varahitechnologies.datetimepicker.DateTimeWheel.TimeWheel.TimePickerPopWin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                FrameLayout splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
                splashScreen.setVisibility(View.GONE);

            }

        }, 3000);

    }

    public void openDatePicker(View view) {
        Intent i = new Intent(MainActivity.this, DatePickerActivity.class);
        startActivity(i);
    }

    public void openTimePicker(View view) {
        Intent i = new Intent(MainActivity.this, TimePickerActivity.class);
        startActivity(i);
    }

    public void openDatePickerDialog(View view) {

        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(MainActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                Toast.makeText(MainActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose("2013-11-11") // date chose when init popwindow
                .build();

        pickerPopWin.showPopWin(this);
    }

    public void openTimePickerDialog(View view) {
        TimePickerPopWin pickerPopWin = new TimePickerPopWin.Builder(MainActivity.this, new TimePickerPopWin.OnTimePickedListener() {
            @Override
            public void onTimePickCompleted(int hour, int min, int sec, String meridium, String timeDesc) {
                Toast.makeText(MainActivity.this, timeDesc, Toast.LENGTH_SHORT).show();
            }

        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .build();

        pickerPopWin.showPopWin(this);
    }
}