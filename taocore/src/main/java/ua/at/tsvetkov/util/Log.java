/**
 * ****************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p/>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p/>
 * Project:
 * TAO Core
 * <p/>
 * License agreement:
 * <p/>
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.util;

import android.text.TextUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Extended logger. Allows you to automatically adequately logged class, method and line call in the log. Makes it easy to write logs. For
 * example Log.v("Test") will in the log some the record: 04-04 08:29:40.336: V > SomeClass: someMethod: 286 Test
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class Log {

   // ==========================================================

   private static final String COLON              = ":";
   private static final String POSTFIX_STRING     = ")";
   private static final String PREFIX_STRING      = "▪ (";
   private static final String PREFIX_MAIN_STRING = " ▪ ";
   private static final String STRING_MORE        = "▪ ";
   private static final String GROUP              = "|Group:";
   private static final String PRIORITY           = "|priority:";
   private static final String ID                 = "|id:";
   private static final String NAME               = "Name:";
   private static final String THREAD             = "▪ Thread";
   private static final int    MAX_TAG_LENGTH     = 40;
   private static final String HEX_FORM           = "%02X ";
   private static final char   PREFIX             = '|';
   private static final String NL                 = "\n";
   private static final String HALF_LINE          = "---------------------";
   private static final String MAP_LINE           = "-------------------------- Map ---------------------------" + NL;
   private static final String LIST_LINE          = "-------------------------- List --------------------------" + NL;
   private static final String LINE               = "----------------------------------------------------------" + NL;


   private static boolean isDisabled           = false;
   private static boolean isAndroidStudioStyle = true;
   private static int     maxTagLength         = MAX_TAG_LENGTH;

   private Log() {
   }

   /**
    * Is logs disabled or enabled
    *
    * @return is disabled
    */
   public static boolean isDisabled() {
      return isDisabled;
   }

   /**
    * Set logs disabled or enabled
    *
    * @param isDisabled is disabled
    */
   public static void setDisabled(boolean isDisabled) {
      Log.isDisabled = isDisabled;
   }

   /**
    * Set Android Studio log style (default)
    */
   public static void setAndroidStudioStyle() {
      Log.isAndroidStudioStyle = true;
   }

   /**
    * Set Eclipse log style
    */
   public static void setEclipseStyle() {
      Log.isAndroidStudioStyle = false;
   }

   /**
    * Is Eclipse log style
    */
   public static boolean istEclipseStyle() {
      return !Log.isAndroidStudioStyle;
   }

   /**
    * Is Android Studio log style
    */
   public static boolean isAndroidStudioStyle() {
      return Log.isAndroidStudioStyle;
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
    * @param tr            An throwable to log
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
    * @param tr            An throwable to log
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
    * @param tr            An throwable to log
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
    * @param tr            An throwable to log
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
    * @param tr            An throwable to log
    */
   public static void e(String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.e(getLocation(), detailMessage, tr);
   }

   /**
    * Send a ERROR log message and log the throwable. RuntimeException is not handled.
    *
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
    */
   public static void rt(String detailMessage, Throwable tr) {
      if(tr instanceof RuntimeException) {
         throw (RuntimeException) tr;
      }
      if (isDisabled) {
         return;
      }
      android.util.Log.e(getLocation(), detailMessage, tr);
   }

   /**
    * What a Terrible Failure: Report an throwable that should never happen. Similar to wtf(String, Throwable), with a message as well.
    *
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
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
    * Send a ERROR log the throwable. RuntimeException is not handled.
    *
    * @param tr An throwable to log
    */
   public static void rt(Throwable tr) {
      if(tr instanceof RuntimeException) {
         throw (RuntimeException) tr;
      }
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
    * @param obj           main class
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
    * @param obj           main class
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
    * @param obj           main class
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
    * @param obj           main class
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
    * @param obj           main class
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
    * @param obj           main class
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
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
    * @param obj           main class
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
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
    * @param obj           main class
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
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
    * @param obj           main class
    * @param detailMessage The message you would like logged.
    * @param tr            An throwable to log
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
    * @param obj           main class
    * @param tr            An throwable to log
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
    * @param obj           main class
    * @param tr            An throwable to log
    * @param detailMessage The message you would like logged.
    */
   public static void wtf(Object obj, String detailMessage, Throwable tr) {
      if (isDisabled) {
         return;
      }
      android.util.Log.wtf(gatExtendedTag(obj), detailMessage, tr);
   }

   // ==========================================================

   /**
    * Logging the current Thread info
    */
   public static void threadInfo() {
      threadInfo("");
   }

   /**
    * Logging the current Thread info and an throwable
    *
    * @param throwable An throwable to log
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
    * @param throwable     An throwable to log
    */
   public static void threadInfo(String detailMessage, Throwable throwable) {
      if (isDisabled) {
         return;
      }
      StringBuilder sb = getThreadInfoString(detailMessage, Thread.currentThread());
      android.util.Log.e(THREAD + getLocation(), sb.toString(), throwable);
   }

   /**
    * Logging the current Thread info and a message and an throwable
    *
    * @param thread    for logging info.
    * @param throwable An throwable to log
    */
   public static void threadInfo(Thread thread, Throwable throwable) {
      if (isDisabled) {
         return;
      }
      StringBuilder sb = getThreadInfoString("", thread);
      android.util.Log.e(THREAD + getLocation(), sb.toString(), throwable);
   }

   // ==========================================================

   /**
    * Return String representation of map. Each item in new line.
    *
    * @param map a Map
    * @return String representation of map
    */
   public static String map(Map<?, ?> map) {
      int max = 0;
      for (Map.Entry<?, ?> item : map.entrySet()) {
         int length = item.getKey().toString().length();
         if (max < length) {
            max = length;
         }
      }
      StringBuilder sb          = new StringBuilder();
      String        fomatString = "%-" + max + "s = %s";
      sb.append(MAP_LINE);
      for (Map.Entry<?, ?> item : map.entrySet()) {
         sb.append(String.format(fomatString, item.getKey(), item.getValue()));
         sb.append(NL);
      }
      sb.append(LINE);
      return sb.toString();
   }

   /**
    * Return String representation of list. Each item in new line.
    *
    * @param list a List
    * @return String representation of map
    */
   public static String list(List<?> list) {
      StringBuilder sb = new StringBuilder();
      sb.append(LIST_LINE);
      for (Object item : list) {
         sb.append(item.toString());
         sb.append(NL);
      }
      sb.append(LINE);
      return sb.toString();
   }

   /**
    * Return String representation of Object. Each field in new line.
    *
    * @param myObj a class for representation
    * @return String representation of class
    */
   public static String objs(Object myObj) {
      Class<?>      cl           = myObj.getClass();
      int           max          = 0;
      String        formatString = PREFIX + "%-" + max + "s = %s" + NL;
      StringBuilder sb           = new StringBuilder();
      Field[]       fields       = cl.getDeclaredFields();

      for (Field field : fields) {
         int length = field.getName().length();
         if (max < length) {
            max = length;
         }
      }
      sb.append(HALF_LINE);
      sb.append(cl.getSimpleName());
      sb.append(HALF_LINE);
      sb.append(NL);
      for (Field field : fields) {
         try {
            Field myField = getField(cl, field.getName());
            myField.setAccessible(true);
            sb.append(String.format(formatString, field.getName(), myField.get(myObj)));
         } catch (Exception e) {
            sb.append(PREFIX);
            sb.append("Can't access to field ");
            sb.append(field.getName());
         }
      }
      sb.append(LINE);
      return sb.toString();
   }

   /**
    * Return String representation of class. Each field in new line.
    *
    * @param myObj a class for representation
    * @return String representation of class
    */
   public static String obj(Object myObj) {
      Class<?>      cl     = myObj.getClass();
      Field[]       fields = cl.getDeclaredFields();
      StringBuilder sb     = new StringBuilder();
      sb.append(cl.getSimpleName());
      sb.append(" [");
      for (int i = 0; i < fields.length; i++) {
         try {
            Field myField = getField(cl, fields[i].getName());
            myField.setAccessible(true);
            sb.append(fields[i].getName());
            sb.append("=");
            sb.append(myField.get(myObj));
            if (fields.length != 1 && i < (fields.length - 1)) {
               sb.append(", ");
            }
         } catch (Exception e) {
            sb.append(PREFIX);
            sb.append("Can't access to the field ");
            sb.append(fields[i].getName());
         }
      }
      sb.append("]");
      return sb.toString();
   }

   /**
    * Print in log readable representation of bytes array data like 0F CD AD.... Each countPerLine bytes will print in new line
    *
    * @param data         your bytes array data
    * @param countPerLine count byte per line
    */
   public static void hex(byte[] data, int countPerLine) {
      StringBuilder sb    = new StringBuilder(countPerLine * 3);
      int           count = 0;
      for (byte element : data) {
         count++;
         sb.append(String.format(HEX_FORM, element));
         if (count >= countPerLine) {
            count = 0;
            sb.trimToSize();
            Log.v(sb.toString());
            sb = new StringBuilder(countPerLine * 3);
         }
      }
   }

   /**
    * Return readable representation of bytes array data like 0F CD AD....
    *
    * @param data your bytes array data
    * @return readable representation
    */
   public static String hex(byte[] data) {
      StringBuilder sb = new StringBuilder(data.length * 3);
      for (byte element : data) {
         sb.append(String.format(HEX_FORM, element));
      }
      sb.trimToSize();
      return sb.toString();
   }

   /**
    * Return readable representation of xml with indentation 2
    *
    * @param xmlStr your xml data
    * @return readable representation
    */
   public String xml(String xmlStr) {
      return xml(xmlStr, 2);
   }

   /**
    * Return readable representation of xml
    *
    * @param xmlStr your xml data
    * @param indentation
    * @return readable representation
    */
   public String xml(String xmlStr, int  indentation) {
      try {
         Source xmlInput = new StreamSource(new StringReader(xmlStr));
         StreamResult xmlOutput = new StreamResult(new StringWriter());
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentation));
         transformer.transform(xmlInput, xmlOutput);
         return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
      } catch (TransformerException e) {
         return xmlStr;
      }
   }
   // ==========================================================

   /**
    * @param detailMessage a message
    * @param thread        thread for logging
    * @return filled StringBuilder for next filling
    */
   private static StringBuilder getThreadInfoString(String detailMessage, Thread thread) {
      long          id        = thread.getId();
      String        name      = thread.getName();
      long          priority  = thread.getPriority();
      String        groupName = thread.getThreadGroup().getName();
      StringBuilder sb        = new StringBuilder();
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

   private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
      try {
         return clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
         Class<?> superClass = clazz.getSuperclass();
         if (superClass == null) {
            throw e;
         } else {
            return getField(superClass, fieldName);
         }
      }
   }

   private static String getLocation() {
      final String              className = Log.class.getName();
      final StackTraceElement[] traces    = Thread.currentThread().getStackTrace();
      boolean                   found     = false;
      for (StackTraceElement trace : traces) {

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
                  if (isAndroidStudioStyle) {
                     int extraSpaceCount = maxTagLength - sb.length();
                     if (extraSpaceCount < 0) {
                        maxTagLength = sb.length();
                        extraSpaceCount = 0;
                     }
                     for (int i = 0; i < extraSpaceCount; i++) {
                        sb.append(' ');
                     }
                     sb.append('\u21DB');
                  }
                  return sb.toString();
               }
            } else if (trace.getClassName().startsWith(className)) {
               found = true;
            }
         } catch (ClassNotFoundException e) {
            android.util.Log.e("LOG", e.toString());
         }
      }
      return "[]: ";
   }

   private static String gatExtendedTag(Object obj) {
      StringBuilder sb = new StringBuilder();
      sb.append(PREFIX_STRING);
      sb.append(obj.getClass().getSimpleName());
      sb.append(POSTFIX_STRING);
      sb.append(getLocation());
      return sb.toString();
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
