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
	int WIDTH = 0;//surfaceView�Ŀ�Ⱥ͸߶�
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
//				Canvas canvas = surfaceView.getHolder().lockCanvas();//����
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
		//��ʼ��һЩ����
		WIDTH = surfaceView.getWidth();
		HEIGHT = surfaceView.getHeight();
		path=new Path();
		path.moveTo(0, HEIGHT);
		int first = 0;
		for(int i=0;i<data.length;i++){//����15���� ����һ����
			path.lineTo(first += 50, HEIGHT - data[i]);
			System.out.println("first=" + data[i]);
			System.out.println("true=" + (HEIGHT - data[i]));
		}
		//�����̻߳���
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
			Canvas canvas = holder.lockCanvas();//���廭��
			canvas.drawColor(Color.BLACK);
			Paint paint = new Paint(); //���廭��
			paint.setAntiAlias(true);  //���û��ʿ����
			paint.setStyle(Paint.Style.STROKE);//���û�ͼ���ͣ�stroke���ģ�
			paint.setStrokeWidth(4);//�����߿�
			paint.setColor(Color.GREEN);
			//����������
			canvas.drawLine(0,HEIGHT,WIDTH,HEIGHT , paint);
			canvas.drawLine(0,0,0,HEIGHT, paint);
			paint.setPathEffect(new CornerPathEffect(20));//���ùսǴ�Ϊ�⻬
			canvas.drawPath(path, paint);
			//��������
			holder.unlockCanvasAndPost(canvas);
		}
		
	}
	
}
