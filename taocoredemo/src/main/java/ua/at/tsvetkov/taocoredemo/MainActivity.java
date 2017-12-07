package ua.at.tsvetkov.taocoredemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.at.tsvetkov.util.ForLog;
import ua.at.tsvetkov.util.Log;

public class MainActivity extends AppCompatActivity {

   private static final String MSG = "TEST MESSAGE LINE 1\nLINE 2";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Logging tests

      Exception ex = new Exception("Test Exception");

      Log.v(ex);
      Log.v(MSG);
      Log.v(this, MSG);
      Log.v(MSG, ex);
      Log.v(this, MSG, ex);

      Log.d(ex);
      Log.d(MSG);
      Log.d(this, MSG);
      Log.d(MSG, ex);
      Log.d(this, MSG, ex);

      Log.i(ex);
      Log.i(MSG);
      Log.i(this, MSG);
      Log.i(MSG, ex);
      Log.i(this, MSG, ex);

      Log.w(ex);
      Log.w(MSG);
      Log.w(this, MSG);
      Log.w(MSG, ex);
      Log.w(this, MSG, ex);

      Log.e(ex);
      Log.e(MSG);
      Log.e(this, MSG);
      Log.e(MSG, ex);
      Log.e(this, MSG, ex);

      Log.wtf(ex);
      Log.wtf(MSG);
      Log.wtf(this, MSG);
      Log.wtf(MSG, ex);
      Log.wtf(this, MSG, ex);

      Thread th = new Thread("Test Thread");
      th.start();

      Log.threadInfo();
      Log.threadInfo(MSG);
      Log.threadInfo(ex);
      Log.threadInfo(MSG, ex);
      Log.threadInfo(th, ex);

      Log.stackTrace();
      Log.stackTrace(MSG);

      RuntimeException rt = new RuntimeException();
      try {
         Log.rt(rt);
      } catch (RuntimeException e) {
         Log.v("Intercepted", e);
      }
      try {
         Log.rt(MSG, rt);
      } catch (RuntimeException e) {
         Log.v("Intercepted", e);
      }

      int[] ints = new int[]{472834, 4235, 657, -1728, 0};
      Log.i(ForLog.array(ints));
      double[] doubles = new double[]{472834, 4235, 657, -1728, 0};
      Log.i(ForLog.array(doubles));
      long[] longs = new long[]{472834, 4235, 657, -1728, 0};
      Log.i(ForLog.array(longs));
      float[] floats = new float[]{645.0f, 235, 57, -128, 0};
      Log.i(ForLog.array(floats));
      boolean[] bools = new boolean[]{true, false, true};
      Log.i(ForLog.array(bools));
      char[] chars = new char[]{'c','h','a','r','s'};
      Log.i(ForLog.array(chars));
      Object[] objects = new Object[]{"String object",new Object(),'a',new Object(),'s'};
      Log.i(ForLog.array(objects));

   }
}
