import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class FlutterBrotherPrinter {
  static const MethodChannel _channel = const MethodChannel('flutter_brother_printer');

  static Future<String> scanDevice() async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('scanDevice');
    return version;
  }

  static Future<List<PairedDevice>> get pairedDevices async {
    // TODO: Not Implemented
    final String devicesJson = await _channel.invokeMethod('getPairedDevices');
    // return jsonDecode(devicesJson);
    return [];
  }

  static Future<String> configure() async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('configure');
    return version;
  }

  static Future<String> connect() async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('connect');
    return version;
  }

  static Future<String> print() async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('print');
    return version;
  }

  static Future<String> disconnect() async {
    // TODO: Not Implemented
    final String version = await _channel.invokeMethod('disconnect');
    return version;
  }
}

class PairedDevice {
}
