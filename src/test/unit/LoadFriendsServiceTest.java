package test.unit;

import model.FriendListModel;
import service.LoadFriendsService;
import android.content.Context;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import com.renren.api.connect.android.PasswordFlowRequestParam;
import com.renren.api.connect.android.Renren;

public class LoadFriendsServiceTest extends ServiceTestCase<LoadFriendsService> {
	private String TAG = "Test LoadFriendsService";
	private Context mContext;
	private static final String API_KEY = "aa72e895b6a84941bb4ef31c8a69c179";
	private static final String SECRET_KEY = "322b904c74674d0ba333d5e4557dc7bf";
	private static final String APP_ID = "235450";
	private static final String USERNAME = "wannan91@163.com";
	private static final String PASSWORD = "101250171";
	private Renren renren;
	private boolean started = false;

	public LoadFriendsServiceTest() {
		super(LoadFriendsService.class);
	}

	protected void setUp() throws Exception {
		Log.i(TAG, "set up");
		mContext = this.getContext();
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, mContext);
		PasswordFlowRequestParam param = new PasswordFlowRequestParam(USERNAME,
				PASSWORD);
		try {
			renren.authorize(param);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.setUp();
	}

	protected void tearDown() {
		Log.i(TAG, "tear down");
		try {
			super.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试LoadFriendsService的启动
	 * 
	 */
	public void testStart() {
		Log.i(TAG, "test start");
		try {
			Intent intent = new Intent(getSystemContext(),
					LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
			LoadFriendsService service = getService();
			assertNotNull(service);
			started = true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * 测试LoadFriendsService的任务完成情况：</br>
	 * 十秒内会得到数据
	 * 
	 */
	public void testLoadFriends() {
		Log.i(TAG, "test load friends");
		try {
			if (!started) {//如果testStart先执行则不需要再启动
				Intent intent = new Intent(getSystemContext(),
						LoadFriendsService.class);
				intent.putExtra(Renren.RENREN_LABEL, renren);
				startService(intent);			
			}
			
			final FriendListModel model = FriendListModel.getInstance();
			
			new Thread() {
				@Override
				public void run() {
					int times = 10;
					while (true) {
						if (times < 0) {
							break;
						}
						
						if (model.isDone()) {
							assertNotNull(model.getAllFriends());
							break;
						}
						
						times--;
						
						try {
							sleep(1000);
						} catch (Exception ex) {

						}											
					}
				}
			}.start();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
