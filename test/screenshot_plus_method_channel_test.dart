import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:screenshot_plus/screenshot_plus_method_channel.dart';

void main() {
  MethodChannelScreenshotPlus platform = MethodChannelScreenshotPlus();
  const MethodChannel channel = MethodChannel('screenshot_plus');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
