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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
		initButtons();
		/*initTextViews*/
		mTextView_time = (TextView)findViewById(R.id.time_left);
		mTextView_number = (TextView)findViewById(R.id.image_number);
		/*初始化imageList，这里先写死*/
		imageList = new ArrayList<ImageDisplay>();
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20130508/2110/h_large_QUg5_7e8f0000005b113e.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn221/20120917/2305/h_large_o5GZ_3a09000032131375.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn521/20120908/2030/h_large_nYKP_77f600000b4d1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn321/20120901/2005/h_large_n9cT_5cde000024bc1376.jpg",0,0));
		imageList.add(new ImageDisplay("http://hdn.xnimg.cn/photos/hdn421/20120717/0830/h_large_4FNU_4d0a000004c61375.jpg",0,0));
		/*初始化imageView,imageLoader*/
		mImageView = (ImageView)findViewById(R.id.iv);
		setImage(0);
		imageLoader = new ImageLoader(GameMainActivity.this);
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
		if(current_index<5){
			/*先改变图片*/
			setImage(current_index);
	        /*再改变数字*/
	        int number = current_index+1;
			mTextView_number.setText("No."+String.valueOf(number));
			
	        current_index++;
		}
	}
	
	public void setImage(int index){
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
}
