package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player_FR implements Player {


	static final String _CNAME = "Group 4 Player";
	static final Color _CColor = new Color(1.0f, 0.67f, 0.67f);
	private int state;
	private OrganismsGame game;

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		
		state = 0;
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

		int maxEnergy = game.M();
		int maxFood = game.K();
		boolean foodExists = false; // food in immediate vicinity
		boolean[] inhabit = new boolean[5];
		for (int i=1; i < 5; i++) {
			inhabit[i] = food[i] && (enemy[i] == -1); 
		}
		
		for (int dir=1; dir < 5; dir++) { 
			if(food[dir] || foodExists)
				foodExists = true;
		}
		
		if(foodleft > 0) {
			return new Move(STAYPUT);
		}
		
//		else if (food[WEST]) {
//			return new Move(WEST);
//		}

	    else if (foodExists){
	    	
	      // inhabit to be true if food is present, enemy is absent
	
	      for (int dir=1; dir < 5; dir++) {
	        if (inhabit[dir]) {
	          // ***check to see if food array is correctly specified
	        	return new Move(REPRODUCE, dir, state);
	        }
	      }
	    }
	    else {
	    	for (int dir=0; dir < 5; dir++) {
	    		if(enemy[dir] < 0 && energyleft > 10*game.v()) {
	    			return new Move(dir);
	    		}
	    	}
	    }
		return new Move(STAYPUT);
	}

}