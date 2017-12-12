package com.wjgchina.phonerecoder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private Button startBtn;
    private Button stopBtn;

    public void showAudio(){
        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            requestAudioPermission();
        }
    }

    public void requestAudioPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale
                (this,Manifest.permission.RECORD_AUDIO)){
            explainAudioDialog();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void explainAudioDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的录音权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }


    public void showReadWrite(){
        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED||
                        ActivityCompat.checkSelfPermission
                                (this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestReadWritePermission();
        }
    }

    public void requestReadWritePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale
                (this,Manifest.permission.READ_EXTERNAL_STORAGE)||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            explainReadWriteDialog();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void explainReadWriteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的读写存储器权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("xyz----------------------------mainactivity---xyz");
        startBtn = (Button) findViewById(R.id.startService);
        stopBtn = (Button) findViewById(R.id.stopService);
        //添加监听器
        startBtn.setOnClickListener(listener);
        stopBtn.setOnClickListener(listener);
    }

    //启动监听器
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RecoderService.class);
            switch (v.getId()) {
                case R.id.startService:
                    intent.putExtra("cmd",0);//0,开启前台服务,1,关闭前台服务
                    startService(intent);

                    System.out.println("xyz-------------------startService-----mainactivity--xyz");
                    showAudio();
                    showReadWrite();
                    break;
                case R.id.stopService:
                    intent.putExtra("cmd",1);//0,开启前台服务,1,关闭前台服务
                    stopService(intent);
                    System.out.println("xyz-------------------stopService-----mainactivity--xyz");
                    break;
                default:
                    break;
            }
        }
    };


}
