
import 'package:flutter/services.dart';


/// Class to capture screenshots with native code working on background
class ScreenshotPlus {
	/// Comunication property to talk to the native background code.
	static const MethodChannel _channel =
			const MethodChannel('screenshot_plus');

	/// Captures everything as is shown in user's device.
	static Future<String?> takeShot() async {
		final String? path = await _channel.invokeMethod('takeShot');

		return path;
	} // takeScreenshot()
} // NativeScreenshot
