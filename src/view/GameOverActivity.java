package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class GameOverActivity extends Activity {
	private Renren renren;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
	}
	
	/*当按后退键时，不会返回游戏主界面而是返回选择界面*/
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginedMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
		return;
	}

}
