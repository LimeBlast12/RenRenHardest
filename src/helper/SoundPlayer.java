package helper;

import java.util.Map;

import edu.nju.renrenhardest.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * 音乐与音效管理器
 * @author DanniWang
 *
 */
public class SoundPlayer {
	private static MediaPlayer music = null;
	private static SoundPool soundPool = null;

	private static boolean musicSt = true; // 音乐开关
	private static boolean soundSt = true; // 音效开关
	private static Context context;

	private static final int[] musicId = { R.raw.bg };
	private static Map<Integer, Integer> soundMap; // 音效资源id与加载过后的音源id的映射关系表

	/**
	 * 初始化方法
	 * 
	 * @param c
	 */
	public static void init(Context c) {
		context = c;

		initMusic();

		initSound();
	}

	// 初始化音效播放器
	private static void initSound() {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
	}

	// 初始化音乐播放器
	private static void initMusic() {
		music = MediaPlayer.create(context, musicId[0]);
		music.setLooping(true);
	}

	/**
	 * 播放音效
	 * 
	 * @param resId
	 *            音效资源id
	 */
	public static void playSound(int resId) {

	}

	/**
	 * 暂停音乐
	 */
	public static void pauseMusic() {
		if (music.isPlaying())
			music.pause();
	}

	/**
	 * 播放音乐
	 */
	public static void startMusic() {
		if (musicSt)
			music.start();
	}

	/**
	 * 切换一首音乐并播放
	 */
	public static void changeAndPlayMusic() {

	}

	/**
	 * 获得音乐开关状态
	 * 
	 * @return
	 */
	public static boolean isMusicSt() {
		return musicSt;
	}

	/**
	 * 设置音乐开关
	 * 
	 * @param musicSt
	 */
	public static void setMusicSt(boolean musicSt) {
		SoundPlayer.musicSt = musicSt;
		if (musicSt)
			music.start();
		else
			music.stop();
	}

	/**
	 * 获得音效开关状态
	 * 
	 * @return
	 */
	public static boolean isSoundSt() {
		return soundSt;
	}

	/**
	 * 设置音效开关
	 * 
	 * @param soundSt
	 */
	public static void setSoundSt(boolean soundSt) {
		SoundPlayer.soundSt = soundSt;
	}

	/**
	 * 发出‘邦’的声音
	 */
	public static void boom() {
		playSound(R.raw.boom);
	}

	public static MediaPlayer getMusic() {
		return music;
	}

	public static void setMusic(MediaPlayer music) {
		SoundPlayer.music = music;
	}

	public static SoundPool getSoundPool() {
		return soundPool;
	}

	public static void setSoundPool(SoundPool soundPool) {
		SoundPlayer.soundPool = soundPool;
	}
	
	
}
