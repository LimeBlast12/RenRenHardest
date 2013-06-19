package game;

import android.util.Log;

/**
 * 
 * @author ZeroNing
 *
 */
public class CheckTimeState extends State {

	@Override
	public void execute(Game theGame) {
		Log.i("State", "CheckTimeState");
		if(theGame.getTimeLeft() <= 0){
			theGame.changeState(new EndGameState());
		}
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
