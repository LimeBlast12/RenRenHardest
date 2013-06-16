package helper;

import java.util.Timer;
import java.util.TimerTask;

import service.LoadFriendsService;
import service.LoadMyImagesService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class NetworkChecker {

	private Context _context;

	private boolean isTaskSheduled = false;
	private Handler handler;
	private Timer timer;
	public Timer getTimer() {
		return timer;
	}
	private final static long TASK_START_TIME = 0;
	private final static long TASK_INTERVAl_TIME = 10000;

	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (!isConnectingToInternet()) {
						Toast.makeText(_context.getApplicationContext(),
								"哎呀，网络有点不给力喔...", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	};
	
	public NetworkChecker(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
	
	public void init(Service service){
		handler = new Handler(Looper.getMainLooper()); // 为当前线程获得Looper
		if(service instanceof LoadFriendsService ){
			
			timer = new Timer("loadfriendsservice"); // 当前线程名为loadfriendsservice
		}
		if(service instanceof LoadMyImagesService ){
			
			timer = new Timer("loadmyimagesservice"); // 当前线程名为loadmyimagesservice
		}
	
	}
	public void checkAndShowTip(Service service,Intent intent){
		
		if (isTaskSheduled == false) {
			timer.scheduleAtFixedRate(task, TASK_START_TIME, TASK_INTERVAl_TIME); // 0秒后,每个10000ms启动计时器
			isTaskSheduled = true;
		}
		if (isConnectingToInternet()) {
			Log.i("NetworkChecker", "network able");
			
			if(service instanceof LoadFriendsService ){
				LoadFriendsService lfs = (LoadFriendsService) service;
				lfs.loadFriends(intent);
			}else if(service instanceof LoadMyImagesService ){
				LoadMyImagesService lfs = (LoadMyImagesService) service;
				lfs.loadMyAlbums(intent);
			}else{
				Log.i("NetworkChecker", "service incompatible");
			}
			
		} else {
			Log.i("NetworkChecker", "network unable");
		}
	}
}