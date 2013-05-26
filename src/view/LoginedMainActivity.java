package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class LoginedMainActivity extends Activity {
	private Renren renren;
	private ActivityHelper helper;
	private Button seeFriendsButton;
	private Button seeMyselfButton;
	private Button startGameButton;
	private Button logoutButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.game_chooseview);
		helper = ActivityHelper.getInstance();
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
		seeFriendsButton = (Button) findViewById(R.id.friend_portrait);
		seeFriendsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startFriendListActivity();
			}
		});

		seeMyselfButton = (Button) findViewById(R.id.self_portrait);
		seeMyselfButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startMyPhotoFragmentActivity();
			}
		});
		
		startGameButton = (Button) findViewById(R.id.start_game);
		startGameButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGameMainActivity();
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
	 * 展示好友列表的界面
	 */
	private void startFriendListActivity() {
		Intent intent = new Intent(this, FriendListActivity.class);
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
	
	private void startMyPhotoFragmentActivity() {
		Intent intent = new Intent(this, MyImagesActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
	
	private void startGameMainActivity(){
		Intent intent = new Intent(this, GameMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}	
}
