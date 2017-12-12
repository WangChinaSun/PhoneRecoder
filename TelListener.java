package com.wjgchina.phonerecoder;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

;

/**
 * Created by Sun on 2017/12/4.
 */

public class TelListener extends PhoneStateListener {

    private Context context;
    private MediaRecorder recorder;
    private File audioFile;
    private String phoneNumber;

    public TelListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE://空闲状态，即无来电也无去电
                Log.i("TelephoneState","IDLE");
                System.out.println("xyzIIIIIIIdeleelele");
                getVideoFileName(getDiskCacheDir(context));
//                Toast.makeText(null,"RecodrServiceCreateIDLE",Toast.LENGTH_SHORT).show();
                if(recorder!=null){
                    recorder.stop();
                    System.out.println("xyz------------------stop---");
                    recorder.reset();
                    recorder.release();
                    getVideoFileName(getDiskCacheDir(context));

                }

                break;
            case TelephonyManager.CALL_STATE_RINGING://来电响铃
                Log.i("xyzTelephoneState","RINGING");
//                Toast.makeText(null,"RecodrServiceCreateRinging",Toast.LENGTH_SHORT).show();
                phoneNumber =incomingNumber;
                System.out.println("xyz----------------phoneNumber-----"+phoneNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK://摘机，即接通
                Log.i("xyzTelephoneState","OFFHOOK");
                System.out.println("xyzofffffffffffhook");
//                Toast.makeText(null,"RecodrServiceCreateOFFHOOK",Toast.LENGTH_SHORT).show();
                recorder=new MediaRecorder();
                System.out.println("xyz----------------recoder-----xyz");
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                System.out.println("xyz----------------recoder-----xyz---mic");

                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                System.out.println("xyz----------------recoder-----xyz---3gp");

                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                System.out.println("xyz----------------recoder-----xyz----amr");

//                System.out.print("xyz-------------"+getExternalStorageState()+"xyz------------------------storgestate");
                audioFile=new File(getDiskCacheDir(context),"recoder"+"_"+System.currentTimeMillis()+".amr");
//                audioFile=new File(getExternalStorageDirectory(),"recoder"+"_"+System.currentTimeMillis()+".3gp");

//                audioFile=new File("/android/audio","recoder"+"_"+System.currentTimeMillis()+".3gp");
                System.out.println("xyz----------------recoder-----xyz---file"+audioFile.getName());

                recorder.setOutputFile(audioFile.getAbsolutePath());
                Log.i("TAG",audioFile.getAbsolutePath());
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recorder.start();

                break;

        }

        Log.i("TelephoneState",String.valueOf(incomingNumber));
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        System.out.println("xyz-------------------cachepath------"+cachePath);
        return cachePath;
    }




//    public static Vector<String> GetVideoFileName(String fileAbsolutePath) {

    public void getVideoFileName(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".amr")) {
                    System.out.println("xyz-----------------list----"+filename);
                    vecFile.add(filename);
                }
            }
        }
//        return vecFile;
    }
}
