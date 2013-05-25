package view;

import loader.ImageLoader;
import edu.nju.renrenhardest.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GameMainActivity extends Activity {
	
	private Button filter_grey;
	private Button filter_old;
	private Button friend;
	
	private String urls[] = {"http://hdn.xnimg.cn/photos/hdn521/20130508/2110/h_large_QUg5_7e8f0000005b113e.jpg"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mainview);
		initButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_main, menu);
		return true;
	}
	
	private void initButtons() {
		filter_grey = (Button) findViewById(R.id.filter_grey);
		filter_grey.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
		
		filter_old = (Button) findViewById(R.id.filter_old);
		filter_old.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
		
		friend = (Button) findViewById(R.id.friend);
		friend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				change_image();
			}
		});
	}
	
	private void change_image(){
		ImageLoader imageLoader = new ImageLoader(GameMainActivity.this);
		Bitmap originalBitmap = imageLoader.getBitmap(urls[0]);
		ImageView iv = (ImageView)findViewById(R.id.iv);
        iv.setImageBitmap(originalBitmap);
	}

}
