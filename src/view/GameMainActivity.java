package view;

import edu.nju.renrenhardest.R;
import edu.nju.renrenhardest.R.layout;
import edu.nju.renrenhardest.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GameMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_main, menu);
		return true;
	}

}
