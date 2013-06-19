package service;

import helper.NetworkChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import model.FriendListModel;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean.Friend;

public class LoadFriendsService extends Service {
	private final IBinder binder = new MyBinder();
	private NetworkChecker networkChecker = new NetworkChecker(
			LoadFriendsService.this);

	
	@Override
	public void onCreate() {
		Log.i("LoadFriendsService", "onCreate");
		super.onCreate();
		networkChecker.init(this);
	}

	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startedId) {
		Log.i("LoadFriendsService", "onStartCommand");
		networkChecker.checkAndShowTip(this,intent);
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("LoadFriendsService", "onBind");
		loadFriends(intent);
		return binder;
	}

	public void loadFriends(Intent intent) {
		Renren renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		Log.i("loadFriends", "begin");
		if (renren != null) {
			Log.i("loadFriends", "renren is not null");
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			FriendsGetFriendsRequestParam param = new FriendsGetFriendsRequestParam();
			AbstractRequestListener<FriendsGetFriendsResponseBean> listener = new AbstractRequestListener<FriendsGetFriendsResponseBean>() {

				@Override
				public void onComplete(final FriendsGetFriendsResponseBean bean) {
					Log.i("FriendListService", "complete");
					setData(bean.getFriendList());
					stopSelf();
				}

				@Override
				public void onRenrenError(RenrenError renrenError) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onFault(Throwable fault) {
					// TODO Auto-generated method stub
				}

			};
			asyncRenren.getFriends(param, listener);
		}

	}

	private void setData(List<Friend> friendList) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		for (Friend friend : friendList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", friend.getName());
			map.put("image", friend.getHeadurl());
			map.put("uid", friend.getUid());
			data.add(map);
		}

		FriendListModel model = FriendListModel.getInstance();
		model.setFriendList(data);
	}

	public class MyBinder extends Binder {
		public LoadFriendsService getService() {
			return LoadFriendsService.this;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		networkChecker.getTimer().cancel();
	}
	
	/*
	 * 停止Service
	 */
	public static void stopservice(Context c){
        Intent iService=new Intent(c,LoadFriendsService.class);
        iService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.stopService(iService);
    }
}
