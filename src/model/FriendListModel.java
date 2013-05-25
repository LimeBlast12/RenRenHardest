package model;

import java.util.List;
import java.util.Map;

import android.util.Log;

public class FriendListModel extends AbstractModel {
	private List<Map<String, Object>> friendList;
	private static FriendListModel thisRef;
	
	private FriendListModel() {}
	
	public static FriendListModel getInstance() {
		return (thisRef == null) ? (thisRef = new FriendListModel()): thisRef;
	}
	
	public void setFriendList(List<Map<String, Object>> data) {
		Log.i("FriendListModel","setFriendList");
		this.friendList = data;
		if (friendList == null) {
			Log.i("FriendListModel setter","null");
		}
		notifyListeners();
	}

	public List<Map<String, Object>> getFriendList() {
		Log.i("FriendListModel","getFriendList");
		if (friendList == null) {
			Log.i("FriendListModel getter","null");
		}
		return this.friendList;
	}
	
	
}
