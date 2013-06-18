package view;


public class GameScorer {
	
	private static long numOfMyImages;
	private static long numOfMyFriendsImages;
	private static long numOfRightImages;
	private static long numOfWrongImages;
	private static final int EASY = 0;
	private static final int NORMAL = 1;
	private static final int HARD = 2;
	private static int gameLevel = EASY;
	private static final int RIGHT_FACTOR = 10;
	private static final int WRONG_FACTOR = 5;
	private static final int EASY_TIMES = 500;
	private static final int NORMAL_TIMES = 600;
	private static final int HARD_TIMES = 700;

	/*
	 * 供主线程调用获取游戏得分
	 */
	
	public static long getScores(long numOfMyImages,long numOfMyFriendsImages,
			long numOfRightImages,long numOfWrongImages,int gameLevel){
		
		setParaAndScore(numOfMyImages,numOfMyFriendsImages,numOfRightImages,numOfWrongImages,gameLevel);
		return 3000; // 先写死为3000，待处理
	}
	
	
	/*
	 * 设置计算游戏得分的各个参数
	 */
	private static void setParaAndScore(long numOfMyImages2,
			long numOfMyFriendsImages2, long numOfRightImages2,
			long numOfWrongImages2, int gameLevel2) {
		
		numOfMyImages = numOfMyImages2;
		numOfMyFriendsImages = numOfMyFriendsImages2;
		numOfRightImages = numOfRightImages2;
		numOfWrongImages = numOfWrongImages2;
		
		switch(gameLevel2){
			case 0:
				gameLevel = EASY;
				break;
			case 1:
				gameLevel = NORMAL;
				break;
			case 2:
				gameLevel = HARD;
				break;
				
		}
		score(gameLevel);
		
	}


	/*
	 * 根据游戏难度分别计分
	 */
	private static long score(int gameLevel){
	
		switch(gameLevel){
			case EASY:
				return easyScore();
			case NORMAL:
				return normalScore();
			case HARD:
				return hardScore();
			default:
				return 0;
		}
	}
	
	/*
	 * 游戏为难度时的计分方式
	 */
	private static long easyScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}
	
	/*
	 * 游戏为一般时的计分方式
	 */
	private static long normalScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}

	/*
	 * 游戏为困难时的计分方式
	 */
	private static long hardScore() {
		return (numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR);
	}

	
}
