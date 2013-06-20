package model;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

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
		for (Handler h: scoreHandlers) {
			Message.obtain(h, score);	
		}
	}
	
}
