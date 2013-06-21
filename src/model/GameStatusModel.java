package model;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GameStatusModel {
	private List<Handler> scoreHandlers;
	private List<Handler> timeHandlers;
	private List<Handler> gameStatusHandlers;
	private static GameStatusModel thisRef;
	public static final int SCORE_MSG = 1;
	public static final int TIME_MSG = 2;
	public static final int GAMEOVER_MSG = 3;
	public static final int GAMEREADY_MSG = 4;
	
	private GameStatusModel() {
		scoreHandlers = new ArrayList<Handler>();
		timeHandlers = new ArrayList<Handler>();
		gameStatusHandlers = new ArrayList<Handler>();
	}
	
	public static GameStatusModel getInstance() {
		return (thisRef == null) ? (thisRef = new GameStatusModel()) : thisRef;
	}
	
	public void addScoreListener(Handler scoreHandler) {
		scoreHandlers.add(scoreHandler);
	}
	
	public void removeScoreListner(Handler scoreHandler) {
		scoreHandlers.remove(scoreHandler);
	}
	
	public void addTimeListener(Handler timeHandler) {
		timeHandlers.add(timeHandler);
	}
	
	public void removeTimeListner(Handler timeHandler) {
		timeHandlers.remove(timeHandler);
	}
	
	public void addGameStatusListener(Handler gameStatusHandler) {
		gameStatusHandlers.add(gameStatusHandler);
	}
	
	public void removeGameStatusListener(Handler gameStatusHandler) {
		gameStatusHandlers.remove(gameStatusHandler);
	}
	
	/**
	 * 通知界面更新分数
	 * @param score 分数
	 */
	public void updateScore(int score) {
		Log.i("GameStatusModel","update score");
		for (Handler h: scoreHandlers) {
			Message msg = Message.obtain(h, SCORE_MSG);
			Bundle data = new Bundle();
			data.putInt("score", score);
			msg.setData(data);
			msg.sendToTarget();
		}
	}
	
	/**
	 * 通知界面更新时间
	 * @param time 时间
	 */
	public void updateTime(int time) {
		Log.i("GameStatusModel","update time");
		for (Handler h: timeHandlers) {
			Message msg = Message.obtain(h, SCORE_MSG);
			Bundle data = new Bundle();
			data.putInt("time", time);
			msg.setData(data);
			msg.sendToTarget();
		}
	}
	
	/**
	 * 通知界面，游戏结束
	 */
	public void notifyGameOver() {
		Log.i("GameStatusModel","notify gameover");
		updateGameStatus(GAMEOVER_MSG);
	}
	
	/**
	 * 通知界面，游戏数据准备完毕
	 */
	public void notifyGameDataReady() {
		Log.i("GameStatusModel","notify gameready");
		updateGameStatus(GAMEREADY_MSG);
	}
	
	/**
	 * 通知界面游戏状态的消息
	 * @param what
	 */
	private void updateGameStatus(int what) {
		for (Handler h: gameStatusHandlers) {
			Message msg = Message.obtain(h, what);
			msg.sendToTarget();
		}
	}
	
}
