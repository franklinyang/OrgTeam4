package organisms.g4;
import java.awt.Color;
import java.util.Random;

import organisms.Move;
import organisms.OrganismsGame;
import organisms.Player;

public class Group4Player_FRANK implements Player {


	static final String _CNAME = "Group 4 Player";
	static final Color _CColor = new Color(0.5f, 0.62f, 0.34f);
	
	private int state, changeStrategy;
	private OrganismsGame game;
	private int boxedIn = -1;
	
	private static double TURNS_TO_CHANGE_STRATEGY = 12;
	private static double ENERGY_PER_FOOD_MOVE_FREELY = 1.9;  // greater is conservative
	private static double ENERGY_PER_FOOD_REP_TO_FOOD_OFF_FOOD = 1.6;  // less is conservative
	private static double ENERGY_PER_FOOD_REP_TO_FREE_FROM_FOOD = 2.1; // less is conservative
	private static double ENERGY_PER_FOOD_REP_TO_FREE_OFF_FOOD = 1.3; // less is conservative
	private static double ENERGY_PER_FOOD_REP_TO_FOOD_FROM_FOOD = 2.5; // less is conservative
	private static double FRAC_MAX_ENERGY_BREAK_CLUSTER = 0.4;
	private static double SCALE_PER_ADJ_MOVE_FREELY = 0.075;  
	private static double SCALE_PER_ADJ_REP_TO_FOOD_OFF_FOOD = 0.3;
	private static double SCALE_PER_ADJ_REP_TO_FREE_FROM_FOOD = -0.3;
	private static double SCALE_PER_ADJ_REP_TO_FREE_OFF_FOOD = -0.35;
	private static double SCALE_PER_ADJ_REP_TO_FOOD_FROM_FOOD = 0.3;  

	@Override
	public void register(OrganismsGame __amoeba, int key) throws Exception {
		
		state = key;
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
		
		changeStrategy++;
		
		if(changeStrategy >= TURNS_TO_CHANGE_STRATEGY) {
			changeStrategy = 0;
			state = (state % 5) + 1;
		}

		double maxEnergy = game.M();
		double perUnitEnergy = game.u();
		double perUnitMove = game.v();
		int surrounding = 0;
		boolean foodExists = false; // food in immediate vicinity
		boolean[] inhabit = new boolean[5];
		Random rand = new Random(); // maybe we should generate a random direction?
		
		if(state == 0)
			state = rand.nextInt(4)+1;
		for (int i=1; i<5; i ++) {
			inhabit[i] = food[i] && (enemy[i] == -1); 
			if(enemy[i] >= 0) { surrounding ++; }
		}
		
		if(surrounding == 4) { boxedIn = 0; return new Move(STAYPUT); }
		else { 
			if(boxedIn != -1) { boxedIn ++; }
		}
	
		// Effective values after scaling for density.
		double EFF_ENERGY_PER_FOOD_MOVE_FREELY = Math.max(ENERGY_PER_FOOD_MOVE_FREELY + SCALE_PER_ADJ_MOVE_FREELY * surrounding, 0);
		double EFF_ENERGY_PER_FOOD_REP_TO_FOOD_OFF_FOOD = Math.max(ENERGY_PER_FOOD_REP_TO_FOOD_OFF_FOOD + SCALE_PER_ADJ_REP_TO_FOOD_OFF_FOOD * surrounding, 0);  
		double EFF_ENERGY_PER_FOOD_REP_TO_FREE_FROM_FOOD = Math.max(ENERGY_PER_FOOD_REP_TO_FREE_FROM_FOOD + SCALE_PER_ADJ_REP_TO_FREE_FROM_FOOD * surrounding, 0); 
		double EFF_ENERGY_PER_FOOD_REP_TO_FREE_OFF_FOOD = Math.max(ENERGY_PER_FOOD_REP_TO_FREE_OFF_FOOD + SCALE_PER_ADJ_REP_TO_FREE_OFF_FOOD * surrounding, 0);
		double EFF_ENERGY_PER_FOOD_REP_TO_FOOD_FROM_FOOD = Math.max(ENERGY_PER_FOOD_REP_TO_FOOD_FROM_FOOD + SCALE_PER_ADJ_REP_TO_FOOD_FROM_FOOD * surrounding, 0);
		
		for (int dir=1; dir<5; dir++) { 
			if(inhabit[dir]) {
				foodExists = true;
				break;
			}
		}
		

		if (foodExists) {
			if (foodleft > 0 && energyleft > maxEnergy - (EFF_ENERGY_PER_FOOD_REP_TO_FOOD_FROM_FOOD *perUnitEnergy)) {
				return repToFreeFood(inhabit, state);
			}
			if(energyleft >= maxEnergy-(EFF_ENERGY_PER_FOOD_REP_TO_FOOD_OFF_FOOD*perUnitEnergy)) {
				return repToFreeFood(inhabit, state);
			}
			else {
				return moveToFood(food);
			}
		}
		
		if(energyleft >= (maxEnergy-EFF_ENERGY_PER_FOOD_REP_TO_FREE_OFF_FOOD*perUnitEnergy)) {
			return repToFree(enemy, state);
		}
		
		if(foodleft > 0) {
			if(energyleft >= maxEnergy-(EFF_ENERGY_PER_FOOD_REP_TO_FREE_FROM_FOOD*perUnitEnergy)) {
				return repToFree(enemy, state);
			}
			return new Move(STAYPUT);
		}

		/*if(energyleft <= maxEnergy*.025) {
			for (int i=state; i<state+5; i ++) {
				if(enemy[i % 5] == -1) { return new Move(i % 5); } 
			}
		}*/
		
	    else {
	    	
	    	
	    	//game.println(Double.toString(ENERGY_PER_FOOD_MOVE_FREELY * perUnitEnergy));
	    	if(energyleft >= (double) EFF_ENERGY_PER_FOOD_MOVE_FREELY*perUnitEnergy) {
		    	
		    		return new Move(state);
		    	
	    
	    	}
			/*if(boxedIn > 0 && boxedIn < 5) {
				for(int i = state; i < state + 5; i ++) {
					if(enemy[i % 5] == -1) { return new Move(i % 5); }
				}
			}*/
	    }
		return new Move(STAYPUT);
	}
	
	private Move repToFreeFood(boolean[] inhabit, int state) throws Exception {
		int newState = (state + 1) % 5;
		this.state=newState;
		
		for(int dir=1; dir<5; dir++) {
			if(inhabit[dir])
				return new Move(REPRODUCE, dir, newState + 1);
		}
		
		return new Move(STAYPUT);
	}
	
	private Move repToFree(int[] enemy, int state) throws Exception {
		int newState = (state + 1) % 5;
		this.state=newState;
		for(int dir=1; dir<5; dir++) {
			if(enemy[dir] == -1)
				return new Move(REPRODUCE, dir, 1);
		}
		return new Move(STAYPUT);
	}
	
	private Move moveToFood(boolean[] food) throws Exception {
		for(int dir=1; dir<5; dir++) {
			if(food[dir])
				return new Move(dir);
		}
		return new Move(STAYPUT);
	}

}