package service;

import helper.NetworkCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import model.MyImagesModel;
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
import com.renren.api.connect.android.photos.AlbumBean;
import com.renren.api.connect.android.photos.AlbumGetRequestParam;
import com.renren.api.connect.android.photos.AlbumGetResponseBean;
import com.renren.api.connect.android.photos.PhotoBean;
import com.renren.api.connect.android.photos.PhotoGetRequestParam;
import com.renren.api.connect.android.photos.PhotoGetResponseBean;

public class LoadMyImagesService extends Service {
	private final IBinder binder = new MyBinder();

	private NetworkCheck networkCheck = new NetworkCheck(LoadMyImagesService.this);
	private Handler handler;
	private boolean isTaskSheduled = false;
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
					
				}});
		}
		
	};
	
	
	@Override
	public void onCreate() {
		Log.i("LoadMyImagesService", "onCreate");
		super.onCreate();
		handler = new Handler(Looper.getMainLooper()); // 为当前线程获得Looper
		timer = new Timer("loadmyimagesservice");	//当前线程名为loadmyimagesservice
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LoadMyImagesService", "onStartCommand");
		if(isTaskSheduled==false){
			timer.scheduleAtFixedRate(task,TASK_START_TIME,TASK_INTERVAl_TIME);  //0秒后,每个10000ms启动计时器
			isTaskSheduled=true;
		}
		if(networkCheck.isConnectingToInternet())
			loadMyAlbums(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("LoadMyImagesService", "onBind");
		return binder;
	}

	public class MyBinder extends Binder {
		public LoadMyImagesService getService() {
			return LoadMyImagesService.this;
		}
	}

	private void loadMyAlbums(Intent intent) {
		final Renren renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		Log.i("loadMyImages", "begin");
		if (renren != null) {
			Log.i("loadMyImages", "renren is not null");
			AlbumGetRequestParam albumGetRequestParam = new AlbumGetRequestParam();
			albumGetRequestParam.setUid(renren.getCurrentUid());

			// 调用SDK异步接口获取相册
			new AsyncRenren(renren).getAlbums(albumGetRequestParam,
					new AbstractRequestListener<AlbumGetResponseBean>() {
						@Override
						public void onComplete(final AlbumGetResponseBean bean) {
							Log.i("MyImagesService", "complete");
							List<AlbumBean> albumBean = bean.getAlbums();

							/* 先找到头像相册的aid */
							for (int i = 0; i < albumBean.size(); i++) {
								if (albumBean.get(i).getName().equals("头像相册")) {
									long aid = albumBean.get(i).getAid();
									long uid = renren.getCurrentUid();
									loadMyImages(uid, aid, renren);
								}

							}
						}

						@Override
						public void onRenrenError(RenrenError renrenError) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onFault(Throwable fault) {
							// TODO Auto-generated method stub
						}
					});
		}

	}

	public void loadMyImages(long uid, long aid, Renren renren) {
		PhotoGetRequestParam photoGetRequestParam = new PhotoGetRequestParam();
		photoGetRequestParam.setUid(uid);
		photoGetRequestParam.setAid(aid);
		photoGetRequestParam.setCount(50);

		// 调用SDK异步接口获取相册
		new AsyncRenren(renren).getPhotos(photoGetRequestParam,
				new AbstractRequestListener<PhotoGetResponseBean>() {
					@Override
					public void onComplete(final PhotoGetResponseBean bean) {
						// 查询完成，结束进度框，然后显示结果
						/* 这里处理PhotoGetResponseBean */
						System.out.println("这里处理PhotoGetResponseBean");
						List<PhotoBean> photoBean = bean.getPhotos();
						String urls[] = new String[bean.getPhotos().size()];
						/* 将头像相册中的所有照片url加入urls */
						for (int i = 0; i < photoBean.size(); i++) {
							urls[i] = photoBean.get(i).getUrlHead();
						}

						setMyImages(urls);
					}

					@Override
					public void onRenrenError(final RenrenError renrenError) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFault(final Throwable fault) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void setMyImages(String[] urls) {
		List<Map<String, Object>> myImages = new ArrayList<Map<String, Object>>();

		for (String url : urls) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", url);
			myImages.add(map);
		}

		MyImagesModel model = MyImagesModel.getInstance();
		model.setMyImages(myImages);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}
}
