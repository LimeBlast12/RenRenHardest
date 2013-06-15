package view;

import java.util.List;
import java.util.Map;

import model.FriendListModel;
import model.ModelListener;
import service.LoadFriendsService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;

import com.origamilabs.library.views.StaggeredGridView;
import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

@SuppressLint("NewApi")
public class RandomFriendsActivity extends Activity implements ModelListener, SensorEventListener {
	//Sensor管理器
    private SensorManager mSensorManager = null;
    //震动
    private Vibrator mVibrator = null;
    
	private ActivityHelper helper;
	private Renren renren;
	private FriendListModel friendListModel;
	/*使用handler来避免更新UI的线程安全问题*/
	private Handler handler = null;
	/*实际用于更新UI的线程*/
	private Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			showData(friendListModel.getRandomFriends(30));
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		
		helper = ActivityHelper.getInstance();
		handler = new Handler();
		
		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		} else {
			renren = helper.getRenren();
		}	
		
		setContentView(R.layout.staggered_grid_view);
		
		friendListModel = FriendListModel.getInstance();
        friendListModel.register(RandomFriendsActivity.this);
		loadFriends();
	}
	
	@Override
	public void onStop() {
		Log.i("RandomFriendsActivity", "stop");
		helper.dismissProgress();	
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onPause() {
		Log.i("RandomFriendsActivity", "pause");
		helper.dismissProgress();
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		Log.i("RandomFriendsActivity", "destroy");
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		helper.dismissProgress();
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("RandomFriendsActivity", "resume");
		if (mSensorManager != null) {// 注册监听器 
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); 
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率 
        } 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(RandomFriendsActivity.this,
					LoginedMainActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showData(List<Map<String, Object>> data) {
		Log.i("RandomFriendsActivity", "show data");
		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin);
		gridView.setPadding(margin, 0, margin, 0);
	
		MyStaggeredAdapter adapter = new MyStaggeredAdapter(
				RandomFriendsActivity.this, data, R.layout.grid_item,
				new String[] { "name", "image" }, new int[] { R.id.ItemText,
						R.id.ItemImage });

		gridView.setAdapter(adapter);
		helper.dismissProgress();
		adapter.notifyDataSetChanged();   
	}

	private void loadFriends() {
		if (friendListModel.isDone()) {//已有数据
			startUpdateUiThread();
		} else {
			Intent intent = new Intent(RandomFriendsActivity.this, LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
		}
	}

	/*当FriendListModel的数据更新时调用*/
	@Override
	public void doSomething() {
		Log.i("RandomFriendsActivity", "do something");
		if (friendListModel.isDone()) {
			startUpdateUiThread();
		} else {
			Log.i("RandomFriendsActivity", "friend list is null");
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (isShaking(event)) {
			Log.i("RandomFriendsActivity", "is shaking");
			//helper.showTip(this, "shaking");
			startUpdateUiThread();
		}		
	}
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	private boolean isShaking(SensorEvent event) {
		float[] values = event.values; 
        float x = values[0]; // x轴方向的重力加速度，向右为正 
        float y = values[1]; // y轴方向的重力加速度，向前为正 
        float z = values[2]; // z轴方向的重力加速度，向上为正  
        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。 
        int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了 
        if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) { 
            mVibrator.vibrate(100); //使手机震动
            return true; 
        } 
        
        return false;
	}
	
	private void startUpdateUiThread() {		
		new Thread(){  
            public void run(){          
            	/*触发更新UI的线程启动*/
                handler.post(runnableUi);   
            }                     
        }.start();   
	}

}