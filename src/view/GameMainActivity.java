package view;

import edu.nju.renrenhardest.R;
import game.Game;
import imagefilter.BitmapFilter;

import java.util.Timer;
import java.util.TimerTask;

import model.GameStatusModel;
import model.ModelListener;
import model.SingleImageModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.renren.api.connect.android.Renren;

/**
 * 游戏主界面，最终将与游戏逻辑脱离。
 * 
 * @author DanniWang
 * 
 */
public class GameMainActivity extends Activity implements ModelListener {
	private Renren renren;
	private ImageView mImageView = null;
	// private TextView mTextView_number = null;
	// private TextView mTextView_time = null;
	private final int COUNT_FILTER_TYPE = 2;
	private Button filter_buttons[] = new Button[COUNT_FILTER_TYPE];// 现版本游戏设定两个filter，0代表左，1代表右
	private Button game_friend_button = null;
	private ActivityHelper helper;
	private Game game;
	/* 使用handler来避免更新UI的线程安全问题 */
	private Handler singleImageHandler, scoreHandler;

	private SingleImageModel singleImageModel;
	private GameStatusModel gameStatusModel;

	/* 实际用于更新UI的线程 */
	private Runnable updateImageThread = new Runnable() {
		@Override
		public void run() {
			// 更换图片
			mImageView = (ImageView) findViewById(R.id.iv);
			Bitmap image = singleImageModel.getImage();
			int width = image.getWidth();
			int height = image.getHeight();
			int newWidth = 300;
			Bitmap bitmap = Bitmap.createScaledBitmap(image, newWidth,
					(int) Math.round(height * newWidth * 1.0 / width), true);
			mImageView.setImageBitmap(bitmap);
		}
	};

	/* 用于计时，有些变量只能是静态变量且不应修改，有些如count切忌设为静态变量 */
	private static final int total_time = 10; // 表示一轮游戏的总时间
	private static final int delay = 1000; // 1s
	private static final int period = 1000; // 1s
	private static final int UPDATE_TEXTVIEW = 0;
	private int count = 0; // 从开始到当前的秒数
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private Handler mHandler = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		initHandlers();
		game = Game.getInstance();// 一定要在initButtons之前

		initButtons();
		/* initTextViews */
		// mTextView_time = (TextView)findViewById(R.id.time_left);
		// mTextView_number = (TextView)findViewById(R.id.image_number);
		mImageView = (ImageView) findViewById(R.id.iv);

		initModels();

		game.start();// 开始游戏
		// timer();
	}

	public void onResume() {
		super.onResume();
		System.out.println("onResume");
	}

	public void onRestart() {
		super.onRestart();
		System.out.println("onRestart");
	}

	public void onDestroy() {
		System.out.println("onDestroy");
		// mTimer.cancel();
		// mTimerTask.cancel();
		super.onDestroy();
	}

	public void onStop() {
		super.onStop();
		count = 0;
		System.out.println("onStop");
	}

	private void initHandlers() {
		singleImageHandler = new Handler();
		
		scoreHandler = new Handler() {
			public void handleMessage(Message msg) {//覆盖handleMessage方法  
			    Log.i("GameMainActivity score", msg.what+"");
			}
		};
	}

	private void initModels() {
		singleImageModel = SingleImageModel.getInstance();
		singleImageModel.register(this);

		gameStatusModel = GameStatusModel.getInstance();
		gameStatusModel.addScoreListener(scoreHandler);
	}

	private void initButtons() {
		filter_buttons[0] = (Button) findViewById(R.id.filter_button0);
		filter_buttons[1] = (Button) findViewById(R.id.filter_button1);

		final int filterType[] = new int[COUNT_FILTER_TYPE];
		for (int i = 0; i < COUNT_FILTER_TYPE; i++) {
			filterType[i] = game.getUsingFilter(i);
			switch (filterType[i]) {
			case BitmapFilter.GRAY_STYLE:
				filter_buttons[i].setText(R.string.filter_grey);
				break;
			case BitmapFilter.OLD_STYLE:
				filter_buttons[i].setText(R.string.filter_old);
				break;
			case BitmapFilter.OIL_STYLE:
				filter_buttons[i].setText(R.string.filter_oil);
				break;
			case BitmapFilter.ECLOSION_STYLE:
				filter_buttons[i].setText(R.string.filter_eclosion);
				break;
			case BitmapFilter.SOFTNESS_STYLE:
				filter_buttons[i].setText(R.string.filter_softness);
				break;
			case BitmapFilter.NONE_STYLE:
			default:
				filter_buttons[i].setText(R.string.filter_none);
				break;
			}
		}

		filter_buttons[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game.pickFilter(filterType[0]);
			}
		});

		filter_buttons[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game.pickFilter(filterType[1]);
			}
		});

		game_friend_button = (Button) findViewById(R.id.game_friend_button);
		game_friend_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game.pickFriend();
			}
		});
	}

	/* timer, sendMessage, updateTextView这3个方法都与计时有关 */
	// private void timer(){
	// mHandler = new Handler(){
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case UPDATE_TEXTVIEW:
	// updateTextView_time();
	// break;
	// default:
	// break;
	// }
	// }
	// };
	// /*start timer*/
	// if (mTimer == null) {
	// mTimer = new Timer();
	// }
	// if (mTimerTask == null) {
	// mTimerTask = new TimerTask() {
	// @Override
	// public void run() {
	// sendMessage(UPDATE_TEXTVIEW);
	// do {
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// }
	// } while (count-total_time>=0);
	// count ++;
	// /*时间到了调至GameOver界面*/
	// if(count==10){
	// /*先要关闭Timer和TimerTask*/
	// mTimer.cancel();
	// mTimerTask.cancel();
	// startGameOverView();
	// }
	// }
	// };
	// }
	//
	// if(mTimer != null && mTimerTask != null )
	// mTimer.schedule(mTimerTask, delay, period);
	// }
	//
	// public void sendMessage(int id){
	// if (mHandler != null) {
	// Message message = Message.obtain(mHandler, id);
	// mHandler.sendMessage(message);
	// }
	// }
	//
	// private void updateTextView_time(){
	// int time_left = total_time-count;
	// mTextView_time.setText(String.valueOf(time_left));
	// }
	//
	public void startGameOverView() {
		Intent intent = new Intent(GameMainActivity.this,
				GameOverActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}

	@Override
	public void doSomething(String message) {
		if (message.equals(singleImageModel.getMessage())) {// 单张图片更新
			startUpdateImageThread();
		}
	}

	private void startUpdateImageThread() {
		new Thread() {
			public void run() {
				/* 触发更新UI的线程启动 */
				singleImageHandler.post(updateImageThread);
			}
		}.start();
	}
}
