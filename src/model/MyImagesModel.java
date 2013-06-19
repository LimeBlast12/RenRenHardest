package model;

import java.util.List;
import java.util.Map;

import android.util.Log;

public class MyImagesModel extends AbstractModel {
	private List<Map<String, Object>> myImages;
	private static MyImagesModel thisRef;
	private final String message = "UPDATE_MY_IMAGES";
	
	private MyImagesModel() {}
	
	public static MyImagesModel getInstance() {
		return (thisRef == null) ? (thisRef = new MyImagesModel()): thisRef;
	}
	
	public void setMyImages(List<Map<String, Object>> images) {
		this.myImages = images;
		notifyListeners();
	}
	
	public String getMessage() {
		return message;
	}

	public List<Map<String, Object>> getMyImages() {
		Log.i("MyImagesModel","getImagesUrl");
		if (myImages == null) {
			Log.i("MyImagesModel getter","null");
		}
		return this.myImages;
	}
	
	
}
