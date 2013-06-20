package game;

import imagefilter.BitmapFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.util.Log;

import model.FriendListModel;
import model.ImageDisplay;
import model.MyImagesModel;

/**
 * 
 * @author ZeroNing
 *
 */
public class PreProcessState extends State {
	List<Map<String, Object>> friendImages;	//好友头像，List中每个Item是一张Map：{'name':好友名字,'image':该好友头像url}
	List<Map<String, Object>> myImages;	//自己的头像，List中每个Item是一张Map：{'name':好友名字,'image':该好友头像url}
	List<ImageDisplay> images;	//最后选出来作为游戏用图的
	private int[] filterArray;
	private static final int FILTER_COUNT = 2;	//每次游戏用到的滤镜数量
	
	@Override
	public void execute(Game theGame) {
		Log.i("State", "PreProcessState");
		initFilter(theGame);		//根据难度准备好滤镜
		initPictureList(theGame);	//准备好随机的照片
		gotoChangePicState(theGame);
	}
	
	private void gotoChangePicState(Game theGame) {
		theGame.changeState(new ChangePictureState());
	}

	private void initFilter(Game theGame) {
		filterArray = new int[2];
		filterArray[0] = theGame.getLeftBtnFilterType();
		filterArray[1] = theGame.getRightBtnFilterType();
	}
	
	private void initPictureList(Game theGame){
		friendImages = FriendListModel.getInstance().getCurrentFriends();
		myImages = MyImagesModel.getInstance().getMyImages();
		randomSelectImage();
		theGame.setImageList(images);
		theGame.setMaxImageCount(images.size());
	}
	
	private void randomSelectImage(){
		int imageCount = friendImages.size()+myImages.size();
		List<Map<String, Object>> mixedImages = new ArrayList<Map<String,Object>>();
		mixedImages.addAll(myImages);
		mixedImages.addAll(friendImages);
		
		int[] randomIndexes = new int[imageCount]; 
        int intRd = 0;
        int count = 0;
        while(count<imageCount){
        	boolean reduplicate = false;
             Random rdm = new Random(System.currentTimeMillis());
             intRd = Math.abs(rdm.nextInt())%imageCount;
             for(int i=0;i<count;i++){
                 if(randomIndexes[i]==intRd){
                	 reduplicate = true;
                     break;
                 }
             }
             if(!reduplicate){
                 randomIndexes[count] = intRd;
                 count++;
             }
        }
        
        images = new ArrayList<ImageDisplay>();
        int boundary = myImages.size();
        Random rdm = new Random(System.currentTimeMillis());
		
        for(int i=0;i<imageCount;i++){
        	ImageDisplay tmp = new ImageDisplay();
        	if(randomIndexes[i]>=boundary){
        		tmp.setOwner(1);
        	}else{
        		tmp.setOwner(0);
        	}
        	tmp.setUrl((String) mixedImages.get(randomIndexes[i]).get("image"));
        	int result = Math.abs(rdm.nextInt())%FILTER_COUNT;
        	tmp.setFilter_type(filterArray[result]);
        	images.add(tmp);
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
