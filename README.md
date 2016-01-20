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

public void startDiscovery() {
   ...
   Log.i("WiFi is enabled.");
   ...
```

you received in log <b>Class Name:Method:Line Number</b> :
```code
04-20 19:28:05.958: I/> PrinterDiscoveryJmDNS:startDiscovery:49         WiFi is enabled.
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
