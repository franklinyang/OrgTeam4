package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player_F implements Player {


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
		
		boolean repr = energyleft > 0.4*maxEnergy;
		
		switch(state) {
			
			case 0:
				if (repr) {
					state = 1;
					return new Move(REPRODUCE, SOUTH, 0);
				}
				else {
					state=4;
				}
			case 1:
				if (repr) {
					state = 2;
					return new Move(REPRODUCE, WEST, 0);
				}
				else {
					state=4;
				}
				
			case 2:
				if (repr) {
					state = 3;
					return new Move(REPRODUCE, NORTH, 0);
				}
				else {
					state=4;
				}
			case 3:
				if (repr) {
					state = 4;
					return new Move(REPRODUCE, EAST, 0);
				}
				else {
					state=4;
				}
			
			default:
				if (energyleft > maxEnergy*0.5) {
					return new Move(REPRODUCE, NORTH, 0);
				}
				else {
					return thrive(food, enemy, foodleft, energyleft);
				}
					
		}
//		return new Move(STAYPUT);
	}
	
	private Move thrive(boolean[] food, int[] enemy, int foodleft, int energyleft) throws Exception{
		int maxEnergy = game.M();
		int maxFood = game.K();
		int neighbors = 0;
		boolean foodExists = false;
		double density = 0;
		for (int dir=1; dir < 5; dir++) { neighbors = (enemy[dir] > 0) ? neighbors+1 : neighbors; }
		for (int dir=1; dir < 5; dir++) { density = (enemy[dir] != 255 && enemy[dir] > 0) ? density + enemy[dir]/4 : density; }

		boolean reproduceErrywhere = (density/4 > .75);

		double threshold = 0; //threshold to reproduce; dynamic based on local population
		switch (neighbors) {
			case 0: threshold = 0.4;
					break;
			case 1: threshold = 0.5; //Alaska
					break;
			case 2: threshold = 0.6; //Minnesota
					break;
			case 3: threshold = 0.8; //Atlanta
					break;
			case 4: threshold = 1; //New York
					break;
		}
		
		if(foodleft > maxFood/8) {
			return new Move(STAYPUT);	
		}
		
		else if(foodExists) {
	      for (int dir=1; dir < 5; dir++) {
	        if (food[dir]) {
	          // ***check to see if food array is correctly specified
	          return new Move(dir);
	        }
	      }
	    }
		
		else {
			for (int dir=1; dir < 5; dir++) {
		        if (enemy[dir] < 0 && energyleft > 4*game.v()) {
		          // ***check to see if food array is correctly specified
		          return new Move(dir);
		        }
		      }
		}
		return new Move(STAYPUT);
	}
}
