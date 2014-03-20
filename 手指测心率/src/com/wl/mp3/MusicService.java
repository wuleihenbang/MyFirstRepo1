package com.wl.mp3;

import com.wl.adrxin.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {

        //Ϊ��־�������ñ�ǩ
        String tag ="MusicService";        

        //�������ֲ���������
        MediaPlayer mPlayer;

        //��������ͨ��bindService����֪ͨ��Serviceʱ�÷����ᱻ����
        @Override
        public IBinder onBind(Intent intent) {
                Toast.makeText(this,"MusicService onBind()",Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onBind()");
                mPlayer.start();
                return null;
        }

        //��������ͨ��unbindService����֪ͨ��Serviceʱ�÷����ᱻ����
        @Override
        public boolean onUnbind(Intent intent){
                Toast.makeText(this, "MusicService onUnbind()", Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onUnbind()");
                mPlayer.stop();
                return false;
        }

        //�÷��񲻴�����Ҫ������ʱ�����ã�����startService()����bindService()����������ʱ���ø÷���
        @Override
        public void onCreate(){
                Toast.makeText(this, "MusicService onCreate()", Toast.LENGTH_SHORT).show();
                //����һ�����ֲ���������
                mPlayer=MediaPlayer.create(getApplicationContext(), R.raw.sarah);
                //���ÿ����ظ�����
                mPlayer.setLooping(true);
                Log.i(tag, "MusicService onCreate()");
        }

        //��startService�������ø÷���ʱ����onCreate()��������֮�󣬻���øķ���
        @Override
        public void onStart(Intent intent,int startid){
                Toast.makeText(this,"MusicService onStart",Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onStart()");
                mPlayer.start();
        }

        //�÷�������ʱ���ø÷���
        @Override
        public void onDestroy(){
                Toast.makeText(this, "MusicService onDestroy()", Toast.LENGTH_SHORT).show();
                mPlayer.stop();
                Log.i(tag, "MusicService onDestroy()");
        }
}