package ua.at.tsvetkov.fieldsvalidator.validators;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Alexandr Tsvetkov on 28.05.2015.
 */
public class EditTextValidator extends AbstractValidator{

   private Pattern  mPattern;

   public EditTextValidator(EditText editText, String regex, String errMsg) {
      super(editText, errMsg);
      mPattern = Pattern.compile(regex);
   }


   public boolean isValid() {
      return mPattern.matcher(getView().getText()).matches();
   }

   @Override
   public String getContentString() {
      return getView().getText().toString();
   }

}
