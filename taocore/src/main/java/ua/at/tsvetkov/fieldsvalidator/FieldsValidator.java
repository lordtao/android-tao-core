package ua.at.tsvetkov.fieldsvalidator;

import android.app.Activity;
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
public class FieldsValidator {

   private final Activity               mActivity;
   protected     Set<AbstractValidator> mFieldHolderList;

   public FieldsValidator(Activity activity) {
      mFieldHolderList = new HashSet<>();
      mActivity = activity;
   }

   public void addEditText(EditText editText, String regex, String errMsg) {
      EditTextValidator fieldHolder = new EditTextValidator(editText, regex, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addEditText(int viewId, String regex, int errMsgId) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      String   errMsg   = mActivity.getResources().getString(errMsgId);
      addEditText(editText, regex, errMsg);
   }

   public void addEditText(EditText editText, String regex, int errMsgId) {
      String errMsg = mActivity.getResources().getString(errMsgId);
      addEditText(editText, regex, errMsg);
   }

   public void addEditText(int viewId, String regex, String errMsg) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      addEditText(editText, regex, errMsg);
   }

   public void addCheckBox(CheckBox checkBox, boolean isMustChecked, String errMsg) {
      CheckBoxValidator fieldHolder = new CheckBoxValidator(checkBox, isMustChecked, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addCheckBox(int viewId, boolean isMustChecked, int errMsgId) {
      CheckBox checkBox = (CheckBox) mActivity.findViewById(viewId);
      String   errMsg   = mActivity.getResources().getString(errMsgId);
      addCheckBox(checkBox, isMustChecked, errMsg);
   }
   public void addCheckBox(CheckBox checkBox, boolean isMustChecked, int errMsgId) {
      String errMsg = mActivity.getResources().getString(errMsgId);
      addCheckBox(checkBox, isMustChecked, errMsg);
   }

   public void addCheckBox(int viewId, boolean isMustChecked, String errMsg) {
      CheckBox checkBox = (CheckBox) mActivity.findViewById(viewId);
      addCheckBox(checkBox, isMustChecked, errMsg);
   }

   public void addDate(EditText editText, Date start, Date finish, String errMsg) {
      DateValidator fieldHolder = new DateValidator(editText, start, finish, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addDate(int viewId, Date start, Date finish, int errMsgId) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      String   errMsg   = mActivity.getResources().getString(errMsgId);
      addDate(editText, start, finish, errMsg);
   }

   public void addDate(EditText editText, Date start, Date finish, int errMsgId) {
      String errMsg = mActivity.getResources().getString(errMsgId);
      addDate(editText, start, finish, errMsg);
   }

   public void addDate(int viewId, Date start, Date finish, String errMsg) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      addDate(editText, start, finish, errMsg);
   }

   public void addDate(EditText editText, int years, String errMsg) {
      DateValidator fieldHolder = new DateValidator(editText, years, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addDate(int viewId, int years, int errMsgId) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      String   errMsg   = mActivity.getResources().getString(errMsgId);
      addDate(editText, years, errMsg);
   }

   public void addDate(EditText editText, int years, int errMsgId) {
      String errMsg = mActivity.getResources().getString(errMsgId);
      addDate(editText, years, errMsg);
   }

   public void addDate(int viewId, int years, String errMsg) {
      EditText editText = (EditText) mActivity.findViewById(viewId);
      addDate(editText, years, errMsg);
   }

   public void addPassword(EditText password, EditText passwordAgain, String regex, String errMsg) {
      PasswordValidator fieldHolder = new PasswordValidator(password, passwordAgain, regex, errMsg);
      mFieldHolderList.add(fieldHolder);
   }

   public void addPassword(int passwordViewId, int passwordAgainViewId, String regex, int errMsgId) {
      EditText password = (EditText) mActivity.findViewById(passwordViewId);
      EditText passwordAgain = (EditText) mActivity.findViewById(passwordAgainViewId);
      String   errMsg   = mActivity.getResources().getString(errMsgId);
      addPassword(password, passwordAgain, regex, errMsg);
   }

   public void addPassword(EditText password, EditText passwordAgain, String regex, int errMsgId) {
      String errMsg = mActivity.getResources().getString(errMsgId);
      addPassword(password, passwordAgain, regex, errMsg);
   }

   public void addPassword(int passwordViewId, int passwordAgainViewId, String regex, String errMsg) {
      EditText password = (EditText) mActivity.findViewById(passwordViewId);
      EditText passwordAgain = (EditText) mActivity.findViewById(passwordAgainViewId);
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

