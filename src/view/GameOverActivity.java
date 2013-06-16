package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.renren.api.connect.android.Renren;

import edu.nju.renrenhardest.R;

public class GameOverActivity extends Activity {
	private Renren renren;
	private ActivityHelper helper;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
	}
	
	/*当按后退键时，不会返回游戏主界面而是返回选择界面*/
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, LoginedMainActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //by lys 避免startActivity创建新的LoginedMainActivity实例
		startActivity(intent);
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_over_menu,menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
			case R.id.item_allMyImages:
				startMyImagesActivity();
				return true;
				
			case R.id.item_allMyFriendsImages:
				startAllFriendsActivity();
				return true;
				
			case R.id.item_gameRule:
				return true;
				
			case R.id.item_quitGame:
				helper.exit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	/**
	 * 展示所有好友列表的界面
	 */
	private void startAllFriendsActivity() {
		Intent intent = new Intent(this, AllFriendsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
	
	/**
	 * 展示所有自己头像的界面
	 */
	private void startMyImagesActivity() {
		Intent intent = new Intent(this, MyImagesActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		startActivity(intent);
	}
	
}
