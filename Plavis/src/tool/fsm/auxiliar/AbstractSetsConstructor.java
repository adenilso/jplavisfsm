package tool.fsm.auxiliar;

import java.util.ArrayList;

import fsm4ws.FiniteStateMachine;

public abstract class AbstractSetsConstructor {
	protected FiniteStateMachine fsm;
	protected ArrayList<String> stateCover;
	protected ArrayList<String> transitionCover;
	
	public AbstractSetsConstructor(FiniteStateMachine fsm) {
		this.fsm = fsm;
		construct();
	}
	
	public abstract void construct();
	
	public ArrayList<String> getStateCover() {
		return stateCover;
	}
	
	public ArrayList<String> getTransitionCover() {
		return transitionCover;
	}
}
