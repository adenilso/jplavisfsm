package fsm4ws.generationMethod;

import java.util.ArrayList;

import fsm4ws.FiniteStateMachine;

public abstract class AbstractGenerator 
{
	FiniteStateMachine fsm;
	ArrayList<String> sequences;
	
	public AbstractGenerator(FiniteStateMachine fsm) 
	{	
		this.fsm = fsm;
		generateSequences();
	}

	public ArrayList<String> getSequences() {
		return sequences;
	}
	
	public abstract void generateSequences();
}
