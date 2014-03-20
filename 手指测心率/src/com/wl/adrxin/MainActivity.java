package com.wl.adrxin;

import com.wl.mp3.MainHelloService;
import com.wl.newJNI.MainJNIActivity;
import com.wl.old.MainOldActivity;
import com.wl.photo.MyPhotoActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private Object[] activities = {
		"myCamera",	MyCameraActivity.class,
		"WaveActivity",	WaveActivity.class,
		"CameraActivity",	CameraActivity.class,
		"MyPhotoActivity",	MyPhotoActivity.class,
		"MainOldActivity",	MainOldActivity.class,
		"MainJNIActivity",	MainJNIActivity.class,
		"MainHelloService",	MainHelloService.class,
		"test1",	MyCameraActivity.class,
		"test1",	MyCameraActivity.class,
		"test1",	MyCameraActivity.class,
		"test1",	MyCameraActivity.class,
		"test1",	MyCameraActivity.class,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CharSequence[] list = new CharSequence[activities.length/2];
		for(int i=0;i<list.length;i++){
			list[i] = (String)activities[i * 2];
		}
		
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1,list);
		ListView listView = (ListView)findViewById(R.id.listView01);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent = new Intent(MainActivity.this,(Class<?>)activities[position*2 +1]);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//监听返回键退出事件
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)){
			new AlertDialog.Builder(this).setTitle("退出")
			.setMessage("退出程序 ?")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).show();
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
