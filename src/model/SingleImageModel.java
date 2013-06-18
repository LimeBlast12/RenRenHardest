package model;

import android.graphics.Bitmap;

/**
 * 封装要显示在游戏界面的图片的所有者、滤镜信息，单例模式，集成AbstractModel，允许被Activity监听
 * @author daisy
 *
 */
public class SingleImageModel extends AbstractModel {
	private static SingleImageModel thisRef;
	private Bitmap image;
	private int owner;	//0代表自己，1代表好友
	private int filter_type;	//数字对应的滤镜见BitmapFilter类中的常量定义

	private SingleImageModel() {
	}

	public static SingleImageModel getInstance() {
		return (thisRef == null) ? (thisRef = new SingleImageModel()) : thisRef;
	}
	
	public void setInfomations(Bitmap image, int owner, int filter_type) {
		this.image = image;
		this.owner = owner;
		this.filter_type = filter_type;
		notifyListeners();
	}
	
	public Bitmap getImage() {
		return image;
	}

	public int getOwner() {
		return owner;
	}

	public int getFilter_type() {
		return filter_type;
	}
}
