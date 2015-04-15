/*******************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 *
 * Project:
 *     TAO Core
 *
 * License agreement:
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 *    caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 *    permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 *    this license agreement will remain unchanged.
 ******************************************************************************/
package ua.at.tsvetkov.util;

import android.text.TextUtils;

/**
 * Extended logger. Allows you to automatically adequately logged class, method and line call in the log. Makes it easy to write logs. For
 * example Log.v("Test") will in the log some the record: 04-04 08:29:40.336: V > SomeClass: someMethod: 286 Test
 * 
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public final class Log {

   // ==========================================================

   private static final String COLON              = ":";
   private static final String POSFIX_STRING      = ")";
   private static final String PREFIX_STRING      = "> (";
   private static final String PREFIX_MAIN_STRING = " > ";
   private static final String STRING_MORE        = "> ";
   private static final String GROUP              = "|Group:";
   private static final String PRIORITY           = "|priority:";
   private static final String ID                 = "|id:";
   private static final String NAME               = "Name:";
   private static final String THREAD             = "< Thread";

   private static boolean      isDisabled         = false;

   public static boolean isDisabled() {
      return isDisabled;
   }

   public static void setDisabled(boolean isDisabled) {
      Log.isDisabled = isDisabled;
   }

   /**
    * Send a VERBOSE log message.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void v(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.v(getLocation(), detailMessage);
   }

   /**
    * Send a DEBUG log message.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void d(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.d(getLocation(), detailMessage);
   }

   /**
    * Send a INFO log message.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void i(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.i(getLocation(), detailMessage);
   }

   /**
    * Send a WARN log message.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void w(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.w(getLocation(), detailMessage);
   }

   /**
    * Send a ERROR log message.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void e(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.e(getLocation(), detailMessage);
   }

   /**
    * What a Terrible Failure: Report a condition that should never happen. The error will always be logged at level ASSERT with the call
    * stack. Depending on system configuration, a report may be added to the DropBoxManager and/or the process may be terminated immediately
    * with an error dialog.
    * 
    * @param detailMessage The message you would like logged.
    */
   public static void wtf(String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(getLocation(), detailMessage);
   }

   // ==========================================================

   /**
    * Send a VERBOSE log message and log the throwable.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void v(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.v(getLocation(), detailMessage, tr);
   }

   /**
    * Send a DEBUG log message and log the throwable.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void d(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.d(getLocation(), detailMessage, tr);
   }

   /**
    * Send a INFO log message and log the throwable.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void i(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.i(getLocation(), detailMessage, tr);
   }

   /**
    * Send a WARN log message and log the throwable.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void w(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.w(getLocation(), detailMessage, tr);
   }

   /**
    * Send a ERROR log message and log the throwable.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void e(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.e(getLocation(), detailMessage, tr);
   }

   /**
    * What a Terrible Failure: Report an throwable that should never happen. Similar to wtf(String, Throwable), with a message as well.
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void wtf(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(getLocation(), detailMessage, tr);
   }

   // ==========================================================

   /**
    * Send a VERBOSE log the throwable.
    * 
    * @param tr An throwable to log
    */
   public static void v(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.v(getLocation(), "", tr);
   }

   /**
    * Send a DEBUG log the throwable.
    * 
    * @param tr An throwable to log
    */
   public static void d(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.d(getLocation(), "", tr);
   }

   /**
    * Send a INFO log the throwable.
    * 
    * @param tr An throwable to log
    */
   public static void i(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.i(getLocation(), "", tr);
   }

   /**
    * Send a WARN log the throwable.
    * 
    * @param tr An throwable to log
    */
   public static void w(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.w(getLocation(), "", tr);
   }

   /**
    * Send a ERROR log the throwable.
    * 
    * @param tr An throwable to log
    */
   public static void e(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.e(getLocation(), "", tr);
   }

   /**
    * What a Terrible Failure: Report an throwable that should never happen. Similar to wtf(String, Throwable), with a message as well.
    * 
    * @param tr An throwable to log
    */
   public static void wtf(Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(getLocation(), "", tr);
   }

   // ==========================================================

   /**
    * Send a <b>VERBOSE</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
    * "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumberClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    */
   public static void v(Object obj, String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.v(gatExtendedTag(obj), detailMessage);
   }

   /**
    * Send a <b>DEBUG</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
    * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    */
   public static void d(Object obj, String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.d(gatExtendedTag(obj), detailMessage);
   }

   /**
    * Send a <b>INFO</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
    * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    */
   public static void i(Object obj, String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.i(gatExtendedTag(obj), detailMessage);
   }

   /**
    * Send a <b>WARN</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
    * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    */
   public static void w(Object obj, String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.w(gatExtendedTag(obj), detailMessage);
   }

   // /**
   // * Send a <b>ERROR</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
   // "this" in "obj" parameter. As result you receive tag string
   // * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
   // *
   // * @param obj main class
   // * @param detailMessage The message you would like logged.
   // */
   // public static void e(Object obj, String detailMessage) {
   // android.util.Log.e(gatExtendedTag(obj), detailMessage);
   // }

   /**
    * Send a <b>What a Terrible Failure: Report a condition that should never happen</b> log message. Using when you extend any Class and
    * wont to receive full info in LogCat tag. Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    */
   public static void wtf(Object obj, String detailMessage) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(gatExtendedTag(obj), detailMessage);
   }

   // ==========================================================

   /**
    * Send a <b>VERBOSE</b> log message and log the throwable. Using when you extend any Class and wont to receive full info in LogCat tag.
    * Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void v(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.v(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Send a <b>DEBUG</b> log message and log the throwable. Using when you extend any Class and wont to receive full info in LogCat tag.
    * Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void d(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.d(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Send a <b>INFO</b> log message and log the throwable. Using when you extend any Class and wont to receive full info in LogCat tag.
    * Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void i(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.i(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Send a <b>WARN</b> log message and log the throwable. Using when you extend any Class and wont to receive full info in LogCat tag.
    * Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void w(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.w(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Send a <b>ERROR</b> log message and log the throwable. Using when you extend any Class and wont to receive full info in LogCat tag.
    * Usually you can use "this" in "obj" parameter. As result you receive tag string
    * "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param tr An throwable to log
    * @param detailMessage The message you would like logged.
    */
   public static void e(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.e(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Send a <b>What a Terrible Failure: Report a condition that should never happen</b> log message and log the throwable. Using when you
    * extend any Class and wont to receive full info in LogCat tag. Usually you can use "this" in "obj" parameter. As result you receive tag
    * string "<b>(Called Main Class) LogginClass:MethodInLogginClass:lineNumber</b>"
    * 
    * @param obj main class
    * @param tr An throwable to log
    * @param detailMessage The message you would like logged.
    */
   public static void wtf(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(gatExtendedTag(obj), detailMessage, tr);
   }

   /**
    * Logging the current Thread info
    */
   public static void threadInfo() {
      threadInfo("");
   }

   /**
    * Logging the current Thread info and an throwable
    * 
    * @param tr An throwable to log
    */
   public static void threadInfo(Throwable throwable) {
      threadInfo("", throwable);
   }

   /**
    * Logging the current Thread info and a message
    */
   public static void threadInfo(String detailMessage) {
      if (isDisabled) {
         return;
      }
      StringBuilder sb = getThreadInfoString(detailMessage, Thread.currentThread());
      android.util.Log.v(THREAD + getLocation(), sb.toString());
   }

   /**
    * Logging the current Thread info and a message and an throwable
    * 
    * @param detailMessage The message you would like logged.
    * @param tr An throwable to log
    */
   public static void threadInfo(String detailMessage, Throwable throwable) {
      if (isDisabled) {
         return;
      }
      StringBuilder sb = getThreadInfoString(detailMessage, Thread.currentThread());
      android.util.Log.v(THREAD + getLocation(), sb.toString(), throwable);
   }

   /**
    * Logging the current Thread info and a message and an throwable
    * 
    * @param thread for logging info.
    * @param tr An throwable to log
    */
   public static void threadInfo(Thread thread, Throwable throwable) {
      if (isDisabled) {
         return;
      }
      StringBuilder sb = getThreadInfoString("", thread);
      android.util.Log.v(THREAD + getLocation(), sb.toString(), throwable);
   }

   /**
    * @param detailMessage
    * @param thread
    * @return
    */
   private static StringBuilder getThreadInfoString(String detailMessage, Thread thread) {
      long id = thread.getId();
      String name = thread.getName();
      long priority = thread.getPriority();
      String groupName = thread.getThreadGroup().getName();
      StringBuilder sb = new StringBuilder();
      sb.append(NAME);
      sb.append(name);
      sb.append(ID);
      sb.append(id);
      sb.append(PRIORITY);
      sb.append(priority);
      sb.append(GROUP);
      sb.append(groupName);
      if (detailMessage != null && detailMessage.length() > 0) {
         sb.append(STRING_MORE);
         sb.append(detailMessage);
      }
      return sb;
   }

   private static String getLocation() {
      final String className = Log.class.getName();
      final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
      boolean found = false;
      for (StackTraceElement trace2 : traces) {
         StackTraceElement trace = trace2;

         try {
            if (found) {
               if (!trace.getClassName().startsWith(className)) {
                  Class<?> clazz = Class.forName(trace.getClassName());
                  StringBuilder sb = new StringBuilder();
                  sb.append(PREFIX_MAIN_STRING);
                  sb.append(getClassName(clazz));
                  sb.append(COLON);
                  sb.append(trace.getMethodName());
                  sb.append(COLON);
                  sb.append(trace.getLineNumber());
                  return sb.toString();
                  // return PREFIX_MAIN_STRING + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber();
               }
            } else if (trace.getClassName().startsWith(className)) {
               found = true;
               continue;
            }
         } catch (ClassNotFoundException e) {
         }
      }
      return "[]: ";
   }

   private static String gatExtendedTag(Object obj) {
      StringBuilder sb = new StringBuilder();
      sb.append(PREFIX_STRING);
      sb.append(obj.getClass().getSimpleName());
      sb.append(POSFIX_STRING);
      sb.append(getLocation());
      return sb.toString();
      // return PREFIX_STRING + obj.getClass().getSimpleName() + POSFIX_STRING + getLocation();
   }

   private static String getClassName(Class<?> clazz) {
      if (clazz != null) {
         if (!TextUtils.isEmpty(clazz.getSimpleName())) {
            return clazz.getSimpleName();
         }

         return getClassName(clazz.getEnclosingClass());
      }

      return "";
   }
}
