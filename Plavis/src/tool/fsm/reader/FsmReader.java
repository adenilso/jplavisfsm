package tool.fsm.reader;

import java.util.HashMap;

import fsm4ws.FiniteStateMachine;
import fsm4ws.State;
import fsm4ws.Transition;


public class FsmReader {
	FiniteStateMachine fsm;
	HashMap<String, State> states;
	
	public FsmReader() {
		fsm = new FiniteStateMachine();
		states = new HashMap<String, State>();
	}
	
	public FiniteStateMachine getFsm() 
		throws Exception
	{
		if(fsm.getStates().size() == 0 || fsm.getTransitions().size() == 0)
			throw new Exception("Empty FSM");
		
		return fsm;
	}
	
	public void addTransition(String line) 
		throws Exception
	{
		//  s1 * ignore s1
		// st1 input output st2
		String token[] = line.split(" ");
		if(token.length != 4)
			throw new Exception("Non well formed transition");
		
		State s1 = states.get(token[0]);
		if(s1 == null) {
			s1 = new State(token[0]);
			if(fsm.getStates().size() == 0)
				fsm.setInitialState(s1);
			fsm.addState(s1);		
			
			states.put(token[0], s1);
		}
		
		State s2 = states.get(token[3]);
		if(s2 == null) {
			s2 = new State(token[3]);	
			fsm.addState(s2);		
			
			states.put(token[3], s2);
		}
		
		Transition t = new Transition(s1, s2, token[1], token[2]);
		fsm.addTransition(t);
	}
}
