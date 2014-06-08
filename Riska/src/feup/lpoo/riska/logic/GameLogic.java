package feup.lpoo.riska.logic;

public class GameLogic {
	
	private GameLogic instance;
	
	
	public GameLogic() {
		
		instance = this;
		
	}
	
	public GameLogic getSharedInstance() {
		return instance;
	}
	
	

}
