package view;

import java.lang.reflect.Field;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.origamilabs.library.views.StaggeredGridView;
import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;
import game.Game;

@SuppressLint("NewApi")
public class RandomFriendsActivity extends Activity implements ModelListener,
		SensorEventListener {
	private final int COUNT_FRIENDS = 30;// 一组好友个数
	private SensorManager mSensorManager = null; // Sensor管理器
	private Vibrator mVibrator = null; // 震动
	private ActivityHelper helper;
	private Renren renren;
	private FriendListModel friendListModel;
	private Handler handler = null; // 使用handler来避免更新UI的线程安全问题
	/* 实际用于更新UI的线程 */
	private Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			showData(friendListModel.getRandomFriends(COUNT_FRIENDS));
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		handler = new Handler();

		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		} else {
			renren = helper.getRenren();
		}

		setContentView(R.layout.random_friends_layout);
		getOverflowMenu();

		friendListModel = FriendListModel.getInstance();
		friendListModel.register(RandomFriendsActivity.this);
		loadFriends();
		
		helper.showLongTip(this, R.string.shake_tips);
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
		friendListModel.remove(RandomFriendsActivity.this);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("RandomFriendsActivity", "resume");
		if (mSensorManager != null) {// 注册监听器
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
		}
	}

	/**
	 * 启动游戏界面
	 */
	private void startGameMainActivity() {
		Intent intent = new Intent(this, GameMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}

	private void showData(List<Map<String, Object>> data) {
		Log.i("RandomFriendsActivity", "show data");
		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView2);

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
		if (friendListModel.isDone()) {// 已有数据
			startUpdateUiThread();
		} else {
			Intent intent = new Intent(RandomFriendsActivity.this,
					LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
		}
	}

	/* 当FriendListModel的数据更新时调用 */
	@Override
	public void doSomething() {
		Log.i("RandomFriendsActivity", "do something");
		if (friendListModel.isDone()) {
			startUpdateUiThread();
		} else {
			Log.i("RandomFriendsActivity", "friend list is null");
		}
	}
	
	/*无论何种机型都显示overflow*/
	@SuppressLint("NewApi")
	private void getOverflowMenu() {

        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

	/* 定义Action Bar的菜单项 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_prepare_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start_game:
			Game game = Game.getInstance();
			game.start();//开始游戏
			startGameMainActivity();//跳转到游戏界面
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
			// helper.showTip(this, "shaking");
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
		if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
				|| Math.abs(z) > medumValue) {
			mVibrator.vibrate(100); // 使手机震动
			return true;
		}

		return false;
	}

	private void startUpdateUiThread() {
		new Thread() {
			public void run() {
				/* 触发更新UI的线程启动 */
				handler.post(runnableUi);
			}
		}.start();
	}

}
