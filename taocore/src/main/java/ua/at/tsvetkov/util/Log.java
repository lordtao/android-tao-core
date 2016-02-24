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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Extended logger. Allows you to automatically adequately logged class, method and line call in the log. Makes it easy to write logs. For
 * example Log.v("Test") will in the log some the record: 04-04 08:29:40.336: V > SomeClass: someMethod: 286 Test
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
public class Log {

    private static final int MAX_TAG_LENGTH = 40;
    private static final char COLON = ':';
    private static final String PREFIX_MAIN_STRING = " ▪ ";
    private static final String STRING_MORE = "▪ ";
    private static final String GROUP = "|Group:";
    private static final String PRIORITY = "|priority:";
    private static final String ID = "|id:";
    private static final String NAME = "Name:";
    private static final String THREAD = "▪ Thread";
    private static final String HALF_LINE = "---------------------";
    private static final String ACTIVITY_COMMON_MESSAGE = HALF_LINE + " Activity lifecycle " + HALF_LINE;
    private static final String JAVA = ".java";


    private static boolean isDisabled = false;
    private static boolean isAndroidStudioStyle = true;
    private static int maxTagLength = MAX_TAG_LENGTH;
    private static String stamp = null;
    private static Application.ActivityLifecycleCallbacks activityLifecycleCallback = null;

    private Log() {
    }

    /**
     * Added auto log messages for activity lifecycle.
     *
     * @param application the application instance
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void enableActivityLifecycleAutoLogger(Application application) {
        enableActivityLifecycleAutoLogger(application, ACTIVITY_COMMON_MESSAGE);
    }

    /**
     * Enabled auto log messages for activity lifecycle.
     *
     * @param commonMessage common message called when activities lifecycle events come.
     * @param application   the application instance
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void enableActivityLifecycleAutoLogger(@NonNull Application application, final String commonMessage) {
        if (application == null) {
            Log.w("Can't enable Activity auto logger, application=null");
        }
        if (isDisabled) {
            return;
        }
        if (activityLifecycleCallback == null) {
            activityLifecycleCallback = new Application.ActivityLifecycleCallbacks() {

                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    android.util.Log.i(getActivityTag(activity), commonMessage);
                }

            };
        }

        application.registerActivityLifecycleCallbacks(activityLifecycleCallback);
    }

    /**
     * Disabled auto log messages for activity lifecycle.
     *
     * @param application the application instance
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void disableActivityLifecycleAutoLogger(@NonNull Application application) {
        if (isDisabled) {
            return;
        }
        if (application == null) {
            Log.w("Can't disable Activity auto logger, application=null");
        } else {
            application.unregisterActivityLifecycleCallbacks(activityLifecycleCallback);
        }
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
     * Set stamp for mark log. You can add a stamp which are awesome for binding the commits/build time to your logs among other things.
     *
     * @param stamp
     */
    public static void setStamp(String stamp) {
        Log.stamp = stamp;
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
        android.util.Log.v(getTag(), detailMessage);
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
        android.util.Log.d(getTag(), detailMessage);
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
        android.util.Log.i(getTag(), detailMessage);
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
        android.util.Log.w(getTag(), detailMessage);
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
        android.util.Log.e(getTag(), detailMessage);
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
        android.util.Log.wtf(getTag(), detailMessage);
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
        android.util.Log.v(getTag(), detailMessage, tr);
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
        android.util.Log.d(getTag(), detailMessage, tr);
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
        android.util.Log.i(getTag(), detailMessage, tr);
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
        android.util.Log.w(getTag(), detailMessage, tr);
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
        android.util.Log.e(getTag(), detailMessage, tr);
    }

    /**
     * Send a ERROR log message and log the throwable. RuntimeException is not handled.
     *
     * @param detailMessage The message you would like logged.
     * @param tr            An throwable to log
     */
    public static void rt(String detailMessage, Throwable tr) {
        if (tr instanceof RuntimeException) {
            throw (RuntimeException) tr;
        }
        if (isDisabled) {
            return;
        }
        android.util.Log.e(getTag(), detailMessage, tr);
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
        android.util.Log.wtf(getTag(), detailMessage, tr);
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
        android.util.Log.v(getTag(), "", tr);
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
        android.util.Log.d(getTag(), "", tr);
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
        android.util.Log.i(getTag(), "", tr);
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
        android.util.Log.w(getTag(), "", tr);
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
        android.util.Log.e(getTag(), "", tr);
    }

    /**
     * Send a ERROR log the throwable. RuntimeException is not handled.
     *
     * @param tr An throwable to log
     */
    public static void rt(Throwable tr) {
        if (tr instanceof RuntimeException) {
            throw (RuntimeException) tr;
        }
        if (isDisabled) {
            return;
        }
        android.util.Log.e(getTag(), "", tr);
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
        android.util.Log.wtf(getTag(), "", tr);
    }

    // ==========================================================

    /**
     * Send a <b>VERBOSE</b> log message. Using when you extend any Class and wont to receive full info in LogCat tag. Usually you can use
     * "this" in "obj" parameter. As result you receive tag string
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumberClass:lineNumber</b>"
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
     * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "this" in "obj" parameter. As result you receive tag string "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
    // * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * string "<b>(Called Main Class) LoggedClass:MethodInLoggedClass:lineNumber</b>"
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
     * Logged the current Thread info
     */
    public static void threadInfo() {
        threadInfo("");
    }

