package nicktv.android.timer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import nicktv.android.timer.timedate.DatePickerFragment;
import nicktv.android.timer.timedate.TimePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFS_DATE_TIME = "DateTimeFile";
    private static final String START_DATE = "10.10.2018";
    private static final String START_TIME = "18:20";
    private TextView mTimerTxtV, mDayTxtV, mPercentTxtV, setDayTxtV, setTimeTextV;
    private FloatingActionButton mSetBtn;
    private ProgressBar mTimeProgress;
    private long maxProgress, nowProgress;
    private String dateCourse;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerTxtV = (TextView) findViewById(R.id.timerTxtV);
        mDayTxtV = (TextView) findViewById(R.id.dayTxtV);
        mPercentTxtV = (TextView) findViewById(R.id.percentTxtV);
        setDayTxtV = (TextView) findViewById(R.id.dateTxtV);
        setTimeTextV = (TextView) findViewById(R.id.timeTxtV);
        mSetBtn = (FloatingActionButton) findViewById(R.id.fab);
        mTimeProgress = (ProgressBar) findViewById(R.id.progressBar);

        setDayTxtV.setOnClickListener(this);
        setTimeTextV.setOnClickListener(this);
        mSetBtn.setOnClickListener(this);

        SharedPreferences preferences = this.getSharedPreferences(PREFS_DATE_TIME, 0);
        String date = preferences.getString("date", START_DATE);
        String time = preferences.getString("time", START_TIME);
        dateCourse = date + time;
        maxProgress = preferences.getLong("progress", timeToCourse(dateCourse));
        checkFirstOpen();
        setDayTxtV.setText(date);
        setTimeTextV.setText(time);
        mTimeProgress.setMax(100);
        startTimer();
    }


    public void startTimer() {
        countDownTimer = new CountDownTimer(timeToCourse(dateCourse), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = timeToCourse(dateCourse);
                nowProgress = time;
                mTimerTxtV.setText(getTime(time));
                mDayTxtV.setText(getDay(time));
                int progressPercent = progressSet(maxProgress, nowProgress);
                mTimeProgress.setProgress(progressPercent);
                mPercentTxtV.setText(String.valueOf(progressPercent) + "%");
            }

            @Override
            public void onFinish() {
                mTimerTxtV.setText(R.string.time_end);
                mDayTxtV.setText(R.string.day_count);
                mTimeProgress.setProgress(0);
                mPercentTxtV.setText("0%");
            }
        }.start();
    }

    public int progressSet(long max, long now) {
        return (int) (now * 100.0 / max + 0.5);
    }

    public long timeToCourse(String date) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        long endTime = dateParse(date);
        return endTime - startTime;
    }

    public long dateParse(String dateParse) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyyHH:mm", Locale.ROOT);
        long courseTime = 0;
        try {
            Date date = format.parse(dateParse);
            courseTime = date.getTime();
        } catch (ParseException e) {
            Log.e("log", e.getMessage(), e);
        }
        return courseTime;

    }

    public String getTime(long mSeconds) {
        long days = (mSeconds / (1000 * 60 * 60 * 24));
        long hours = (mSeconds - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60);
        long min = (mSeconds - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        long sec = (mSeconds / (1000)) % 60;

        return addO(hours) + "ч " + addO(min) + "м " + addO(sec) + "с ";
    }

    private static String addO(long c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public String getDay(long mSeconds) {
        long days = (mSeconds / (1000 * 60 * 60 * 24));
        return String.valueOf(days);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateTxtV:
                newDate();
                break;
            case R.id.timeTxtV:
                newTime();
                break;
            case R.id.fab:
                newTimerSet();
                break;
        }
    }

    public void newDate() {
        DatePickerFragment mDatePicker = new DatePickerFragment();
        mDatePicker.show(getSupportFragmentManager(), "Select date");
    }

    public void newTime() {
        TimePicker mTimePicker = new TimePicker();
        mTimePicker.show(getSupportFragmentManager(), "Select time");
    }

    public void newTimerSet() {
        countDownTimer.cancel();
        SharedPreferences preferences = this.getSharedPreferences(PREFS_DATE_TIME, 0);
        String date = preferences.getString("date", START_DATE);
        String time = preferences.getString("time", START_TIME);
        dateCourse = date + time;
        setDayTxtV.setText(date);
        setTimeTextV.setText(time);
        maxProgress = timeToCourse(dateCourse);
        mTimeProgress.setMax(100);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("progress", maxProgress);
        editor.apply();
        startTimer();
    }

    public void checkFirstOpen() {
        SharedPreferences preferences = this.getSharedPreferences(PREFS_DATE_TIME, 0);
        boolean hasVisited = preferences.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("date", START_DATE);
            editor.putString("time", START_TIME);
            editor.putLong("progress", timeToCourse(START_DATE + START_TIME));
            editor.putBoolean("hasVisited", true);
            editor.apply();
        }
    }
}
