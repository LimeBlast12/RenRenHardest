package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class FriendListModel extends AbstractModel {
	private List<Map<String, Object>> friendList;
	private List<Map<String, Object>> randomFriendList;
	private static FriendListModel thisRef;

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

	public List<Map<String, Object>> getFriendList() {
		Log.i("FriendListModel", "getFriendList");
		if (friendList == null) {
			Log.i("FriendListModel getter", "null");
		}
		return this.friendList;
	}

	/**
	 * 随机获取一组好友
	 * @param num 好有个数
	 * @return 随机的一组好友
	 */
	public List<Map<String, Object>> getRandomFriendList(int num) {
		Log.i("FriendListModel", "getRandomFriendList");
		if (this.friendList == null) {
			return null;
		}

		this.randomFriendList = new ArrayList<Map<String, Object>>();
		int size = this.friendList.size();
		for (int i = 0; i < num; i++) {
			int location = (int) (Math.random() * size);
			Map<String, Object> friend = this.friendList.get(location);
			randomFriendList.add(friend);
		}
		
		return this.randomFriendList;
	}

}
