/**
 * Mef View Project
 * 
 * This class implements the functions that check if an FSM is initially connected
 * 
 * @author Arineiza Pinheiro
 * @date 2010 October
 */

package automata;

import java.util.*;

import automata.mealy.MealyTransition;

/**
 * The nondeterminism detector object can be used to find all the
 * nondeterministic states in an automaton (i.e. all states with equal outward
 * transitions).
 * 
 * @author Ryan Cavalcante
 */

public class MinimalDetector {
	/**
	 * Creates an instance of <CODE>CompletenessDetector</CODE>
	 */
	public MinimalDetector() {
	}
	
	
	public boolean isEqual(Transition t1, Transition t2) 
    {
        MealyTransition transition1 = (MealyTransition) t1;
        MealyTransition transition2 = (MealyTransition) t2;
        if(transition1.getLabel().equals(transition2.getLabel())) 
            return true;
        else if(transition1.getLabel().startsWith(transition2.getLabel()))
            return true;
        else if(transition2.getLabel().startsWith(transition1.getLabel()))
            return true;
        else
            return false;
    }

	/**
	 * Remove an element of the list
	 * @param list
	 * @param state
	 */
	public void removeElement(List list, State state) 
    {
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
        	if(iterator.next().equals(state)){
        		iterator.remove();
        	}
        }
    }

	/**
	 * Checks if an element is present in the list
	 * @param list
	 * @param state
	 * @return true if the element is presente or false otherwise
	 */
	public boolean hasElement(List list, State state) 
    {
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
        	if(iterator.next().equals(state)){
        		return true;
        	}
        }
        return false;
    }
	
	public boolean isEquivalent(List inputList, Automaton auto, State state1, State state2, int numberOfStates){
		

		if(numberOfStates<=1){
			System.out.println(true);
			return true;
			
		}
		int number = numberOfStates - 1;
		boolean isEqual = false;
		Transition[] tranState1 = auto.getTransitionsFromState(state1);
		Transition[] tranState2 = auto.getTransitionsFromState(state2);
		
		for(int i = 0; i < tranState1.length; i++){
			for(int k = 0; k < tranState2.length; k++){
				MealyTransition t1 = (MealyTransition) tranState1[i];
				MealyTransition t2 = (MealyTransition) tranState2[k];
				//System.out.println(t1.getLabel()+"/"+t1.getOutput()+" "+t2.getLabel()+"/"+t2.getOutput());
				if(t1.getLabel().equals(t2.getLabel())){
					if(t1.getOutput().equals(t2.getOutput())){
						isEqual = isEquivalent(inputList, auto, t1.getToState(), t2.getToState(), number);
						if(!isEqual) 
							return false;
					}
					else
						return false;
				}	
			}
		}
		return isEqual;
	}

	/**
	 * Debug Method
	 */
//	private void print(List list){
//		Iterator iterator = list.iterator();
//        while(iterator.hasNext()){
//        	System.out.println(iterator.next());
//        	
//        }
//	}
	
	/**
	 * Returns an array of states that was initially disconnected.
	 * 
	 * @return an array of states that was initially disconnected.
	 */

	public State[] getEquivalentStates(Automaton automaton) {
	
		/* Lits of all possible inputs */
		ArrayList inputList = new ArrayList() ;
		
		/* List to iterate the automaton */
		ArrayList<State> stateList = new ArrayList();
		
		/* Get all states in automaton. */
		State[] states = automaton.getStates();
		
		LambdaTransitionChecker lc = LambdaCheckerFactory
		.getLambdaChecker(automaton);
		
		/*Check all transitions to collect all possible inputs*/
		Transition[] transitions = automaton.getTransitions();
		for(int j = 0; j < transitions.length; j++){
			MealyTransition t1 = (MealyTransition) transitions[j];
			for(int l = j+1; l < transitions.length; l++){
				MealyTransition t2 = (MealyTransition) transitions[l];
				if( (!isEqual(t1, t2)) && 
					(!inputList.contains(t1.getLabel())) && 
					(!lc.isLambdaTransition(t1))){
						inputList.add(t1.getLabel());
				}
			}
		}		
		
		int count = 0;
		for (int k = 0; k < states.length; k++) {
			for(int j = k+1; j  <states.length; j++){
				//System.out.println(states[k].getName()+" "+states[j].getName());
				if(isEquivalent(inputList, automaton, states[k], states[j], states.length)){
//					if(!hasElement(stateList,states[k])){
//						stateList.add(states[k]);
//					}
//					if(!hasElement(stateList,states[j])){
//						stateList.add(states[j]);
//					}
					//The pair of states are inserted
					stateList.add(states[k]);
					stateList.add(states[j]);
					
				}
			}
		}
		return (State[]) stateList.toArray(new State[0]);
	}

}
