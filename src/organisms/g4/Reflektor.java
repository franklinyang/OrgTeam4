package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

@SuppressWarnings("serial")
public class Reflektor implements Player {

	static final String ORGANISM_NAME = "Reflektor";
	static final Color ORGANISM_COLOR = new Color(155, 194, 158);
	private OrganismsGame game;
	
	// Game constants
	private static double MAX_ENERGY;
	private static double ENERGY_PER_FOOD;
	private static double ENERGY_PER_MOVE;
	
	/*
	 * KNOBS AND SWITCHES!
	 * 
	 * PME = Percent Max Energy
	 * FUBT = Food Units Below Threshold
	 * 
	 * DENS_SCALE = Density Scaling Factor
	 * FL_SCALE = Food Left Scaling Factor
	 * 
	 * NTN = Nothing To Nothing
	 * NTF = Nothing To Food
	 * FTN = Food To Nothing
	 * FTF = Food To Food
	 * MV_REG = Move regularly
	 * MV_BRK = Move to break clusters
	 * MV_CLN = Move to clone
	 */
	private static final double TURNS_TO_STRATEGY_CHANGE = 10;
	
	private static final double PME_MV_REG = 0.2;
	private static final double PME_MV_BRK = 0.6;
	private static final double PME_MV_CLN = 0.5;
	private static final double PME_REP_NTN = 0.9;
	private static final double PME_REP_NTF = 0.5;
	private static final double PME_REP_FTN = 0.7;
	private static final double PME_REP_FTF = 0.1;
	
	/*
	private static final double FUBT_MV_REG = 0.7;
	private static final double FUBT_MV_BRK = 0.7;
	private static final double FUBT_MV_CLN = 0.7;
	private static final double FUBT_REP_NTN = 0.7;
	private static final double FUBT_REP_NTF = 0.7;
	private static final double FUBT_REP_FTN = 0.7;
	*/
	private static final double FUBT_REP_FTF = 3.0;
	
	/*
	private static final double FL_SCALE_MV_REG = 0.0;
	private static final double FL_SCALE_MV_BRK = 0.0;
	private static final double FL_SCALE_MV_CLN = 0.0;
	private static final double FL_SCALE_PME_REP_NTN = 0.0;
	private static final double FL_SCALE_PME_REP_NTF = 0.0;
	private static final double FL_SCALE_PME_REP_FTN = 0.0;
	private static final double FL_SCALE_PME_REP_FTF = 0.0;
	*/
	
	// DECREASE IS MORE AGGRESSIVE
	private static final double DENS_SCALE_MV_REG = 0.05;
	private static final double DENS_SCALE_MV_BRK = -0.2;
	private static final double DENS_SCALE_MV_CLN = 0.1;
	private static final double DENS_SCALE_PME_REP_NTN = 0.2;
	private static final double DENS_SCALE_PME_REP_NTF = 0.1;
	private static final double DENS_SCALE_PME_REP_FTN = -0.1;
	private static final double DENS_SCALE_PME_REP_FTF = -0.3;
	
	
	// Live values, updated to reflect how much values are scaled due to environmental factors.
	private double effective_PME_MV_REG;
	private double effective_PME_MV_BRK;
	private double effective_PME_MV_CLN;
	private double effective_PME_REP_NTN;
	private double effective_PME_REP_NTF;
	private double effective_PME_REP_FTN;
	private double effective_PME_REP_FTF;
	
	private double effective_FUBT_MV_REG;
	private double effective_FUBT_MV_BRK;
	private double effective_FUBT_MV_CLN;
	private double effective_FUBT_REP_NTN;
	private double effective_FUBT_REP_NTF;
	private double effective_FUBT_REP_FTN;
	private double effective_FUBT_REP_FTF;
	
	
	private int currentMoveDirection;
	private int strategyCounter;
	private int state;

