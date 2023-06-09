
import 'package:flutter/services.dart';


/// Class to capture screenshots with native code working on background
class ScreenshotPlus {
	/// Comunication property to talk to the native background code.
	static const MethodChannel _channel =
			MethodChannel('screenshot_plus');

	/// Captures everything as is shown in user's device.
	static Future<String> takeShot() async {
		final String path = await _channel.invokeMethod('takeShot')??'';
    //It will return path ,else blank path
		return path;
	} // takeScreenshot()
} // NativeScreenshot
