package com.wl.photo;

import com.wl.adrxin.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * @author MyHP
 *
 */
public class MyPhotoActivity extends Activity implements OnItemClickListener,OnGestureListener,ViewFactory{

	private ImageSwitcher mImageSwitcher;
	private Gallery mGallery;
	private Integer[] mThumbIds;
	private Integer[] mImageIds;
	private ImageAdapter mImageAdapter;
	private int Position;               //��ǵ�ǰѡ�е���
	
	private GestureDetector detector;    //�������Ƽ����ʵ��
	private final int FLIP_DISTANCE = 50;//�������ƶ�������֮�����С����
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myphoto);
		
		initData();
		setupView();
	}

	private void initData() {
		mThumbIds = new Integer[]{
				R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
                R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7
		};
		mImageIds = new Integer[]{
	            R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
	            R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7};
		
		detector = new GestureDetector(MyPhotoActivity.this,this);
	}

	private void setupView() {
		mImageSwitcher = (ImageSwitcher)findViewById(R.id.switcher);
		mGallery = (Gallery)findViewById(R.id.gallery);
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		mImageSwitcher.setImageResource(mImageIds[0]);
		
		mImageAdapter = new ImageAdapter(this,mThumbIds);
		mGallery.setAdapter(mImageAdapter);
		mGallery.setOnItemClickListener(this);
		
	}

	/* 
	 * ����Activity�ϵĴ����¼�����GestureDetector����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return detector.onTouchEvent(event);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		setPosition(position);
		mImageSwitcher.setImageResource(mImageIds[position]);
	}


	/* 
	 * �������¼�����ʱ�����÷���
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* 
	 * //���û��ڴ������ϻ���ʱ�����÷���
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//�����һ�������x������ڵڶ��������x���곬��FLIP_DISTANCE�����ƴ������һ�����
		if((e1.getX() - e2.getX() > FLIP_DISTANCE) && (Position < mImageIds.length-1)){
			setPosition(++Position);
			mImageSwitcher.setImageResource(mImageIds[Position]);
		}
		//�����һ�������x����С�ڵڶ��������x���곬��FLIP_DISTANCE�����ƴ������󻬶���
		else if((e2.getX() - e1.getX() > FLIP_DISTANCE) && (Position > 0)){
			setPosition(--Position);
			mImageSwitcher.setImageResource(mImageIds[Position]);
		}
		mGallery.setSelection(Position);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * ���û��ڴ������Ϲ���ʱ�����÷���
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * ���û��ڴ������ϰ��¡�����δ�ƶ����ɿ�ʱ�����÷���
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	/* 
	 * ���û��ڴ����������ʱ�����÷���
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Intent intent = new Intent(MyPhotoActivity.this,MultiTouch.class);
		intent.putExtra("id", mImageIds[getPosition()]);
		startActivity(intent);
		//�����л����������ұ߽��룬����˳�
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);  
		return true;
	}
	
	/* 
	 * @see android.widget.ViewSwitcher.ViewFactory#makeView()
	 */
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return i;
	}
	
	private void setPosition(int position){
		this.Position = position;
	}
	
	private int getPosition(){
		return this.Position;
	}
	
	
}
