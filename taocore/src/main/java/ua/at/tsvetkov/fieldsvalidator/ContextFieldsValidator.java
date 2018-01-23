package ua.at.tsvetkov.fieldsvalidator;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ua.at.tsvetkov.fieldsvalidator.validators.AbstractValidator;
import ua.at.tsvetkov.fieldsvalidator.validators.CheckBoxValidator;
import ua.at.tsvetkov.fieldsvalidator.validators.DateValidator;
import ua.at.tsvetkov.fieldsvalidator.validators.EditTextValidator;
import ua.at.tsvetkov.fieldsvalidator.validators.PasswordValidator;
import ua.at.tsvetkov.util.Log;

/**
 * Created by Alexandr Tsvetkov on 28.05.2015
 */
public class ContextFieldsValidator {

   private final Context mContext;
   protected final Set<AbstractValidator> mFieldHolderList;

   public ContextFieldsValidator(Context context) {
      mFieldHolderList = new HashSet<>();
      mContext = context;
   }

   public void addEditText(EditText editText, String regex, String errMsg) {
      EditTextValidator fieldHolder = new EditTextValidator(editText, regex, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addEditText(EditText editText, String regex, int errMsgId) {
      String errMsg = mContext.getResources().getString(errMsgId);
      addEditText(editText, regex, errMsg);
   }

   public void addCheckBox(CheckBox checkBox, boolean isMustChecked, String errMsg) {
      CheckBoxValidator fieldHolder = new CheckBoxValidator(checkBox, isMustChecked, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addCheckBox(CheckBox checkBox, boolean isMustChecked, int errMsgId) {
      String errMsg = mContext.getResources().getString(errMsgId);
      addCheckBox(checkBox, isMustChecked, errMsg);
   }

   public void addDate(EditText editText, Date start, Date finish, String dateFormat, String errMsg) {
      DateValidator fieldHolder = new DateValidator(editText, start, finish, dateFormat, errMsg);
      mFieldHolderList.add(fieldHolder);
   }


   public void addDate(EditText editText, Date start, Date finish, String dateFormat, int errMsgId) {
      String errMsg = mContext.getResources().getString(errMsgId);
      addDate(editText, start, finish, dateFormat, errMsg);
   }


   public void addDate(EditText editText, int years, String dateFormat, String errMsg) {
      DateValidator fieldHolder = new DateValidator(editText, years, dateFormat, errMsg);
      mFieldHolderList.add(fieldHolder);
   }


   public void addDate(EditText editText, int years, String dateFormat, int errMsgId) {
      String errMsg = mContext.getResources().getString(errMsgId);
      addDate(editText, years, dateFormat, errMsg);
   }

   public void addPassword(EditText password, EditText passwordAgain, String regex, String errMsg) {
      PasswordValidator fieldHolder = new PasswordValidator(password, passwordAgain, regex, errMsg);
      mFieldHolderList.add(fieldHolder);
   }


   public void addPassword(EditText password, EditText passwordAgain, String regex, int errMsgId) {
      String errMsg = mContext.getResources().getString(errMsgId);
      addPassword(password, passwordAgain, regex, errMsg);
   }

   public boolean isValid() {
      boolean result = true;
      for (AbstractValidator fieldHolder : mFieldHolderList) {
         if (!fieldHolder.isValid()) {
            fieldHolder.setError();
            Log.w(fieldHolder.getFieldName() + " > Not correct data in field: " + fieldHolder.getContentString());
            result = false;
         } else {
            Log.i(fieldHolder.getFieldName() + " > Correct data in field: " + fieldHolder.getContentString());
         }
      }
      return result;
   }

   public void clear() {
      for (AbstractValidator fieldHolder : mFieldHolderList) {
         fieldHolder.clearError();
      }
   }
}

