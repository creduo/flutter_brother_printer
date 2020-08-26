package com.example.flutter_brother_printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.annotation.NonNull;

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

import com.brother.ptouch.sdk.LabelInfo
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.ptouch.sdk.PrinterStatus

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream

/** FlutterBrotherPrinterPlugin */
public class FlutterBrotherPrinterPlugin: FlutterPlugin, MethodCallHandler {
  private val _printer = Printer()

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_brother_printer")
    channel.setMethodCallHandler(FlutterBrotherPrinterPlugin());
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_brother_printer")
      channel.setMethodCallHandler(FlutterBrotherPrinterPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "print" -> {
        print(call, result)
      }
      "configure" -> {
        configure(call, result)
      }
      "scanDevice" -> {
        scanDevice(call, result)
      }
      "getPairedDevices" -> {
        getPairedDevices(call, result)
      }
      "connect" -> {
        connect(call, result)
      }
      "disconnect" -> {
        disconnect(call, result)
      }
      else -> {
        result.notImplemented()
      }
    }
  }



  private fun print(call: MethodCall, result: MethodChannel.Result) {
    val address = call.argument<String>("address")
    val base64 = call.argument<String>("base64")

    var bitmap = bmpFromBase64(base64)
    if (bitmap != null) {
      Log.d(this.javaClass.name, "image size: ${bitmap.width}x${bitmap.height}")
      Log.d(this.javaClass.name, "calling printImage")
      val bit: PrinterStatus = _printer.printImage(bitmap)
      if (bit.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
        Log.d(this.javaClass.name, "ERROR - " + bit.errorCode)
        result.error("ERROR", bit.errorCode.toString(), "")
      }
    } else {
      result.error("INVALID_BITMAP", "Failed to load bitmap from base64", "");
    }

    result.success(0)
  }

  private fun configure(call: MethodCall, result: MethodChannel.Result) {
    val address = call.argument<String>("address")
    val model = call.argument<String>("model")
    val label = call.argument<String>("label")

    // Specify printer
    val settings = _printer.printerInfo
    settings.workPath = "print"
//    settings.printerModel = PrinterInfo.Model.QL_820NWB

    if (model != null) {
      settings.printerModel = PrinterInfo.Model.valueOf(model)
    }

    if (label != null) {
//      settings.printMode = LabelInfo.
    }

    settings.port = PrinterInfo.Port.BLUETOOTH
    settings.macAddress = address

    // Print Settings
//    settings.labelNameIndex = LabelInfo.QL700.W12.ordinal
    settings.labelNameIndex = LabelInfo.PT.W12.ordinal
    settings.printQuality = PrinterInfo.PrintQuality.HIGH_RESOLUTION

    settings.printMode = PrinterInfo.PrintMode.FIT_TO_PAGE
    settings.align = PrinterInfo.Align.CENTER
    settings.isAutoCut = true

//    settings.workPath = this.filesDir.absolutePath

    _printer.setBluetooth(BluetoothAdapter.getDefaultAdapter())
    _printer.printerInfo = settings
  }

  private fun scanDevice(call: MethodCall, result: MethodChannel.Result) {
    result.notImplemented()
  }

  private fun getPairedDevices(call: MethodCall, result: MethodChannel.Result) {
    Log.d(this.javaClass.name, "getPairedDevices")
    val pairList: MutableList<JSONObject> = ArrayList()
    val discoveredPrinters = enumerateBluetoothPrinters()

    Log.d(this.javaClass.name, discoveredPrinters.toString())
    for (dis in discoveredPrinters) {
      try {
        pairList.add(dis.toJSON())
        Log.d(this.javaClass.name, pairList[0].toString())
      } catch (e: JSONException) {
        e.printStackTrace()
      }
    }

    Log.d(this.javaClass.name, "returns")
    Log.d(this.javaClass.name, pairList.toString())
    result.success(pairList.toString())
  }

  private fun connect(call: MethodCall, result: MethodChannel.Result) {
    Log.d(this.javaClass.name, "connect")
    if (_printer.startCommunication()) {
      result.success(0)
    } else {
      result.error("", "", "")
    }
  }

  private fun disconnect(call: MethodCall, result: MethodChannel.Result) {
    Log.d(this.javaClass.name, "disconnect")
    if (_printer.endCommunication()) {
      result.success(0)
    } else {
      result.error("", "", "")
    }
  }

  private class DiscoveredPrinter {
    var model: PrinterInfo.Model? = null
    var port: PrinterInfo.Port
    var modelName: String? = null
    var serNo: String? = null
    var ipAddress: String? = null
    var macAddress: String? = null
    var nodeName: String? = null
    var location: String? = null
    var paperLabelName: String? = null

    constructor(device: BluetoothDevice) {
      port = PrinterInfo.Port.BLUETOOTH
      ipAddress = null
      serNo = null
      nodeName = null
      location = null
      macAddress = device.address
      modelName = device.name
      val deviceName: String = device.name
      val models: Array<PrinterInfo.Model> = PrinterInfo.Model.values()
      for (model in models) {
        val modelName: String = model.toString().replace("_", "-")
        if (deviceName.startsWith(modelName)) {
          this.model = model
          break
        }
      }
    }

    constructor(obj: JSONObject) {
      model = PrinterInfo.Model.valueOf(obj.getString("model"))
      port = PrinterInfo.Port.valueOf(obj.getString("port"))
      if (obj.has("modelName")) {
        modelName = obj.getString("modelName")
      }
      if (obj.has("ipAddress")) {
        ipAddress = obj.getString("ipAddress")
      }
      if (obj.has("macAddress")) {
        macAddress = obj.getString("macAddress")
      }
      if (obj.has("serialNumber")) {
        serNo = obj.getString("serialNumber")
      }
      if (obj.has("nodeName")) {
        nodeName = obj.getString("nodeName")
      }
      if (obj.has("location")) {
        location = obj.getString("location")
      }
      if (obj.has("paperLabelName")) {
        paperLabelName = obj.getString("paperLabelName")
      }
    }

    @Throws(JSONException::class)
    fun toJSON(): JSONObject {
      val result = JSONObject()
      result.put("model", model.toString())
      result.put("port", port.toString())
      result.put("modelName", modelName)
      result.put("ipAddress", ipAddress)
      result.put("macAddress", macAddress)
      result.put("serialNumber", serNo)
      result.put("nodeName", nodeName)
      result.put("location", location)
      return result
    }
  }

  private fun enumerateBluetoothPrinters(): List<DiscoveredPrinter> {
    Log.d(this.javaClass.name, "enumerateBluetoothPrinters")
    val results = ArrayList<DiscoveredPrinter>()
    try {
      val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        ?: return results
      if (!bluetoothAdapter.isEnabled) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }

      val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
      if (pairedDevices == null || pairedDevices.size == 0) {
        return results
      }
      for (device in pairedDevices) {
        val printer = DiscoveredPrinter(device)
        if (printer.model == null) {
          continue
        }
        results.add(printer)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return results
  }

  private fun bmpFromBase64(base64: String?): Bitmap? {
    return try {
      val bytes: ByteArray = Base64.decode(base64, Base64.DEFAULT)
      val stream: InputStream = ByteArrayInputStream(bytes)
      BitmapFactory.decodeStream(stream)
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}
