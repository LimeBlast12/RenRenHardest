package game;

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
		downloadImage(theGame);
		applyFilter(theGame);
		displayImage(theGame);
		gotoInputState(theGame);
	}
	
	private void gotoInputState(Game theGame){
		theGame.changeState(new InputState());
	}
	
	private void downloadImage(Game theGame){
		Bitmap newBitmap = ImageDownloader.downloadBitmap(theGame.getCurrentImageUrl());
		theGame.setCurrentBitmap(newBitmap);
	}
	
	private void applyFilter(Game theGame){
		Bitmap oldBitmap = theGame.getCurrentBitmap();
		Bitmap newBitmap = BitmapFilter.changeStyle(oldBitmap, theGame.getCurrentFilterType());
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
		// TODO Auto-generated method stub
	}

}
