android-tao-util
================

Android App Configurator, easy Log with detailed information, Net checker.

#AppConfig:
Return the basic parameters of the application. Initialize and restoration of essential parameters for the app. Data saves and loads in to the Shared Preferences with name equals the app packages name. Automatically create the working directory for the application in the standard place. Checks is the app is new or is new version and whether it was previously installed. Print to log general info. Give information about device type (tablet or phone). Must be init in Application.

```java
AppConfig.init(this);
AppConfig.printInfo();
```

you received in log:

04-20 18:57:05.817: I/<Leitz Icon>(19396): ? Log enabled.
04-20 18:57:05.817: I/(19396): ================================================================================
04-20 18:57:05.817: I/(19396): | App name:                    Leitz Icon
04-20 18:57:05.825: I/(19396): | First installation:          false
04-20 18:57:05.825: I/(19396): | Working directory:           /storage/emulated/0/Android/data/com.leitz.icon/
04-20 18:57:05.825: I/(19396): | Strict mode:                 false
04-20 18:57:05.825: I/(19396): | Diagonal:                    TABLET_10
04-20 18:57:05.840: I/(19396): | Application signature SHA-1: frGG2Kpy0QC+DesuQC2/B4Y4418=
04-20 18:57:05.840: I/(19396): ================================================================================
04-20 18:57:05.840: I/(19396): |                                  Shared Data
04-20 18:57:05.840: I/(19396): ================================================================================
04-20 18:57:05.840: I/(19396): | APP_NAME                   = Leitz Icon
04-20 18:57:05.840: I/(19396): | NEW_VERSION                = false
04-20 18:57:05.840: I/(19396): | PRINT_SERVICE_KEY          = [fe80::21e:c0ff:fe17:dd96]
04-20 18:57:05.840: I/(19396): | LASTDEVICELANGUAGE         = eng
04-20 18:57:05.840: I/(19396): | LABEL_TEMPLATE_FROM_ASSETS = true
04-20 18:57:05.840: I/(19396): | selected_region            = US
04-20 18:57:05.848: I/(19396): | FRESH_INSTALL              = false
04-20 18:57:05.848: I/(19396): | APP_WORKING_DIRECTORY      = /storage/emulated/0/Android/data/com.leitz.icon/
04-20 18:57:05.848: I/(19396): | LABEL_TEMPLATE_FILE_NAME   = templates/Standard Address Label - Address.LeitzLbl
04-20 18:57:05.848: I/(19396): | OBJECT_ADDING_MENU_INDEX   = 3
04-20 18:57:05.848: I/(19396): | NEW_INSTALL                = false
04-20 18:57:05.848: I/(19396): | APP_VERSION_NAME           = 1.0.05
04-20 18:57:05.848: I/(19396): | SAVEDLABELSSORTTYPE        = 0
04-20 18:57:05.848: I/(19396): | FAVORITESLABELSSORTTYPE    = 0
04-20 18:57:05.848: I/(19396): | usps_mailer_id             = 
04-20 18:57:05.848: I/(19396): | is_metric_selected         = false
04-20 18:57:05.848: I/(19396): | IS_LOLLIPOP_VERSION        = false
04-20 18:57:05.848: I/(19396): | NEED_OPEN_LEFT_PANEL       = false
04-20 18:57:05.848: I/(19396): | IS_LOCKED                  = false
04-20 18:57:05.848: I/(19396): | APP_VERSION_CODE           = 1005
04-20 18:57:05.848: I/(19396): ================================================================================
04-20 18:57:05.848: I/(19396): |                          Default Shared Preferences
04-20 18:57:05.848: I/(19396): ================================================================================
04-20 18:57:05.856: I/(19396): | acra.lastVersionNr         = 1005
04-20 18:57:05.856: I/(19396): | pref_usemetric             = true
04-20 18:57:05.856: I/(19396): | pref_FontSize              = 4
04-20 18:57:05.856: I/(19396): | pref_Font                  = Roboto Serif
04-20 18:57:05.856: I/(19396): | pref_region                = US
04-20 18:57:05.856: I/(19396): ================================================================================

#Log:

```java

public void startDiscovery() {
   ...
   Log.i("WiFi is enabled.");
   ...
```

you received in log class:method:lineNumber :
04-20 19:28:05.958: I/> PrinterDiscoveryJmDNS:startDiscovery:49         WiFi is enabled.

#FileIO:
Static methods for file and directory/assets operations - copy, delete, move

#FileInet:
Static methods for downloading files from the Internet

#FilePath:
Static methods for dismantling the filename

#Screen:
Static methods for different screen parameters

#Converter:
Units measures converter

