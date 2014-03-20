package com.wl.mp3;

import com.wl.adrxin.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {

        //为日志工具设置标签
        String tag ="MusicService";        

        //定义音乐播放器变量
        MediaPlayer mPlayer;

        //其他对象通过bindService方法通知该Service时该方法会被调用
        @Override
        public IBinder onBind(Intent intent) {
                Toast.makeText(this,"MusicService onBind()",Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onBind()");
                mPlayer.start();
                return null;
        }

        //其他对象通过unbindService方法通知该Service时该方法会被调用
        @Override
        public boolean onUnbind(Intent intent){
                Toast.makeText(this, "MusicService onUnbind()", Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onUnbind()");
                mPlayer.stop();
                return false;
        }

        //该服务不存在需要被创建时被调用，不管startService()还是bindService()都会在启动时调用该方法
        @Override
        public void onCreate(){
                Toast.makeText(this, "MusicService onCreate()", Toast.LENGTH_SHORT).show();
                //创建一个音乐播放器对象
                mPlayer=MediaPlayer.create(getApplicationContext(), R.raw.sarah);
                //设置可以重复播放
                mPlayer.setLooping(true);
                Log.i(tag, "MusicService onCreate()");
        }

        //用startService方法调用该服务时，在onCreate()方法调用之后，会调用改方法
        @Override
        public void onStart(Intent intent,int startid){
                Toast.makeText(this,"MusicService onStart",Toast.LENGTH_SHORT).show();
                Log.i(tag, "MusicService onStart()");
                mPlayer.start();
        }

        //该服务被销毁时调用该方法
        @Override
        public void onDestroy(){
                Toast.makeText(this, "MusicService onDestroy()", Toast.LENGTH_SHORT).show();
                mPlayer.stop();
                Log.i(tag, "MusicService onDestroy()");
        }
}