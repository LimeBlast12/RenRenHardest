package view;

import java.util.List;
import java.util.Map;

import model.FriendListModel;
import model.ModelListener;
import service.LoadFriendsService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.origamilabs.library.views.StaggeredGridView;
import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class AllFriendsActivity extends Activity implements ModelListener {
	private ActivityHelper helper;
	private Renren renren;
	private FriendListModel friendListModel;
	/*使用handler来避免更新UI的线程安全问题*/
	private Handler handler = null;
	/*实际用于更新UI的线程*/
	private Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			showData(friendListModel.getAllFriends());
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		} else {
			renren = helper.getRenren();
		}	
		setContentView(R.layout.staggered_grid_view);
		handler = new Handler();
		friendListModel = FriendListModel.getInstance();
        friendListModel.register(AllFriendsActivity.this);
		loadFriends();
	}
	
	@Override
	public void onStop() {
		Log.i("FriendListActivity", "stop");
		helper.dismissProgress();	
		super.onStop();
	}

	@Override
	public void onPause() {
		Log.i("FriendListActivity", "pause");
		helper.dismissProgress();
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		Log.i("FriendListActivity", "destroy");
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		helper.dismissProgress();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(AllFriendsActivity.this,
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
		Log.i("FriendListActivity", "show data");
		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin);
		gridView.setPadding(margin, 0, margin, 0);
	
		MyStaggeredAdapter adapter = new MyStaggeredAdapter(
				AllFriendsActivity.this, data, R.layout.grid_item,
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
			Intent intent = new Intent(AllFriendsActivity.this, LoadFriendsService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
		}
	}

	/*当FriendListModel的数据更新时调用*/
	@Override
	public void doSomething() {
		Log.i("FriendListActivity", "do something");
		if (friendListModel.isDone()) {
			startUpdateUiThread();
		} else {
			Log.i("FriendListActivity", "friend list is null");
		}
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
