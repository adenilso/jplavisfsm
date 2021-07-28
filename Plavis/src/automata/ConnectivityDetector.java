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

public class ConnectivityDetector {
	/**
	 * Creates an instance of <CODE>CompletenessDetector</CODE>
	 */
	public ConnectivityDetector() {
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

	public State[] getInitiallyDisconnectedStates(Automaton automaton) {
		/* Lits of all disconnected states */
		ArrayList<State> finalList = new ArrayList() ;
		/* List to iterate the automaton */
		ArrayList<State> stateList = new ArrayList();
		/* Get all states in automaton. */
		State[] states = automaton.getStates();
		/* Get the initial state in automaton */
		State initialState = automaton.getInitialState();
		/* If there is possibly reachable states */ 
		boolean hasPossibleStates = true;
		
		//the list is initialized with all states
		for (int k = 0; k < states.length; k++) {
			finalList.add(states[k]);
		}
		//removes the initial state of the final list
		finalList.remove(initialState);
		State actualState = initialState;
		
		while((hasPossibleStates) && !(finalList.isEmpty())){
			//get all transitions of the current state
			Transition[] transitions = automaton.getTransitionsFromState(actualState);
			for(int j = 0; j < transitions.length; j++){
				if( !(transitions[j].getToState().equals(actualState)) ){
					if(hasElement(finalList, transitions[j].getToState()))
					{
						//if the transitions visit a new (different) state, add to the check list 
						//and remove from final list, because it could be reached from initial state
						stateList.add(transitions[j].getToState());
						removeElement(finalList, transitions[j].getToState());
					}
				}
			}
			//If there are still states to be checked
			if(!stateList.isEmpty()){
				Iterator<State> iterator = stateList.iterator();
				//get the next one
				if(iterator.hasNext()){
					actualState = iterator.next();
					stateList.remove(actualState);
				}
			}
			else{ 
				hasPossibleStates = false;
			}
		}
		return (State[]) finalList.toArray(new State[0]);
	}

}
