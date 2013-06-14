package view;

import helper.NetworkChecker;
import service.LoadFriendsService;
import service.LoadMyImagesService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.photos.PhotoHelper;
import com.renren.api.connect.android.view.RenrenAuthListener;

import edu.nju.renrenhardest.R;

public class MainActivity extends Activity {
	private static final String API_KEY = "aa72e895b6a84941bb4ef31c8a69c179";
	private static final String SECRET_KEY = "322b904c74674d0ba333d5e4557dc7bf";
	private static final String APP_ID = "235450";
	private Renren renren;
	private Handler handler;
	private NetworkChecker checker;
	private ActivityHelper helper;
	private Button oAuthButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, this);
		helper = ActivityHelper.getInstance();
		helper.setRenren(renren);
		handler = new Handler();
		checker = new NetworkChecker(MainActivity.this);
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
	 * initialize the buttons and events
	 */
	private void initButtons() {
		final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				Log.d("test", values.toString());
				loadFriends();
				loadMyImages();
				startLoginedMainActivity();
			}

			@Override
			public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						helper.showTip(MainActivity.this, MainActivity.this
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
				String[] permissions = {PhotoHelper.GET_PHOTOS_PERMISSION, PhotoHelper.GET_ALBUMS_PERMISSION};
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
		if (checker.isConnectingToInternet()) {
			Log.i("FriendListActivity", "network enable");
			startService(intent);
			helper.showWaitingDialog(MainActivity.this);
		} else {
			Log.i("FriendListActivity", "network unable");
		}
	}
	
	private void loadMyImages() {
		Intent intent = new Intent(this, LoadMyImagesService.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startService(intent);
	}

}