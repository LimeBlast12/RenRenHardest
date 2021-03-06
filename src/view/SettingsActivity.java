package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import edu.nju.renrenhardest.R;
import game.Game;
import helper.SoundPlayer;
import helper.ValueStorer;

@SuppressLint("NewApi")
public class SettingsActivity extends Activity {
	public static final String PREFS_NAME = "SettingFile";
	
	private ActivityHelper helper;
	private ValueStorer storer;
	/*Declare Android widget*/
	private ToggleButton musicButton;
	private ToggleButton soundEffectButton;
	private Button difficultyButton;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("SettingsActivity", "on create");
		super.onCreate(savedInstanceState);
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		getActionBar().setTitle("游戏设置");
		setContentView(R.layout.game_settings);
		storer = ValueStorer.getInstance();
		initButtons();
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);		
	}

	private void initButtons() {
		musicButton = (ToggleButton) findViewById(R.id.music_button);
		soundEffectButton = (ToggleButton) findViewById(R.id.sound_effect_button);
		difficultyButton = (Button) findViewById(R.id.difficulty_button);
		
		initMusicButton();
		initSoundEffectButton();
		initDifficultyButton();
		
		final Game game = Game.getInstance();
		final int difficulty = game.getDifficulty();
		setDifficultyButton(difficulty);
		
		musicButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//打开音乐
					SoundPlayer.setMusicSt(true);
				}else{
					//关闭音乐
					SoundPlayer.setMusicSt(false);
				}
				//存储音乐设置
				storer.editMusicSetting(getApplicationContext(), PREFS_NAME, SoundPlayer.isMusicSt());
			}		
		});
		
		soundEffectButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//打开音乐
					SoundPlayer.setSoundSt(true);
				}else{
					//关闭音乐
					SoundPlayer.setSoundSt(false);
				}
				//存储音乐设置
				storer.editSoundEffectSetting(getApplicationContext(), PREFS_NAME, SoundPlayer.isSoundSt());
			}		
		});
		
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
				/*存储难度设置*/
				storer.editDifficultySetting(getApplicationContext(), PREFS_NAME, newDifficulty);
			}
		});
	}

	private void initMusicButton(){
		boolean musicSt = storer.readMusicSetting(getApplicationContext(), PREFS_NAME);
		if(musicSt){
			musicButton.setChecked(true);
		}else{
			musicButton.setChecked(false);
		}
	}
	
	private void initSoundEffectButton(){
		boolean soundEffectSt = storer.readSoundEffectSetting(getApplicationContext(), PREFS_NAME);
		if(soundEffectSt){
			soundEffectButton.setChecked(true);
		}else{
			soundEffectButton.setChecked(false);
		}
	}
	
	private void initDifficultyButton(){
		
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
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(SettingsActivity.this,
					LoginedMainActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;

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

}
