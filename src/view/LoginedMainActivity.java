package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
		scoresButton = (Button) findViewById(R.id.friend_portrait);
		scoresButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startScoresActivity();
			}
		});

		settingsButton = (Button) findViewById(R.id.self_portrait);
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
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
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
				helper.showTip(LoginedMainActivity.this,"gameRule");
				return true;
				
			case R.id.item_quitGame:
				helper.exit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
}
