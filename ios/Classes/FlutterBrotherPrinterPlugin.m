#import "FlutterBrotherPrinterPlugin.h"
#if __has_include(<flutter_brother_printer/flutter_brother_printer-Swift.h>)
#import <flutter_brother_printer/flutter_brother_printer-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_brother_printer-Swift.h"
#endif

@implementation FlutterBrotherPrinterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterBrotherPrinterPlugin registerWithRegistrar:registrar];
}
@end
