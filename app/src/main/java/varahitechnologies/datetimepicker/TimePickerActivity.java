package varahitechnologies.datetimepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import varahitechnologies.datetimepicker.DateTimeWheel.TimeWheel.LoopListener;
import varahitechnologies.datetimepicker.DateTimeWheel.TimeWheel.LoopView;


public class TimePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DEFAULT_MIN_HOUR = 1;
    private static final int DEFAULT_MIN_MIN = 0;
    private static final int DEFAULT_MIN_SEC = 0;
    private static final int DEFAULT_MAX_HOUR = 12;
    private static final int DEFAULT_MAX_MIN = 59;
    private static final int DEFAULT_MAX_SEC = 59;

    public Button confirmBtn;
    public LoopView hourLoopView;
    public LoopView minLoopView;
    public LoopView secLoopView;
    public LoopView timeMeridiemView;

    private int hourPos = 0;
    private int minPos = 0;
    private int secPos = 0;
    private int timeMeridiemPos = 0;
    ArrayList hourList = new ArrayList();
    ArrayList minList = new ArrayList();
    ArrayList secList = new ArrayList();
    ArrayList merediumList = new ArrayList();

    private int viewTextSize = 25;

    int minHour;
    int maxHour;
    int minMin;
    int maxMin;
    int minSec;
    int maxSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_picker);

        //Option
        minHour = DEFAULT_MIN_HOUR;
        maxHour = DEFAULT_MAX_HOUR;
        minMin = DEFAULT_MIN_MIN;
        maxMin = DEFAULT_MAX_MIN;
        minSec = DEFAULT_MIN_SEC;
        maxSec = DEFAULT_MAX_SEC;

        long milliseconds = getLongFromyyyyMMdd(getStrTime());
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        if (milliseconds != -1) {

            calendar.setTimeInMillis(milliseconds);
            hourPos = calendar.get(Calendar.HOUR);
            minPos = calendar.get(Calendar.MINUTE);
            secPos = calendar.get(Calendar.SECOND);

            String[] date = getStrTime().split(" ");
            if (date[1].equals("AM")) {
                timeMeridiemPos = 0;
            } else if (date[1].equals("PM")) {
                timeMeridiemPos = 1;
            }
        }

        initialiseTimeWheel();
    }

    public static long getLongFromyyyyMMdd(String time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrTime() {
        SimpleDateFormat dd = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return dd.format(new Date());
    }


    public void initialiseTimeWheel() {

        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        hourLoopView = (LoopView) findViewById(R.id.picker_hour);
        minLoopView = (LoopView) findViewById(R.id.picker_min);
        secLoopView = (LoopView) findViewById(R.id.picker_sec);
        timeMeridiemView = (LoopView) findViewById(R.id.picker_meridiem);


        //do not loop,default can loop
        hourLoopView.setNotLoop();
        minLoopView.setNotLoop();
        secLoopView.setNotLoop();
        timeMeridiemView.setNotLoop();

        //set loopview text btnTextsize
        hourLoopView.setTextSize(viewTextSize);
        minLoopView.setTextSize(viewTextSize);
        secLoopView.setTextSize(viewTextSize);
        timeMeridiemView.setTextSize(viewTextSize);

        //set checked listen
        hourLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                hourPos = item;
            }
        });
        minLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                minPos = item;
            }
        });
        secLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                secPos = item;
            }
        });

        timeMeridiemView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                timeMeridiemPos = item;
            }
        });

        initPickerViews();

        confirmBtn.setOnClickListener(this);

    }

    private void initPickerViews() {

        int hourCount = maxHour;
        int minCount = maxMin;
        int secCount = maxSec;

        for (int i = 1; i <= hourCount; i++) {
            hourList.add(format2LenStr(i));
        }

        for (int j = 0; j <= minCount; j++) {
            minList.add(format2LenStr(j));
        }

        for (int k = 0; k <= secCount; k++) {
            secList.add(format2LenStr(k));
        }

        hourLoopView.setArrayList(hourList);
        hourLoopView.setInitPosition(hourPos);

        minLoopView.setArrayList(minList);
        minLoopView.setInitPosition(minPos);

        secLoopView.setArrayList(secList);
        secLoopView.setInitPosition(secPos);

        merediumList.add("AM");
        merediumList.add("PM");
        timeMeridiemView.setArrayList(merediumList);
        timeMeridiemView.setInitPosition(timeMeridiemPos);

    }

    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    @Override
    public void onClick(View view) {
        if (view == confirmBtn) {

            int hour = hourPos + 1;
            int min = minPos;
            int sec = secPos;
            int meredium = timeMeridiemPos;
            String merediumText = "";
            if (meredium == 0) {
                merediumText = "AM";
            } else if (meredium == 1) {
                merediumText = "PM";
            }

            StringBuffer sb = new StringBuffer();

            sb.append(String.valueOf(hour));
            sb.append(":");
            sb.append(format2LenStr(min));
            sb.append(":");
            sb.append(format2LenStr(sec));
            sb.append(" ");
            sb.append(merediumText);
            onTimePickCompleted(hour, min, sec, merediumText, sb.toString());
        }
    }

    void onTimePickCompleted(int hour, int min, int sec, String meridium,
                             String timeDesc) {
        Toast.makeText(TimePickerActivity.this, timeDesc, Toast.LENGTH_SHORT).show();
    }
}
