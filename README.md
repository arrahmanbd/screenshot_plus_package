## screenshot_plus
<p align="center">  
  <a href="https://github.com/arrahmanbd/screenshot_plus/releases" <img height="20" alt="GitHub All Releases" src="https://img.shields.io/github/downloads/arrahmanbd/screenshot_plus/total.svg?style=for-the-badge">
  </a>
  <a href=""><img src="https://api.codacy.com/project/badge/Grade/ccdaffb33883461b8570cd80f5051631"/>
  </a>
  <a href=""> <img height="20" alt="Pub" src="https://img.shields.io/pub/v/liquid_swipe.svg?style=for-the-badge">
  </a>
  <a href="https://github.com/arrahmanbd/screenshot_plus/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-APACHE2.0-blue.svg?longCache=true&style=flat-square">
  </a>
  <a href="https://flutter.dev"><img src="https://img.shields.io/badge/Built%20for-Flutter-blue.svg?longCache=true&style=flat-square" ">
  </a>
  <a href="https://github.com/arrahmanbd">
   <img alt="Android 12" src="https://img.shields.io/badge/Android-12-green.svg?longCache=true&style=flat-square" />
  </a>
  <a href="">
  <img src="https://codecov.io/gh/arrahmanbd/screenshot_plus/branch/master/graph/badge.svg?token=lGlgjaHbqJ"/>
  </a>
</p>
Screenshot Plus is a Dart package for taking screenshots on Android and iOS. This package provides a simple API to capture the current screen and save it as an image file in the device's storage. 
The captured image can then be used for various purposes such as sharing, printing, or analyzing app behavior. The package is easy to use and integrates seamlessly with existing Flutter projects. This plugin saves the image and returns the path. Also working on almost all version of **Android** or **iOS**.
</br>
<p align="center">
<img src="https://raw.githubusercontent.com/arrahmanbd/screenshot_plus_package/master/Preview/preview.png" width="310" height="640">
<img src="https://raw.githubusercontent.com/arrahmanbd/screenshot_plus_package/master/Preview/preview.png" width="310" height="640">
</p>

## Instalation

Run this command in terminal:

```
flutter pub add screenshot_plus
```
Or add this on `pubspec.yaml` file.

```
screenshot_plus:
```



### Android
You must grant Write permission on Storage

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

to your `AndroidManifest.xml` inside `android/src/main/` directory.

Also you need to add a property to `application` tag to fix an issue with permissions writing to `EXTERNAL_STORAGE`:

```
android:requestLegacyExternalStorage="true"
```

### iOS
If don't add

```
<key>NSPhotoLibraryAddUsageDescription</key>
<string>Take pretty screenshots and save it to the PhotoLibrary.</string>
```

to your `info.plist` file inside `ios/Runner` directory, the application will crash.

## Use

Import the library:

```
import 'package:screenshot_plus/screenshot_plus.dart';
```

and take a screenshot:

```
//Get the absolute path
String path = await ScreenshotPlus.takeShot();
debugPrint('Screenshot taken: $path');
```

In error case the function returns `empty path` to Show the captured image use this widget

```
//imageFilepath is the absolute File(path)
Image.file(imageFilepath)
```


## 🔗 Follow
[![portfolio](https://img.shields.io/badge/my_portfolio-000?style=for-the-badge&logo=ko-fi&logoColor=white)](https://arrahmanbd.github.io/)
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/arrahmanbd)
[![linkedin](https://img.shields.io/badge/Github-22272e?style=for-the-badge&logo=github&logoColor=white)](https://www.github.com/arrahmanbd)
