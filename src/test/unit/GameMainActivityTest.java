package test.unit;

import edu.nju.renrenhardest.R;
import view.GameMainActivity;
import android.annotation.SuppressLint;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

public class GameMainActivityTest extends
		ActivityInstrumentationTestCase2<GameMainActivity> {

	private GameMainActivity mActivity;
	private TextView mTextView_number;
	private TextView mTextView_time;
	private Button mButton_filter_grey;
	private Button mButton_filter_old;
	private Button mButton_friend;
	
	@SuppressLint("NewApi")
	public GameMainActivityTest() {
		super(GameMainActivity.class);
	}

	@SuppressLint("NewApi")
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mTextView_number = (TextView) mActivity.findViewById(R.id.image_number);
		mTextView_time = (TextView) mActivity.findViewById(R.id.time_left);
		mButton_filter_grey = (Button) mActivity.findViewById(R.id.filter_button0);
		mButton_filter_old = (Button) mActivity.findViewById(R.id.filter_button1);
		mButton_friend = (Button) mActivity.findViewById(R.id.game_friend_button);
	}

	@SuppressLint("NewApi")
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	/*Add an initial conditions test*/
	public void testPreConditions(){
		assertNotNull(mActivity);
		assertNotNull(mTextView_number);
		assertNotNull(mTextView_time);
		assertNotNull(mButton_filter_grey);
		assertNotNull(mButton_filter_old);
		assertNotNull(mButton_friend);
	}

	/*testGameMainUI单独测试时正确，但与其他测试用例一起测会出错，很奇怪。*/
	/*Add a UI test*/
//	public void testGameMainUI(){
//		mActivity.runOnUiThread(
//				new Runnable(){
//					public void run(){
//						mButton_filter_grey.performClick();	
//						String numberText = (String) mTextView_number.getText();
//						assertEquals("No.2", numberText);
//					}
//				}		
//		);	
//	}
	
	@SuppressLint("NewApi")
	public void testStateDestroy(){
		mActivity.finish();
		mActivity = this.getActivity();
		mTextView_time = (TextView) mActivity.findViewById(R.id.time_left);
		String time_left = (String) mTextView_time.getText();
		assertEquals("10", time_left);
	}
}
