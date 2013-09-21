package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

@SuppressWarnings("serial")
public class RainbowCastle implements Player {

	static final String ORGANISM_NAME = "Rainbow Castle";
	static final Color ORGANISM_YOUNG_COLOR = new Color(50, 255, 100);
	static final Color ORGANISM_OLD_COLOR = new Color(50, 100, 255);
	private OrganismsGame game;
	
	// Game constants
	private static double MAX_ENERGY;
	private static double ENERGY_PER_FOOD;
	private static double ENERGY_PER_MOVE;
	private static int AGE_THRESHOLD = 20;
	
	
	private double reproductionThreshold = 0.72;
	private double moveEnergyThreshold = 0.0;
	private int moveDirection;
	
	private boolean isOld;
	
	//Wallz
	private boolean recover;
	private int recoverDirection;
	
	private int ageCounter;
	private int generation;
	private int state;

	@Override
	public void register(OrganismsGame game, int incomingState) throws Exception {
		
		MAX_ENERGY = game.M();
		ENERGY_PER_FOOD = game.u();
		ENERGY_PER_MOVE = game.v();
		isOld = false;
		
		
		
		if (incomingState == -1) {
			state = 128 + WEST;
			moveDirection = WEST;
		} else {
			this.state = incomingState + 128;
			moveDirection = incomingState; 
		}

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
		
		if (energyleft > MAX_ENERGY * reproductionThreshold) {
			if (isOld) {
				isOld = false;
				ageCounter = 0;
			}
			return generateMove(REPRODUCE, getOrthogonal(moveDirection), getOrthogonal(moveDirection));
		}
		
		if (foodleft > 0) {
			return generateMove(STAYPUT);
		}
		
		for (int i = 1; i < 5; i++) {
			if (food[i] && enemy[i] == -1) return generateMove(i);	
			// Stalk non-RainbowCastle organisms
			if (!isOld && (enemy[i] & 0b10000000) != 0b10000000 && enemy[i] != -1) return generateMove(i);
			
		}	
		
		if (isOld) {
			return generateMove(STAYPUT);
		} else if (energyleft > MAX_ENERGY * moveEnergyThreshold) {
			if (enemy[moveDirection % 5] != -1) {
				moveDirection = moveDirection + 1;
			}
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
	
	
	private int getOrthogonal(int move) {
		switch (move) {
		case NORTH: return (new Random().nextBoolean() ? WEST : EAST);
		case SOUTH: return (new Random().nextBoolean() ? WEST : EAST);
		case EAST: return (new Random().nextBoolean() ? NORTH : SOUTH);
		case WEST: return (new Random().nextBoolean() ? NORTH : SOUTH);
		default: return WEST;
		}
	}
}
