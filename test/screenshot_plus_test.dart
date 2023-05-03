import 'package:flutter_test/flutter_test.dart';
import 'package:screenshot_plus/screenshot_plus.dart';
import 'package:screenshot_plus/screenshot_plus_platform_interface.dart';
import 'package:screenshot_plus/screenshot_plus_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockScreenshotPlusPlatform
    with MockPlatformInterfaceMixin
    implements ScreenshotPlusPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ScreenshotPlusPlatform initialPlatform = ScreenshotPlusPlatform.instance;

  test('$MethodChannelScreenshotPlus is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelScreenshotPlus>());
  });

  test('getPlatformVersion', () async {
    ScreenshotPlus screenshotPlusPlugin = ScreenshotPlus();
    MockScreenshotPlusPlatform fakePlatform = MockScreenshotPlusPlatform();
    ScreenshotPlusPlatform.instance = fakePlatform;

    expect(await screenshotPlusPlugin.getPlatformVersion(), '42');
  });
}
