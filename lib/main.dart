import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('device_admin');
  bool isAdminActive = false;

  @override
  void initState() {
    super.initState();
    checkAdminStatus(); // Initial check
    platform.setMethodCallHandler(_handleNativeMessages); // Listen for updates
  }

  // Handle messages from native code
  Future<void> _handleNativeMessages(MethodCall call) async {
    if (call.method == "onAdminStatusChanged") {
      setState(() {
        isAdminActive = call.arguments as bool;
      });
    }
  }

  Future<void> checkAdminStatus() async {
    try {
      final bool result = await platform.invokeMethod('isDeviceAdminActive');
      setState(() {
        isAdminActive = result;
      });
    } on PlatformException catch (e) {
      print("Failed to check admin status: ${e.message}");
    }
  }

  Future<void> activateDeviceAdmin() async {
    try {
      await platform.invokeMethod('activateDeviceAdmin');
    } on PlatformException catch (e) {
      print("Failed to activate device admin: ${e.message}");
    }
  }

  Future<void> deactivateDeviceAdmin() async {
    try {
      await platform.invokeMethod('deactivateDeviceAdmin');
    } on PlatformException catch (e) {
      print("Failed to deactivate device admin: ${e.message}");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Device Admin Example')),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "Device Admin Status: ${isAdminActive ? "Active" : "Inactive"}",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: activateDeviceAdmin,
              child: Text('Activate Device Admin'),
            ),
            SizedBox(height: 10),
            ElevatedButton(
              onPressed: deactivateDeviceAdmin,
              child: Text('Deactivate Device Admin'),
            ),
          ],
        ),
      ),
    );
  }
}
