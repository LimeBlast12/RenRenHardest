package model;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GameStatusModel {
	private List<Handler> scoreHandlers;
	private static GameStatusModel thisRef;
	public static final int SCORE_MSG = 1; 
	
	private GameStatusModel() {
		scoreHandlers = new ArrayList<Handler>();
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
	
	public void updateScore(int score) {
		Log.i("GameStatusModel","updateScore");
		for (Handler h: scoreHandlers) {
			Message msg = Message.obtain(h, SCORE_MSG);
			Bundle data = new Bundle();
			data.putInt("score", score);
			msg.setData(data);
			msg.sendToTarget();
		}
	}
	
}
