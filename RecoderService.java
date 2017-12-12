package com.wjgchina.phonerecoder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class RecoderService extends Service {
    public RecoderService() {
    }

    /**
     * id不可设置为0,否则不能设置为前台service
     */
//    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;

    private boolean isRemove=false;//是否需要移除
    private NotificationManager notifyManager;
    /**
     * Notification
     */
    public void createNotification_24(){
        notifyManager= (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("Send Notification")
                .setContentText("Hi,My id is 1")
                .setContentIntent(pendingIntent);
        notifyManager.notify(1, builder.build());

        System.out.println("xyz----------createNotification-----after----xyz");
    }
    @TargetApi(26)
    public void createNotification_26(){
        notifyManager= (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        String id="channel_1";
        String description="123";
        int importance=NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel=new NotificationChannel(id,"123",importance);
        notifyManager.createNotificationChannel(mChannel);
        Notification notification=new Notification.Builder(this,id).setContentTitle("Title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("你有一条新通知")
                .setContentText("PhoneRecoder")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notifyManager.notify(1,notification);


    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

//        System.out.println("xyz----------------------------xyz----before--recoderService");
//        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        telMgr.listen(new TelListener(getApplicationContext()), PhoneStateListener.LISTEN_CALL_STATE);
//
//        System.out.println("xyz----------------------------xyz-----after--recoderService");
//        Toast.makeText(null,"RecodrServiceCreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i=intent.getExtras().getInt("cmd");
        if(i==0){
            if(!isRemove) {

                System.out.println("xyz----------createNotification---------xyz");
                if(Build.VERSION.SDK_INT>=26){
                    createNotification_26();
                }else{
                    createNotification_24();
                }

            }
            isRemove=true;
        }else {
            //移除前台服务
            if (isRemove) {
                notifyManager.cancelAll();
            }
            isRemove=false;
        }
        System.out.println("xyz----------------------------xyz--onstartcommand--before--recoderService");
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telMgr.listen(new TelListener(getApplicationContext()), PhoneStateListener.LISTEN_CALL_STATE);

        System.out.println("xyz----------------------------xyz--onstartcommand---after--recoderService");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //移除前台服务
        if (isRemove) {
            notifyManager.cancelAll();
        }
        isRemove=false;
        super.onDestroy();
    }
}
