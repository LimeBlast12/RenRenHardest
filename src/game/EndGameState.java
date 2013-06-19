package game;

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
		// TODO Auto-generated method stub
		//调用计分系统得到最终分数
		//显示最终结果Activity
		theGame.stop();
		Log.i("EndGameState", "EndGameState");
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
