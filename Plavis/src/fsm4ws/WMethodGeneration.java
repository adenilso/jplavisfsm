package fsm4ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tool.fsm.auxiliar.CharacterizationSetConstructor;
import tool.fsm.reader.FsmReader;

import fsm4ws.generationMethod.WMethod;

import automata.Automaton;
import automata.State;
import automata.mealy.MealyTransition;

public class WMethodGeneration {

	
	public WMethodGeneration(Automaton automaton){
		
		this.fsm = null;
		this.automaton = automaton;
		
	}
	
	public fsm4ws.State hasElement(ArrayList<fsm4ws.State> list, String state) 
    {
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
        	fsm4ws.State s = (fsm4ws.State) iterator.next();
        	if(s.getLabel().equals(state)){
        		return s;
        	}
        }
        return null;
    }
	
	/**
	 * Debug Method
	 */
	private void printFSM(){
		System.out.println("nS: "+this.fsm.getNumberOfStates());
		System.out.println("nT: "+this.fsm.getNumberOfTransitions());
		
		ArrayList states = this.fsm.getStates();
		ArrayList transition = this.fsm.getTransitions();
		
		Iterator iteratorState = states.iterator();
		while(iteratorState.hasNext()){
			System.out.println("S: "+iteratorState.next());
		}
		Iterator iteratorTrans = transition.iterator();
		while(iteratorTrans.hasNext()){
			fsm4ws.Transition t = (fsm4ws.Transition) iteratorTrans.next();
			System.out.println(t.getIn().getLabel()+" -- "+t.getInput()+"/"+t.getOutput()+" -> "+t.getOut().getLabel());
		}
		System.out.println("Initial: "+this.fsm.getInitialState());
		
	}
	/**
	 * Method that encodes the FSM format of W Method
	 * @param automaton
	 * @return a FSM encoded 
	 * @author arineiza
	 */
	public ArrayList getSequences(){
		
		this.fsm = new FiniteStateMachine();
		
		//automata.Transition[] transitions = automaton.getTransitions();
		automata.State[] states = this.automaton.getStates();
		
		automata.State initialState = this.automaton.getInitialState();
		automata.Transition[] transFromInitialState = automaton.getTransitionsFromState(initialState);
		

		this.fsm = new FiniteStateMachine();
		FsmReader reader = new FsmReader();
		try {
			
			for(int i = 0; i < transFromInitialState.length; i++){
				MealyTransition t1 = (MealyTransition) transFromInitialState[i];
				//System.out.println(t1.getFromState().getName()+" "+t1.getLabel()+" "+t1.getOutput()+" "+t1.getToState().getName());
				reader.addTransition(t1.getFromState().getName()+" "+t1.getLabel()+" "+t1.getOutput()+" "+t1.getToState().getName());
			}
			for(int j = 0; j < states.length; j++){
				if(!states[j].getName().equals(initialState.getName())){
					automata.Transition[] transitions = automaton.getTransitionsFromState(states[j]);
					for(int i = 0; i < transitions.length; i++){
						MealyTransition t = (MealyTransition) transitions[i];
						//System.out.println(t.getFromState().getName()+" "+t.getLabel()+" "+t.getOutput()+" "+t.getToState().getName());
						reader.addTransition(t.getFromState().getName()+" "+t.getLabel()+" "+t.getOutput()+" "+t.getToState().getName());
					}
				}
			}
	
			fsm = reader.getFsm();
			printFSM();
//			System.out.println();
//			System.out.println("W Method - sequences");
			WMethod wmethod = new WMethod(fsm);
//			for(String s : wmethod.getSequences())
//			System.out.println(s);
//	
//			System.out.println("# of TCs:" + wmethod.getSequences().size());
			
			return wmethod.getSequences();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} 
	}
		
	private FiniteStateMachine fsm;
	private Automaton automaton;
}
