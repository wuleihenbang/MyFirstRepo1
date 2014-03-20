package com.wl.adrxin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyCameraActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MySurfaceView(this));
	}
	
	//MySurfaceView
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

		public MySurfaceView(Context context) {
			super(context);
			//SurfaceHolder.addCallback(callback)添加回调函数
			getHolder().addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d("TEST", "surfaceChanged");
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d("TEST", "surfaceChanged");
			Canvas canvas = holder.lockCanvas();
			Paint paint = new Paint();
			canvas.drawColor(Color.WHITE);
			paint.setColor(Color.BLUE);
			paint.setAntiAlias(true); //设置画笔的锯齿效果
			paint.setTextSize(24);
			canvas.drawText("Hello,Android SurfaceView!",0,paint.getTextSize(),paint);
			holder.unlockCanvasAndPost(canvas);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d("TEST", "surfaceDestroyed");
		}
	}
	
	class myThread extends Thread{

		@Override
		public void run() {
			super.run();
		}
		
	}
}