    /**
     * Logged the current Thread info and an throwable
     *
     * @param throwable An throwable to log
     */
    public static void threadInfo(Throwable throwable) {
        threadInfo("", throwable);
    }

    /**
     * Logged the current Thread info and a message
     */
    public static void threadInfo(String detailMessage) {
        if (isDisabled) {
            return;
        }
        StringBuilder sb = getThreadInfoString(detailMessage, Thread.currentThread());
        android.util.Log.v(THREAD + getTag(), sb.toString());
    }

    /**
     * Logged the current Thread info and a message and an throwable
     *
     * @param detailMessage The message you would like logged.
     * @param throwable     An throwable to log
     */
    public static void threadInfo(String detailMessage, Throwable throwable) {
        if (isDisabled) {
            return;
        }
        StringBuilder sb = getThreadInfoString(detailMessage, Thread.currentThread());
        android.util.Log.e(THREAD + getTag(), sb.toString(), throwable);
    }

    /**
     * Logged the current Thread info and a message and an throwable
     *
     * @param thread    for Logged info.
     * @param throwable An throwable to log
     */
    public static void threadInfo(Thread thread, Throwable throwable) {
        if (isDisabled) {
            return;
        }
        StringBuilder sb = getThreadInfoString("", thread);
        android.util.Log.e(THREAD + getTag(), sb.toString(), throwable);
    }

    /**
     * Logged stack trace at this time.
     *
     * @param msg a custom message
     */
    public static void stackTrace(String msg) {
        if (isDisabled) {
            return;
        }
        Log.i(ForLog.stackTrace(msg));
    }

    /**
     * Logged stack trace at this time.
     */
    public static void stackTrace() {
        if (isDisabled) {
            return;
        }
        Log.i(ForLog.stackTrace());
    }

    // ==========================================================

    /**
     * @param detailMessage a message
     * @param thread        thread for Logged
     * @return filled StringBuilder for next filling
     */
    static StringBuilder getThreadInfoString(String detailMessage, Thread thread) {
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

    static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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

    static String getTag() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();

        sb.append(PREFIX_MAIN_STRING);
        addStamp(sb);
        addLocation(className, traces, sb);
        addSpaces(sb);

        return sb.toString();
    }

    static String gatExtendedTag(Object obj) {
        if (obj == null) {
            Log.v("null");
        }
        Class clazz = obj.getClass();
        String className = clazz.getName();
        String classSimpleName = clazz.getSimpleName();
        String parentClassName = Log.class.getName();

        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_MAIN_STRING);
        addStamp(sb);

        int sbPrefixLength = sb.length();

        if (!clazz.isAnonymousClass()) {
            for (int i = 0; i < traces.length; i++) {
                if (traces[i].getClassName().startsWith(className)) {
                    sb.append(classSimpleName);
                    sb.append(JAVA);
                    sb.append(' ');
                    break;
                }
            }
        } else {
            sb.append("(Anonymous Class) ");
        }
        if (sb.length() > sbPrefixLength) {
            sb.append('<');
            sb.append('-');
            sb.append(' ');
        }
        addLocation(parentClassName, traces, sb);
        addSpaces(sb);

        return sb.toString();
    }

    static String getActivityTag(Activity activity) {
        String classSimpleName = activity.getClass().getSimpleName();

        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_MAIN_STRING);
        addStamp(sb);

        for (int i = 0; i < traces.length; i++) {
            if (traces[i].getClassName().startsWith("android.app.Activity")) {
                sb.append('(');
                sb.append(classSimpleName);
                sb.append(JAVA);
                sb.append(COLON);
                sb.append('0');
                sb.append(')');
                sb.append(' ');
                sb.append(traces[i].getMethodName());
                break;
            }
        }

        addSpaces(sb);

        return sb.toString();
    }

    static void addStamp(StringBuilder sb) {
        if (stamp != null && stamp.length() > 0) {
            sb.append(stamp);
            sb.append(' ');
        }
    }

    static void addLocation(String className, StackTraceElement[] traces, StringBuilder sb) {
        boolean found = false;
        for (int i = 0; i < traces.length; i++) {
            try {
                if (found) {
                    if (!traces[i].getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(traces[i].getClassName());
                        addClassLink(sb, getClassName(clazz), traces[i].getLineNumber());
                        sb.append(traces[i].getMethodName());
                        break;
                    }
                } else if (traces[i].getClassName().startsWith(className)) {
                    found = true;
                }
            } catch (ClassNotFoundException e) {
                android.util.Log.e("LOG", e.toString());
            }
        }
    }

    static void addClassLink(StringBuilder sb, String className, int lineNumber) {
        sb.append('(');
        sb.append(className);
        sb.append(JAVA);
        sb.append(COLON);
        sb.append(lineNumber);
        sb.append(')');
        sb.append(' ');
    }

    static void addSpaces(StringBuilder sb) {
        if (isAndroidStudioStyle) {
            sb.append(' ');
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
    }

    static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                if (clazz.getName().contains("$")) {
                    return clazz.getName().substring(clazz.getName().lastIndexOf(0x2e) + 1, clazz.getName().lastIndexOf(0x24));
                } else {
                    return clazz.getSimpleName();
                }
            }
            return getClassName(clazz.getEnclosingClass());
        }
        return "";
    }

}
