package game;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;

import model.ImageDisplay;

/**
 * 代表游戏的类，封装了当前游戏的一些状态信息，以及与其他部分通信需要用到的某些引用
 * @author ZeroNing
 *
 */
public class Game {
	private static Game instance=null;
	private StateMachine stateMachine;
	private MyThread theThread;
	
	/*游戏状态信息*/
	private int timeLeft;	//剩余时间
	private int level;		//游戏难度级别
	private int totalPic_own;	//自己的头像数量
	private int totalPic_friends;	//好友头像数量
	private int rightPic_own;		//答对的自己头像数
	private int rightPic_friends;	//答对的好友头像数
	private int currentImageIndx;	//当前是第几张图了
	private int maxImageCount;		//游戏中最多有多少张图（自己的&好友的）
	
	private Bitmap currentBitmap;
	
	private List<ImageDisplay> imageList;
	
	/*游戏其他部分的引用*/
	TimerTask updateTimeTask = new TimerTask() {  
        @Override
        public void run(){
        	updateTime();
        }
    };
	
	private Game() {
		stateMachine = new StateMachine(this);
	}
	
	public static Game getInstance(){
		if(instance==null){
			instance = new Game();
		}
		return instance;
	}
	
	public void start(){
		initGameState();
		startTimeUpdate();
		startStateMachine();
	}
	
	private void initGameState(){
		this.setTimeLeft(10);	//10秒钟一局
		this.setRightPic_own(0);
		this.setRightPic_friends(0);
		this.setCurrentImageIndx(0);
	}
	
	private void startTimeUpdate(){
		Timer timer = new Timer();
		timer.schedule(updateTimeTask, 1000, 1000);
	}
	
	public void changeState(State newState){
		stateMachine.changeState(newState);
	}
	
	/**
	 * 更新时间，并更新界面显示
	 */
	private void updateTime(){
		timeLeft--;
		// TODO 更新界面
	}
	
	private void startStateMachine(){
		if(stateMachine==null){
			stateMachine = new StateMachine(this);
		}
		
		stateMachine.setCurrentState(new PreProcessState());
		stateMachine.setGlobalState(new CheckTimeState());
		
		theThread = new MyThread();
		theThread.start();
	}
	
	public class MyThread extends Thread {
		@Override
		public void run() {
			stateMachine.update();
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public int getTotalPic_own() {
		return totalPic_own;
	}

	public void setTotalPic_own(int totalPic_own) {
		this.totalPic_own = totalPic_own;
	}

	public int getTotalPic_friends() {
		return totalPic_friends;
	}

	public void setTotalPic_friends(int totalPic_friends) {
		this.totalPic_friends = totalPic_friends;
	}

	public int getRightPic_friends() {
		return rightPic_friends;
	}

	public void setRightPic_friends(int rightPic_friends) {
		this.rightPic_friends = rightPic_friends;
	}

	public int getRightPic_own() {
		return rightPic_own;
	}

	public void setRightPic_own(int rightPic_own) {
		this.rightPic_own = rightPic_own;
	}

	public MyThread getTheThread() {
		return theThread;
	}

	public void setTheThread(MyThread theThread) {
		this.theThread = theThread;
	}

	public List<ImageDisplay> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageDisplay> imageList) {
		this.imageList = imageList;
	}

	public int getCurrentImageIndx() {
		return currentImageIndx;
	}

	public void setCurrentImageIndx(int currentImageIndx) {
		this.currentImageIndx = currentImageIndx;
	}

	public Bitmap getCurrentBitmap() {
		return currentBitmap;
	}

	public void setCurrentBitmap(Bitmap currentBitmap) {
		this.currentBitmap = currentBitmap;
	}

	public int getMaxImageCount() {
		return maxImageCount;
	}

	public void setMaxImageCount(int maxImageCount) {
		this.maxImageCount = maxImageCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
