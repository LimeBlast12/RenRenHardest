package model;

import android.graphics.Bitmap;

/**
 * 封装要显示在游戏界面的图片，继承AbstractModel，允许被Activity监听
 * @author daisy
 *
 */
public class SingleImageModel extends AbstractModel {
	private static SingleImageModel thisRef;
	private Bitmap image;

	private SingleImageModel() {
	}

	public static SingleImageModel getInstance() {
		return (thisRef == null) ? (thisRef = new SingleImageModel()) : thisRef;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
		notifyListeners();
	}
	
	public Bitmap getImage() {
		return image;
	}
}
