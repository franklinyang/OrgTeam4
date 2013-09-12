package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Offensive implements Player {


	static final String _CNAME = "The Offender";
	static final Color _CColor = new Color(143, 254, 183);
	private int state;
	private OrganismsGame game;
	private static final Random r = new Random();
	
	private static final int    BLOCK_THRESHOLD = 10;
	private static final double RANDOM_REPRODUCTION_THRESHOLD = 0.3;
	
	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		if (key == -1) {
			state = (r.nextInt(255) & 0b11111000);
		} else {
			state = key;
		}
		
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
		
		this.state = (Integer.toString(this.state & 0b11111000).hashCode() % 255) | (this.state & 0b00000111);
		
		int maxFood = game.K();
	
		// -1 for nothing, 0 for foe, 1 for friend
		int[] friends = new int[5];
		boolean friendNear = false;
		
		for (int i = 1; i < 5; i++) {
			if (enemy[i] == this.state) {
				friends[i] = 1;
				friendNear = true;
			} else if (enemy[i] == -1) {
				friends[i] = -1;
			}
		}
		
		if (friendNear) {
			for (int i = 1; i < 5; i++) {
				if (friends[i] == -1) {
					return new Move(i);
				}
			}
		}
		
		for (int i = 1; i < 5; i++) {
			if (enemy[i] != -1 && (enemy[i]) != (this.state) && energyleft > maxFood / BLOCK_THRESHOLD) {
				//game.println("Staying put to block enemies");
				return new Move(STAYPUT);
			}
		}
		
		if (energyleft > maxFood / 2 && new Random().nextDouble() > RANDOM_REPRODUCTION_THRESHOLD) {
			for (int i = 1; i < 5; i++) {
				if (food[i]) return new Move(REPRODUCE, i, this.state);
			}
		} else if (energyleft > maxFood / 2) {
			return new Move(new Random().nextInt(4) + 1);
		} else if (energyleft > maxFood / 4) {
			for (int i = 1; i < 5; i++) {
				if (!food[i]) return new Move(i);
			}
		} else {
			for (int i = 1; i < 5; i++) {
				if (food[i]) return new Move(i);
			}
		}
	
	
		
	
		

		return new Move(new Random().nextInt(4) + 1);
		/*
		toggle = !toggle;
		
		
		//game.println("State: " + Integer.toString(this.state & 0b00000111));
		switch(this.state & 0b00000111) {
		case NORTHWEST:
			int[] movesNW = {NORTH, WEST, SOUTH, EAST};
			int[] movesNW_tog = {WEST, NORTH, SOUTH, EAST};
			if (toggle) {
				for (int move : movesNW_tog) {
					if (enemy[move] == -1) return new Move(move);
				}
			} else {
				for (int move : movesNW) {
					if (enemy[move] == -1) return new Move(move);
				}
			}
			break;
		case NORTHEAST:
			int[] movesNE = {NORTH, EAST, SOUTH, WEST};
			int[] movesNE_tog = {EAST, NORTH, SOUTH, WEST};
			if (toggle) {
				for (int move : movesNE_tog) {
					if (enemy[move] == -1) return new Move(move);
				}
			} else {
				for (int move : movesNE) {
					if (enemy[move] == -1) return new Move(move);
				}
			}
			break;
		case SOUTHEAST:
			int[] movesSE = {SOUTH, EAST, NORTH, WEST};
			int[] movesSE_tog = {EAST, SOUTH, NORTH, WEST};
			if (toggle) {
				for (int move : movesSE_tog) {
					if (enemy[move] == -1) return new Move(move);
				}
			} else {
				for (int move : movesSE) {
					if (enemy[move] == -1) return new Move(move);
				}
			}
			break;
		case SOUTHWEST:
			int[] movesSW = {SOUTH, WEST, NORTH, EAST};
			int[] movesSW_tog = {WEST, SOUTH, NORTH, EAST};
			if (toggle) {
				for (int move : movesSW_tog) {
					if (enemy[move] == -1) return new Move(move);
				}
			} else {
				for (int move : movesSW) {
					if (enemy[move] == -1) return new Move(move);
				}
			}
			break;
		}
		*/
		
		
		
	}

}
