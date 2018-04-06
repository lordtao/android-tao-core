package ua.at.tsvetkov.fieldsvalidator.validators;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Password validator
 * Created by Alexandr Tsvetkov on 28.05.2015.
 */
public class PasswordValidator extends AbstractValidator {

    private final EditText mPasswordAgain;
    private final Pattern mPattern;

    public PasswordValidator(EditText password, EditText passwordAgain, String regex, String errMsg) {
        super(password, errMsg);
        mPattern = Pattern.compile(regex);
        mPasswordAgain = passwordAgain;
    }


    public boolean isValid() {
        String text = getView().getText().toString();
        return mPattern.matcher(text).matches() && mPasswordAgain.getText().toString().equals(text);
    }

    @Override
    public String getContentString() {
        return getView().getText().toString();
    }

}
