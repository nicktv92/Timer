package nicktv.android.timer.timedate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import nicktv.android.timer.R;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String PREFS_DATE_TIME = "DateTimeFile";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
    }
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        TextView editDate = (TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.dateTxtV);
        String date = pad(day) + "." + pad(month + 1) + "." + pad(year);
        editDate.setText(date);
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_DATE_TIME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("date", date);
        editor.apply();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
