package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.LoadFriendsService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class FriendListActivity extends Activity {
	private ActivityHelper helper;
	private Renren renren;
	private LoadFriendsService service;

	private ServiceConnection sConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = ((LoadFriendsService.MyBinder) binder).getService();
			Log.i("ServiceConnection", "connected");
			//helper.showWaitingDialog(FriendListActivity.this);
			while (true) {
				if (service.isDone()) {
					helper.dismissProgress();
					showData(service.getFriendList());
					break;
				}
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			service = null;
			Log.i("ServiceConnection", "disconnected");
		}
	};


	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		helper = ActivityHelper.getInstance();
		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		} else {
			renren = helper.getRenren();
		}	
		setContentView(R.layout.friend_list);
		loadFriends();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		helper.dismissProgress();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(FriendListActivity.this,
					LoginedMainActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showData(List<Map<String, Object>> data) {
		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin);
		gridView.setPadding(margin, 0, margin, 0);
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(StaggeredGridView parent, View view,
//					int position, long id) {
//				ListAdapter adapter = parent.getAdapter();
//
//				@SuppressWarnings("unchecked")
//				HashMap<String, Object> item = (HashMap<String, Object>) adapter
//						.getItem(position);
//
//				long uid = (Long) item.get("uid");
//				Intent intent = new Intent(FriendListActivity.this,
//						PhotoFragmentActivity.class);
//				intent.putExtra("uid", uid);
//				intent.putExtra(Renren.RENREN_LABEL, renren);
//				startActivity(intent);
//			}
//		});
	
		MyStaggeredAdapter adapter = new MyStaggeredAdapter(
				FriendListActivity.this, data, R.layout.friend_list_item,
				new String[] { "name", "image" }, new int[] { R.id.ItemText,
						R.id.ItemImage });

		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void loadFriends() {
		Intent intent = new Intent(FriendListActivity.this, LoadFriendsService.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
	    bindService(intent, sConnection, Context.BIND_AUTO_CREATE);
	    Log.i("log service", "bind");
	}

}
