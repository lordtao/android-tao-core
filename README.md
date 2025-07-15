android-tao-core
================

App utility: App Info (Fingerprint, SignatureKeyHash, etc.), Screen dimensions, Units converter (mm/cm/inch/pix/pt/twip), MD5, File utils.

Download from Bintray: [ ![Download](https://api.bintray.com/packages/lordtao/maven/android-tao-core/images/download.svg) ](https://bintray.com/lordtao/maven/android-tao-core/_latestVersion)

Application utils
-----------------

### AppConfig
Easy access to the basic information about your device and the application. Shows in the log the detailed information.

```
AppConfig.init(this);
AppConfig.printInfo();
```

![Image of App info example](log_app_info.png)

Easy work with default Shared preferences:
```
AppConfig.putSetting(key, isGood);
...
AppConfig.getBoolean(key, true);
```

### Apps
Returns information about applications installed on the device (Fingerprint, SignatureKeyHash, etc.)

### Md5:
Calculate Md5 from InputStream, files. Generate a hash String for different data sources.

UI utils
--------

### Screen
Static methods for different screen parameters

### Converter
Units measures converter

Files utils
-----------

### FileDownloader
Simple file downloader

### FileOperations
Operations with files, assets and dirs - copy/rename/delete/create. Clear the app data.

### FileNameUtil
Operations with file name - extracting, adding, modifying path parts and file extensions.

Add android-tao-core to your project
----------------------------
Android tao core lib is available on Bintray. Please ensure that you are using the latest versions by [ ![Download](https://api.bintray.com/packages/lordtao/maven/android-tao-core/images/download.svg) ](https://bintray.com/lordtao/maven/android-tao-core/_latestVersion)

Gradle dependency for your Android app:

add to general build.gradle
```
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
    compile 'ua.at.tsvetkov:taocore:1.5.0'
```

Changelog
---------

#### 1.6.119 -- Refactoring
* fixing
* added extensions and bindings classes 

#### 1.5.0 -- Refactoring
* Gradle version update
* Kotlin version update
* fixing

#### 1.4.5 -- Fixed Kotlin access to some methods
* fixing

#### 1.4.1 -- Added Demo
* refactoring.
* added Demo

#### 1.4.0 -- refactoring
* refactoring.
* fields validator moved in the separate project.
* convert to Kotlin.

#### 1.3.5 -- removed Log module
* removed Log module. [See separate project - android-tao-log](https://github.com/lordtao/android-tao-log)
