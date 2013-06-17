package game;

/**
 * 状态抽象类,其具体子类封装了状态转换的规则，以及该状态负责的任务
 * @author ZeroNing
 *
 */
public abstract class State {
	public abstract void execute(Game theGame);	//每次StateMachine的update被调用时会被调用的方法
	public abstract void exit(Game theGame);	//离开状态时，该状态的此方法被调用一次
	public abstract void enter(Game theGame);	//进入状态时，该状态的此方法被调用一次
}
