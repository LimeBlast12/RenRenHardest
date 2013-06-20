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
}
