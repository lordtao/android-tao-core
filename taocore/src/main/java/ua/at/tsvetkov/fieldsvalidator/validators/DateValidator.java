package ua.at.tsvetkov.fieldsvalidator.validators;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.at.tsvetkov.util.Const;
import ua.at.tsvetkov.util.Log;

/**
 * Created by Alexandr Tsvetkov on 28.05.2015.
 */
public class DateValidator extends AbstractValidator {

    private final Date mStartDate;
    private final Date mFinishDate;
    private SimpleDateFormat sdf;

    public DateValidator(EditText editText, Date startDate, Date finishDate, String dateFormat, String errMsg) {
        super(editText, errMsg);
        mStartDate = startDate;
        mFinishDate = finishDate;
        sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    }


    public DateValidator(EditText editText, int years, String dateFormat, String errMsg) {
        super(editText, errMsg);
        if (editText == null || errMsg == null) {
            throw new IllegalArgumentException("Validators field can' be null");
        }
        mStartDate = new Date(System.currentTimeMillis() - (Const.YEAR * years));
        mFinishDate = null;
        sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    }

    public boolean isValid() {
        String dateString = getView().getText().toString();
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            Log.w("Wrong date: " + dateString);
            return false;
        }
        if (mFinishDate == null) {
            return mStartDate.after(date);
        }
        return mStartDate.after(date) && mFinishDate.before(date);
    }

    @Override
    public String getContentString() {
        String dateString = getView().getText().toString();
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            // nothing
        }
        if (mFinishDate == null) {
            return "start:" + format(mStartDate) + " < field:" + format(date);
        } else {
            return "start:" + format(mStartDate) + " < field:" + format(date) + " > finish:" + format(mFinishDate);
        }
    }

    private String format(Date date) {
        if (date == null) return null;
        return java.text.DateFormat.getDateInstance().format(date);
    }

}
