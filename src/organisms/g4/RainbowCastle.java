package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

@SuppressWarnings("serial")
public class RainbowCastle implements Player {

	static final String ORGANISM_NAME = "Rainbow Castle";
	static final Color ORGANISM_YOUNG_COLOR = new Color(0, 255, 0);
	static final Color ORGANISM_OLD_COLOR = new Color(0, 0, 255);
	private OrganismsGame game;
	
	// Game constants
	private static double MAX_ENERGY;
	private static double ENERGY_PER_FOOD;
	private static double ENERGY_PER_MOVE;
	
	private static int AGE_MAX_THRESHOLD = 15;
	private static int DELTA_THRESHOLD_PER_GEN = 0;
	private static int MOVE_ENERGY_THRESHOLD = 0;
	private double FOOD_UNITS_BELOW_MAX_TO_REPRODUCE = 1.0;
	private static int ENERGY_TO_BREAK_CLUSTER = 200;
	private int REPRODUCTION_THRESHOLD;
	
	private int moveDirection;
	private int AGE_THRESHOLD;
	private int ageCounter;
	private int generation;
	private boolean isOld;
	private int state;

	@Override
	public void register(OrganismsGame game, int incomingState) throws Exception {
		
		MAX_ENERGY = game.M();
		ENERGY_PER_FOOD = game.u();
		ENERGY_PER_MOVE = game.v();
		isOld = false;
		
		state = incomingState >> 3;
		
		if (incomingState == -1) {
			moveDirection = WEST;
		} else {
			moveDirection = incomingState & 0b00000111;
		}		
		
		generation = incomingState >> 3;
		AGE_THRESHOLD = AGE_MAX_THRESHOLD - DELTA_THRESHOLD_PER_GEN * generation;
		REPRODUCTION_THRESHOLD = (int) ((double) MAX_ENERGY - ((double) FOOD_UNITS_BELOW_MAX_TO_REPRODUCE * (double) ENERGY_PER_FOOD));
	
		this.game = game;
	}

	@Override
	public String name() throws Exception {
	  return ORGANISM_NAME;
	}

	@Override
	public Color color() throws Exception {
		if (isOld) {
			return ORGANISM_OLD_COLOR;
		} else {
			return ORGANISM_YOUNG_COLOR;
		}
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
		
		ageCounter++;
		if (ageCounter > AGE_THRESHOLD) {
			isOld = true;
		}
		
		if (energyleft > REPRODUCTION_THRESHOLD) {
			return generateMove(REPRODUCE, getOrthogonal(moveDirection), generateChildState());
		}
		
		if (foodleft > 0) {
			return generateMove(STAYPUT);
		}
		
		
		
		for (int i = 1; i < 5; i++) {
			if (food[i] && enemy[i] == -1) return generateMove(i);
		}
		
		/*
		for (int i = 1; i < 5; i++) {
			if (/*(enemy[i] & 0b10000000) == 128 && enemy[i] != -1 && energyleft > ENERGY_TO_BREAK_CLUSTER && isOld) {
				for (int j = 1; j < 5; j++) {
					if (enemy[j] == -1) return generateMove(j);
				}
			}
		}*/
		
		if (isOld) {
			return generateMove(STAYPUT);
		} else if (enemy[moveDirection] != -1) {
			return generateMove(getOrthogonal(moveDirection));
		} else if (energyleft > ENERGY_PER_MOVE * MOVE_ENERGY_THRESHOLD) {
			return generateMove(moveDirection);
		} else {
			return generateMove(STAYPUT);
		}
	}
	
	private Move generateMove(int type, int direction, int state) throws Exception {
		return new Move(REPRODUCE, direction, state);
	}
	
	private Move generateMove(int dir) throws Exception {
		return new Move(dir);
	}
	
	private int generateChildState() {
		return (((generation + 1) % (DELTA_THRESHOLD_PER_GEN - 1)) << 3) + getOrthogonal(moveDirection) + 128;
	}
	
	private int getOrthogonal(int move) {
		switch (move) {
		case NORTH: return WEST;
		case SOUTH: return EAST;
		case EAST: return NORTH;
		case WEST: return SOUTH;
		default: return WEST;
		}
	}
}
