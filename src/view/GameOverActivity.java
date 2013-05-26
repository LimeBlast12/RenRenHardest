package view;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;
import edu.nju.renrenhardest.R.layout;
import edu.nju.renrenhardest.R.menu;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class GameOverActivity extends Activity {
	private Renren renren;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true); //添加后退键
		setContentView(R.layout.game_over);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(GameOverActivity.this,
					LoginedMainActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
