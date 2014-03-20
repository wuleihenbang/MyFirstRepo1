package com.wl.mp3;

import com.wl.adrxin.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainHelloService extends Activity {

    //Ϊ��־�������ñ�ǩ
    String tag = "MusicService";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp4);

        //���Toast��Ϣ����־��¼
        Toast.makeText(MainHelloService.this, "MainHelloService onCreate", Toast.LENGTH_SHORT).show();
        Log.i(tag, "MainHelloService onCreate");

        //�����������
        Button b1= (Button)findViewById(R.id.Button01);
        Button b2= (Button)findViewById(R.id.Button02);
        Button b3= (Button)findViewById(R.id.Button03);
        Button b4= (Button)findViewById(R.id.Button04);

        //����������Ӷ���
        final ServiceConnection conn = new ServiceConnection(){

           @Override
           public void onServiceConnected(ComponentName name, IBinder service) {
               Toast.makeText(MainHelloService.this, "ServiceConnection onServiceConnected", Toast.LENGTH_SHORT).show();
               Log.i(tag, "ServiceConnection onServiceConnected");

           }

           @Override
            public void onServiceDisconnected(ComponentName name) {
                    Toast.makeText(MainHelloService.this, "ServiceConnection onServiceDisconnected", Toast.LENGTH_SHORT).show();
                    Log.i(tag, "ServiceConnection onServiceDisconnected");

            }
        };

                //������������
        OnClickListener ocl= new OnClickListener(){

            @Override
            public void onClick(View v) {
                //��ʾָ��intent��ָ�Ķ����Ǹ�Service
                Intent intent = new Intent(MainHelloService.this,MusicService.class);
                switch(v.getId()){
                case R.id.Button01:
                        //��ʼ����
                        startService(intent);
                        break;
                case R.id.Button02:
                        //ֹͣ����
                        stopService(intent);
                        break;
                case R.id.Button03:
                        //�󶨷���
                        bindService(intent,conn,Context.BIND_AUTO_CREATE);
                        break;
                case R.id.Button04:
                        //�����
                        unbindService(conn);
                        break;
                }
            }
        };

        //�󶨵��������
        b1.setOnClickListener(ocl);
        b2.setOnClickListener(ocl);
        b3.setOnClickListener(ocl);
        b4.setOnClickListener(ocl);    

    }

    @Override
    public void onDestroy(){
            super.onDestroy();
                Toast.makeText(MainHelloService.this, "MainHelloService onDestroy", Toast.LENGTH_SHORT).show();
                Log.i(tag, "MainHelloService onDestroy");
    }
}