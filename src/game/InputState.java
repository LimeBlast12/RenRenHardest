package game;

import model.GameStatusModel;
import android.util.Log;

/**
 * 
 * @author ZeroNing
 *
 */
public class InputState extends State{

	@Override
	public void execute(Game theGame) {
		Log.i("State", "InputState");
		if(theGame.isInputed()){
			if(isInputCorrect(theGame)){
				updateGameState(theGame);
			}
			theGame.updateScore();
			GameStatusModel.getInstance().updateScore(theGame.getScore());
			gotoChangePicState(theGame);
		}
	}

	private boolean isInputCorrect(Game theGame) {
		if(theGame.isCurrentMyImage()){
			if(theGame.isClickedFriends()){
				return false;
			}
			if(theGame.getInputFilterType() == theGame.getCurrentFilterType()){
				return true;
			}
		}else{
			if(theGame.isClickedFriends()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 答对了，更新对应的状态
	 * @param theGame
	 */
	private void updateGameState(Game theGame){
		if(theGame.isCurrentMyImage()){
			theGame.increaseMyRightImg();
		}else{
			theGame.increaseFriendRightImg();
		}
	}
	
	private void gotoChangePicState(Game theGame){
		theGame.changeState(new ChangePictureState());
	}

	@Override
	public void exit(Game theGame) {
		// TODO Auto-generated method stub
	}

	@Override
	public void enter(Game theGame) {
		//清空输入状态
		theGame.setClickedFriends(false);
		theGame.setInputed(false);
		theGame.setInputFilterType(-1);
	}

}
