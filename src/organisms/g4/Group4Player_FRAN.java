package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player_FRAN implements Player {


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
		int threshold = 2*maxEnergy/3;
		boolean foodExists = false; // food in immediate vicinity
		boolean[] inhabit = new boolean[5];
		for (int i=1; i < 5; i++) {
			inhabit[i] = food[i] && (enemy[i] == -1); 
		}
		for (int dir=1; dir < 5; dir++) { 
			if(food[dir] || foodExists)
				foodExists = true;
		}
		
		if(state < 30) {
			thrive(food, enemy, foodleft, energyleft, foodExists, inhabit);
		}
		
		switch (state) {
			
			case 0:
				state++;
				if (energyleft > threshold)
					return new Move(REPRODUCE, SOUTH, 255);
			
			case WEST:
				state = 255;
				return findFood(inhabit);
			
			default:

				if (energyleft > threshold)
					return new Move(REPRODUCE, SOUTH, 2);

				else if(foodleft != 0 && foodExists) {
					return findFood(inhabit);
				}

				return thrive(food, enemy, foodleft, energyleft, foodExists, inhabit);

		}
	}
	
	public Move thrive(boolean[] food, int[] enemy, int foodleft, int energyleft, 
			boolean foodExists, boolean[] inhabit) throws Exception {
		
		Random generator = new Random();
		
		if(foodleft == 0) {
			if (foodExists) {
				return findFood(inhabit);
			}
			else {
				if(generator.nextInt(4) > 2) {
					for(int dir = 1; dir < 5; dir++) {
						if(enemy[dir] == -1)
							return new Move(dir);
					}
				}
			}
		}
		
		return new Move(STAYPUT);
	}
	
	public Move findFood(boolean[] inhabit) throws Exception{
		for(int dir = 1; dir < 5; dir++) {
			if(inhabit[dir])
				return new Move(dir);
		}
		return new Move(STAYPUT);
	}
}