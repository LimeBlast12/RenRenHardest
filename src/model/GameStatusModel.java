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
	private List<Handler> gameOverHandlers;
	private static GameStatusModel thisRef;
	public static final int SCORE_MSG = 1;
	public static final int TIME_MSG = 2;
	public static final int GAMEOVER_MSG = 3;
	
	private GameStatusModel() {
		scoreHandlers = new ArrayList<Handler>();
		timeHandlers = new ArrayList<Handler>();
		gameOverHandlers = new ArrayList<Handler>();
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
	
	public void addGameOverListener(Handler gameOverHandler) {
		gameOverHandlers.add(gameOverHandler);
	}
	
	public void removeGameOverListener(Handler gameOverHandler) {
		gameOverHandlers.remove(gameOverHandler);
	}
	
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
	
	public void notifyGameOver() {
		Log.i("GameStatusModel","notify gameover");
		for (Handler h: gameOverHandlers) {
			Message msg = Message.obtain(h, GAMEOVER_MSG);
			msg.sendToTarget();
		}
	}
	
}
