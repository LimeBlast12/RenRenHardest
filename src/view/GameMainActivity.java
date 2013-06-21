package view;

import edu.nju.renrenhardest.R;
import game.Game;
import imagefilter.BitmapFilter;
import model.GameStatusModel;
import model.ModelListener;
import model.SingleImageModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
	private TextView mTextView_score = null;
	//private TextView mTextView_number = null;
	private TextView mTextView_time = null;
	private final int COUNT_FILTER_TYPE = 2;
	private Button filter_buttons[] = new Button[COUNT_FILTER_TYPE];// 现版本游戏设定两个filter，0代表左，1代表右
	private Button game_friend_button = null;
	private ActivityHelper helper;
	private ProgressDialog loadingDialog;
	private Game game;
	/* 使用handler来避免更新UI的线程安全问题 */
	private Handler singleImageHandler;
	private Handler scoreHandler, timeHandler, gameStatusHandler;

	private SingleImageModel singleImageModel;
	private GameStatusModel gameStatusModel;

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
		initTextViews();		
		mImageView = (ImageView) findViewById(R.id.iv);

		initModels();
				
		//helper.showWaitingDialog(GameMainActivity.this);
		loadingDialog = ProgressDialog.show(this, "Loading...", "正在努力加载中...");
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
		super.onDestroy();
	}

	public void onStop() {
		super.onStop();
		System.out.println("onStop");
	}

	private void initHandlers() {
		singleImageHandler = new Handler();
		
		scoreHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {//覆盖handleMessage方法  
				int score = msg.getData().getInt("score");
				updateScore(score);
			}
		};
		
		timeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {//覆盖handleMessage方法  
				int time = msg.getData().getInt("time");
				updateTime(time);
			}
		};
		
		gameStatusHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {//覆盖handleMessage方法
				Log.i("gameStatusHandler", msg.what+"");
				switch (msg.what) {
				case GameStatusModel.GAMEOVER_MSG:
					startGameOverView();
					break;				
				case GameStatusModel.GAMEREADY_MSG:				
					//helper.dismissProgress();
					loadingDialog.dismiss();
					break;
				default:
					break;
				}
				
			}
		};
	}

	private void initModels() {
		singleImageModel = SingleImageModel.getInstance();
		singleImageModel.register(this);

		gameStatusModel = GameStatusModel.getInstance();
		gameStatusModel.addScoreListener(scoreHandler);
		gameStatusModel.addTimeListener(timeHandler);
		gameStatusModel.addGameStatusListener(gameStatusHandler);
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
	
	private void initTextViews() {
		mTextView_score = (TextView)findViewById(R.id.score_realtime);
		mTextView_time = (TextView)findViewById(R.id.time_left);
		//mTextView_number = (TextView)findViewById(R.id.image_number);
	}

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
				/* 实际用于更新UI的线程 */
				Runnable updateImageThread = new Runnable() {
					@Override
					public void run() {
						// 更换图片
						Bitmap image = singleImageModel.getImage();
						updateImage(image);
					}
				};
				
				/* 触发更新UI的线程启动 */
				singleImageHandler.post(updateImageThread);
			}
		}.start();
	}
	
	/**
	 * 更新图片
	 * @param image 要猜的图片
	 */
	private void updateImage(Bitmap image) {
		mImageView = (ImageView) findViewById(R.id.iv);
		int width = image.getWidth();
		int height = image.getHeight();
		int newWidth = 300;
		Bitmap bitmap = Bitmap.createScaledBitmap(image, newWidth,
				(int) Math.round(height * newWidth * 1.0 / width), true);
		mImageView.setImageBitmap(bitmap);
	}
	
	/**
	 * 更新分数
	 * @param score 分数
	 */
	private void updateScore(int score) {
		mTextView_score = (TextView) findViewById(R.id.score_realtime);
		mTextView_score.setText("分数："+score);
	}
	
	/**
	 * 更新时间
	 * @param time 时间
	 */
	private void updateTime(int time) {
		mTextView_time = (TextView) findViewById(R.id.time_left);
		mTextView_time.setText(time+"秒");
	}
	
}
