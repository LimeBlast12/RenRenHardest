package model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel {
	protected List<ModelListener> listeners = new ArrayList<ModelListener>();
	protected boolean done = false; 
	
	public void register(ModelListener listener) {
		this.listeners.add(listener);
	}
	
	public void remove(ModelListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners() {
		done = true;
		for (ModelListener listener : listeners) {
			listener.doSomething(getMessage());
		}
	}
	
	public abstract String getMessage();
	
	public boolean isDone() {
		return done;
	}
}
