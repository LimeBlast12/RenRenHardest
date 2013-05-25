package model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel {
	private List<ModelListener> listeners = new ArrayList<ModelListener>();
	
	public void register(ModelListener listener) {
		this.listeners.add(listener);
	}
	
	public void remove(ModelListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners() {
		for (ModelListener listener : listeners) {
			listener.doSomething();
		}
	}
}
