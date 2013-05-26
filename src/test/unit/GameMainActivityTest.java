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
		mButton_filter_grey = (Button) mActivity.findViewById(R.id.filter_grey);
		mButton_filter_old = (Button) mActivity.findViewById(R.id.filter_old);
		mButton_friend = (Button) mActivity.findViewById(R.id.friend);
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

	/*Add a UI test*/
	public void testGameMainUI(){
		mActivity.runOnUiThread(
				new Runnable(){
					public void run(){
						mActivity = getActivity();
						mTextView_number = (TextView) mActivity.findViewById(R.id.image_number);
						mButton_filter_grey.performClick();	
						String numberText = (String) mTextView_number.getText();
						assertEquals("No.2", numberText);
					}
				}		
		);
		
	}
}
