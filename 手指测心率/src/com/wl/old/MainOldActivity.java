package com.wl.old;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.wl.adrxin.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainOldActivity extends Activity implements Camera.PreviewCallback {
	private static final String TAG = "MainActivity";
	private SurfaceView surfaceView;
	LinearLayout linearLayout;
	private Camera camera;
	private boolean preview;
	private TextView textView_xinlv;
	private int xinlv = 0;
	
	//计算帧频-----------------------------------
	private long firstFrameTime = 0;   //第一帧
	private int frameRate = 0;
	
	// 坐标图变量（多个数据其他图标）--------------------------------
	XYSeries series[] = new XYSeries[3];	//用于提供绘制的点集合的数据，根据每条线的名称创建
	XYMultipleSeriesDataset dataset;		//用于保存点集数据，包括每条曲线的X，Y坐标
    private GraphicalView chart;
    float addY1; // 坐标图显示的X值
    long addX,addY;
    String[] titles;
    
    //坐标图变量（时间序列表）
    private TimeSeries seriesTime;				//用于提供绘制的点集合的数据，根据每条线的名称创建
    private XYMultipleSeriesDataset datasetTime;//用于保存点集数据，包括每条曲线的X，Y坐标
	Date[] xcache = new Date[20];				//时间数据
	int[] ycache = new int[20];					//数据 
	
	public float colorG_tol = 0,colorG_avg = 0,colorG_avg1 = 0,colorG_avg2 = 0;						// colorG_avg * 1000%130 colorG_avg *10000%13000;
	
    private TimerTask task;
    
    MyHandler wlHandler = new MyHandler(this);
    
    //时间
    //private Date date = null; 
    //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private Timer timer = new Timer();
    
	
	private List<Integer> list = null;
	private int myFlag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);						//没有标题
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//设置高亮
        setContentView(R.layout.main_old);
        
        list = new ArrayList<Integer>();
        //曲线
        textView_xinlv = (TextView)findViewById(R.id.textView_xinlv);
        linearLayout = (LinearLayout) (LinearLayout)findViewById(R.id.mylinearlayout);
        chart = ChartFactory.getTimeChartView(this, getMyDataset(),getMyRenderer(), "hh:mm:ss");
        linearLayout.addView(chart);
        //摄像头
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceListener());

        
		task = new TimerTask() {
			@Override
			public void run() {
				//writeText("test111111.txt",(int)colorG_avg + " "); 
				Message message = new Message();
				message.what = 200;
				wlHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 1 * 1000, 100);
        // 更新坐标图
    }
    
    static class MyHandler extends Handler {
    	WeakReference<MainOldActivity> mActivity;
    	
    	MyHandler(MainOldActivity activity){
    		mActivity = new WeakReference<MainOldActivity>(activity);
    	}
    	
		@Override
		public void handleMessage(Message msg) {
			// 刷新图表
			MainOldActivity theActivity = mActivity.get();
			theActivity.updateChartNew();
			super.handleMessage(msg);
			theActivity.textView_xinlv.setText("您目前的心率是：" + Math.abs((theActivity.xinlv))*8*60/64);
		}
	}
    
	private final class SurfaceListener implements SurfaceHolder.Callback{

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		public void surfaceCreated(SurfaceHolder holder) {
			try{
				camera = Camera.open();								//打开摄像头
			}catch(RuntimeException e){
				System.out.println("open方法有问题");
			}
			try{
				Camera.Parameters parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				//parameters.setPictureSize(800, 480);
				
				parameters.setPreviewSize(320,240); 				//默认是640*480
				camera.setParameters(parameters);
				camera.setDisplayOrientation(90);					//手机默认的是横向的
				camera.setPreviewCallback(MainOldActivity.this);
				camera.setPreviewDisplay(surfaceView.getHolder());	//通过surfaceView显示取景画面
				camera.startPreview();
				preview = true;
			} catch (IOException e) {
				Log.e(TAG,e.toString());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				if(preview)
					camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
    }
   
	//data长度和setPreviewSize的设置有关
	public void onPreviewFrame(byte[] data, Camera camera) {
	
		System.out.println("====data=" + data.length);
		colorG_tol =0;
		if(data!=null){
			int imageWidth = camera.getParameters().getPreviewSize().width;
	        int imageHeight = camera.getParameters().getPreviewSize().height;
	        
	        System.out.println("====imageWidth=" + imageWidth);
	        System.out.println("====imageHeight=" + imageHeight);
	        
	        //计算帧频
	        if(firstFrameTime == 0){
	        	firstFrameTime = System.currentTimeMillis();//开始时间
	        }
	        
	        frameRate++;
	        if(frameRate%30 == 0){
	        	long rate = frameRate * 1000 / (System.currentTimeMillis() - firstFrameTime);
                Toast.makeText(MainOldActivity.this, "Frame Rate:" + rate, Toast.LENGTH_SHORT)
                .show();
                firstFrameTime = 0;
                frameRate = 0;
	        }
	        
	        //帧数组
	        int RGBData[] = new int[imageWidth * imageHeight];
	        //将YUV转成RGB格式
	        DecodeRGB.decodeYUV420SP(RGBData, data, imageWidth, imageHeight);
	        Bitmap bmp = Bitmap.createBitmap(RGBData, imageWidth, imageHeight,Config.RGB_565);
	        
	        System.out.println("宽度：" + bmp.getWidth()+ "高度：" +bmp.getHeight());
	        //recogniseFinger(bmp);	//判断手指位置
	        
	        int[][] onePicture = new int[imageWidth][imageHeight]; //用该数组获取一帧的所有像素值
	        for(int i=0;i<bmp.getWidth();i++){
				for(int j=0;j<bmp.getHeight();j++){
					onePicture[i][j] = bmp.getPixel(i, j);
					colorG_tol += Color.green(onePicture[i][j]);
				}
			}
			colorG_avg = colorG_tol/(bmp.getHeight()*bmp.getWidth()*100);
			colorG_avg = colorG_avg *10000%13000;
			
			if(myFlag < 64){
				list.add((int)colorG_avg);
			}
			myFlag++;
			
			if(myFlag == 64){				//如果到了64帧就计算心率
				int[] flo = new int[64];
				
				for(int j=0;j<64;j++){
					flo[j] = (Integer)list.get(j);
					System.out.print(flo[j]+ " ");
				}
				try {
					NewFFT newFFT = new NewFFT();
					xinlv = newFFT.init(flo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.remove(0);
				myFlag--;
			}

			if(bmp!=null&&!bmp.isRecycled()){
				bmp.recycle() ;
				 bmp=null;
				 System.gc();
				}
//			date = new Date();
//			writeText("mytime",formatter.format(date) + " ");
			
		}
	}
	
	
	/**
	 * 判断手指方向
	 * @param bmp
	 
	private void recogniseFinger(Bitmap bmp){
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		if(bmp!=null){
			int a[] = new int[4];
			//上方
			for(int j=0;j<width;j++){
				int imagePixel = bmp.getPixel(0, j);
				if(imagePixel>20){
					a[0]++;
				}
			}
			
			//下方
			for(int j=0;j<width;j++){
				int imagePixel = bmp.getPixel(143, j);
				if(imagePixel>20){
					a[1]++;
				}
			}
			
			//左方
			for(int i=0;i<height;i++){
				int imagePixel = bmp.getPixel(i, 0);
				if(imagePixel>20){
					a[2]++;
				}
			}
			
			//右方
			for(int i=0;i<height;i++){
				int imagePixel = bmp.getPixel(i, 175);
				if(imagePixel>20){
					a[3]++;
				}
			}
			
			System.out.println("a0 = " + a[0]);
			System.out.println("a1 = " + a[1]);
			System.out.println("a2 = " + a[2]);
			System.out.println("a3 = " + a[3]);
			
			//求最大值
			int temp = 0;
			for(int i=1;i<4;i++){
				if(a[temp]<a[i])
					temp = i;
			}
			switch(temp){
				case 0:
					System.out.println("手指在上方");
					break;
				case 1:
					System.out.println("手指在下方");
					break;
				case 2:
					System.out.println("手指在左方");
					break;
				case 3:
					System.out.println("手指在右方");
					break;
			}
			
			//求覆盖面积
			for(int i=0;i<4;i++){
				for(int j=i+1;j<4;j++){
					if(a[i]<a[j]){
						temp = a[i];
						a[i] = a[j];
						a[j] = temp;
					}
				}
			}
			System.out.println("覆盖面积：" + a[0]*a[1]/(double)(176*144));
		}
	}
	
	*/
	
	
	/**
	 * 过滤波形
	 * @param DataList
	 * @return
	
	private Float[] Filter(List<Float> DataList){
		int total_number = DataList.size();
		Float temp_Data_One [] = new Float [total_number];
		Float temp_Data_Two [] = new Float [total_number];
		Float reslut_Data [] = new Float [total_number];
		for(int index=0;index< total_number;index++){
			temp_Data_One [index] = DataList.get(index);
			temp_Data_Two [index] = 0f;
			reslut_Data [index] = 0f;
		}
		int sample_Index=10;
		for(int index=0;index<= sample_Index;index++){
			temp_Data_Two [index] = temp_Data_One [index];
			reslut_Data [index] = temp_Data_Two [index];
		}
		for(int index_i= sample_Index +1;index_i< total_number;index_i++){
			for(int index_j=0; index_j<= sample_Index +1;index_j++){
				temp_Data_Two [index_i]+= temp_Data_One [index_i-index_j];
			}
			reslut_Data [index_i]= temp_Data_Two [index_i]/( sample_Index +1);
		}
		return reslut_Data;
	}
	 */
	
	
	/**
	 * 展示心率
	 
    private void showHB(){
    	a[k] = colorG_avg;
		k++;
		if(k>=10){
			k=0;
			System.out.println("");
			System.out.print("a的值是：");
			
			for(int j=0;j<10;j++){
				
				System.out.print(" " + a[j]);
			}
			
			float max = a[0];
			float min = a[0];
			int flag1 = 0;
			int flag2 = 0;
			int flag3 = 0;
			int flag4 = 0;
			for(int i=0;i<5;i++){
				if(max<=a[i]){
					max = a[i];
					flag1 = i;
				}
			
				if(min>=a[i]){
					min = a[i];
					flag2 = i;
				}
			}
			for(int i=5;i<10;i++){
				if(max<=a[i]){
					max = a[i];
					flag3 = i;
				}
			
				if(min>=a[i]){
					min = a[i];
					flag4 = i;
				}
			}
			
			System.out.println("两个波峰之间：" + String.valueOf(flag3-flag1));
			textView_xinlv.setText("您目前的心率是：" + Math.abs((xinlv))*8*60/64);
		}
    }
    */
    
    //展示曲线
    private void updateChartNew(){
    	int length = seriesTime.getItemCount(); 
		if(length>=20) 
			length = 20; 
		addX=new Date().getTime(); 
		addY=(long)colorG_avg; 			//加入colorG_avg的值
		//将前面的点放入缓存 
		for (int i = 0; i < length; i++) {
			xcache[i] = new Date((long)seriesTime.getX(i)); 
			ycache[i] = (int) seriesTime.getY(i);
			}
		seriesTime.clear(); 
		seriesTime.add(new Date(addX), addY);
		for (int k = 0; k < length; k++) { 
			seriesTime.add(xcache[k], ycache[k]); 
		} 
		//在数据集中添加新的点集
		datasetTime.removeSeries(seriesTime); 
		datasetTime.addSeries(seriesTime); 
		//曲线更新 
		chart.invalidate(); 
    }
    
	/** 
	 * 设定如表样式 
	 * @return 
	 */
	private XYMultipleSeriesRenderer getMyRenderer() { 
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setChartTitle("实时曲线");				//标题
		renderer.setChartTitleTextSize(20); 			//标题文字大小
		renderer.setXTitle("时间");						//x轴说明
		renderer.setAxisTitleTextSize(16);				//设置轴标题大小
		renderer.setAxesColor(Color.BLACK); 			//设置轴颜色
		renderer.setLabelsTextSize(15);	 				//数轴刻度字体大小
		renderer.setLabelsColor(Color.BLACK);			//数轴颜色
		//renderer.setLegendTextSize(15); 				//图例大小
		//renderer.setXLabelsColor(Color.BLACK); 			
		//renderer.setYLabelsColor(0,Color.BLACK);
		renderer.setShowLegend(false); 					//不显示图例
		renderer.setMargins(new int[] {10, 10, 5, 0});	//上，左，下，右
		//以下是曲线的属性
		XYSeriesRenderer r = new XYSeriesRenderer(); 
		r.setColor(Color.BLUE);
		r.setChartValuesTextSize(15);
		r.setChartValuesSpacing(3);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true); 
		renderer.addSeriesRenderer(r);
		renderer.setMarginsColor(Color.WHITE);
		renderer.setPanEnabled(false,false);			//设置x，y轴能否拖动
		renderer.setShowGrid(true);
		renderer.setYAxisMax(150);
		renderer.setYAxisMin(50);
		renderer.setInScroll(true); 					//调整大小
		return renderer;
	}
	
	/** 
	 *  数据对象
	 *  @return
	 */
	private XYMultipleSeriesDataset getMyDataset() { 
		datasetTime = new XYMultipleSeriesDataset(); 
		final int nr = 10; 
		long value = new Date().getTime(); 
		Random r = new Random();
		for (int i = 0; i < 1; i++) { 
			seriesTime = new TimeSeries("Myseries " + (i + 1)); 
			for (int k = 0; k < nr; k++) { 
				seriesTime.add(new Date(value+k*1000), 20 +r.nextInt() % 10); 
				} 
			datasetTime.addSeries(seriesTime);
			} 
		Log.i(TAG, datasetTime.toString()); 
		return datasetTime; 
	} 
	
	//文件操作
	public void writeText(String fileName,String data){
    	File file = new File(Environment.getExternalStorageDirectory(),fileName);  
        FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
           try {
			fileOutputStream.write(data.getBytes());
			fileOutputStream.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override 
	public void onDestroy() { 
		//当结束程序时关掉Timer
		timer.cancel(); 
		super.onDestroy(); 
	}
	
}