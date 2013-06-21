package game;

import model.GameStatusModel;
import model.SingleImageModel;
import imagefilter.BitmapFilter;
import helper.ImageDownloader;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * @author ZeroNing
 *
 */
public class ChangePictureState extends State{

	@Override
	public void execute(Game theGame) {
		Log.w("State", "ChangePictureState");
		theGame.increaseCurImgIndx();
		applyFilter(theGame);
		displayImage(theGame);
		gotoInputState(theGame);
	}
	
	private void gotoInputState(Game theGame){
		theGame.changeState(new InputState());
	}
	
	private void applyFilter(Game theGame){
		Bitmap oldBitmap = theGame.getCurrentBitmap();
		Bitmap newBitmap = BitmapFilter.changeStyle(oldBitmap, theGame.getCurrentFilterType());
//		String s = String.valueOf(theGame.getCurrentFilterType());
//		Log.i("Filter", s);
//		if(theGame.isCurrentMyImage()){
//			Log.i("Filter", "myImage");
//		}else{
//			Log.i("Filter", "friend's");
//		}
		theGame.setCurrentBitmap(newBitmap);
	}
	
	private void displayImage(Game theGame){
		SingleImageModel.getInstance().setImage(theGame.getCurrentBitmap());
	}

	@Override
	public void exit(Game theGame) {
		// TODO Auto-generated method stub
	}

	@Override
	public void enter(Game theGame) {
		if(!theGame.isStarted()){
			GameStatusModel.getInstance().notifyGameDataReady();
			theGame.startTimer();
			theGame.setStarted(true);
		}
	}

}
