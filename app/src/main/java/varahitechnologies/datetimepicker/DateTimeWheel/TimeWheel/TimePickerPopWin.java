package varahitechnologies.datetimepicker.DateTimeWheel.TimeWheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import varahitechnologies.datetimepicker.R;

/**
 * PopWindow for Date Pick
 */
public class TimePickerPopWin extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_HOUR = 1;
    private static final int DEFAULT_MIN_MIN = 0;
    private static final int DEFAULT_MIN_SEC = 0;
    private static final int DEFAULT_MAX_HOUR = 12;
    private static final int DEFAULT_MAX_MIN = 59;
    private static final int DEFAULT_MAX_SEC = 59;

    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView hourLoopView;
    public LoopView minLoopView;
    public LoopView secLoopView;
    public LoopView timeMeridiemView;
    public View pickerContainerV;
    public View contentView;//root view

    private int maxHour; // max year
    private int maxMin; // max year
    private int maxSec; // max year
    private int hourPos = 0;
    private int minPos = 0;
    private int secPos = 0;
    private int timeMeridiemPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;

    List<String> hourList = new ArrayList();
    List<String> minList = new ArrayList();
    List<String> secList = new ArrayList();
    List<String> merediumList = new ArrayList();

    public static class Builder {

        //Required
        private Context context;
        private OnTimePickedListener listener;

        public Builder(Context context, OnTimePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        //Option
        private int minHour = DEFAULT_MIN_HOUR;
        private int maxHour = DEFAULT_MAX_HOUR;
        private int minMin = DEFAULT_MIN_MIN;
        private int maxMin = DEFAULT_MAX_MIN;
        private int minSec = DEFAULT_MIN_SEC;
        private int maxSec = DEFAULT_MAX_SEC;

        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private String timeChose = getStrTime();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder timeChose(String timeChose) {
            this.timeChose = timeChose;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         *
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build() {
            if (minHour > maxHour) {
                throw new IllegalArgumentException();
            }
            if (minMin > maxMin) {
                throw new IllegalArgumentException();
            }
            if (minSec > maxSec) {
                throw new IllegalArgumentException();
            }
            return new TimePickerPopWin(this);
        }
    }

    public TimePickerPopWin(Builder builder) {
        this.maxHour = builder.maxHour;
        this.maxMin = builder.maxMin;
        this.maxSec = builder.maxSec;

        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        setSelectedDate(builder.timeChose);
        initView();
    }

    private OnTimePickedListener mListener;

    private void initView() {

        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.layout_time_picker, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        hourLoopView = (LoopView) contentView.findViewById(R.id.picker_hour);
        minLoopView = (LoopView) contentView.findViewById(R.id.picker_min);
        secLoopView = (LoopView) contentView.findViewById(R.id.picker_sec);
        timeMeridiemView = (LoopView) contentView.findViewById(R.id.picker_meridiem);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        cancelBtn.setText(textCancel);
        confirmBtn.setText(textConfirm);
        cancelBtn.setTextColor(colorCancel);
        confirmBtn.setTextColor(colorConfirm);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn.setTextSize(btnTextsize);

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
                //initDayPickerView();
            }
        });
        minLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                minPos = item;
                //initDayPickerView();
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

        initPickerViews(); // init year and month loop view
        //initDayPickerView(); //init day loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        //setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
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

        hourLoopView.setArrayList((ArrayList) hourList);
        hourLoopView.setInitPosition(hourPos);

        minLoopView.setArrayList((ArrayList) minList);
        minLoopView.setInitPosition(minPos);

        secLoopView.setArrayList((ArrayList) secList);
        secLoopView.setInitPosition(secPos);

        merediumList.add("AM");
        merediumList.add("PM");
        timeMeridiemView.setArrayList((ArrayList) merediumList);
        timeMeridiemView.setInitPosition(timeMeridiemPos);

    }


    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    public void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                hourPos = calendar.get(Calendar.HOUR);
                minPos = calendar.get(Calendar.MINUTE);
                secPos = calendar.get(Calendar.SECOND);

                String[] date = getStrTime().split(" ");
                if(date[1].equals("AM")){
                    timeMeridiemPos = 0;
                }else  if(date[1].equals("PM")){
                    timeMeridiemPos = 1;
                }
            }
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {

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
                mListener.onTimePickCompleted(hour, min, sec, merediumText, sb.toString());
            }

            dismissPopWin();
        }
    }

    /**
     * get long from hh:mm:ss
     *
     * @param time
     * @return
     */
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

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnTimePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param hour
         * @param min
         * @param sec
         * @param meridium
         * @param timeDesc HH:mm:ss
         */
        void onTimePickCompleted(int hour, int min, int sec, String meridium,
                                 String timeDesc);
    }
}