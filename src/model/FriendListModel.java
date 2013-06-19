package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class FriendListModel extends AbstractModel {
	private List<Map<String, Object>> friendList;
	private List<Map<String, Object>> randomFriendList;
	private static FriendListModel thisRef;
	private final String message = "UPDATE_FRIENDS";

	private FriendListModel() {
	}

	public static FriendListModel getInstance() {
		return (thisRef == null) ? (thisRef = new FriendListModel()) : thisRef;
	}

	public void setFriendList(List<Map<String, Object>> data) {
		Log.i("FriendListModel", "setFriendList");
		this.friendList = data;
		if (friendList == null) {
			Log.i("FriendListModel setter", "null");
		}
		notifyListeners();
	}
	
	public String getMessage() {
		return message;
	}

	public List<Map<String, Object>> getAllFriends() {
		Log.i("FriendListModel", "getFriendList");
		if (friendList == null) {
			Log.i("FriendListModel getter", "null");
		}
		return this.friendList;
	}
	
	/**
	 * 获取一组随机生成的好友
	 * @param num 好友个数
	 * @return 随机生成的一组好友
	 */
	public List<Map<String, Object>> getRandomFriends(int num) {
		Log.i("FriendListModel", "getRandomFriendList");
		if (friendList == null) {
			Log.i("FriendListModel getter", "null");
			return null;
		}
		
		this.randomFriendList = new ArrayList<Map<String, Object>>();
		int size = this.friendList.size();
		int i = 0;
		while (i < num) {
			int location = (int) (Math.random() * size);
			Map<String, Object> friend = this.friendList.get(location);
			if (!randomFriendList.contains(friend)) {
				randomFriendList.add(friend);
				i++;
			}
		}
		
		return randomFriendList;
	}
	
	/**
	 * 获取当前的一组随机好友
	 * @return 当前的一组随机好友
	 */
	public List<Map<String, Object>> getCurrentFriends() {
		Log.i("FriendListModel", "getCurrentFriendList");
		return randomFriendList;
	}

}
