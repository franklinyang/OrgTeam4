package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player implements Player {


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
		state = foodleft;
		
		if (energyleft > maxEnergy/2 || foodleft > (.5*maxFood)) {
			return new Move(REPRODUCE, NORTH, state);
		}
		
		if(foodleft > 0) {
			return new Move(STAYPUT);
		}
		
		if (food[WEST]) {
			state = foodleft + maxFood;
			return new Move(WEST);
		}
		
		if (food[EAST]) {
			state = foodleft + maxFood;			
			return new Move(EAST);
		}
		
		if (food[SOUTH]) {
			state = foodleft + maxFood;
			return new Move(SOUTH);
		}

	    else {
	      // inhabit to be true if food is present & enemy is absent
	      boolean[] inhabit = new boolean[4];
	      for (int i=0; i < 4; i++) {
	        inhabit[i] = food[i] && (enemy[i] == -1); 
	      }
	
	      for (int dir=0; dir < 4; dir++) {
	        if (inhabit[dir]) {
	          // ***check to see if food array is correctly specified
	          return new Move(dir+1);
	        }
	      }
	
	      return new Move(STAYPUT);
	    }
	}

}