	@Override
	public void register(OrganismsGame game, int incomingState) throws Exception {
		
		MAX_ENERGY = game.M();
		ENERGY_PER_FOOD = game.u();
		ENERGY_PER_MOVE = game.v();
		
		currentMoveDirection = new Random().nextInt(4) + 1;
		
		if (incomingState == -1) {
			this.state = 128;
		} else {
			this.state = incomingState;
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
		
		strategyCounter++;
		if (strategyCounter >= TURNS_TO_STRATEGY_CHANGE) {
			strategyCounter = 0;
			currentMoveDirection = new Random().nextInt(4) + 1;
		}
		
		// Count the number of organisms in your neighborhood and shout it loud and proud
		int neighbors = 0;
		int extendedNeighbors = 0; 
		// This method will double-count on extended neighbors; this is actually useful, as it gives us an idea 
		// of how densely packed the immediate area around the cell is.
		for (int i = 1; i < 5; i++) {
			neighbors = neighbors + (enemy[i] == -1 ? 0 : 1);
			if ((enemy[i] & 0b10000000) == 128) {
				extendedNeighbors += enemy[i] - 128;
			}
		}
		extendedNeighbors = neighbors;
		this.state = 128 + neighbors;
		
		if (neighbors == 4) {
			return generateMove(STAYPUT, 0, this.state);
		}
		
		// Calculate effectives based on local state
		effective_PME_MV_REG  = Math.max(PME_MV_REG + DENS_SCALE_MV_REG * extendedNeighbors, 0);
		effective_PME_MV_BRK  = Math.max(PME_MV_BRK + DENS_SCALE_MV_BRK * extendedNeighbors, 0);
		effective_PME_MV_CLN  = Math.max(PME_MV_CLN + DENS_SCALE_MV_CLN * extendedNeighbors, 0);
		effective_PME_REP_NTN = Math.max(PME_REP_NTN + DENS_SCALE_PME_REP_NTN * extendedNeighbors, 0);
		effective_PME_REP_NTF = Math.max(PME_REP_NTF + DENS_SCALE_PME_REP_NTF * extendedNeighbors, 0);
		effective_PME_REP_FTN = Math.max(PME_REP_FTN + DENS_SCALE_PME_REP_FTN * extendedNeighbors, 0);
		effective_PME_REP_FTF = Math.max(PME_REP_FTF + DENS_SCALE_PME_REP_FTF * extendedNeighbors, 0);
		
		/*
		effective_FUBT_MV_REG  = Math.max(FUBT_MV_REG, 0);
		effective_FUBT_MV_BRK  = Math.max(FUBT_MV_BRK, 0);
		effective_FUBT_MV_CLN  = Math.max(FUBT_MV_CLN, 0);
		effective_FUBT_REP_NTN = Math.max(FUBT_REP_NTN, 0);
		effective_FUBT_REP_NTF = Math.max(FUBT_REP_NTF, 0);
		effective_FUBT_REP_FTN = Math.max(FUBT_REP_FTN, 0);
		*/
		effective_FUBT_REP_FTF = Math.max(FUBT_REP_FTF, 0);
		
		// Check whether there is food adjacent to the current square, and mark its direction
		int foodDirection = -1;
		boolean foodAdjacent = false;
		for (int i = 1; i < 5; i++) {
			if (food[i] && enemy[i] == -1) {
				foodAdjacent = true;
				foodDirection = i;
				break;
			}
		}
		
		// REPRODUCTION PROCESS
		if (foodleft == 0) {
			if (foodAdjacent && energyleft > effective_PME_REP_NTF * MAX_ENERGY) {
				return generateMove(REPRODUCE, nextAvailableFood(food), state);
			} else if (!foodAdjacent && energyleft > effective_PME_REP_NTN * MAX_ENERGY) {
				return generateMove(REPRODUCE, getDiffDir(currentMoveDirection), state);
			}
		} else {
			if (foodAdjacent && (energyleft > (MAX_ENERGY - effective_FUBT_REP_FTF * ENERGY_PER_FOOD) 
					|| foodleft > effective_FUBT_REP_FTF)) {
				return generateMove(REPRODUCE, nextAvailableFood(food), state);
			} else if (!foodAdjacent && energyleft > effective_PME_REP_FTN * MAX_ENERGY) {
				return generateMove(REPRODUCE, getDiffDir(currentMoveDirection), state);
			}
		}
	
		// MOVE PROCESS
		if (foodleft == 0) {
			if (foodAdjacent) {
				return generateMove(foodDirection, 0, this.state); 
			} else if (energyleft > effective_PME_MV_REG * MAX_ENERGY) {
				if (neighbors > 0 && energyleft > effective_PME_MV_BRK * MAX_ENERGY) {
					return generateMove(nextAvailableMove(enemy), 0, this.state);
				} else {
					return generateMove(currentMoveDirection, 0, this.state);
				}
			}
		} else {
			if (foodleft * ENERGY_PER_FOOD > energyleft / 2 && energyleft > 
					(effective_PME_MV_CLN * MAX_ENERGY + ENERGY_PER_MOVE)) {
				return generateMove(nextAvailableMove(enemy), 0, this.state);
			} else if (foodAdjacent) {
				return generateMove(nextAvailableFood(food), 0, this.state);
			} else {
				return generateMove(STAYPUT, 0, this.state);
			}
		}
		
		
	
		return new Move(STAYPUT);
	}
	
	// Helper method so that any checks that must *always* occur at end of turn can be centralized.
	private Move generateMove(int type, int direction, int state) throws Exception {
		if (type == REPRODUCE) {
			return new Move(REPRODUCE, direction, state);
		} else {
			return new Move(type);
		}
	}
	
	private int getDiffDir(int direction) {
		int newDirection = direction;
		Random rand = new Random();
		while (newDirection == direction) {
			newDirection = rand.nextInt(4) + 1;
		}
		
		return newDirection;
	}
	
	private int nextAvailableMove(int[] enemies) {
		Random rand = new Random();
		int i = rand.nextInt(4) + 1;
		for (int j = 0; j < 100; j++) {
			if (enemies[i] == -1) return i;
		}
		
		return i;
	}
	
	private int nextAvailableFood(boolean[] food) {
		Random rand = new Random();
		int i = rand.nextInt(4) + 1;
		for (int j = 0; j < 100; j++) {
			if (food[i]) return i;
		}
		
		return i;
	}
}
