package nicktv.android.timer.timedate;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import nicktv.android.timer.R;


public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String PREFS_DATE_TIME = "DateTimeFile";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        TextView editTime = (TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.timeTxtV);
        String time = pad(hourOfDay) + ":" + pad(minute);
        editTime.setText(time);

        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_DATE_TIME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("time", time);
        editor.apply();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
