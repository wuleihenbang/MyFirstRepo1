package com.wl.adrxin;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

public class WaveActivity extends Activity implements SurfaceHolder.Callback{

	private SurfaceView surfaceView = null;
	private SurfaceHolder holder = null;
	private int[] data = {60,50,90,300,70,100,50,90,50,160};
	int WIDTH = 0;//surfaceView的宽度和高度
	int HEIGHT = 0;
	private Path path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surfaceview);
		surfaceView = (SurfaceView)findViewById(R.id.SurfaceView01);
		
		holder = surfaceView.getHolder();
		holder.addCallback(this);
		surfaceView.setZOrderOnTop(true);
		surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		surfaceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				SurfaceView surfaceView = (SurfaceView)v;
//				Canvas canvas = surfaceView.getHolder().lockCanvas();//加锁
//				Paint paint = new Paint();
//				paint.setColor(Color.WHITE);
//				canvas.drawText("Hello!",100,100, paint);
//				surfaceView.getHolder().unlockCanvasAndPost(canvas);
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//初始化一些数据
		WIDTH = surfaceView.getWidth();
		HEIGHT = surfaceView.getHeight();
		path=new Path();
		path.moveTo(0, HEIGHT);
		int first = 0;
		for(int i=0;i<data.length;i++){//画出15个点 连成一条线
			path.lineTo(first += 50, HEIGHT - data[i]);
			System.out.println("first=" + data[i]);
			System.out.println("true=" + (HEIGHT - data[i]));
		}
		//启动线程画线
		MyThread myThread = new MyThread(); 
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	class MyThread extends Thread{

		@Override
		public void run() {
			Canvas canvas = holder.lockCanvas();//定义画布
			canvas.drawColor(Color.BLACK);
			Paint paint = new Paint(); //定义画笔
			paint.setAntiAlias(true);  //设置画笔抗锯齿
			paint.setStyle(Paint.Style.STROKE);//设置绘图类型（stroke空心）
			paint.setStrokeWidth(4);//设置线宽
			paint.setColor(Color.GREEN);
			//绘制坐标轴
			canvas.drawLine(0,HEIGHT,WIDTH,HEIGHT , paint);
			canvas.drawLine(0,0,0,HEIGHT, paint);
			paint.setPathEffect(new CornerPathEffect(20));//设置拐角处为光滑
			canvas.drawPath(path, paint);
			//解锁画布
			holder.unlockCanvasAndPost(canvas);
		}
		
	}
	
}
