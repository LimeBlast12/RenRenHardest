package view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.demo.activity.BaseActivity;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.photos.AlbumBean;
import com.renren.api.connect.android.photos.AlbumGetRequestParam;
import com.renren.api.connect.android.photos.AlbumGetResponseBean;
import com.renren.api.connect.android.photos.PhotoBean;
import com.renren.api.connect.android.photos.PhotoGetRequestParam;
import com.renren.api.connect.android.photos.PhotoGetResponseBean;

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
public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "edu.nju.mystaggeredgridview.MESSAGE";
	/**
	 * 调用SDK接口的Renren对象
	 */
	protected Renren renren;

	private String[] mToggleLabels = { "Show Titles", "Hide Titles" };
	private static final String ACTION_DIALOG = "com.example.android.hcgallery.action.DIALOG";
	private int mThemeId = -1;
	private boolean mDualFragments = false;
	private boolean mTitlesHidden = false;
	private String urls[] = new String[50];
	private StaggeredGridView gridView;
	Long aid = (long) 0;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* HoneyComb Gallery: MainActivity */
		if (savedInstanceState != null) {
			if (savedInstanceState.getInt("theme", -1) != -1) {
				mThemeId = savedInstanceState.getInt("theme");
				this.setTheme(mThemeId);
			}
		}

		/* from BaseActivity */
		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		}

		setContentView(R.layout.activity_main);

		gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the
													// sides as well
		
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(StaggeredGridView parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ImageFilterActivity.class);
				String url = urls[position];
				System.out.println("url = "+url);
				intent.putExtra(EXTRA_MESSAGE, url);
				startActivity(intent);
			}
			
		});

		/* 先获取当前用户的所有相册 */
		this.getAlbum();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		// If the device doesn't support camera, remove the camera menu item
		if (!getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			menu.removeItem(R.id.menu_camera);
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If not showing both fragments, remove the "toggle titles" menu item
		if (!mDualFragments) {
			menu.removeItem(R.id.menu_toggleTitles);
		} else {
			menu.findItem(R.id.menu_toggleTitles).setTitle(
					mToggleLabels[mTitlesHidden ? 0 : 1]);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_camera:
			Intent intent = new Intent(this, CameraActivity.class);
			intent.putExtra("theme", mThemeId);
			startActivity(intent);
			return true;

		case R.id.menu_toggleTitles:
			toggleVisibleTitles();
			return true;

		case R.id.menu_toggleTheme:
			if (mThemeId == R.style.AppTheme_Dark) {
				mThemeId = R.style.AppTheme_Light;
			} else {
				mThemeId = R.style.AppTheme_Dark;
			}
			this.recreate();
			return true;

		case R.id.menu_showDialog:
			showDialog("This is indeed an awesome dialog.");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Respond to the "toogle titles" item in the action bar */
	public void toggleVisibleTitles() {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (ACTION_DIALOG.equals(intent.getAction())) {
			showDialog(intent.getStringExtra(Intent.EXTRA_TEXT));
		}
	}

	@SuppressLint("NewApi")
	void showDialog(String text) {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		DialogFragment newFragment = MyDialogFragment.newInstance(text);

		// Show the dialog.
		newFragment.show(ft, "dialog");
	}

	PendingIntent getDialogPendingIntent(String dialogText) {
		return PendingIntent.getActivity(
				this,
				dialogText.hashCode(), // Otherwise previous PendingIntents with
										// the same
										// requestCode may be overwritten.
				new Intent(ACTION_DIALOG).putExtra(Intent.EXTRA_TEXT,
						dialogText).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("theme", mThemeId);
		outState.putBoolean("titlesHidden", mTitlesHidden);
	}

	public void getAlbum() {
		AlbumGetRequestParam albumGetRequestParam = new AlbumGetRequestParam();

		albumGetRequestParam.setUid(renren.getCurrentUid());

		// 调用SDK异步接口获取相册
		new AsyncRenren(renren).getAlbums(albumGetRequestParam,
				new AbstractRequestListener<AlbumGetResponseBean>() {
					@Override
					public void onRenrenError(final RenrenError renrenError) {
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询出错，结束进度框，显示错误信息
							}
						});
					}

					@Override
					public void onFault(final Throwable fault) {
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询出错，结束进度框，显示错误信息
							}
						});
					}

					@Override
					public void onComplete(final AlbumGetResponseBean bean) {
						System.out.println("onComplete--getAlbums");
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询完成，结束进度框，然后显示结果
								/* 这里处理AlbumGetResponseBean */
								System.out.println("这里处理AlbumGetResponseBean");
								List<AlbumBean> albumBean = bean.getAlbums();

								/* 先找到头像相册的aid */
								for (int i = 0; i < albumBean.size(); i++) {
									if (albumBean.get(i).getName()
											.equals("头像相册")) {
										aid = albumBean.get(i).getAid();
										System.out
												.println("aid of portraint album is "
														+ aid);
									}

								}
								getPortrait();
							}
						});

					}
				});

		System.out.println("method getAlbum is going to finish");
	}

	public void getPortrait() {
		PhotoGetRequestParam photoGetRequestParam = new PhotoGetRequestParam();

		photoGetRequestParam.setUid(renren.getCurrentUid());
		photoGetRequestParam.setAid(aid);
		photoGetRequestParam.setCount(50);

		// 调用SDK异步接口获取相册
		new AsyncRenren(renren).getPhotos(photoGetRequestParam,
				new AbstractRequestListener<PhotoGetResponseBean>() {
					@Override
					public void onRenrenError(final RenrenError renrenError) {
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询出错，结束进度框，显示错误信息
							}
						});
					}

					@Override
					public void onFault(final Throwable fault) {
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询出错，结束进度框，显示错误信息
							}
						});
					}

					@Override
					public void onComplete(final PhotoGetResponseBean bean) {
						System.out.println("onComplete--getPortrait");
						MainActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// 查询完成，结束进度框，然后显示结果
								/* 这里处理PhotoGetResponseBean */
								System.out.println("这里处理PhotoGetResponseBean");
								List<PhotoBean> photoBean = bean.getPhotos();

								/* 将头像相册中的所有照片url加入urls */
								for (int i = 0; i < photoBean.size(); i++) {
									urls[i] = photoBean.get(i).getUrlHead();
								}

								/* construct adapter with urls */
								StaggeredAdapter adapter = new StaggeredAdapter(
										MainActivity.this, R.id.imageView1,
										urls);

								gridView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						});
					}
				});

		System.out.println("method getPortrait is going to finish");
	}

	/** Dialog implementation that shows a simple dialog as a fragment */
	@SuppressLint("NewApi")
	public static class MyDialogFragment extends DialogFragment {

		@SuppressLint("NewApi")
		public static MyDialogFragment newInstance(String title) {
			MyDialogFragment frag = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putString("text", title);
			frag.setArguments(args);
			return frag;
		}

		@SuppressLint("NewApi")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String text = getArguments().getString("text");

			return new AlertDialog.Builder(getActivity())
					.setTitle("A Dialog of Awesome")
					.setMessage(text)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		}
	}

}
