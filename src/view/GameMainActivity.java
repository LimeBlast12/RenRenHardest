package view;

import helper.ImageLoader;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import model.ImageDisplay;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class GameMainActivity extends Activity {
	private Renren renren;
	private ImageView mImageView = null;
	private TextView mTextView_number = null;
	private TextView mTextView_time = null;
	private Button mButton_filter_grey = null;
	private Button mButton_filter_old = null;
	private Button mButton_friend = null;
	/*用于计时，有些变量只能是静态变量且不应修改，有些如count切忌设为静态变量*/
	private static final int total_time = 10; //表示一轮游戏的总时间
	private static final int delay = 1000;  //1s
	private static final int period = 1000;  //1s
	private static final int UPDATE_TEXTVIEW = 0;
	private int count = 0; //从开始到当前的秒数
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private Handler mHandler = null;
	/*用于更换图片*/
	private ArrayList<ImageDisplay> imageList; //ImageDisplay是一个包含三项图片信息的对象
	private int current_index = 1; //表示当前遍历到的图片index,因为刚进入游戏已经展示第0张图片，故设为1
	ImageLoader imageLoader = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
		initButtons();
		/*initTextViews*/
		mTextView_time = (TextView)findViewById(R.id.time_left);
		mTextView_number = (TextView)findViewById(R.id.image_number);
		/*初始化imageList，这里先写死*/
		imageList = new ArrayList<ImageDisplay>();
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20120617/1240/h_large_aXvi_092e0000015a1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn221/20120411/0920/h_large_xDMR_5630000481282f76.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20120908/2030/h_large_nYKP_77f600000b4d1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn321/20120901/2005/h_large_n9cT_5cde000024bc1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn421/20120717/0830/h_large_4FNU_4d0a000004c61375.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn121/20120328/2205/h_large_swwA_5f400002f1642f75.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20120316/2300/h_large_Ozk8_563d00019bca2f76.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn121/20120315/2325/h_large_1Fli_5f4c00017f242f75.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn421/20120315/2320/h_large_aMGY_5f3d00017e712f75.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn121/20120229/2150/h_large_hyxy_7a94000784202f75.jpg",0,0));
		/*初始化imageView,imageLoader*/
		mImageView = (ImageView)findViewById(R.id.iv);
		setImage(0);
		/*计时开始*/
		timer();
	}
	
	public void onResume(){
		super.onResume();
		System.out.println("onResume");
	}
	
	public void onRestart(){
		super.onRestart();
		System.out.println("onRestart");
	}
	
	public void onDestroy(){
		System.out.println("onDestroy");
		mTimer.cancel();
		mTimerTask.cancel();
		super.onDestroy();
	}
	
	public void onStop(){
		super.onStop();
		count = 0;
		System.out.println("onStop");
	}
	
	private void initButtons() {
		mButton_filter_grey = (Button) findViewById(R.id.filter_grey);
		mButton_filter_grey.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				change_image_and_number();
			}
		});
		
		mButton_filter_old = (Button) findViewById(R.id.filter_old);
		mButton_filter_old.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				change_image_and_number();
			}
		});
		
		mButton_friend = (Button) findViewById(R.id.friend);
		mButton_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				change_image_and_number();
			}
		});
	}
	
	/*change_image与更换图片有关*/
	private void change_image_and_number(){
		/*这里的5是一组图片的总数*/
		if(current_index<10){
			/*先改变图片*/
			setImage(current_index);
	        /*再改变数字*/
	        int number = current_index+1;
			mTextView_number.setText("No."+String.valueOf(number));
			
	        current_index++;
		}
	}
	
	public void setImage(int index){
		imageLoader = new ImageLoader(GameMainActivity.this);
		Bitmap originalBitmap = imageLoader.getBitmap(imageList.get(index).getUrl());
		mImageView = (ImageView)findViewById(R.id.iv);
		mImageView.setImageBitmap(originalBitmap);
	}
	
	/*timer, sendMessage, updateTextView这3个方法都与计时有关*/
	private void timer(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_TEXTVIEW:
					updateTextView_time();
					break;
				default:
					break;
				}
			}
		};		
		/*start timer*/
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					sendMessage(UPDATE_TEXTVIEW);					
					do {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}	
					} while (count-total_time>=0);
					count ++;
					/*时间到了调至GameOver界面*/
					if(count==10){
						/*先要关闭Timer和TimerTask*/
						mTimer.cancel();
						mTimerTask.cancel();
						startGameOverView();
					}
				}
			};
		}

		if(mTimer != null && mTimerTask != null )
			mTimer.schedule(mTimerTask, delay, period);
	}
	
	public void sendMessage(int id){
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, id);   
			mHandler.sendMessage(message); 
		}
	}
	
	private void updateTextView_time(){
		int time_left = total_time-count;
		mTextView_time.setText(String.valueOf(time_left));
	}
	
	public void startGameOverView(){
		Intent intent = new Intent(GameMainActivity.this, GameOverActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
}
