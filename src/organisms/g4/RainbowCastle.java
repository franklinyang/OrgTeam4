package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

@SuppressWarnings("serial")
public class RainbowCastle implements Player {

	static final String ORGANISM_NAME = "Rainbow Castle";
	static final Color ORGANISM_COLOR = new Color(155, 194, 158);
	private OrganismsGame game;
	
	// Game constants
	private static double MAX_ENERGY;
	private static double ENERGY_PER_FOOD;
	private static double ENERGY_PER_MOVE;
	
	private static int AGE_THRESHOLD = 15;
	private int REPRODUCTION_THRESHOLD = 400;
	
	private int moveDirection;
	private int ageCounter;
	private boolean isOld;
	private int state;

	@Override
	public void register(OrganismsGame game, int incomingState) throws Exception {
		
		MAX_ENERGY = game.M();
		ENERGY_PER_FOOD = game.u();
		ENERGY_PER_MOVE = game.v();
		isOld = false;
		
		if (incomingState == -1) {
			moveDirection = WEST;
		} else {
			moveDirection = incomingState;
		}
		
		state = 0;
		this.game = game;
	}

	@Override
	public String name() throws Exception {
	  return ORGANISM_NAME;
	}

	@Override
	public Color color() throws Exception {
		return ORGANISM_COLOR;
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
			return generateMove(REPRODUCE, getOrthogonal(moveDirection), getOrthogonal(moveDirection));
		}
		
		if (foodleft > 0) {
			return generateMove(STAYPUT);
		}
		
		for (int i = 1; i < 5; i++) {
			if (food[i] && enemy[i] == -1) return generateMove(i);
		}
		
		if (isOld) {
			return generateMove(STAYPUT);
		} else {
			return generateMove(moveDirection);
		}
	}
	
	private Move generateMove(int type, int direction, int state) throws Exception {
		return new Move(REPRODUCE, direction, state);
	}
	
	private Move generateMove(int dir) throws Exception {
		return new Move(dir);
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
