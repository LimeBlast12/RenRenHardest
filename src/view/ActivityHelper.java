package view;

import java.util.LinkedList;
import java.util.List;

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
	 * 显示Toast提示
	 * 
	 * @param message
	 */
	public void showTip(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
