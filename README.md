android-tao-core
================

Android App Configurator, easy Log with detailed information, Screen dimensions calculator, Units measures converter (mm - cm - inch - pix - pt - twip).

Download from Bintray: [ ![Download](https://api.bintray.com/packages/lordtao/maven/android-tao-core/images/download.svg) ](https://bintray.com/lordtao/maven/android-tao-core/_latestVersion)

##AppConfig:
Easy access to the basic parameters of the application. Print to log detailed app info. Give information about device type (tablet or phone).

```java
AppConfig.init(this);
AppConfig.printInfo();
```

you received in log:

```code
Log enabled.
-------------------------------------------------------------------------------
 App name:                    My Great App
 First installation:          false
 Working directory:           /storage/emulated/0/Android/data/com.great.app/
 Strict mode:                 false
 Diagonal:                    TABLET_10
 Application signature SHA-1: frGG2Kp50QC+DesuQft/B4Y4318=
-------------------------------------------------------------------------------
                                  Shared Data
-------------------------------------------------------------------------------
 APP_NAME                   = My Great App
 NEW_VERSION                = false
 PRINT_SERVICE_KEY          = [fe00::21e:c05f:fed7:dd16]
 LASTDEVICELANGUAGE         = eng
 LABEL_TEMPLATE_FROM_ASSETS = true
 selected_region            = US
 FRESH_INSTALL              = false
 APP_WORKING_DIRECTORY      = /storage/emulated/0/Android/data/com.com.great.app/
 OBJECT_ADDING_MENU_INDEX   = 3
 NEW_INSTALL                = false
 APP_VERSION_NAME           = 1.0.05
 APP_VERSION_CODE           = 1005
-------------------------------------------------------------------------------
                             Default Shared Preferences
-------------------------------------------------------------------------------
 acra.lastVersionNr         = 1005
 pref_usemetric             = true
 pref_FontSize              = 4
 pref_Font                  = Roboto Serif
 pref_region                = US
-------------------------------------------------------------------------------
```
Store some data in Shared preferences:
```java
AppConfig.putSetting(key, isGood);
```
Restore data in Shared preferences:
```java
AppConfig.getBoolean(key, true);
```

##Easy for using and detail Log:

```java
   Log.v("Verbose");
   Log.d("Debug");
   Log.i("Info");
   Log.e("Error");
   try{
       int i = 10/0;
   } catch (Exception e) {
       Log.e("Some exception", e);
   }
   try{
       int i = 10/0;
   } catch (Exception e) {
       Log.rt("RuntimeException is not handled by Log.rt()", e);
   }
```

You'll get in LogCat the lines like below. 
Clicking on the tag brings you to log into the source code of the class which was caused by the logger:

[[https://github.com/lordtao/android-tao-core/log_example.png|alt=octocat]]

```code
V/ ▪ (AcMain.java:48) onCreate            ⇛: Verbose
D/ ▪ (AcMain.java:49) onCreate            ⇛: Debug
I/ ▪ (AcMain.java:50) onCreate            ⇛: Info
E/ ▪ (AcMain.java:51) onCreate            ⇛: Error
E/ ▪ (AcMain.java:55) onCreate            ⇛: Some exception
                                              java.lang.ArithmeticException: divide by zero
                                                  at test.tsvetkov.at.ua.test.AcMain.onCreate(AcMain.java:53)
                                                  at android.app.Activity.performCreate(Activity.java:6052)
                                                  at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1106)
                                                  at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2308)
                                                  at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2418)
                                                  at android.app.ActivityThread.access$800(ActivityThread.java:167)
                                                  at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1326)
                                                  at android.os.Handler.dispatchMessage(Handler.java:102)
                                                  at android.os.Looper.loop(Looper.java:135)
                                                  at android.app.ActivityThread.main(ActivityThread.java:5309)
                                                  at java.lang.reflect.Method.invoke(Native Method)
                                                  at java.lang.reflect.Method.invoke(Method.java:372)
                                                  at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:908)
                                                  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:703)
E/AndroidRuntime: FATAL EXCEPTION: main
                                               Process: test.tsvetkov.at.ua.test, PID: 20538
                                               java.lang.RuntimeException: Unable to start activity ComponentInfo{test.tsvetkov.at.ua.test/test.tsvetkov.at.ua.test.AcMain}: java.lang.ArithmeticException: divide by zero
                                                   at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2355)
                                                   at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2418)
                                                   at android.app.ActivityThread.access$800(ActivityThread.java:167)
                                                   at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1326)
                                                   at android.os.Handler.dispatchMessage(Handler.java:102)
                                                   at android.os.Looper.loop(Looper.java:135)
                                                   at android.app.ActivityThread.main(ActivityThread.java:5309)
                                                   at java.lang.reflect.Method.invoke(Native Method)
                                                   at java.lang.reflect.Method.invoke(Method.java:372)
                                                   at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:908)
                                                   at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:703)
                                                Caused by: java.lang.ArithmeticException: divide by zero
                                                       at test.tsvetkov.at.ua.test.AcMain.onCreate(AcMain.java:58)
```

##Screen:
Static methods for different screen parameters

##Converter:
Units measures converter

Add android-tao-core to your project
----------------------------
greenDAO is available on Bintray. Please ensure that you are using the latest versions by [ ![Download](https://api.bintray.com/packages/lordtao/maven/android-tao-core/images/download.svg) ](https://bintray.com/lordtao/maven/android-tao-core/_latestVersion)

Gradle dependency for your Android app:

add to general build.gradle
```
buildscript {
    repositories {
        jcenter()
        maven {
            url  "http://dl.bintray.com/lordtao/maven"
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.0.0-alpha5'
    }
}

allprojects {
    repositories {
        jcenter()
        maven {
            url  "http://dl.bintray.com/lordtao/maven"
        }
    }
}
```
add to your module build.gradle
```
    compile 'ua.at.tsvetkov:taocore:1.2.1'
```
