package fsm4ws;

import java.util.ArrayList;
import java.util.Iterator;

public class FsmProperties {
	
	FiniteStateMachine fsm;
	
	public FsmProperties(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}
	
	public boolean isCompletelySpecified() {
		
		Iterator<State> states = fsm.getStates().iterator();
		//verify for each state
		while(states.hasNext()) 
		{
			State st = states.next();
			System.out.println("st:" + st.getLabel());
			Iterator<String> inputalphab = fsm.getInputAlphabet().iterator();
			while (inputalphab.hasNext()) {
				String input = inputalphab.next();
				System.out.println("->" + input);
				if(! inTransitions(input, st.getOut()))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean inTransitions(String input, ArrayList<Transition> outs) {
		
		for (Iterator<Transition> iterator = outs.iterator(); iterator.hasNext();) {
			Transition transition = iterator.next();
			if(input.equals(transition.getInput()))
				return true;
		}
		return false;
	}
	
	
}
