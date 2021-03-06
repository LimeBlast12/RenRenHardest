package view;

import helper.SoundPlayer;
import helper.ValueStorer;

import java.lang.reflect.Field;

import service.LoadFriendsService;
import service.LoadMyImagesService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.photos.PhotoHelper;
import com.renren.api.connect.android.view.RenrenAuthListener;

import edu.nju.renrenhardest.R;

public class MainActivity extends Activity {
	public static final String PREFS_NAME = "FirstTimeFile";
	
	private static final String API_KEY = "aa72e895b6a84941bb4ef31c8a69c179";
	private static final String SECRET_KEY = "322b904c74674d0ba333d5e4557dc7bf";
	private static final String APP_ID = "235450";
	private Renren renren;
	private Handler handler;
	private ActivityHelper helper;
	private ValueStorer storer;
	private Button oAuthButton;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*待登录界面不应播放BGM，因为这里没有游戏设置的入口，无法关闭音乐会让用户觉得无法控制*/
		SoundPlayer.stopMusic();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		getOverflowMenu();
		getActionBar().setTitle("连接人人");
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, this);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		helper.setRenren(renren);
		handler = new Handler();
		storer = ValueStorer.getInstance();
		initButtons();
		if (renren.isSessionKeyValid()) {//已经登录
			loadFriends();
			loadMyImages();
			Log.i("MainActivity", "logined");
			startLoginedMainActivity();
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		renren.init(this);
		if (renren.isSessionKeyValid()) {//已经登录
			startLoginedMainActivity();
			//startTutorialsActivity();
		} 
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		helper.dismissProgress();
	}
	
	/**
	 * 展示已登录的主界面
	 */
	private void startLoginedMainActivity() {
		Log.i("MainActivity", "start LoginedActivity");
		Intent intent = new Intent(this, LoginedMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);		
		startActivity(intent);
		this.finish();//关闭当前界面，避免按返回键时出错
	}
	
	
	/**
	 * 展示游戏教程滑动分页界面
	 */
	private void startTutorialsActivity() {
		Log.i("MainActivity", "start TutorialsActivity");
		Intent intent = new Intent(this, TutorialsActivity.class);
		String enterFrom = null;
		intent.putExtra("enterFrom","main");
		intent.putExtra(Renren.RENREN_LABEL, renren);		
		startActivity(intent);
		this.finish();//关闭当前界面，避免按返回键时出错
	}

	/**
	 * initialize the buttons and events
	 */
	private void initButtons() {
		final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				Log.d("test", values.toString());
				loadFriends();
				loadMyImages();

				/*这里判断是否第一次登录， by DanniWANG*/
				boolean isFirstTime = storer.readFirstTimeSetting(getApplicationContext(), PREFS_NAME);
				if(isFirstTime){
					System.out.println("It is the first time to play.");
					storer.editFirstTimeSetting(getApplicationContext(), PREFS_NAME, false);
					//跳转去新手教程
					startTutorialsActivity();
				}else{
					System.out.println("It is NOT the first time to play.");
					//直接跳转至选择界面
					startLoginedMainActivity();
				}
			}

			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						helper.showShortTip(MainActivity.this, MainActivity.this
										.getString(R.string.auth_failed));						
					}
				});

				Log.d("error", renrenAuthError.getError());
			}

			@Override
			public void onCancelLogin() {
				helper.dismissProgress();
			}

			@Override
			public void onCancelAuth(Bundle values) {
				helper.dismissProgress();
			}

		};

		oAuthButton = (Button) findViewById(R.id.auth_site_mode);
		oAuthButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//需要用户授予read_user_photo权限及read_user_album权限
				String[] permissions = {PhotoHelper.GET_PHOTOS_PERMISSION, PhotoHelper.GET_ALBUMS_PERMISSION, PhotoHelper.UPLOAD_PHPTO_PERMISSION};
				renren.authorize(MainActivity.this, permissions, listener);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (renren != null) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}
	}
	
	private void loadFriends() {
		Intent intent = new Intent(this, LoadFriendsService.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startService(intent);
		helper.showWaitingDialog(MainActivity.this);
	}
	
	private void loadMyImages() {
		Intent intent = new Intent(this, LoadMyImagesService.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startService(intent);
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
		inflater.inflate(R.menu.unlogin_menu,menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
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