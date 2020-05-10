# Corona Virus(covid) Infomation library

[![License](https://img.shields.io/badge/License-Apache%202.0-yellowgreen.svg)](https://opensource.org/licenses/Apache-2.0)
[![Release](https://img.shields.io/github/release/choiman1559/CoronaVirusInfo.svg?label=jitpack)](https://jitpack.io/#choiman1559/CoronaVirusInfo)
[![Language](https://img.shields.io/badge/Language-Java-green?logo=java)](https://www.java.com)

this Library helps to get simple corona virus infomations via java on android.

This library is using [covid19api](https://covid19api.com)'s database.

# How-to-use

## 1. edit gradle and manifist

First, add ```INTERNET``` permission in manifist if you haven't added it.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example">

    <uses-permission android:name="android.permission.INTERNET" />  <!-- add this line -->

    <application
           ...
           
    </application>
</manifest>
```

Secondly, add the JitPack repository (usually to the top-level build.gradle file)
```
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ...
}

allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" } //add this line
    }
}

```

Next, add the repository of the library to be added as follows.
```
android {
    ...
}

dependencies {
    implementation 'com.github.choiman1559:CoronaVirusInfo:0.1.4' //add this line
}
```

Lastly, re-sync project with gradle files.
## 2. Usage
```
public int getInt(String Country, int TYPE)
```
get corona virus infomations by integer.

#### arguments datails :

- ```String Country``` : The value of this parameter uses [alpha-2 code](https://www.iban.com/country-codes) according to ISO 3166, and the country name must be one of those declared in [CountryCode.java](https://github.com/choiman1559/CoronaVirusInfo/blob/master/app/src/main/java/corona/virus/info/CountryCode.java).

- ``` int TYPE``` : The argument should be one of those listed in the table below.
(class : [CoronaVirusInfo](https://github.com/choiman1559/CoronaVirusInfo/blob/master/app/src/main/java/corona/virus/info/CoronaVirusInfo.java) )
    | Name  | Usage | Value |
    | ------| ----- | ----- |
    | ACTIVE | Returns the number of people currently infected | 4|
    | RECOVERED | Returns the number of people recovered | 5|
    | DEAD | Returns the number of people killed by the virus | 6|
    | TOTAL | Returns the total number of infected people | 7|

### example
``` 
 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = "Corona Virus status in South Korea : \n";
            str = str + String.format(Locale.getDefault(), "Total : %d", new CoronaVirusInfo().getInt(CountryCode.South_Korea, CoronaVirusInfo.TOTAL)) + "\n";
            str = str + String.format(Locale.getDefault(), "Deaths : %d", new CoronaVirusInfo().getInt(CountryCode.South_Korea, CoronaVirusInfo.DEAD)) + "\n";
            str = str + String.format(Locale.getDefault(), "Recovered : %d", new CoronaVirusInfo().getInt(CountryCode.South_Korea, CoronaVirusInfo.RECOVERED)) + "\n";
            str = str + String.format(Locale.getDefault(), "Active : %d", new CoronaVirusInfo().getInt(CountryCode.South_Korea, CoronaVirusInfo.ACTIVE)) + "\n";

        TextView textView = findViewById(R.id.textview);
        textView.setText(str);
    }
}
```
#### example - result 

<img src="https://i.imgur.com/ISprg4A.jpg" alt="drawing" width="200">
(South Korea, data as of May 09, 2020)
