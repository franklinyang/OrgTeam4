package organisms.g4;

import java.awt.Color;
import java.util.HashMap;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class PredictorPlayer implements Player {

	static final String _CNAME = "Predictor Player";
	static final Color _CColor = new Color(1.0f, 0.67f, 0.67f);
	private int state;
	private OrganismsGame game;
	HashMap<String, Integer> history = new HashMap<String, Integer> ();

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
		int seenProfessors;
		int maxEnergy = game.M();
		int maxFood = game.K();
		int neighbors = 0;
		int location = 0;
		double density = 0;
		for (int dir=1; dir < 5; dir++) { neighbors = (enemy[dir] > 0) ? neighbors+1 : neighbors; }
		

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
		
		//state != 0 implies professor
		if(state != 0) {
			int prev = 0;
			String current = "";
			location++;
			location = location % 4;
			switch(location) {
				case 0: current = "sw";
					break;
				case 1: current = "nw";
					break;
				case 2: current = "ne";
					break;
				case 3: current = "se";
					break;
			}
			prev = history.get(current);
			history.put(current,  foodleft);
			switch(location) {
				case 0: return new Move(NORTH);
				case 1: return new Move(EAST);
				case 2: return new Move(SOUTH);
				case 3: return new Move(WEST);
			}
		} else {
		
			if (energyleft > maxEnergy*threshold) {
				return new Move(REPRODUCE, NORTH, 1);
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
						return new Move(dir+1);
					}
				}	
			}
			return new Move(STAYPUT);
		}
		return null;
	}

}
