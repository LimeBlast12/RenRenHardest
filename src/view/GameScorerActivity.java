package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameScorerActivity extends Activity{
	
	private long numOfMyImages;
	private long numOfMyFriendsImages;
	private long numOfRightImages;
	private long numOfWrongImages;
	private static final int EASY = 0;
	private static final int NORMAL = 1;
	private static final int HARD = 2;
	private int gameLevel = EASY;
	private static final int RIGHT_FACTOR = 10;
	private static final int WRONG_FACTOR = 5;
	private static final int EASY_TIMES = 500;
	private static final int NORMAL_TIMES = 600;
	private static final int HARD_TIMES = 700;
	// SABCDEF等级是怎么回事？ 
	private ActivityHelper helper;
	
	private long myScores = 1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		myScores = score();
		Intent intent1 = new Intent();
		intent1.putExtra("result",myScores);
		GameScorerActivity.this.setResult(RESULT_OK,intent1);   // startActivityForResult的方式
		GameScorerActivity.this.finish();
	}
	
	public void setAllParameters(){
		 Intent intent = getIntent();
		 numOfMyImages=Long.parseLong(intent.getStringExtra("numOfMyImages"));;
		 numOfMyFriendsImages=Long.parseLong(intent.getStringExtra("numOfMyFriendsImages"));
		 numOfRightImages=Long.parseLong(intent.getStringExtra("numOfRightImages"));;
		 numOfWrongImages=Long.parseLong(intent.getStringExtra("numOfWrongImages"));
		 gameLevel =Integer.parseInt(intent.getStringExtra("gameLevel"));
	}
	
	public long score(){
		setAllParameters();
		switch(gameLevel){
			case EASY:
				return easyScore();
			case NORMAL:
				return normalScore();
			case HARD:
				return hardScore();
			default:
				return 0;
		}
	}
	
	private long easyScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}
	
	private long normalScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}

	private long hardScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}

	
}
