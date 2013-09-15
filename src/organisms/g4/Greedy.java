package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Greedy implements Player {


	static final String _CNAME = "Group 4 Player";
	static final Color _CColor = new Color(0, 255, 255);
	private int state;
	private OrganismsGame game;

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		state = 255;
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
		
		int foodEnergy = game.u();
		int maxEnergy = game.v();
		
		switch (state) {
		
		case 0:
			state = 255;
			return new Move(REPRODUCE, NORTH, 255);
		
		default:
			if (foodleft >= 1 || energyleft > maxEnergy/2) {
				
				for (int i = 1; i < 5; i++) {
			    	if (food[i]) {
			    		return new Move(REPRODUCE, i, 255);
			    	}
			    }
				
			} else {
				
				if (foodleft*foodEnergy > energyleft/4) {
					for (int i = 1; i < 5; i++) {
						if (food[i]) return new Move(i);
					}
				}
			
				for (int i = 1; i < 5; i++) {
					if (food[i]) {
						return new Move(i);
					}
				}
				
				return new Move(WEST);
			}
		}
		//return null;
		return null;
		
		
	}

}
