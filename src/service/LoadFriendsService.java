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
	private NetworkChecker networkCheck  = new NetworkChecker(LoadFriendsService.this);
	
	private boolean isTaskSheduled = false;
    private Handler handler;
	private Timer timer;
	private final static long TASK_START_TIME = 0;
	private final static long TASK_INTERVAl_TIME = 10000;
	private TimerTask task = new TimerTask(){
		
		@Override
		public void run() {
			handler.post(new Runnable(){

				@Override
				public void run() {
					if(!networkCheck.isConnectingToInternet()){
						Toast.makeText(getApplicationContext(),"No network", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
		}
		
	};
	
	@Override
	public void onCreate() {
		Log.i("LoadFriendsService", "onCreate");
		super.onCreate();
		handler = new Handler(Looper.getMainLooper()); // 为当前线程获得Looper
		timer = new Timer("loadfriendsservice");   // 当前线程名为loadfriendsservice
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startedId) {
		Log.i("LoadFriendsService", "onStartCommand");
		if(isTaskSheduled==false){
			timer.scheduleAtFixedRate(task,TASK_START_TIME,TASK_INTERVAl_TIME);  //0秒后,每个10000ms启动计时器
			isTaskSheduled=true;
		}
		if(networkCheck.isConnectingToInternet())
			loadFriends(intent);
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("LoadFriendsService", "onBind");
		loadFriends(intent);		
		return binder;
	}
	
	private void loadFriends(Intent intent) {
		Renren renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		Log.i("loadFriends", "begin");
		if (renren != null) {
			Log.i("loadFriends", "renren is not null");
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			FriendsGetFriendsRequestParam param = new FriendsGetFriendsRequestParam();
			AbstractRequestListener<FriendsGetFriendsResponseBean> listener = new AbstractRequestListener<FriendsGetFriendsResponseBean>() {

				@Override
				public void onComplete(final FriendsGetFriendsResponseBean bean) {
					Log.i("FriendListService","complete");
					setData(bean.getFriendList());				
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
		List<Map<String, Object>>data = new ArrayList<Map<String, Object>>();
        
		for (Friend friend: friendList) {
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
		timer.cancel();
	}
}
