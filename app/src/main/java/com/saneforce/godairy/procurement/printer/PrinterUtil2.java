package com.saneforce.godairy.procurement.printer;

import static com.saneforce.godairy.procurement.printer.Printama2.CENTER;
import static com.saneforce.godairy.procurement.printer.Printama2.FULL_WIDTH;
import static com.saneforce.godairy.procurement.printer.Printama2.ORIGINAL_WIDTH;
import static com.saneforce.godairy.procurement.printer.Printama2.RIGHT;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class PrinterUtil2 {
    private static final String TAG = "PRINTAMA";

    private static final int PRINTER_WIDTH = 384;
    private static final int INITIAL_MARGIN_LEFT = -4;
    private static final int BIT_WIDTH = 384;
    private static final int WIDTH = 48;
    private static final int HEAD = 8;

    // printer commands
    private static final byte[] NEW_LINE = {10};
    private static final byte[] ESC_ALIGN_CENTER = {0x1b, 'a', 0x01};
    private static final byte[] ESC_ALIGN_RIGHT = {0x1b, 'a', 0x02};
    private static final byte[] ESC_ALIGN_LEFT = {0x1b, 'a', 0x00};
    private static final byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};

    private static final byte[] SMALL = new byte[]{0x1B, 0x21, 0x01};
    private static final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};
    private static final byte[] BOLD = new byte[]{0x1B, 0x21, 0x08};
    private static final byte[] WIDE = new byte[]{0x1B, 0x21, 0x20};
    private static final byte[] TALL = new byte[]{0x1B, 0x21, 0x10};
    private static final byte[] UNDERLINE = new byte[]{0x1B, 0x21, (byte) 0x80};
    private static final byte[] DELETE_LINE = new byte[]{0x1B, 0x21, (byte) 0x40};
    private static final byte[] WIDE_BOLD = new byte[]{0x1B, 0x21, 0x20 | 0x08};
    private static final byte[] TALL_BOLD = new byte[]{0x1B, 0x21, 0x10 | 0x08};
    private static final byte[] WIDE_TALL = new byte[]{0x1B, 0x21, 0x20 | 0x10};
    private static final byte[] WIDE_TALL_BOLD = new byte[]{0x1B, 0x21, 0x20 | 0x10 | 0x08};
    String queryPaperSizeCommand = new String(new byte[]{0x1D, 0x49} );
    byte[] fontSizeCommand = new byte[]{0x1B, 0x21, 0x30};

    private BluetoothDevice printer;
    private BluetoothSocket btSocket = null;
    private OutputStream btOutputStream = null;
    //int fontSize=16;

    PrinterUtil2(BluetoothDevice printer,int paperSize ) {
        this.printer = printer;
        if(paperSize==58) {
            this.fontSizeCommand =  new byte[]{0x1B, 0x4d, 0x00};

        } else if (paperSize == 80) {
            this.fontSizeCommand = new byte[]{0x1B, 0x21, 0x08};
        } else if (paperSize == 102) {
            this.fontSizeCommand = new byte[]{0x1B, 0x21, 0x30};
        } else {
            this.fontSizeCommand = new byte[]{0x1B, 0x21, 0x10};
        }
      //  printUsrLog("ytugym",""+paperSize+"papersize1e"+fontSizeCommand);
    }

    void connectPrinter(final PrinterConnected successListener, PrinterConnectFailed failedListener) {
        new ConnectAsyncTask(new ConnectAsyncTask.ConnectionListener() {
            @Override
            public void onConnected(BluetoothSocket socket) {
                btSocket = socket;
                try {
                    btOutputStream = socket.getOutputStream();

                    successListener.onConnected();

                } catch (IOException e) {
                    failedListener.onFailed();
                }
            }

            @Override
            public void onFailed() {
                failedListener.onFailed();
            }
        }).execute(printer);
    }

    boolean isConnected() {
        return btSocket != null && btSocket.isConnected();
    }

    void finish() {
        if (btSocket != null) {
            try {
                btOutputStream.close();
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btSocket = null;
        }
    }

    private boolean printUnicode(byte[] data) {
        try {

            btOutputStream.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //----------------------------------------------------------------------------------------------
    // PRINT TEXT
    //----------------------------------------------------------------------------------------------

    boolean printText(String text) {
        try {
            String s = StrUtil.encodeNonAscii(text);
            // sendEscPosCommand(btOutputStream, queryPaperSizeCommand.getBytes());

            btOutputStream.write(fontSizeCommand);
            btOutputStream.write(s.getBytes(Charset.forName("UTF-8")));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void setNormalText() {
        printUnicode(NORMAL);
    }

    void setSmallText() {
        printUnicode(SMALL);
    }

    void setBold() {
        printUnicode(BOLD);
    }

    void setUnderline() {
        printUnicode(UNDERLINE);
    }

    void setDeleteLine() {
        printUnicode(DELETE_LINE);
    }

    void setTall() {
        printUnicode(TALL);
    }

    void setWide() {
        printUnicode(WIDE);
    }

    void setWideBold() {
        printUnicode(WIDE_BOLD);
    }

    void setTallBold() {
        printUnicode(TALL_BOLD);
    }

    void setWideTall() {
        printUnicode(WIDE_TALL);
    }

    void setWideTallBold() {
        printUnicode(WIDE_TALL_BOLD);
    }

    void printEndPaper() {
        printUnicode(FEED_PAPER_AND_CUT);
    }

    boolean addNewLine() {
        return printUnicode(NEW_LINE);
    }

    int addNewLine(int count) {
        int success = 0;
        for (int i = 0; i < count; i++) {
            if (addNewLine()) success++;
        }
        return success;
    }

    void setAlign(int alignType) {
        byte[] d;
        switch (alignType) {
            case CENTER:
                d = ESC_ALIGN_CENTER;
                break;
            case RIGHT:
                d = ESC_ALIGN_RIGHT;
                break;
            default:
                d = ESC_ALIGN_LEFT;
                break;
        }
        try {
            //  sendEscPosCommand(btOutputStream, queryPaperSizeCommand.getBytes());
            //printUsrLog("ytugym","papersize4e"+fontSizeCommand);
            btOutputStream.write(fontSizeCommand);
            btOutputStream.write(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setLineSpacing(int lineSpacing) {
        byte[] cmd = new byte[]{0x1B, 0x33, (byte) lineSpacing};
        printUnicode(cmd);
    }

    //----------------------------------------------------------------------------------------------
    // PRINT IMAGE
    //----------------------------------------------------------------------------------------------

    boolean printImage(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth() > PRINTER_WIDTH ? FULL_WIDTH : ORIGINAL_WIDTH;
            return printImage(CENTER, bitmap, width);
        } catch (NullPointerException e) {
         //   printUsrLog(TAG, "Maybe resource is vector or mipmap?");
            return false;
        }
    }

    boolean printImage(Bitmap bitmap, int width) {
        return printImage(CENTER, bitmap, width);
    }

    boolean printImage(int alignment, Bitmap bitmap, int width) {
        if (width == FULL_WIDTH) width = PRINTER_WIDTH;
        Bitmap scaledBitmap = scaledBitmap(bitmap, width);
        if (scaledBitmap != null) {
            int marginLeft = INITIAL_MARGIN_LEFT;
            if (alignment == CENTER) {
                marginLeft = marginLeft + ((PRINTER_WIDTH - scaledBitmap.getWidth()) / 2);
            } else if (alignment == RIGHT) {
                marginLeft = marginLeft + PRINTER_WIDTH - scaledBitmap.getWidth();
            }
            byte[] command = autoGrayScale(scaledBitmap, marginLeft, 5);
            int lines = (command.length - HEAD) / WIDTH;
            System.arraycopy(new byte[]{
                    0x1D, 0x76, 0x30, 0x00, 0x30, 0x00, (byte) (lines & 0xff),
                    (byte) ((lines >> 8) & 0xff)
            }, 0, command, 0, HEAD);
            return printUnicode(command);
        } else {
            return false;
        }
    }

    private static byte[] autoGrayScale(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
        byte[] result;
        int n = bm.getHeight() + bitMarginTop;
        int offset = HEAD;
        result = new byte[n * WIDTH + offset];
        for (int y = 0; y < bm.getHeight(); y++) {
            for (int x = 0; x < bm.getWidth(); x++) {
                if (x + bitMarginLeft < BIT_WIDTH) {
                    int color = bm.getPixel(x, y);
                    int alpha = Color.alpha(color);
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    if (alpha > 128 && (red < 128 || green < 128 || blue < 128)) {
                        // set the color black
                        int bitX = bitMarginLeft + x;
                        int byteX = bitX / 8;
                        int byteY = y + bitMarginTop;
                        result[offset + byteY * WIDTH + byteX] |= (0x80 >> (bitX - byteX * 8));
                    }
                } else {
                    // ignore the rest data of this line
                    break;
                }
            }
        }
        return result;
    }

    private Bitmap scaledBitmap(Bitmap bitmap, int width) {
        try {
            int desiredWidth = width == 0 || bitmap.getWidth() <= PRINTER_WIDTH ? bitmap.getWidth() : PRINTER_WIDTH;
            if (width > 0 && width <= PRINTER_WIDTH) {
                desiredWidth = width;
            }
            int height;
            float scale = (float) desiredWidth / (float) bitmap.getWidth();
            height = (int) (bitmap.getHeight() * scale);
            return Bitmap.createScaledBitmap(bitmap, desiredWidth, height, true);
        } catch (NullPointerException e) {
       //     printUsrLog(TAG, "Maybe resource is vector or mipmap?");
            return null;
        }
    }

    void feedPaper() {
        addNewLine();
        addNewLine();
        addNewLine();
        addNewLine();
    }

    private static class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {
        private ConnectionListener listener;

        private ConnectAsyncTask(ConnectionListener listener) {
            this.listener = listener;
        }


        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            // BluetoothDevice device1 = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("00:11:22:33:44:55");


            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            BluetoothDevice device1 = bluetoothDevices[0];
            //  UUID uuid;

            //  ParcelUuid[] uuids = device1.getUuids();

            //  printUsrLog("czx","fdf"+device1.getUuids());
         /*  if (device1 != null) {
                uuid = device.getUuids()[0].getUuid();
            } else {
              return null;
            }*/
            BluetoothSocket socket = null;
            boolean connected = true;
            try {
//
//                if (!isBluetoothAvailable()) {
//                    Toast.makeText(MilkCollEntryActivity.mPrint_view_activity!=null?PrintViewActivity.mPrint_view_activity:CallPreviewActivity.mPrint_view_activity,"Check Bluetooth Connection",Toast.LENGTH_SHORT).show();
//                }else if (isBluetoothAvailable()&& ContextCompat.checkSelfPermission(PrintViewActivity.mPrint_view_activity!=null?PrintViewActivity.mPrint_view_activity:CallPreviewActivity.mPrint_view_activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    ActivityCompat.requestPermissions(MilkCollEntryActivity.mPrint_view_activity!=null?PrintViewActivity.mPrint_view_activity:CallPreviewActivity.mPrint_view_activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
//                } else {
//                    socket = device1.createRfcommSocketToServiceRecord(uuidSting);
//                    socket.connect();
//                }

                // socket = device1.createRfcommSocketToServiceRecord(uuidSting);
                //socket.connect();
                //  }

            } catch(Exception e2){
                connected = false;

            }
            return connected ? socket : null;


        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket) {
            if (listener != null) {
                if (bluetoothSocket != null) listener.onConnected(bluetoothSocket);
                else listener.onFailed();
            }
        }

        private interface ConnectionListener {
            void onConnected(BluetoothSocket socket);

            void onFailed();
        }
    }

    public interface PrinterConnected {
        void onConnected();
    }

    public interface PrinterConnectFailed {
        void onFailed();
    }

    private static void sendEscPosCommand(OutputStream outputStream, byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isBluetoothAvailable() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return bluetoothAdapter != null
                && bluetoothAdapter.isEnabled()
                && bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;
    }
}
