package service;

import helper.NetworkChecker;
import helper.ScreenShot;

import java.io.File;

import view.ActivityHelper;
import view.GameOverActivity;

import model.GameStatusModel;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;

public class UploadScoreService extends Service {
	private final IBinder binder = new MyBinder();
	private NetworkChecker networkChecker = new NetworkChecker(
			UploadScoreService.this);
	private final String TAG = "UploadScoreService";

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		//networkChecker.init(this);
	}

	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startedId) {
		Log.i(TAG, "onStartCommand");
		//networkChecker.checkAndShowTip(this, intent);
		uploadScore(intent);
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		uploadScore(intent);
		return binder;
	}

	public void uploadScore(Intent intent) {
		Log.i("uploadScore", "begin");
		final Renren renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		final File file = ScreenShot.getFile();
		if (renren != null && file != null) {
			Log.i("uploadScore", "renren and picture is not null");
			PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();
			photoParam.setFile(file);
			photoParam.setCaption("from RenRenHardest");			
			// 调用SDK异步上传照片的接口
			new AsyncRenren(renren).publishPhoto(photoParam,
					new AbstractRequestListener<PhotoUploadResponseBean>() {
						@Override
						public void onRenrenError(RenrenError renrenError) {
							if (renrenError != null) {
								Log.i("upload error",renrenError.getMessage());
								// Message message = new Message();
								// Bundle bundle = new Bundle();
								// bundle.putString(ERROR_LABEL,
								// renrenError.getMessage());
								// message.what = DATA_ERROR;
								// message.setData(bundle);
								// handler.sendMessage(message);
							}
						}

						@Override
						public void onFault(Throwable fault) {
							if (fault != null) {
								Log.i("upload error",fault.getMessage());
								// Message message = new Message();
								// Bundle bundle = new Bundle();
								// bundle.putString(ERROR_LABEL,
								// fault.getMessage());
								// message.what = DATA_FAULT;
								// message.setData(bundle);
								// handler.sendMessage(message);
							}
						}

						@Override
						public void onComplete(PhotoUploadResponseBean bean) {
							if (bean != null) {
								Log.i("uploadScore", "finish");
								GameStatusModel.getInstance().notifyUploadFinish();
							}
						}
					});
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
	public static void stopservice(Context c) {
		Intent iService = new Intent(c, LoadFriendsService.class);
		iService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.stopService(iService);
	}

	public class MyBinder extends Binder {
		public UploadScoreService getService() {
			return UploadScoreService.this;
		}
	}
}
