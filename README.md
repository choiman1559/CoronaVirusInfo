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
    implementation 'com.github.choiman1559:CoronaVirusInfo:0.1.5' //add this line
}
```

Lastly, re-sync project with gradle files.
## 2. Usage
```
public int getInt(String Country, int TYPE)
```
Returns the most recent corona virus information as an integer.

```
public int getInt(String Country,int TYPE,Calender date)
```
Corona virus information on the specified date is returned as an integer.

#### arguments datails :

- ***```String Country```*** : The value of this parameter uses [alpha-2 code](https://www.iban.com/country-codes) according to ISO 3166, and the country name must be one of those declared in [CountryCode.java](https://github.com/choiman1559/CoronaVirusInfo/blob/master/app/src/main/java/corona/virus/info/CountryCode.java).

- ***``` int TYPE```*** : The argument should be one of those listed in the table below.
(class : [CoronaVirusInfo](https://github.com/choiman1559/CoronaVirusInfo/blob/master/app/src/main/java/corona/virus/info/CoronaVirusInfo.java) )
    | Name  | Usage | Value |
    | ------| ----- | ----- |
    | ACTIVE | Returns the number of people currently infected | 4|
    | RECOVERED | Returns the number of people recovered | 5|
    | DEAD | Returns the number of people killed by the virus | 6|
    | TOTAL | Returns the total number of infected people | 7|
    
- ***```Calender date```*** Specifies the date of the information to be returned. The format of the date is ```yyyy-MM-dd```, and If the argument is null, ```public int getInt(String Country,int TYPE,Calender date)``` will be behave the same as ```public int getInt(String Country, int TYPE)```.

### example
``` 
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2020);
        cal.set(Calendar.MONTH,3);
        cal.set(Calendar.DATE,24);

        String str = "Corona Virus info example : \n";
            str = str + "South Korea, 2020/04/24 Total confirmed : " + new CoronaVirusInfo().getInt(CountryCode.South_Korea, CoronaVirusInfo.TOTAL, cal) + "\n";
            str = str + "Global, latest infomation of Recovered : " + new CoronaVirusInfo().getInt(CountryCode.Global, CoronaVirusInfo.DEAD) + "\n";
            str = str + "USA, latest information of Actived : " + new CoronaVirusInfo().getInt(CountryCode.Italy, CoronaVirusInfo.ACTIVE) + "\n";
            str = str + "Russia, 2020/04/24 Total Deaths : " + new CoronaVirusInfo().getInt(CountryCode.Russian_Federation, CoronaVirusInfo.DEAD,cal) + "\n";

        TextView textView = findViewById(R.id.textview);
        textView.setText(str);
    }
}
```
#### example - result 

<img src="https://i.imgur.com/jQ77yZK.jpg" alt="drawing" width="200">
