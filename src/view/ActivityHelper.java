package view;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;

public class ActivityHelper {
	private static ActivityHelper helper;

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
}
