package ua.at.tsvetkov.fieldsvalidator.validators;

import android.widget.TextView;

/**
 * Abstract class for field validation
 * Created by Alexandr Tsvetkov on 28.05.2015.
 */
public abstract class AbstractValidator {

   protected TextView mView;
   protected String   mErrMsg;

   public AbstractValidator(TextView view, String errMsg) {
      if (view == null || errMsg == null) {
         throw new IllegalArgumentException("Validators field can' be null");
      }
      mView = view;
      mErrMsg = errMsg;
   }

   public String getFieldName() {
      return mView.getResources().getResourceEntryName(mView.getId());
   }

   /**
    * Set error messages
    */
   public void setError() {
      mView.requestFocus();
      mView.setError(mErrMsg);
   }

   /**
    * Clear error messag
    */
   public void clearError() {
      mView.setError(null);
   }

   /**
    * Check for correct data in field
    *
    * @return is correct
    */
   public abstract boolean isValid();

   /**
    * Return string representation of field
    * @return
    */
   public abstract String getContentString();

   protected TextView getView() {return mView;}
}
