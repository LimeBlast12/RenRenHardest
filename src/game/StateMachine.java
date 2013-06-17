
package game;

/**
 * 游戏状态机，封装了让游戏运行下去的一些规则
 * @author ZeroNing
 *
 */
public class StateMachine {
	private Game theGame;
	private State currentState;
	private State previousState;
	private State globalState;	//每次状态转换都需要调用的全局状态
	
	public StateMachine(Game theGame){
		this.theGame = theGame;
	}
	
	public void update(){
		if(globalState!=null){
			globalState.execute(theGame);
		}
		if(currentState!=null){
			currentState.execute(theGame);
		}
	}
	
	public void changeState(State newState){
		previousState = currentState;
		currentState.exit(theGame);
		currentState = newState;
		currentState.enter(theGame);
	}
	
	public void revertState(){
		changeState(previousState);
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getPreviousState() {
		return previousState;
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public State getGlobalState() {
		return globalState;
	}

	public void setGlobalState(State globalState) {
		this.globalState = globalState;
	}
}
