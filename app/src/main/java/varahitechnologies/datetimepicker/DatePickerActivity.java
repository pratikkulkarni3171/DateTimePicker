package varahitechnologies.datetimepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import varahitechnologies.datetimepicker.DateTimeWheel.DateWheel.LoopListener;
import varahitechnologies.datetimepicker.DateTimeWheel.DateWheel.LoopView;

public class DatePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DEFAULT_MIN_YEAR = 1900;

    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public Button confirmBtn;

    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;

    ArrayList yearList = new ArrayList();
    ArrayList monthList = new ArrayList();
    ArrayList dayList = new ArrayList();

    private int minYear;
    private int maxYear;
    private int viewTextSize;

    //private boolean isLeapYear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);

        minYear = DEFAULT_MIN_YEAR;
        viewTextSize = 25;
        maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        setSelectedDate();

        initialiseDateWheel();

    }

    private void initialiseDateWheel() {

        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        yearLoopView = (LoopView) findViewById(R.id.picker_year);
        monthLoopView = (LoopView) findViewById(R.id.picker_month);
        dayLoopView = (LoopView) findViewById(R.id.picker_day);

        //do not loop,default can loop
        yearLoopView.setNotLoop();
        monthLoopView.setNotLoop();
        dayLoopView.setNotLoop();

        //set loopview text btnTextsize
        yearLoopView.setTextSize(viewTextSize);
        monthLoopView.setTextSize(viewTextSize);
        dayLoopView.setTextSize(viewTextSize);

        //set checked listen
        yearLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {

                /*if (isLeapYear(item)) {
                    isLeapYear = true;
                }else{
                    isLeapYear = false;
                }*/
                yearPos = item;
                initDayPickerView();
            }
        });
        monthLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                monthPos = item;
                initDayPickerView();
            }
        });
        dayLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                dayPos = item;
            }
        });

        initPickerViews(); // init year and month loop view
        initDayPickerView(); //init day loop view

        confirmBtn.setOnClickListener(this);

    }

    public void setSelectedDate() {

        Calendar today = Calendar.getInstance();
        yearPos = today.get(Calendar.YEAR) - minYear;
        monthPos = today.get(Calendar.MONTH);
        dayPos = today.get(Calendar.DAY_OF_MONTH) - 1;

    }


    private void initPickerViews() {

        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        yearLoopView.setArrayList((ArrayList) yearList);
        yearLoopView.setInitPosition(yearPos);

        monthLoopView.setArrayList((ArrayList) monthList);
        monthLoopView.setInitPosition(monthPos);
    }

    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setArrayList((ArrayList) dayList);
        dayLoopView.setInitPosition(dayPos);
    }

    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    @Override
    public void onClick(View view) {
        if (view == confirmBtn) {

            int year = /*minYear + */yearPos;
            int month = monthPos + 1;
            int day = dayPos + 1;
            StringBuffer sb = new StringBuffer();

            sb.append(format2LenStr(day));
            sb.append("-");
            sb.append(format2LenStr(month));
            sb.append("-");
            sb.append(String.valueOf(year));

            onDatePickCompleted(year, month, day, sb.toString());
        }
    }

    void onDatePickCompleted(int year, int month, int day,
                             String dateDesc) {

        Toast.makeText(DatePickerActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
    }
}