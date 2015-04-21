android-tao-util
================

Android App Configurator, easy Log with detailed information, Net checker.

##AppConfig:
Return the basic parameters of the application. Initialize and restoration of essential parameters for the app. Data saves and loads in to the Shared Preferences with name equals the app packages name. Automatically create the working directory for the application in the standard place. Checks is the app is new or is new version and whether it was previously installed. Print to log general info. Give information about device type (tablet or phone). Must be init in Application.

```java
AppConfig.init(this);
AppConfig.printInfo();
```

you received in log:

Log enabled.
-------------------------------------------------------------------------------
 App name:                    Leitz Icon
 First installation:          false
 Working directory:           /storage/emulated/0/Android/data/com.leitz.icon/
 Strict mode:                 false
 Diagonal:                    TABLET_10
 Application signature SHA-1: frGG2Kpy0QC+DesuQC2/B4Y4418=
-------------------------------------------------------------------------------
                                  Shared Data
-------------------------------------------------------------------------------
 APP_NAME                   = Leitz Icon
 NEW_VERSION                = false
 PRINT_SERVICE_KEY          = [fe80::21e:c0ff:fe17:dd96]
 LASTDEVICELANGUAGE         = eng
 LABEL_TEMPLATE_FROM_ASSETS = true
 selected_region            = US
 FRESH_INSTALL              = false
 APP_WORKING_DIRECTORY      = /storage/emulated/0/Android/data/com.leitz.icon/
 LABEL_TEMPLATE_FILE_NAME   = templates/Standard Address Label - Address.LeitzLbl
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

##Log:

```java

public void startDiscovery() {
   ...
   Log.i("WiFi is enabled.");
   ...
```

you received in log class:method:lineNumber :
04-20 19:28:05.958: I/> PrinterDiscoveryJmDNS:startDiscovery:49         WiFi is enabled.

##FileIO:
Static methods for file and directory/assets operations - copy, delete, move

##FileInet:
Static methods for downloading files from the Internet

##FilePath:
Static methods for dismantling the filename

##Screen:
Static methods for different screen parameters

##Converter:
Units measures converter

