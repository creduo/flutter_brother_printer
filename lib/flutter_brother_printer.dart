import 'dart:async';

import 'package:flutter/services.dart';

class FlutterBrotherPrinter {
  static const MethodChannel _channel =
      const MethodChannel('flutter_brother_printer');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
