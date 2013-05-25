package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean.Friend;

public class LoadFriendsService extends Service {
	private List<Map<String, Object>> data;
	private boolean isDone = false;
	private final IBinder binder = new MyBinder();
	
	@Override
	public void onCreate() {
		Log.i("LoadFriendsService", "onCreate");
		super.onCreate();
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startedId) {
		Log.i("LoadFriendsService", "onStartCommand");
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("LoadFriendsService", "onBind");
		loadFriends(intent);		
		return binder;
	}
	
	public void setData(List<Friend> friendList) {
		data = new ArrayList<Map<String, Object>>();
        
		for (Friend friend: friendList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", friend.getName());	    
	        map.put("image", friend.getHeadurl());
	        map.put("uid", friend.getUid());
	        data.add(map);
		}
	}
	
	public List<Map<String, Object>> getFriendList() {
		return data;
	}
	
	public boolean isDone() {
		return isDone;
	}

	public class MyBinder extends Binder {
		public LoadFriendsService getService() {
			return LoadFriendsService.this;
		}
	}
	
	private void loadFriends(Intent intent) {
		Renren renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		Log.i("loadFriends", "begin");
		if (renren != null) {
			Log.i("loadFriends", "renren is not null");
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			//helper.showWaitingDialog(this);
			FriendsGetFriendsRequestParam param = new FriendsGetFriendsRequestParam();
			AbstractRequestListener<FriendsGetFriendsResponseBean> listener = new AbstractRequestListener<FriendsGetFriendsResponseBean>() {

				@Override
				public void onComplete(final FriendsGetFriendsResponseBean bean) {
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							helper.dismissProgress();
//							setData(bean.getFriendList());	
//							isDone = true;
//							display();
//						}
//					});
					//helper.dismissProgress();
					setData(bean.getFriendList());	
					isDone = true;
				}

				@Override
				public void onRenrenError(final RenrenError renrenError) {
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							helper.dismissProgress();
//						}
//					});
				}

				@Override
				public void onFault(final Throwable fault) {
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							helper.dismissProgress();
//						}
//					});
				}
			};
			asyncRenren.getFriends(param, listener);	
		}
		
	}

}