package view;

import game.Game;
import helper.ScreenShot;

import java.lang.reflect.Field;

import model.GameStatusModel;

import service.UploadScoreService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class GameOverActivity extends Activity {
	private Renren renren;
	private ActivityHelper helper;
	private TextView mTextView_score;
	private Button mButton_share;
	private Button mButton_replay, mButton_return;
	private Handler uploadFinishHandler;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		getOverflowMenu();
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		if (renren == null) {
			renren = helper.getRenren();
		}

		uploadFinishHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {//覆盖handleMessage方法
				helper.showShortTip(GameOverActivity.this, "上传成功，你可以到你的人人新鲜事查看");
			}
		};
		
		GameStatusModel.getInstance().addUploadFinishListener(uploadFinishHandler);
		initButtons();
		showScore();
	}

	private void initButtons() {
		mButton_replay = (Button) findViewById(R.id.replay);
		mButton_replay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				replay();
			}
		});

		mButton_return = (Button) findViewById(R.id.return_menu);
		mButton_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				returnHome();
			}
		});

		mButton_share = (Button) findViewById(R.id.share_button);
		mButton_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ScreenShot.shoot(GameOverActivity.this);
				uploadScore();
			}
		});

	}
	
	/**
	 * 上传分数
	 */
	private void uploadScore() {
		Intent intent = new Intent(GameOverActivity.this, UploadScoreService.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startService(intent);
		Log.i("GameOverActivity", "click share");
	}

	/**
	 * 显示分数
	 */
	private void showScore() {
		int score = Game.getInstance().getScore();
		mTextView_score = (TextView) findViewById(R.id.score_final);
		mTextView_score.setText(score + "");
	}

	/**
	 * 展示随机好友列表的界面，即重新玩游戏
	 */
	private void replay() {
		Intent intent = new Intent(this, RandomFriendsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}

	/**
	 * 返回主菜单页
	 */
	private void returnHome() {
		Intent parentActivityIntent = new Intent(GameOverActivity.this,
				LoginedMainActivity.class);
		parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(parentActivityIntent);
		finish();
	}

	/* 当按后退键时，不会返回游戏主界面而是返回选择界面 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginedMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // by lys
															// 避免startActivity创建新的LoginedMainActivity实例
		startActivity(intent);
		return;
	}

	/* 无论何种机型都显示overflow */
	@SuppressLint("NewApi")
	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_over_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_allMyImages:
			helper.startMyImagesActivity(this);
			return true;
		case R.id.item_allMyFriendsImages:
			helper.startAllFriendsActivity(this);
			return true;
		case R.id.item_gameRule:
			helper.showGameRule(this);
			return true;
		case R.id.item_quitGame:
			helper.exit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
