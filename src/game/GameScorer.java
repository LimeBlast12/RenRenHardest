package game;


public class GameScorer {
	
	private static int numOfMyImages;       				// 自己所有的头像
	private static int numOfMyFriendsImages;				// 好友所有的头像	
	private static int numOfRightImages;					// 猜对的头像张数
	private static int numOfWrongImages;					// 猜错的头像张数
	private static final int RIGHT_FACTOR = 10;				// 答对时相乘的系数
	private static final int WRONG_FACTOR = 5;				// 答错时相乘的系数
	private static int difficulty;
	private static final double FACTOR_SCORE_EASY = 2.3;		// 游戏难度为简单时相乘的系数 
	private static final double FACTOR_SCORE_NORMAL = 2.6;		// 游戏难度为一般时相乘的系数
	private static final double FACTOR_SCORE_HARD = 2.9;		// 游戏难度为困难时相乘的系数
	public static final int DIFFICULTY_SIMPLE = 0;
	public static final int DIFFICULTY_MIDDLE = 1;
	public static final int DIFFICULTY_HARD = 2;

	/*
	 * 根据游戏难度进行计分,供主线程调用获取游戏得分
	 */
	public static int score(int numOfMyImages1,
			int numOfMyFriendsImages1, int numOfRightImages1,
			int numOfWrongImages1, int difficulty1) {
		
		init(numOfMyImages1,numOfMyFriendsImages1, 
				numOfRightImages1,numOfWrongImages1,difficulty1);	
		
		switch(difficulty){
		
			case DIFFICULTY_SIMPLE:
				return easyScore();
				
			case DIFFICULTY_MIDDLE:
				return normalScore();
				
			case DIFFICULTY_HARD:
				return hardScore();
				
			default:
				return 0;
				
		}
		
	}
	
	private static void init(int numOfMyImages1,
			int numOfMyFriendsImages1, int numOfRightImages1,
			int numOfWrongImages1,int difficulty1) {
		
		numOfMyImages = numOfMyImages1;
		numOfMyFriendsImages = numOfMyFriendsImages1;
		numOfRightImages = numOfRightImages1;
		numOfWrongImages = numOfWrongImages1;
		
		switch(difficulty1){
		
			case 0:
				difficulty = DIFFICULTY_SIMPLE;
				break;
				
			case 1:
				difficulty = DIFFICULTY_MIDDLE;
				break;
				
			case 2:
				difficulty = DIFFICULTY_HARD;
				break;
				
			default:
				difficulty = DIFFICULTY_SIMPLE;
				break;
		}
		 
		
	}

	/*
	 * 游戏为难度时的计分方式
	 */
	private static int easyScore() {
		return (int)FACTOR_SCORE_EASY*(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}
	
	/*
	 * 游戏为一般时的计分方式
	 */
	private static int normalScore() {
		return (int)FACTOR_SCORE_NORMAL*(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}

	/*
	 * 游戏为困难时的计分方式
	 */
	private static int hardScore() {
		return  (int)FACTOR_SCORE_HARD*(numOfRightImages*RIGHT_FACTOR-numOfWrongImages*WRONG_FACTOR)*
				Math.min(30+30,numOfMyImages+numOfMyFriendsImages);
	}

	
}
