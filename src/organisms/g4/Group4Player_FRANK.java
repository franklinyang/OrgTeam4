package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player_FRANK implements Player {


	static final String _CNAME = "Group 4 Player";
	static final Color _CColor = new Color(0.5f, 0.67f, 0.67f);
	private int state, changeStrategy;
	private OrganismsGame game;
	private int boxedIn = -1;

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		
		state = key;
		this.game = __amoeba;
		
	}

	@Override
	public String name() throws Exception {
	  return _CNAME;
	}

	@Override
	public Color color() throws Exception {
		return _CColor;
	}

	@Override
	public boolean interactive() throws Exception {
		return false;
	}

	@Override
	public int externalState() throws Exception {
		return state;
	}

	@Override
	public Move move(boolean[] food, int[] enemy, int foodleft, int energyleft)
			throws Exception {
		
		changeStrategy++;
		
		if(changeStrategy == 20) {
			changeStrategy = 0;
			state = (state % 5) + 1;
		}

		int maxEnergy = game.M();
		int perUnitEnergy = game.u();
		int perUnitMove = game.v();
		int surrounding = 0;
		boolean foodExists = false; // food in immediate vicinity
		boolean[] inhabit = new boolean[5];
		Random rand = new Random(); // maybe we should generate a random direction?
		
		if(state == 0)
			state = rand.nextInt(4)+1;
		for (int i=1; i<5; i ++) {
			inhabit[i] = food[i] && (enemy[i] == -1); 
			if(enemy[i] >= 0) { surrounding ++; }
		}
		
		if(surrounding == 4) { boxedIn = 0; }
		else { 
			if(boxedIn != -1) { boxedIn ++; }
		}
		
		
		for (int dir=1; dir<5; dir++) { 
			if(inhabit[dir]) {
				foodExists = true;
				break;
			}
		}

		if (foodExists) {
			if(energyleft >= maxEnergy-(2*perUnitEnergy)) {
				return repToFreeFood(inhabit, state);
			}
			else {
				return moveToFood(food);
			}
		}
		
		if(energyleft >= (maxEnergy-perUnitEnergy)) {
			return repToFree(enemy, state);
		}
		
		if(foodleft > 0) {
			if(energyleft >= maxEnergy-(2*perUnitEnergy)) {
				return repToFree(enemy, state);
			}
			return new Move(STAYPUT);
		}

		/*if(energyleft <= maxEnergy*.025) {
			for (int i=state; i<state+5; i ++) {
				if(enemy[i % 5] == -1) { return new Move(i % 5); } 
			}
		}*/
		
	    else {
	    	if(energyleft >= 2*perUnitEnergy) {
	    		return new Move(state);
	    	}
			/*if(boxedIn > 0 && boxedIn < 5) {
				for(int i = state; i < state + 5; i ++) {
					if(enemy[i % 5] == -1) { return new Move(i % 5); }
				}
			}*/
	    }
		return new Move(STAYPUT);
	}
	
	private Move repToFreeFood(boolean[] inhabit, int state) throws Exception {
		int newState = (state + 1) % 5;
		this.state=newState;
		
		for(int dir=1; dir<5; dir++) {
			if(inhabit[dir])
				return new Move(REPRODUCE, dir, newState);
		}
		
		return new Move(STAYPUT);
	}
	
	private Move repToFree(int[] enemy, int state) throws Exception {
		int newState = (state + 1) % 5;
		this.state=newState;
		for(int dir=1; dir<5; dir++) {
			if(enemy[dir] == -1)
				return new Move(REPRODUCE, dir, 1);
		}
		return new Move(STAYPUT);
	}
	
	private Move moveToFood(boolean[] food) throws Exception {
		for(int dir=1; dir<5; dir++) {
			if(food[dir])
				return new Move(dir);
		}
		return new Move(STAYPUT);
	}

}