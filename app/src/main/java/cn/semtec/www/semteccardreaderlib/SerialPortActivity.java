package cn.semtec.www.semteccardreaderlib;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.ToolBarActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import cn.semtec.www.epcontrol.EPControl;
import cn.semtec.www.epcontrol.Util;


/**
 * Created by mac on 16/2/26.
 * Update by Leslie on 16/05/25, add 2nd Gen ID support
 */
public abstract class SerialPortActivity<T extends BasePresenter, E extends BaseModel> extends ToolBarActivity<T, E> {
    //protected Application mApplication;
    protected SerialPort mSerialPort;
    protected static OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    final String TAG = "CARD_READER";

    //semtec card readerer 通信协议, total 12 bytes
    //protocol header 6bytes + card serial number 4bytes + checksum 1 bytes + ender 1 byte
    //RC522

    /*private static class crp {
        private static final int len = 12;
        private static final byte[] header = {0x41, 0x54, 0x58, 0x5f, 0x02, 0x02};
        private static final int typelen = 0;
        private static final int datalen = 4;
        private static final byte ender = 0x23;
    }*/


    //semtec type B card reader protocol,total 14bytes
    //Header 3 bytes + data type 1byte+ csn 8 bytes + checksum 1 byte + ender 1 byte
    //RC523
    private static class crp {
        private static final int len = 14;
        private static final byte[] header = {0x57, 0x43, 0x44};
        private static final int typelen = 1;
        private static final int datalen = 8;
        private static final byte ender = 0x23;
    }

    private long start_time,current_time;
    private static final long timeout = 2000; //unit in ms
    private static byte[] cardDateBuf;
    protected static int cardDataBufSize;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    Log.e("sizesize", size+"==="+Util.bytesToHex(buffer, size));
                    if (size > 0) {
                        if(cardDataBufSize==0) {
                            start_time = System.currentTimeMillis();
                        }
                        else{
                            current_time = System.currentTimeMillis();
                            if(current_time-start_time>timeout) {
                                cardDataBufSize = 0;
                                start_time = current_time;
                                Log.i(TAG,"read buffer timeout");
                            }
                        }
                        System.arraycopy(buffer,0,cardDateBuf,cardDataBufSize,size);
                        cardDataBufSize+=size;
                        if(cardDataBufSize==crp.len) {
                            cardDataBufSize = 0;
                            //verify header
                            boolean readyToSend = true;
                            for(int i =0;i<crp.header.length;i++) {
                                if (cardDateBuf[i] != crp.header[i]) {
                                    Log.i(TAG,"header is not match");
                                    readyToSend = false;
                                }
                            }
                            //verify ender
                            if (cardDateBuf[crp.len-1] != crp.ender) {
                                Log.i(TAG,"Ender is not match");
                                readyToSend = false;

                            }
                            byte checkSum = 0;
                            for(int i =0;i< crp.datalen;i++) {
                                checkSum+=cardDateBuf[crp.header.length+crp.typelen+i];
                            }
                            if(checkSum != cardDateBuf[crp.len-2]) {
                                Log.i(TAG,"checkSum is not match");
                                readyToSend = false;
                            }
                            if(readyToSend == true) {
                                Log.i(TAG,"Sending card serial number");
                                byte[] cardSerialNumber = new byte[crp.datalen+crp.typelen];
                                System.arraycopy(cardDateBuf, 8, cardSerialNumber, 0, 4);
                                onDataReceived(cardSerialNumber, 4);
//                                System.arraycopy(cardDateBuf, crp.header.length, cardSerialNumber, 0, crp.datalen+crp.typelen);
//                                onDataReceived(cardSerialNumber, crp.datalen+crp.typelen);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SerialPortActivity.this.finish();
            }
        });
        b.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mApplication = (Application) getApplication();
        try {
            //mSerialPort = mApplication.getSerialPort();
            cardDateBuf=new byte[64];
            cardDataBufSize =0;
            EPControl.EPSet485(0);
//            String path = "/dev/ttyS3";
            String path = "/dev/ttyS1";
            int baudrate = 9600;
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            //DisplayError(R.string.error_security);
        } catch (IOException e) {
            //DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            //DisplayError(R.string.error_configuration);
        }
    }

    /**
     * 往串口发送数据
     * @param mBuffer
     * @return
     */
    public static boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        String tail = "";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        EPControl.EPSet485(0);
        return result;
    }

    protected abstract void onDataReceived(final byte[] buffer, final int size);

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        //mApplication.closeSerialPort();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        super.onDestroy();
    }
}

