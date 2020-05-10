# CoronaVirusInfo
this Library helps to get simple corona virus infomations via java on android.

# How-to-use

## 1. edit gradle

First, add the JitPack repository (usually to the top-level build.gradle file)
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
    | Name  | Usage | Value |
    | ------| ----- | ----- |
    | ACTIVE | Returns the number of people currently infected | 4|
    | RECOVERED | Returns the number of people recovered | 5|
    | DEAD | Returns the number of people killed by the virus | 6|
    | TOTAL | Returns the total number of infected people | 7|
