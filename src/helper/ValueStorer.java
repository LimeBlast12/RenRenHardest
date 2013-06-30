package helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 负责读写游戏长期所需的变量
 * @author DanniWang
 *
 */
public class ValueStorer {
	
	/*单例模式*/
	private static ValueStorer storer;
	public static ValueStorer getInstance(){
		return (storer == null) ? (storer = new ValueStorer()) : storer;
	}
	
	/*更改是否初次使用游戏*/
	public void editFirstTimeSetting(Context ctx, String prefsName, boolean firstTimeSetting){
		SharedPreferences firstTimePref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = firstTimePref.edit();
		editor.putBoolean("firstTimeSetting", firstTimeSetting);
		// Commit the edits
		editor.commit();
	}
	/*读取是否初次使用游戏*/
	public boolean readFirstTimeSetting(Context ctx, String prefsName){
		SharedPreferences firstTimePref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		boolean firstTimeSetting= firstTimePref.getBoolean("firstTimeSetting", true);
		return firstTimeSetting;
	}

	/*更改音乐设置*/
	public void editMusicSetting(Context ctx, String prefsName, boolean musicSetting){
		SharedPreferences musicPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = musicPref.edit();
		editor.putBoolean("musicSetting", musicSetting);
		// Commit the edits
		editor.commit();
	}

	/*读取音乐设置*/
	public boolean readMusicSetting(Context ctx, String prefsName){
		SharedPreferences musicPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		boolean musicSetting= musicPref.getBoolean("musicSetting", true);
		return musicSetting;
	}
	
	/*更改音效设置*/
	public void editSoundEffectSetting(Context ctx, String prefsName, boolean soundEffectSetting){
		SharedPreferences soundEffectPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = soundEffectPref.edit();
		editor.putBoolean("soundEffectSetting", soundEffectSetting);
		// Commit the edits
		editor.commit();
	}

	/*读取音效设置*/
	public boolean readSoundEffectSetting(Context ctx, String prefsName){
		SharedPreferences soundEffectPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		boolean soundEffectSetting= soundEffectPref.getBoolean("soundEffectSetting", true);
		return soundEffectSetting;
	}
	
	/*更改难度设置*/
	public void editDifficultySetting(Context ctx, String prefsName, int difficultySetting){
		SharedPreferences difficultyPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = difficultyPref.edit();
		editor.putInt("difficultySetting", difficultySetting);
		// Commit the edits
		editor.commit();
	}

	/*读取难度设置*/
	public int readDifficultySetting(Context ctx, String prefsName){
		SharedPreferences difficultyPref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		int difficultySetting= difficultyPref.getInt("difficultySetting", 0);
		return difficultySetting;
	}
	
	/*更改最高分*/
	public void editHighestScore(Context ctx, String prefsName, int highestScore){
		SharedPreferences highestScorePref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = highestScorePref.edit();
		editor.putInt("highestScore", highestScore);
		// Commit the edits
		editor.commit();
	}

	/*读取最高分*/
	public int readHighestScore(Context ctx, String prefsName){
		SharedPreferences highestScorePref = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
		int highestScore= highestScorePref.getInt("highestScore", 0);
		return highestScore;
	}
	
	
}
