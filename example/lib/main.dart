import 'dart:io';
import 'package:flutter/material.dart';
import 'package:screenshot_plus/screenshot_plus.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File imageFile = File('');
  @override
  void initState() {
    super.initState();
  } // initState()

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(useMaterial3: true),
      home: Scaffold(
        appBar: AppBar(
          title: Text('Screenshot Plus'),
        ),
        bottomNavigationBar: ButtonBar(
          alignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              child: Text('Clear'),
              onPressed: () {
                imageFile = File('');
                setState(() {});
              },
            )
          ],
        ),
        body: Container(
          constraints: BoxConstraints.expand(),
          child: imageFile.isAbsolute
              ? Container(
                  padding: EdgeInsets.all(30),
                  color: Colors.blueGrey[900],
                  child: Image.file(imageFile))
              : Icon(Icons.image),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () async {
            debugPrint('Screen shot captured');
            //Get the absolute path
            String path = await ScreenshotPlus.takeShot() ?? '';
            debugPrint('Screenshot taken @: $path');

            imageFile = File(path);
            setState(() {});
          },
          child: Icon(Icons.camera),
        ),
      ),
    );
  } // build()
} // _MyAppState
