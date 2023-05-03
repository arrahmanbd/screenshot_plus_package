import 'dart:io';
import 'package:flutter/material.dart';
import 'package:screenshot_plus/screenshot_plus.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}



class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
   File imageFile = File('');
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(useMaterial3: true),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Screenshot Plus'),
        ),
        bottomNavigationBar: ButtonBar(
          alignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              child: const Text('Clear'),
              onPressed: () {
                imageFile = File('');
                setState(() {});
              },
            )
          ],
        ),
        body: Container(
          constraints: const BoxConstraints.expand(),
          child: imageFile.isAbsolute
              ? Container(
                  padding: const EdgeInsets.all(30),
                  color: Colors.blueGrey[900],
                  child: Image.file(imageFile))
              : const Icon(Icons.image),
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
          child: const Icon(Icons.camera),
        ),
      ),
    );

  }
}
