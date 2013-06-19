package view;

import java.util.LinkedList;
import java.util.List;

import service.LoadFriendsService;
import service.LoadMyImagesService;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;

public class ActivityHelper {
	private static ActivityHelper helper;
	
	private List<Activity> activityList = new LinkedList<Activity>();
	
	public static ActivityHelper getInstance() {
		return (helper == null) ? (helper = new ActivityHelper()) : helper;
	}
	
	private ActivityHelper(){}

	private ProgressDialog progressDialog;
	/**
	 * 调用SDK接口的Renren对象
	 */
	private static Renren renren;

	public void setRenren(Renren r) {
		renren = r;
	}

	public Renren getRenren() {
		return renren;
	}

	/**
	 * 显示等待框
	 */
	public void showWaitingDialog(Context context) {
		showProgress(context, "Please wait", "progressing");
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	public void showProgress(Context context, String title, String message) {
		progressDialog = ProgressDialog.show(context, title, message);
		progressDialog.setCancelable(true);
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 显示短时间Toast提示，大概1秒
	 * 
	 * @param context 要显示提示的界面
	 * @param message 要提示的内容
	 */
	public void showShortTip(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示长时间Toast提示，大概3秒
	 * 
	 * @param context 要显示提示的界面
	 * @param message 要提示的内容	 
	 */
	public void showLongTip(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示长时间Toast提示，大概3秒
	 * 
	 * @param context 要显示提示的界面
	 * @param stringId 要提示的内容的资源id	 
	 */
	public void showLongTip(Context context, int stringId) {
		Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 添加Activity到容器中  
	 * 
	 * @param activity
	 */
    public void addActivity(Activity activity)  
    {  
    	activityList.add(activity);  
    }  
	
	/**
	 * 遍历所有Activity并finish  
	 * 
	 */
    public void exit()  
    {   
	    for(Activity activity:activityList)  
	    {  
	    	activity.finish();  
	    }
	    LoadFriendsService.stopservice(activityList.get(1));
	    LoadMyImagesService.stopservice(activityList.get(1));
	    System.exit(0);  
	
    }
    
    /**
	 * 展示所有好友列表的界面
	 */
	public void startAllFriendsActivity(Activity activity) {
		Intent intent = new Intent(activity, AllFriendsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}
	
	/**
	 * 展示所有自己头像的界面
	 */
	public void startMyImagesActivity(Activity activity) {
		Intent intent = new Intent(activity, MyImagesActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}
	
	
	public void showGameRule(Activity activity) {
		Intent intent = new Intent(activity, TutorialsActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		intent.putExtra("enterFrom","overflow");
		activity.startActivity(intent);
	}
}
