package view;


import game.Game;
import helper.SoundPlayer;
import helper.ValueStorer;

import java.lang.reflect.Field;

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
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class LoginedMainActivity extends Activity {
	private Renren renren;
	private ActivityHelper helper;
	private ValueStorer storer;
	private Button scoresButton;
	private Button settingsButton;
	private Button startGameButton;
	private Button logoutButton;
	private long mExitTime;
//	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*初始化音乐和音效管理器*/
		initSoundPlayer();
		/*初始化游戏难度*/
		initDifficulty();
		
		super.onCreate(savedInstanceState);
//	    this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
		this.setContentView(R.layout.game_chooseview);
		getOverflowMenu();
		getActionBar().setTitle("游戏主页");
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
		/*scoresButton = (Button) findViewById(R.id.choose_scores);
		scoresButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startScoresActivity();
			}
		});*/

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
				helper.showShortTip(LoginedMainActivity.this,
						LoginedMainActivity.this
								.getString(R.string.logout_success));
				startMainActivity();
			}
		});
	}
	
	public void initSoundPlayer(){
		/*初始化音乐和音效管理器，根据存储的设置来判断是否播放BGM*/
		storer = ValueStorer.getInstance();
		boolean musicSt = storer.readMusicSetting(getApplicationContext(), SettingsActivity.PREFS_NAME);
		boolean soundSt = storer.readSoundEffectSetting(getApplicationContext(), SettingsActivity.PREFS_NAME);
		if(SoundPlayer.getMusic() == null){
			SoundPlayer.init(getApplicationContext());
			if(musicSt){
				SoundPlayer.setMusicSt(true);
			}
			if(soundSt){
				SoundPlayer.setSoundSt(true);
			}
		}
	}
	
	public void initDifficulty(){
		/*初始化游戏难度*/
		final Game game = Game.getInstance();
		int difficulty = storer.readDifficultySetting(getApplicationContext(), SettingsActivity.PREFS_NAME);
		game.setDifficulty(difficulty);
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
	
	/*无论何种机型都显示overflow*/
	@SuppressLint("NewApi")
	private void getOverflowMenu() {

        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
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
                  if ((System.currentTimeMillis() - mExitTime) > 3000) {
                	  // System.currentTimeMillis()无论何时调用，肯定大于3000
                          helper.showShortTip(this, "再按一次退出程序");
                          mExitTime = System.currentTimeMillis();

                  } else {
                         helper.exit();
                  }
                  return true;
          }
          /*
          if (keyCode == KeyEvent.KEYCODE_HOME) {
        	  helper.showShortTip(getApplicationContext(),"home home home");
        	  helper.pauseMusic();
          }
          */
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
