package game;

import model.GameStatusModel;
import android.util.Log;

/**
 * 
 * @author ZeroNing
 *
 */
public class EndGameState extends State {

	@Override
	public void execute(Game theGame) {
		Log.i("State", "EndGameState");
		//显示最终结果Activity
		theGame.stop();
		int rightCount = theGame.getRightPic_own() + theGame.getRightPic_friends();
		int wrongCount = theGame.getCurrentImageIndx() + 1 - rightCount;
		int score = GameScorer.score(theGame.getTotalPic_own(), theGame.getTotalPic_friends(), rightCount, wrongCount, theGame.getDifficulty());
		GameStatusModel.getInstance().updateScore(score);
		
		Log.i("filter", "MyImage : " + String.valueOf(theGame.getTotalPic_own()));
		Log.i("filter", "Friends : " + String.valueOf(theGame.getTotalPic_friends()));
		Log.i("filter", "rightFriends : " + String.valueOf(theGame.getRightPic_friends()));
		Log.i("filter", "rightMyImage : " + String.valueOf(theGame.getRightPic_own()));
		Log.i("filter", "wrongs : " + String.valueOf(wrongCount));
		Log.i("filter", "score : " + String.valueOf(score));
	}

	@Override
	public void exit(Game theGame) {
		// TODO Auto-generated method stub
	}
	@Override
	public void enter(Game theGame) {
		// TODO Auto-generated method stub
	}
}
