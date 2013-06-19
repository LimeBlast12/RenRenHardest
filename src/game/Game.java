package game;

import imagefilter.BitmapFilter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.ImageDisplay;
import android.graphics.Bitmap;

/**
 * 代表游戏的类，封装了当前游戏的一些状态信息，以及与其他部分通信需要用到的某些引用
 * @author ZeroNing
 *
 */
public class Game {
	public static final int DIFFICULTY_SIMPLE = 1;
	public static final int DIFFICULTY_MIDDLE = 2;
	public static final int DIFFICULTY_HARD = 3;
	
	private static Game instance=null;
	private StateMachine stateMachine;
	private MyThread theThread;
	
	/*游戏状态信息*/
	private int timeLeft;	//剩余时间
	private int difficulty = DIFFICULTY_SIMPLE;		//游戏难度级别,初始值为简单
	private int totalPic_own;	//自己的头像数量
	private int totalPic_friends;	//好友头像数量
	private int rightPic_own;		//答对的自己头像数
	private int rightPic_friends;	//答对的好友头像数
	private int currentImageIndx;	//当前是第几张图了
	private int maxImageCount;		//游戏中最多有多少张图（自己的&好友的）
	
	private boolean inputed;	//这一轮用户是否已经输入
	private boolean clickedFriends;	//用户是否按了“好友”按钮
	private int leftBtnFilterType;	//左边按钮对应的滤镜
	private int rightBtnFilterType;	//右边按钮对应的滤镜
	
	private Bitmap currentBitmap;
	
	private List<ImageDisplay> imageList;
	
	TimerTask updateTimeTask = new TimerTask() {  
        @Override
        public void run(){
        	updateTime();
        }
    };
	
	private Game() {
		stateMachine = new StateMachine(this);
		leftBtnFilterType = BitmapFilter.ECLOSION_STYLE;	//给一个默认值
		rightBtnFilterType = BitmapFilter.OIL_STYLE;
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
	
	/**
	 * 若用户选择了滤镜，则此方法会被界面调用
	 * @param index	滤镜对应的数值
	 */
	public void pickFilter(int index) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 若用户点击了“好友”按钮，则此方法会被界面调用
	 */
	public void pickFriend() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获取当前难度下使用的滤镜
	 * @param index	0代表左边按钮的滤镜，1代表右边按钮的滤镜
	 * @return	滤镜对应的数值
	 */
	public int getUsingFilter(int index) {
		// TODO Auto-generated method stub
		return 0;
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

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getLeftBtnFilterType() {
		return leftBtnFilterType;
	}

	public void setLeftBtnFilterType(int leftBtnFilterType) {
		this.leftBtnFilterType = leftBtnFilterType;
	}

	public int getRightBtnFilterType() {
		return rightBtnFilterType;
	}

	public void setRightBtnFilterType(int rightBtnFilterType) {
		this.rightBtnFilterType = rightBtnFilterType;
	}

	public boolean isInputed() {
		return inputed;
	}

	public void setInputed(boolean inputed) {
		this.inputed = inputed;
	}

	public boolean isClickedFriends() {
		return clickedFriends;
	}

	public void setClickedFriends(boolean clickedFriends) {
		this.clickedFriends = clickedFriends;
	}
}
