# flutter_brother_printer

[![pub package](https://img.shields.io/pub/v/flutter_brother_printer.svg)](https://pub.dev/packages/flutter_brother_printer)

A Flutter plugin wraps Brother Mobile SDK for Brother PT, QL series label printers 

THIS PLUGIN IS STILL UNDER DEVELOPMENT, PLEASE DO NOT USE THIS PLUGIN IN PRODUCTION.
====================================================================================

## Getting Started

1. Add "flutter_brother_print" to pubspec.yaml
2. Proper permissions for bluetooth and external media (for android only) are required
3. scan, configure, and print bitmap image

## How to Print

scanDevice()
- initiate device scan

getPairedDevices()
- retrive paired devices 

configure()
- configure printer info

connect()
- starts communication with printer

print()
- sends a bitmap image to printer 

disconnect()
- ends communication with printer

## Tested Printers

QL-820NWB, PT-P710BT

## License

Apache 2.0
for BrotherPrintLibrary, please refer [https://support.brother.com/g/s/agreement/English/agree.html](https://support.brother.com/g/s/agreement/English/agree.html)
