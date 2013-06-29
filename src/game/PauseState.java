package game;

/**
 * 
 * @author ZeroNing
 *
 */
public class PauseState extends State {

	@Override
	public void execute(Game theGame) {
		if(theGame.isPaused()){
			Thread thread = theGame.getTheThread();
			synchronized (thread) {
				try {
					thread.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void exit(Game theGame) {
	}

	@Override
	public void enter(Game theGame) {
	}

}
