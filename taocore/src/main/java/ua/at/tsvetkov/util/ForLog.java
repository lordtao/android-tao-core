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

import android.app.Application;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
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
 * Util which prepare formatted string from different objects for visual printing in {@link Log}
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class ForLog {

   private static final int MAX_TAG_LENGTH = 40;
   private static final String HEX_FORM = "%02X ";
   private static final char PREFIX = '|';
   private static final String NL = "\n";
   private static final String HALF_LINE = "---------------------";
   private static final String MAP_LINE = "-------------------------- Map ---------------------------" + NL;
   private static final String LIST_LINE = "-------------------------- List --------------------------" + NL;
   private static final String OBJECT_ARRAY_LINE = "----------------------- Objects Array ---------------------" + NL;
   private static final String LINE =              "----------------------------------------------------------" + NL;


   private static boolean isDisabled = false;
   private static boolean isAndroidStudioStyle = true;
   private static int maxTagLength = MAX_TAG_LENGTH;
   private static boolean isJumpLink = true;
   private static String stamp = null;
   private static Application.ActivityLifecycleCallbacks activityLifecycleCallback = null;

   private ForLog() {
   }

   /**
    * Return String representation of map. Each item in new line.
    *
    * @param map a Map
    * @return String representation of map
    */
   public static String map(Map<?, ?> map) {
      if (map == null) {
         return "null";
      }
      int max = 0;
      for (Map.Entry<?, ?> item : map.entrySet()) {
         int length = item.getKey().toString().length();
         if (max < length) {
            max = length;
         }
      }
      StringBuilder sb = new StringBuilder();
      String formatString = "%-" + max + "s = %s";
      sb.append(MAP_LINE);
      for (Map.Entry<?, ?> item : map.entrySet()) {
         sb.append(String.format(formatString, item.getKey(), item.getValue()));
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
      if (list == null) {
         return "null";
      }
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
    * Return String representation of Objects array. Each item in new line.
    *
    * @param array an array
    * @return String representation of array
    */
   public static <T> String array(T[] array) {
      if (array == null) {
         return "null";
      }
      List<T> list = Arrays.asList(array);
      StringBuilder sb = new StringBuilder();
      sb.append(OBJECT_ARRAY_LINE);
      for (Object item : list) {
         sb.append(item.toString());
         sb.append(NL);
      }
      sb.append(LINE);
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(int[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(float[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(boolean[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(char[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(double[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of array.
    *
    * @param array an array
    * @return String representation of array
    */
   public static String array(long[] array) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (int i = 0; i < array.length; i++) {
         sb.append(array[i]);
         if (i < array.length - 1) {
            sb.append(',');
         } else {
            sb.append(']');
         }
      }
      return sb.toString();
   }

   /**
    * Return String representation of Object. Each field in new line.
    *
    * @param myObj a class for representation
    * @return String representation of class
    */
   public static String objs(Object myObj) {
      if (myObj == null) {
         return "null";
      }
      Class<?> cl = myObj.getClass();
      int max = 0;
      String formatString = PREFIX + "%-" + max + "s = %s" + NL;
      StringBuilder sb = new StringBuilder();
      Field[] fields = cl.getDeclaredFields();

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
            Field myField = Log.getField(cl, field.getName());
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
      if (myObj == null) {
         return "null";
      }
      Class<?> cl = myObj.getClass();
      Field[] fields = cl.getDeclaredFields();
      StringBuilder sb = new StringBuilder();
      sb.append(cl.getSimpleName());
      sb.append(" [");
      for (int i = 0; i < fields.length; i++) {
         try {
            Field myField = Log.getField(cl, fields[i].getName());
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
      if (data == null) {
         Log.v("null");
      }
      StringBuilder sb = new StringBuilder(countPerLine * 3);
      int count = 0;
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
      if (data == null) {
         Log.v("null");
      }
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
   public static String xml(String xmlStr) {
      return xml(xmlStr, 2);
   }

   /**
    * Return readable representation of xml
    *
    * @param xmlStr      your xml data
    * @param indentation xml identetion
    * @return readable representation
    */
   public static String xml(String xmlStr, int indentation) {
      if (xmlStr == null) {
         Log.v("null");
      }
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

}
