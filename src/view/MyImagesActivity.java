package view;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import model.ModelListener;
import model.MyImagesModel;
import service.LoadMyImagesService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.origamilabs.library.views.StaggeredGridView;
import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

/**
 * 
 * This will not work so great since the heights of the imageViews are
 * calculated on the imageLoader callback ruining the offsets. To fix this try
 * to get the (intrinsic) image width and height and set the views height
 * manually. I will look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz
 * @author Danni Wang
 */
public class MyImagesActivity extends Activity implements ModelListener  {
	protected Renren renren;	
	private ActivityHelper helper;
	private MyImagesModel myImagesModel;
	/*使用handler来避免更新UI的线程安全问题*/
	private Handler handler = null;
	/*实际用于更新UI的线程*/
	private Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			showData(myImagesModel.getMyImages());
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("我的头像");
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
		getOverflowMenu();
		handler = new Handler();
		myImagesModel = MyImagesModel.getInstance();
		myImagesModel.register(MyImagesActivity.this);
		loadMyImages();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("MyImagesActivity", "on destroy");
		myImagesModel.remove(MyImagesActivity.this);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case android.R.id.home:
				Intent parentActivityIntent = new Intent(MyImagesActivity.this,
						LoginedMainActivity.class);
				parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(parentActivityIntent);
				finish();
				return true;
			
			case R.id.item_allMyImages:
				helper.startMyImagesActivity(this);
				return true;
				
			case R.id.item_allMyFriendsImages:
				helper.startAllFriendsActivity(this);
				return true;
				
			case R.id.item_gameRule:
				helper.showGameRule(this);
				return true;
				
			case R.id.item_quitGame:
				helper.exit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);	
		}
	
	}

	private void showData(List<Map<String, Object>> data) {
		Log.i("FriendListActivity", "show data");
		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin);
		gridView.setPadding(margin, 0, margin, 0);
	
		MyStaggeredAdapter adapter = new MyStaggeredAdapter(
				MyImagesActivity.this, data, R.layout.grid_item,
				new String[] {"image" }, new int[] {R.id.ItemImage });
		
		gridView.setAdapter(adapter);
		helper.dismissProgress();
		adapter.notifyDataSetChanged();   
	}
	
	private void loadMyImages() {
		if (myImagesModel.isDone()) {//已有数据
			startUpdateUiThread();
		} else {
			Intent intent = new Intent(MyImagesActivity.this, LoadMyImagesService.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			startService(intent);
		}
	    helper.showWaitingDialog(MyImagesActivity.this);
	}
	
	@Override
	public void doSomething(String message) {
		if (message.equals(myImagesModel.getMessage())) {
			Log.i("MyImagesActivity", "do something");
			if (myImagesModel.isDone()) {
				startUpdateUiThread();
			} else {
				Log.i("MyImagesActivity", "images is null");
			}
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
