import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_brother_printer/flutter_brother_printer.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_brother_printer');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('configure', () async {
    expect(await FlutterBrotherPrinter.configure, '0');
  });
}
