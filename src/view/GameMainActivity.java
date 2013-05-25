package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import loader.ImageLoader;
import model.ImageDisplay;
import edu.nju.renrenhardest.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameMainActivity extends Activity {
	
	private TextView mTextView_number = null;
	private TextView mTextView_time = null;
	private Button mButton_filter_grey = null;
	private Button mButton_filter_old = null;
	private Button mButton_friend = null;
	/*用于计时*/
	private static int total_time = 10;//表示一轮游戏的总时间
	private static int count = 0;//从开始到当前的秒数
	private static int delay = 1000;  //1s
	private static int period = 1000;  //1s
	private static final int UPDATE_TEXTVIEW = 0;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private Handler mHandler = null;
	/*用于更换图片*/
	private ArrayList<ImageDisplay> imageList;//每一个map存放url, 头像主人的标识（自己为0，好友为1）, 滤镜种类（黑白是0，复古是1）。
	private int current_index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
		initButtons();
		/*初始化imageList*/
		imageList = new ArrayList<ImageDisplay>();
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20130508/2110/h_large_QUg5_7e8f0000005b113e.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn221/20120917/2305/h_large_o5GZ_3a09000032131375.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20120908/2030/h_large_nYKP_77f600000b4d1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn321/20120901/2005/h_large_n9cT_5cde000024bc1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn421/20120717/0830/h_large_4FNU_4d0a000004c61375.jpg",0,0));
		/*计时开始*/
		timer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_main, menu);
		return true;
	}
	
	private void initButtons() {
		mButton_filter_grey = (Button) findViewById(R.id.filter_grey);
		mButton_filter_grey.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
		
		mButton_filter_old = (Button) findViewById(R.id.filter_old);
		mButton_filter_old.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
		
		mButton_friend = (Button) findViewById(R.id.friend);
		mButton_friend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
	}
	
	/*change_image与更换图像有关*/
	private void change_image(){
		/*这里的5是一组图片的总数*/
		if(current_index<5){
			ImageLoader imageLoader = new ImageLoader(GameMainActivity.this);
			Bitmap originalBitmap = imageLoader.getBitmap(imageList.get(current_index).getUrl());
			ImageView iv = (ImageView)findViewById(R.id.iv);
	        iv.setImageBitmap(originalBitmap);
	        current_index++;
		}
	}
	
	/*timer, sendMessage, updateTextView这3个方法都与计时有关*/
	private void timer(){
		mTextView_time = (TextView)findViewById(R.id.time_left);
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_TEXTVIEW:
					updateTextView();
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
	
	private void updateTextView(){
		mTextView_time.setText(String.valueOf(total_time-count));
	}

}
