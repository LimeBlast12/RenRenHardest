package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nju.renrenhardest.R;
import game.Game;

@SuppressLint("NewApi")
public class SettingsActivity extends Activity {
	private ActivityHelper helper;
	private Button difficultyButton;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("SettingsActivity", "on create");
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.game_settings);
		initButtons();
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
	}
	
	private void initButtons() {		
		difficultyButton = (Button) findViewById(R.id.difficulty_button);
		final Game game = Game.getInstance();
		final int difficulty = game.getDifficulty();
		setDifficultyButton(difficulty);
		difficultyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int currentDifficulty = game.getDifficulty();
				int newDifficulty = Game.DIFFICULTY_SIMPLE;
				switch (currentDifficulty) {
				case Game.DIFFICULTY_HARD:
					newDifficulty = Game.DIFFICULTY_SIMPLE;
					break;
				case Game.DIFFICULTY_MIDDLE:
					newDifficulty = Game.DIFFICULTY_HARD;
					break;
				case Game.DIFFICULTY_SIMPLE:
					newDifficulty = Game.DIFFICULTY_MIDDLE;			
					break;
				default:					
					break;			
				}
				game.setDifficulty(newDifficulty);
				setDifficultyButton(newDifficulty);
			}			
		});
	}
	
	private void setDifficultyButton(int difficulty) {
		switch (difficulty) {
		case Game.DIFFICULTY_HARD:
			difficultyButton.setText(R.string.difficulty_hard);
			break;
		case Game.DIFFICULTY_MIDDLE:
			difficultyButton.setText(R.string.difficulty_middle);
			break;
		case Game.DIFFICULTY_SIMPLE:
			difficultyButton.setText(R.string.difficulty_simple);
			break;
		default:
			break;			
		}
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
