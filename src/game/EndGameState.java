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
		theGame.stop();
		GameStatusModel.getInstance().notifyGameOver();
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
