package view;

import java.lang.reflect.Field;

import edu.nju.renrenhardest.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class TutorialsActivity extends Activity implements OnGestureListener{
	
	
	private int[] imageID = { R.drawable.t1, R.drawable.t2, R.drawable.t3,  
	            R.drawable.t4, R.drawable.t5,R.drawable.t6};  
	private ViewFlipper viewFlipper = null;
	private GestureDetector gestureDetector = null; 
	private ActivityHelper helper;
	private int viewCount = 5;
	private Intent intent; 
	 
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.tutorials);	
		getOverflowMenu();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("游戏规则");
		init();
		
	}
	
	/*
	 * 初始化
	 */
	private void init() {
		helper = ActivityHelper.getInstance();
		helper.addActivity(this);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);    
        gestureDetector = new GestureDetector(this);  // 生成GestureDetector对象，用于检测手势事件
        intent = getIntent();
        for (int i = 0; i < imageID.length; i++)  
        {    
            ImageView image = new ImageView(this);  
            image.setImageResource(imageID[i]);  
            image.setScaleType(ImageView.ScaleType.FIT_XY);  // 充满父控件
            viewFlipper.addView(image, new LayoutParams(  // 添加到viewFlipper中
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
        }  
        
	}


	/*
	 *响应Touch事件
	 */
	 @Override  
	public boolean onTouchEvent(MotionEvent event)  
	{  
	    return this.gestureDetector.onTouchEvent(event);  
	}  
	 
	 
	/*
	 * 设置从左向右滑的动画效果,若教程结束,调用进入主界面的方法
	 */ 
	private void animationLtoR(){
		
		  this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
                  R.anim.push_left_in));  
          this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
                  R.anim.push_left_out));
          this.viewFlipper.showNext(); 
          viewCount--;
          
          if(viewCount==0){
        	// 从overflow进入教程
        	if(intent.getStringExtra("enterFrom").equals("overflow"))
        		viewFlipper.stopFlipping();
     
        	// 第一次使用从main进入教程
        	else if(intent.getStringExtra("enterFrom").equals("main"))
        		StartMainWhenAnimEnd();
          }
          
	}
	
	/*
	 * 设置从左向右滑的动画效果,若教程结束,调用进入主界面的方法
	 */ 
	private void animationRtoL(){
          
		  // 从overflow进入教程
		  if(intent.getStringExtra("enterFrom").equals("overflow")){
	          this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,  
	                  R.anim.push_right_in));  
	          this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
	                  R.anim.push_right_out));  
	          this.viewFlipper.showPrevious(); 
		  }
		  // 第一次使用从main进入教程
		  else{
			  
		  }
	}
	
	 
	/*
	 * 新手教程动画结束后,进入主界面
	 */
	private void StartMainWhenAnimEnd(){
		
		Log.i("TutorialsActivity", "start LoginedActivity");
		Intent intent = new Intent(TutorialsActivity.this, LoginedMainActivity.class);
		startActivity(intent);
		TutorialsActivity.this.finish();//关闭当前界面，避免按返回键时出错
				
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float arg2,
			float arg3) {
		
		//手指滑动距离大于等于120像素，做切换动作，否则不做任何切换动作。  
        if (motionEvent1.getX() - motionEvent2.getX() >= 120){  
        	animationLtoR();   // 从左向右滑动   
            return true;  
        }      
        else if (motionEvent1.getX() - motionEvent2.getX() < -120){  
        	animationRtoL();  // 从右向左滑动   
            return true;  
        } 
        return true;  
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
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
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu,menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		

		switch(item.getItemId()){
		
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
	
}
