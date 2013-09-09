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
		int neighbors = 0;
		for (int dir=1; dir < 5; dir++) { neighbors = (enemy[dir] > 0) ? neighbors+1 : neighbors; }
		
		double threshold = 0; //threshold to reproduce; dynamic based on local population
		switch (neighbors) {
			case 0: threshold = 0.4;
					break;
			case 1: threshold = 0.5; //Alaska
					break;
			case 2: threshold = 0.8; //Minnesota
					break;
			case 3: threshold = 0.8; //Atlanta
					break;
			case 4: threshold = 1; //New York
					break;
		}
		
		if (energyleft > maxEnergy*threshold) {
			return new Move(REPRODUCE, NORTH, state);
		}
		
		else if(foodleft > maxFood/8) {
			return new Move(STAYPUT);
		}
		
		else if (food[WEST]) {
			return new Move(WEST);
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
	    }
		return new Move(STAYPUT);
	}

}
