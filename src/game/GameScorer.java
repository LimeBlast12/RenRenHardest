package game;


public class GameScorer {
	
	private static long numOfMyImages;       				// 自己所有的头像
	private static long numOfMyFriendsImages;				// 好友所有的头像	
	private static long numOfRightImages;					// 猜对的头像张数
	private static long numOfWrongImages;					// 猜错的头像张数
	private static final int RIGHT_FACTOR = 10;				// 答对时相乘的系数
	private static final int WRONG_FACTOR = 5;				// 答错时相乘的系数
	private static final int BASE_SCORE_EASY = 1000;		// 游戏难度为简单时基础得分 
	private static final int BASE_SCORE_NORMAL = 1500;		// 游戏难度为一般时基础得分
	private static final int BASE_SCORE_HARD = 2000;		// 游戏难度为困难时基础得分


	/*
	 * 根据游戏难度进行计分,供主线程调用获取游戏得分
	 */
	public static long score(long numOfMyImages1,
			long numOfMyFriendsImages1, long numOfRightImages1,
			long numOfWrongImages1, int difficulty) {
		
		numOfMyImages = numOfMyImages1;
		numOfMyFriendsImages = numOfMyFriendsImages1;
		numOfRightImages = numOfRightImages1;
		numOfWrongImages = numOfWrongImages1;
		
		switch(difficulty){
			case 0:
				return easyScore();
			case 1:
				return normalScore();
			case 2:
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
