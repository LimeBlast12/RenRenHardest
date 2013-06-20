package game;


public class GameScorer {
	
	private static long numOfMyImages;       				// 自己所有的头像
	private static long numOfMyFriendsImages;				// 好友所有的头像	
	private static long numOfRightImages;					// 猜对的头像张数
	private static long numOfWrongImages;					// 猜错的头像张数
	private static final int EASY = 0;						// 游戏难度为简单
	private static final int NORMAL = 1;					// 游戏难度为一般
	private static final int HARD = 2;						// 游戏难度为困难
	private static int gameLevel = EASY;					// 游戏的难度级别
	private static final int RIGHT_FACTOR = 10;				// 答对时相乘的系数
	private static final int WRONG_FACTOR = 5;				// 答错时相乘的系数
	private static final int BASE_SCORE_EASY = 1000;		// 游戏难度为简单时基础得分 
	private static final int BASE_SCORE_NORMAL = 1500;		// 游戏难度为一般时基础得分
	private static final int BASE_SCORE_HARD = 2000;		// 游戏难度为困难时基础得分
	private static long myScore;							// 我的得分

	/*
	 * 供主线程调用获取游戏得分
	 */
	
	public static long getScores(long numOfMyImages,long numOfMyFriendsImages,
			long numOfRightImages,long numOfWrongImages,int gameLevel){
		
		setPara(numOfMyImages,numOfMyFriendsImages,numOfRightImages,numOfWrongImages,gameLevel);
		return score(gameLevel);
	}
	
	
	/*
	 * 设置计算游戏得分的各个参数
	 */
	private static void setPara(long numOfMyImages2,
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
		return BASE_SCORE_EASY+(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}
	
	/*
	 * 游戏为一般时的计分方式
	 */
	private static long normalScore() {
		return BASE_SCORE_NORMAL+(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}

	/*
	 * 游戏为困难时的计分方式
	 */
	private static long hardScore() {
		return  BASE_SCORE_HARD+(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}

	
}
