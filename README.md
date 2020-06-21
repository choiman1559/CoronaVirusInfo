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
    implementation 'com.github.choiman1559:CoronaVirusInfo:0.2.1' //add this line
}
```

Lastly, re-sync project with gradle files.
## 2. Usage

```
public int getInt(String Country, int TYPE)
```
Returns the most recent corona virus information as an integer. (time limit is 3.6 seconds.)
_____________________________________________________________________________
```
public int getInt(String Country, int TYPE, int limitMs)
```
Returns the most recent corona virus information as an integer with custom time limit when get data from web.
_____________________________________________________________________________
```
public int getInt(String Country,int TYPE,Calender date)
```
Corona virus information on the specified date is returned as an integer. (time limit is 3.6 seconds.)
_____________________________________________________________________________
```
public int getInt(String Country,int TYPE,Calender date,int limitMs)
```
Corona virus information on the specified date is returned as an integer with custom time limit when get data from web.
_____________________________________________________________________________
```
@Deprecated
public int getInt_old(String Country, int TYPE, Calendar date)
```
_(DEPRECATED)_ Corona virus information on the specified date is returned as an integer.
_____________________________________________________________________________

#### arguments details :

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

- ***```int limitsMs```*** It measures the time to get json data from the web and returns a specific value (usually -2) when the specified time (default 3600 ms(=3.6 seconds)) is exceeded.

#### Limitations / additions
* Specified error return value
    | Value (int) | Meaning |
    | ------| ----- |
    | -1 | For some reasons, error occured when processing json data. See stacktrace to cause. |
    | -2 | While loading json data from web, it exceeded the specified time.  |
    | ETC | Request processed normally |
* If the value of the ```String Country``` argument is ```CountryCode.Global```, the ```Calender date``` argument cannot be used.
* If a non-response condition such as ANR occurs due to the use of the library, the problem is that it takes time for the library to receive the value online, there is no fundamental solution, and if the problem should be avoided, Use Multi-Thread function such as ```Runnable``` and ```AsyncTask``` to solve.

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
            str = str + "Italy, latest information of Actived : " + new CoronaVirusInfo().getInt(CountryCode.Italy, CoronaVirusInfo.ACTIVE) + "\n";
            str = str + "Russia, 2020/04/24 Total Deaths : " + new CoronaVirusInfo().getInt(CountryCode.Russian_Federation, CoronaVirusInfo.DEAD,cal) + "\n";

        TextView textView = findViewById(R.id.textview);
        textView.setText(str);
    }
}
```
#### example - result 

<img src="https://i.imgur.com/RriDkQq.jpg" alt="drawing" width="200">
