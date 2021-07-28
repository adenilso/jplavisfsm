/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
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

public class CompletenessDetector {
	/**
	 * Creates an instance of <CODE>CompletenessDetector</CODE>
	 */
	public CompletenessDetector() {
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
	 * Returns an array of states that was partially specified.
	 * 
	 * @return an array of states that partially specified.
	 */

	public State[] getPartiallySpecifiedStates(Automaton automaton) {
		LambdaTransitionChecker lc = LambdaCheckerFactory
				.getLambdaChecker(automaton);
		ArrayList list = new ArrayList();
		/**/
		ArrayList inputList = new ArrayList();
		/* Get all states in automaton. */
		State[] states = automaton.getStates();
		
		boolean hasTransition;
		
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
		
		
		/* Check each state for partial specification. */
		for (int k = 0; k < states.length; k++) {
			
			State state = states[k];
			/* Get all transitions from each state. */
			Transition[] stateTransitions = automaton.getTransitionsFromState(state);
			
			//System.out.println("state: "+state);
			Iterator iterator = inputList.iterator();
			
			while(iterator.hasNext()){
				String input = iterator.next().toString();
				hasTransition = false;
				for (int i = 0; i < stateTransitions.length; i++) {
					MealyTransition t1 = (MealyTransition) stateTransitions[i];
					if (lc.isLambdaTransition(t1)) {
						continue; //Do nothing
					}
					//System.out.println("t1:"+t1.getLabel());
					
					if(input.equals(t1.getLabel())){
						hasTransition = true;
						//System.out.println("iterator:"+t1.getLabel());
					}
				}
				//System.out.println(hasTransition);
				if(!hasTransition){
					list.add(state);
					//System.out.println("stateAdd: "+state);
					break;
				}
			}
			
		}
		
		return (State[]) list.toArray(new State[0]);
	}

}
