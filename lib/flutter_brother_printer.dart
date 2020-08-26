import 'dart:async';

import 'package:flutter/services.dart';

class FlutterBrotherPrinter {
  static const MethodChannel _channel = const MethodChannel('flutter_brother_printer');

  static Future<String> get configure async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('configure');
    return version;
  }

  static Future<String> get scanDevice async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('scanDevice');
    return version;
  }

  static Future<String> get getPairedDevices async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('getPairedDevices');
    return version;
  }

  static Future<String> get connect async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('connect');
    return version;
  }

  static Future<String> get print async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('print');
    return version;
  }

  static Future<String> get disconnect async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('disconnect');
    return version;
  }
}
