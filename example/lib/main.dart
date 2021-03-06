import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_brother_printer/flutter_brother_printer.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<PairedDevice> _pairedDevices = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;

    if (!mounted) return;

    // setState(() {
    //   _platformVersion = platformVersion;
    // });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              RaisedButton(
                onPressed: () async {
                  await FlutterBrotherPrinter.pairedDevices;
                },
                child: Text('Paired Devices'),
              ),

              (_pairedDevices != null && _pairedDevices.length > 0) ?
                ListView.builder(
                  itemCount: _pairedDevices.length,
                  itemBuilder: (BuildContext context, int index) {
                        return ListTile(
                          title: Text("Device $index"),
                        );
                      },
                ) : Container(
                child: Text("Could not retrieve any paired device"),
              ),

              RaisedButton(
                onPressed: () async {
                  await FlutterBrotherPrinter.disconnect();
                },
                child: Text('Start Session - connect()'),
              ),
              RaisedButton(
                onPressed: () async {
                  await FlutterBrotherPrinter.print();
                },
                child: Text('Print'),
              ),
              RaisedButton(
                onPressed: () async {
                  await FlutterBrotherPrinter.disconnect();
                },
                child: Text('End Session - disconnect()'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
