package com.wl.adrxin;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CameraActivity extends Activity implements Camera.PreviewCallback{
	
	private Camera camera;
	private SurfaceView surfaceView;
	private boolean preview;
	private Camera.Parameters parameters;
	private ToggleButton button_flash;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
		surfaceView.getHolder().addCallback(new SurfaceListener());
		button_flash = (ToggleButton)findViewById(R.id.toggleButton);
		button_flash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				if (button_flash.isChecked()) {
					List<String> list = parameters.getSupportedFlashModes();
					if (list != null && list.size() > 0) { // 设备有闪光灯的情况
						parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					} else {
						button_flash.setEnabled(false);
						Toast.makeText(CameraActivity.this, "此设备没有闪光灯",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					List<String> list = parameters.getSupportedFlashModes();
					if (list != null && list.size() > 0) { // 设备有闪光灯的情况
						parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					} else {
						Toast.makeText(CameraActivity.this, "此设备没有闪光灯",
								Toast.LENGTH_SHORT).show();
					}
				}

				camera.setParameters(parameters);
				
				
			}
		});
	}
	
	//MySurfaceView
	class SurfaceListener implements SurfaceHolder.Callback{

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d("TEST", "surfaceChanged");
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			setCamera();
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				if(preview)
					camera.setPreviewCallback(null);
					camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
		
		//设置相机参数
		private void setCamera(){
			camera = Camera.open();
			parameters = camera.getParameters();
			//parameters.set("orientation", "portrait");
			camera.setParameters(parameters);
			camera.setPreviewCallback(CameraActivity.this);
			try {
				camera.setPreviewDisplay(surfaceView.getHolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//通过surfaceView显示取景画面
			camera.startPreview();
			preview = true;
		}
	}
	//打开和关闭闪光灯
	

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		
	}
}
