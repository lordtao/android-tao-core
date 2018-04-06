package ua.at.tsvetkov.fieldsvalidator.validators;

import android.widget.CheckBox;

/**
 * Validator for CheckBox field
 * Created by Alexandr Tsvetkov on 28.05.2015.
 */
public class CheckBoxValidator extends AbstractValidator {

    private boolean mIsMustChecked;

    public CheckBoxValidator(CheckBox checkBox, boolean isMustChecked, String errMsg) {
        super(checkBox, errMsg);
        mIsMustChecked = isMustChecked;
    }

    @Override
    public boolean isValid() {
        CheckBox checkBox = (CheckBox) getView();
        return checkBox.isChecked() == mIsMustChecked;
    }

    @Override
    public String getContentString() {
        CheckBox checkBox = (CheckBox) getView();
        return String.valueOf(checkBox.isChecked());
    }

}
