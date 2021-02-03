<p align="center">
  <img src="https://github.com/gzeinnumer/MyLibUtils/blob/master/preview/bg.jpg"/>
</p>

<h1 align="center">
    MyLibUtils
</h1>

<p align="center">
    <a><img src="https://img.shields.io/badge/Version-0.4.0-brightgreen.svg?style=flat"></a>
    <a><img src="https://img.shields.io/badge/ID-gzeinnumer-blue.svg?style=flat"></a>
    <a><img src="https://img.shields.io/badge/Java-Suport-green?logo=java&style=flat"></a>
    <a><img src="https://img.shields.io/badge/kotlin-Suport-green?logo=kotlin&style=flat"></a>
    <a href="https://github.com/gzeinnumer"><img src="https://img.shields.io/github/followers/gzeinnumer?label=follow&style=social"></a>
    <br>
    <p>Simple.</p>
</p>

---
# Content List
* [Download](#download)
* [Feature List](#feature-list)
* [Tech stack and 3rd library](#tech-stack-and-3rd-library)
* [Usage](#usage)
* [Example Code/App](#example-codeapp)
* [Version](#version)
* [Contribution](#contribution)

---
# Download
Add maven `jitpack.io` and `dependencies` in `build.gradle (Project)` :
```gradle
// build.gradle project
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

// build.gradle app/module
dependencies {
  ...
  implementation 'com.github.gzeinnumer:MyLibUtils:version'
}
```

---
# Feature List
- [x] []()

---
# Tech stack and 3rd library
- []()

---
# Usage

#
### **File To Base64.**
File Image From Path and convert to `Base64` with format `data:image/jpeg;base64,` + `....kagsfkajha`
> **Java**
```java
String filePath = "/storage/emulated/0/YourFolder/file_image.jpg";
val result_9 = MBBase64.convertToBase64FromPath(filePath);
Log.d(TAG, "onCreate_9: "+ result_8); //   data:image/jpeg;base64,kasgfkaghaksfakgshalgal
```
> **Kotlin**
```kotlin
```

---

## Example Code/App

[]()

[]()

---

# Version
- **0.3.0**
  - First Release

---

# Contribution
You can sent your constibution to `branch` `open-pull`.

---

```
Copyright 2021 M. Fadli Zein
```
