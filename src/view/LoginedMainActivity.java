package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class LoginedMainActivity extends Activity {
	private Renren renren;
	private ActivityHelper helper;
	private Button scoresButton;
	private Button settingsButton;
	private Button startGameButton;
	private Button logoutButton;
	private long mExitTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.game_chooseview);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		} else {
			renren = helper.getRenren();
		}
		if (!renren.isSessionKeyValid()) {//未登录	
			Log.i("LoginedActivity", "not logined");
			startMainActivity();
		} 
		initButtons();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		helper.dismissProgress();
	}

	private void initButtons() {
		scoresButton = (Button) findViewById(R.id.choose_scores);
		scoresButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startScoresActivity();
			}
		});

		settingsButton = (Button) findViewById(R.id.choose_settings);
		settingsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSettingsActivity();
			}
		});
		
		startGameButton = (Button) findViewById(R.id.start_game);
		startGameButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startRandomFriendsActivity();
			}
		});

		logoutButton = (Button) findViewById(R.id.logout);
		logoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				renren.logout(LoginedMainActivity.this);
				helper.showTip(LoginedMainActivity.this,
						LoginedMainActivity.this
								.getString(R.string.logout_success));
				startMainActivity();
			}
		});
	}

	/**
	 * 展示成绩单界面
	 */
	private void startScoresActivity() {
		Intent intent = new Intent(this, ScoresActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
	
	/**
	 * 展示游戏设置界面
	 */
	private void startSettingsActivity() {
		Log.i("LoginedMainActivity", "start settings activity");
		Intent intent = new Intent(this, SettingsActivity.class);
		//intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}

	/**
	 * 展示随机好友列表的界面
	 */
	private void startRandomFriendsActivity() {
		Intent intent = new Intent(this, RandomFriendsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
	
	
	/**
	 * 展示未登录的主界面
	 */
	private void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);		
		startActivity(intent);
		this.finish();//关闭当前界面，避免按返回键时出错
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu,menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		

		switch(item.getItemId()){
		
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
	/**
	 * 连续两次按返回键退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK) {
                  if ((System.currentTimeMillis() - mExitTime) > 2000) {
                	  // System.currentTimeMillis()无论何时调用，肯定大于2000
                          helper.showTip(this, "再按一次退出程序");
                          mExitTime = System.currentTimeMillis();

                  } else {
                         helper.exit();
                  }
                  return true;
          }
          return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 屏蔽Home键，但是好像屏蔽不了,待处理...
	 */
    @SuppressLint("NewApi")
	public void onAttachedToWindow() {  
         this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);     
         super.onAttachedToWindow();    
    }
}
