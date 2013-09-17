package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class VladTheImpaler implements Player {


	static final String _CNAME = "Vlad The Impaler";
	static final Color _CColor = new Color(90, 90, 90);
	private int state;
	private OrganismsGame game;
	
	private int foodEnergy;
	private int maxEnergy;
	
	private static final double REPRODUCE_THRESHOLD = 0.5;
	private static final double REPRODUCE_ON_FOOD_THRESHOLD = 0.3;
	private static final double MOVE_THRESHOLD = 0.05;
	private static final double STOP_THRESHOLD = 0.05;
	private static final double REPRODUCE_CHANCE = 0.40;
	private static final double MOVE_CHANCE = 0.20;

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		this.state = key == -1 ? new Random().nextInt(255) : key;	
		this.game = __amoeba;
		this.foodEnergy = game.u();
		this.maxEnergy = game.M();
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
		/*
		 - Anti-clustering: move away from others of same species
		 - Greedily move onto food
		 - reproduce while food available on current square
		    - Prioritize dumping onto new food
		    - If energy above some threshold, reproduce anyway
		 - Block out other teams
		*/
		
		this.state = Integer.toString(this.state).hashCode() % 255;
		
		// Sitting on a square with food...
		if (foodleft > 0 || energyleft > (double) REPRODUCE_ON_FOOD_THRESHOLD * (double) maxEnergy) {
			for (int i = 1; i < 5; i++) {
				if (food[i] && new Random().nextDouble() > REPRODUCE_CHANCE) return new Move(REPRODUCE, i, this.state);
			}
		} else if (energyleft > (double) REPRODUCE_THRESHOLD * (double) maxEnergy) {
			game.println(Double.toString((double) REPRODUCE_THRESHOLD * (double) maxEnergy));
			return new Move(REPRODUCE, getWeightedDirection(0.9, 0.5, 0.4), this.state);
		} 
				
		// Greedily eat food.
		for (int i = 1; i < 5; i++) {
			if (food[i] && foodleft == 0) return new Move(i);
		}
		
		/*
		// Trap enemies if you have the donk to maintain it.
		if (energyleft < (double) MARTYR_THRESHOLD * (double) maxEnergy) {
			for (int i = 1; i < 5; i++) {
				if (enemy[i] != this.state && enemy[i] != -1) return new Move(STAYPUT);
			}
		} */
		
		// Move away from friends
		for (int i = 1; i < 5; i++) {
			if (enemy[i] != this.state && enemy[i] != -1) {
				for (int j = 1; j < 5; j++) {
					if (enemy[j] == -1) {
						return new Move(j);
					}
				}
			}
		}
		
		// Reproduce onto stuff
		
		if (energyleft > (double) MOVE_THRESHOLD * (double) maxEnergy) {
			game.println("THRESHOLD: " + (double) MOVE_THRESHOLD * (double) maxEnergy);
			if (new Random().nextDouble() > MOVE_CHANCE) {
				return new Move(getWeightedDirection(0.5, 0.5, 0.01));
			}
		}
		
		return null;
	}
	
	private static int getWeightedDirection (double ns, double sw, double we) {
		double choice = new Random().nextDouble();
		
		if (choice > ns) {
			return NORTH;
		} else if (choice > sw) {
			return SOUTH;
		} else if (choice > we) {
			return WEST;
		} else {
			return EAST;
		}
	}
	private static int getOppositeDirection(int dir) {
		switch(dir) {
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		case WEST: return EAST;
		case EAST: return WEST;
		default: return WEST;
		}
	}

}
