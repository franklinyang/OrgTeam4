package organisms.g4;

import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

@SuppressWarnings("serial")
public class IceNine implements Player {

	static final String _CNAME = "Ice Nine";
	static final Color _CColor = new Color(143, 220, 224);
	private int state;
	private OrganismsGame game;

	private int lastNorthState = -1;
	private int lastSouthState = -1;
	private int lastEastState  = -1;
	private int lastWestState  = -1;
	
	private static final int SPAWN_THRESHOLD = 1;
	
	// States for the role of any particular instance of this organism
	private static final int GEN_STATE  = 0b11110000;
	private static final int NW_STATE   = 0b00000000;
	private static final int NE_STATE   = 0b00010000;
	private static final int W_STATE    = 0b00100000;
	private static final int E_STATE    = 0b00110000;
	private static final int SW_STATE   = 0b01000000;
	private static final int SE_STATE   = 0b01010000;
	private static final int CON_STATE  = 0b01100000; // Initial SW organism

	// Communicating who is the next person to go
	private static final int FIRST  = 0b00000001;  // First clockwise from NORTH
	private static final int SECOND = 0b00000001;  // Second clockwise from NORTH
	private static final int TRUE = 1;
	private static final int FALSE = 0;
	private boolean inCenter = false;
	private boolean nextToMove = false; // -1 allows for communication of beginning of game state.
	

	// List of moves for the first organism to take during the beginning of the
	// game
	private static Move[] initialMoves;
	private int initialMoveCounter = 0;

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		if (key == -1) {
			this.state = generateState(GEN_STATE);
		} else {
			this.state = key;
			if (this.state == W_STATE || this.state == E_STATE) {
				nextToMove = true;
			}
			// By convention, the side elements of each farm move in first.
		}
		
		this.game = __amoeba;
		
		initialMoves = new Move[] { 
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				// 100!!!!
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT),
				new Move(STAYPUT), 
				new Move(REPRODUCE, EAST, E_STATE),
				new Move(REPRODUCE, NORTH, NE_STATE),
				new Move(REPRODUCE, SOUTH, SE_STATE),
				new Move(WEST), 
				new Move(WEST) };
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
		Move m = null;
		
		switch (state & 0b11110000) {
		case GEN_STATE:
			m = getGenesisMove(food, enemy, foodleft, energyleft);
			break;
		case NW_STATE:
			m = getNWMove(food, enemy, foodleft, energyleft);
			break;
		case NE_STATE:
			m = getNEMove(food, enemy, foodleft, energyleft);
			break;
		case W_STATE:
			m = getWMove(food, enemy, foodleft, energyleft);
			break;
		case E_STATE:
			m = getEMove(food, enemy, foodleft, energyleft);
			break;
		case SE_STATE:
			m = getSEMove(food, enemy, foodleft, energyleft);
			break;
		case SW_STATE:
			m = getSWMove(food, enemy, foodleft, energyleft);
			break;
		}
		
		// Update to last state of a *seen* organism.
		lastNorthState = enemy[NORTH] != -1 ? 0 : lastNorthState + 1;
		lastSouthState = enemy[SOUTH] != -1 ? 0 : lastSouthState + 1;
		lastEastState  = enemy[EAST]  != -1 ? 0 : lastEastState  + 1;
		lastWestState  = enemy[WEST]  != -1 ? 0 : lastWestState  + 1;
		return m;
	}

	private Move getGenesisMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		if (initialMoveCounter == initialMoves.length) {
			this.state = W_STATE;
			return new Move(STAYPUT);
		} else {
			return initialMoves[initialMoveCounter++];
		}
	}

	private Move getNWMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		
		if (enemy[EAST] == -1 && lastEastState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, EAST, NE_STATE);
		} else
			
		if (enemy[NORTH] == -1 && lastNorthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, NORTH, E_STATE);
		} else if (enemy[WEST] == -1 && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, WEST, SE_STATE);
		}
		return new Move(STAYPUT);
	}

	private Move getNEMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		if (enemy[WEST] == -1 && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, WEST, NW_STATE);
		} else if (enemy[NORTH] == -1 && lastNorthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, NORTH, W_STATE);
		} else if (enemy[EAST] == -1 && lastEastState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, EAST, SW_STATE);
		}
		return new Move(STAYPUT);
	}

	private Move getWMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		/*if (enemy[WEST] == -1  && !inCenter && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, WEST, E_STATE);
		} else */if (enemy[NORTH] == -1 && !inCenter && lastNorthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, NORTH, SE_STATE);
		} /*else if (enemy[SOUTH] == -1 && !inCenter && lastSouthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, SOUTH, NE_STATE);
		}*/
		
		return new Move(STAYPUT);
	}

	private Move getEMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		/*if (enemy[EAST] == -1 && !inCenter && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, EAST, W_STATE);
		} else */if (enemy[NORTH] == -1 && !inCenter && lastNorthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, NORTH, SW_STATE);
		} /* else if (enemy[SOUTH] == -1 && !inCenter && lastSouthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, SOUTH, NW_STATE);
		} */
		
		return new Move(STAYPUT);
	}

	private Move getSWMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		/*if (enemy[EAST] == -1 && lastEastState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, EAST, SE_STATE);
		} else if (enemy[SOUTH] == -1 && lastSouthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, SOUTH, E_STATE);
		} else */ if (enemy[WEST] == -1 && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, WEST, NE_STATE);
		} 
		
		return new Move(STAYPUT);
	}

	private Move getSEMove(boolean[] food, int[] enemy, int foodLeft,
			int energyLeft) throws Exception {
		// REPAIR 
		/*if (enemy[WEST] == -1 && lastWestState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, WEST, SW_STATE);
		} else if (enemy[SOUTH] == -1 && lastSouthState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, SOUTH, W_STATE);
		}  else */if (enemy[EAST] == -1 && lastEastState > SPAWN_THRESHOLD) {
			return new Move(REPRODUCE, EAST, NW_STATE);
		}
		return new Move(STAYPUT);
	}

	private static int generateState(int... states) {
		int finalState = 0;
		for (int s : states)
			finalState = finalState | s;
		return finalState;
	}
}
