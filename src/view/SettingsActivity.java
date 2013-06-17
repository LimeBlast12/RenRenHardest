package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.nju.renrenhardest.R;

@SuppressLint("NewApi")
public class SettingsActivity extends Activity {
	private ActivityHelper helper;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("SettingsActivity", "on create");
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.game_settings);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_allMyImages:
			helper.startMyImagesActivity(this);
			return true;
		case R.id.item_allMyFriendsImages:
			helper.startAllFriendsActivity(this);
			return true;
		case R.id.item_gameRule:
			helper.showShortTip(SettingsActivity.this, "gameRule");
			return true;
		case R.id.item_quitGame:
			helper.exit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
