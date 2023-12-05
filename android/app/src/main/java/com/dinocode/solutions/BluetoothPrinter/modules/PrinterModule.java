package com.dinocode.solutions.BluetoothPrinter.modules;

import static com.dinocode.solutions.BluetoothPrinter.Constants.printer;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.brother.ptouch.sdk.Paper;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.dinocode.solutions.BluetoothPrinter.Constants;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class PrinterModule extends ReactContextBaseJavaModule {

    public PrinterModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "PrinterModule";
    }

    @ReactMethod
    public void executePrint(String text,Promise promise) {
        if (printer != null) {
            printer.printImage(textAsBitmap(text, 12, Color.BLACK));
            Log.e("Bluetooth PRINTER", text + "PRINTING IN PROGRESS");
            promise.resolve(text+" Sent to printer");
        } else {
            promise.reject("Printer is null");
        }
//         printer.setPaper();
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    @ReactMethod
    public void connectPrinterBluetooth(String bluetoothAddress, String printerName, Promise promise) {
        // Initialize the printer
        if(printer == null){
            printer = new Printer();
        }

        // Get the ReactApplicationContext
        ReactApplicationContext reactContext = getReactApplicationContext();

        // Get the BluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // Bluetooth is not supported on this device
            Log.e("Bluetooth Error", "Bluetooth is not supported on this device");
            promise.reject("Bluetooth is not supported on this device");
            return;
        }

        // Set Bluetooth connection parameters
        PrinterInfo printerInfo = printer.getPrinterInfo();
        printerInfo.printerModel = PrinterInfo.Model.PT_P750W;
        printerInfo.port = PrinterInfo.Port.BLUETOOTH;
        printerInfo.macAddress = bluetoothAddress; // Use the provided Bluetooth address
        printerInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAGE;

        // Set the Bluetooth adapter for communication
        printer.setBluetooth(bluetoothAdapter);
        // Log the Bluetooth address
        Log.d("Printer Module", "Bluetooth address: " + printerInfo.macAddress);

        try {
            // Connect to the printer
            if (printer.startCommunication()) {
                // Successfully connected
                Log.d("Printer Module", "Successfully connected to the printer: " + printerName);
                promise.resolve("Connected to printer: " + printerName);
            } else {
                // Failed to connect
                Log.e("Printer Module", "Failed to connect to the printer");
                promise.reject("Failed to connect to the printer");
            }
        } catch (Exception e) {
            // Handle connection exception
            Log.e("Printer Module", "Error during printer connection: " + e.getMessage());
            promise.reject("Error during printer connection", e.getMessage());
        }
    }
}