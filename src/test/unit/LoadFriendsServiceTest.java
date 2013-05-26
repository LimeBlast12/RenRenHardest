package test.unit;

import service.LoadFriendsService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import com.renren.api.connect.android.PasswordFlowRequestParam;
import com.renren.api.connect.android.Renren;

public class LoadFriendsServiceTest extends ServiceTestCase<LoadFriendsService>{
	private String TAG = "Test LoadFriendsService";
	private Context mContext;
	private static final String API_KEY = "aa72e895b6a84941bb4ef31c8a69c179";
	private static final String SECRET_KEY = "322b904c74674d0ba333d5e4557dc7bf";
	private static final String APP_ID = "235450";
	private static final String USERNAME = "wannan91@163.com";
	private static final String PASSWORD = "101250171";
	private Renren renren;
	
	public LoadFriendsServiceTest() {
		super(LoadFriendsService.class);
	}
	
	protected void setUp() throws Exception {
		Log.i(TAG, "set up");
		mContext = this.getContext();
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, mContext);
		PasswordFlowRequestParam param = new PasswordFlowRequestParam(USERNAME, PASSWORD);
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
			Intent intent = new Intent(getSystemContext(), LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
			LoadFriendsService service = getService();
			assertNotNull(service);
			serviceIsRunning();
		} catch(Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testStop() {
		Log.i(TAG, "test stop");
		try {
			Intent intent = new Intent(getSystemContext(), LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
			LoadFriendsService service = getService();

			service.stopService(intent);
			serviceIsRunning();
		} catch(Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private boolean serviceIsRunning() {
		Context mContext = getContext();
		ActivityManager manager=(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	    	System.out.println(service.service.getClassName());
	        if ("service.LoadFriendsService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
