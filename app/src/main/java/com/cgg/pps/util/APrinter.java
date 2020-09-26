package com.cgg.pps.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Handler;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.analogics.utils.AnalogicsUtil;
import com.analogics.utils.AndroidBarcodeView;
import com.analogics.utils.PrintSpliter;
import com.analogics.utils.TextConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@SuppressLint({"NewApi"})
public class APrinter {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer = null;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    UUID uuid;

    private static APrinter aPrinter;

    private APrinter() {

    }

    private boolean setConnection() {
        boolean flag = false;
        uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            this.mmSocket = this.mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            this.mmSocket.connect();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;

    }

    public static APrinter getaPrinter() {
        if (aPrinter == null) {
            aPrinter = new APrinter();
        }
        return aPrinter;
    }

    public boolean openBT(String address) throws IOException {
        boolean flag = false;
        this.findBT(address);
        flag = setConnection();
        if (flag) {
            try {
                this.mmOutputStream = this.mmSocket.getOutputStream();
                this.mmInputStream = this.mmSocket.getInputStream();
            } catch (NullPointerException var3) {
                var3.printStackTrace();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
        return flag;
    }

    private void findBT(String address) {
        try {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!this.mBluetoothAdapter.isEnabled()) {
                this.mBluetoothAdapter.enable();
            }

            Set<BluetoothDevice> pairedDevices = this.mBluetoothAdapter.getBondedDevices();
            BluetoothDevice device;
            if (pairedDevices.size() > 0) {
                Iterator var4 = pairedDevices.iterator();

                while (var4.hasNext()) {
                    device = (BluetoothDevice) var4.next();
                    if (device.getAddress().equals(address)) {
                        this.mmDevice = device;
                        break;
                    }
                }
            }

            device = this.mBluetoothAdapter.getRemoteDevice(address);
            this.mmDevice = device;
            this.mmDevice.createBond();
        } catch (NullPointerException var5) {
            var5.printStackTrace();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void closeBT() throws IOException {
        try {
            this.mmOutputStream.close();
            this.mmInputStream.close();
            this.mmSocket.close();
        } catch (NullPointerException var2) {
            var2.printStackTrace();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();
            boolean delimiter = true;
            this.stopWorker = false;
            this.readBufferPosition = 0;
            this.readBuffer = new byte[1024];
            this.workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; ++i) {
                                    byte b = packetBytes[i];
                                    if (b == 10) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException var7) {
                            stopWorker = true;
                        }
                    }

                }
            });
            this.workerThread.start();
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public boolean printData(byte[] msg) {
        boolean flag = false;

        try {
            this.mmOutputStream.write(msg);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }

    public boolean printData(String msg) {
        boolean flag = false;

        try {
            this.mmOutputStream.write(msg.getBytes());
            this.mmOutputStream.flush();
            flag = true;
        } catch (IOException var4) {
            var4.printStackTrace();
        }


        return flag;
    }

    public void _2DbarcodePrint(Context context, String address, String barcodedata) {
        int height = 0;
        AnalogicsUtil utils = new AnalogicsUtil();
        if (barcodedata.length() < 50) {
            height = 100;
        } else if (barcodedata.length() < 200) {
            height = 150;
        } else if (barcodedata.length() < 500) {
            height = 170;
        } else if (barcodedata.length() < 800) {
            height = 200;
        } else if (barcodedata.length() < 1500) {
            height = 250;
        }

        AndroidBarcodeView view = new AndroidBarcodeView(context, barcodedata);
        Bitmap bitmap = Bitmap.createBitmap(384, height, Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.rgb(0, 0, 0));
        view.draw(canvas);
        Bitmap mBitmap = bitmap.copy(Config.ARGB_4444, true);
        byte[] c = null;

        try {
            c = utils.prepareBarcodeDataToPrint(address, mBitmap);
        } catch (InterruptedException var13) {
            var13.printStackTrace();
        }

        try {
            this.printData(c);
            Thread.sleep(3000L);
        } catch (InterruptedException var12) {
            var12.printStackTrace();
        }

    }

    public void multiLingualPrint(String address, String Printdata, int text_size, Typeface typeface) {
        AnalogicsUtil utils = new AnalogicsUtil();
        TextConverter convert = new TextConverter();
        PrintSpliter split = new PrintSpliter();
        Bitmap bmp = convert.textAsBitmap(Printdata, (float) text_size, 9.0F, -16711681, typeface);
        Bitmap image = convert.addBorder(bmp, 0, 0);
        int height;
        if (image.getHeight() < 100) {
            height = 100;
        } else {
            height = image.getHeight();
        }

        int rows = height / 100;
        ArrayList<Bitmap> smallImages = split.splitImage(image, rows, rows);
        Object var13 = null;

        try {
            for (int i = 0; i < smallImages.size(); ++i) {
                Bitmap mBitmap = ((Bitmap) smallImages.get(i)).copy(Config.ARGB_4444, true);
                byte[] c = utils.prepareDataToPrint(address, mBitmap);
                this.printData(c);
                Thread.sleep(1300L);
            }
        } catch (InterruptedException var16) {
            var16.printStackTrace();
        }

    }

    public void multiLinguallinePrint(String address, String Printdata, int text_size, Typeface typeface) {
        AnalogicsUtil utils = new AnalogicsUtil();
        TextConverter convert = new TextConverter();
        new PrintSpliter();
        Bitmap bmp = convert.textAsBitmap(Printdata, (float) text_size, 9.0F, -16711681, typeface);
        convert.addBorder(bmp, 0, 0);
        Object var11 = null;

        try {
            Bitmap mBitmap = bmp.copy(Config.ARGB_4444, true);
            byte[] c = utils.prepareDataToPrint(address, mBitmap);
            this.printData(c);
            Thread.sleep(800L);
        } catch (InterruptedException var14) {
            var14.printStackTrace();
        }

    }

    public void logoPrinting(String address, Bitmap bmp) {
        try {
            new AnalogicsUtil();
            Bitmap mBitmap = bmp.copy(Config.ARGB_4444, true);
            Bluetooth_Printer_2inch_ThermalAPI api = new Bluetooth_Printer_2inch_ThermalAPI();
            byte[] c = api.prepareLogoImageDataToPrint(address, mBitmap);
            this.printData(c);
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

    }
}
