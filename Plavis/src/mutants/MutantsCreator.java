package mutants;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import automata.Automaton;
import automata.State;
import automata.Transition;
import automata.mealy.MealyTransition;

public class MutantsCreator {


	public MutantsCreator(Automaton automaton) {
		super();
		this.automaton = automaton;
		this.mutants = new ArrayList<Mutant>();
	}

	public ArrayList<Mutant> createMutants(){
		/* *** Mutation operators *** */
		IniStaAlt();
		TraDesStaAlt(); 
		TraArcDel(); 
		TraEveAlt(); 
//		TraEveIns();  Disabled operator - Nota: Quando a MEF é completa, o operador não gera mutantes; Qdo é parcial, gera mutantes quasi-equivalentes
		StaDel();                      // Ou seja, ele é equivalente à MEF especificada, porém com uma transição a mais
		OutAlt();                      // Como os métodos de geração se baseiam na especificação, nunca será gerado sequências que cubram essa transição
		/*DEBUG*/ //printMutants();
		
		return this.mutants;
	}
	
	/**
	 * Output changed. This method changes the output by other event outputs.
	 */
	public void OutAlt(){
		
		automata.Transition[] transitions = this.automaton.getTransitions();
		automata.State[] states = this.automaton.getStates();
		ArrayList<String> outputList = new ArrayList<String>();
		//Creates a list of the output alphabet
		for(int t = 0; t < transitions.length; t++){
			MealyTransition t1 = (MealyTransition) transitions[t];
			if(!outputList.contains(t1.getOutput())){
				outputList.add(t1.getOutput());
			}
		}
		outputList = getOrder(outputList);
		for(int s = 0; s < states.length; s++){
			automata.Transition[] trans = this.getOrderTransition(states[s]);
			for(int t = 0; t < trans.length; t++){
				MealyTransition t1 = (MealyTransition) trans[t];
	
				Iterator<String> itOutputList = outputList.iterator();
				while(itOutputList.hasNext()){
					String out = itOutputList.next();
					if(!out.equals(t1.getOutput())){
						//Creates a new mutant
						Mutant mutant = new Mutant(this.automaton);
						//Finds the transition object 
						automata.Transition transition = mutant.getTransition(trans[t].getToState().getName(), trans[t].getFromState().getName());
						//Removes the old transition
						mutant.getAutomaton().removeTransition(transition);
						//Creates a new transition with the new output
						MealyTransition newTrans = new MealyTransition(transition.getFromState(), transition.getToState(), t1.getLabel(), out);
						mutant.getAutomaton().addTransition(newTrans);
						//Adds the new mutant
						this.mutants.add(mutant);
					}
				}
			}
		}
		System.out.println(this.mutants.size());
		/*DEBUG*/
		//printMutants();
	}
	
	/**
	 * Arc missing. This operator removes an FSM's arc. 
	 */
	public void TraArcDel(){
	
		
		automata.State[] states = this.automaton.getStates();
		//All transitions
		for(int s = 0; s < states.length; s++){
			automata.Transition[] transitions = this.automaton.getTransitionsFromState(states[s]);
			for(int t = 0; t < transitions.length; t++){
				//create a new mutant
				Mutant mutant = new Mutant(this.automaton);
				automata.Transition transition = mutant.getTransition(transitions[t].getToState().getName(), transitions[t].getFromState().getName(), transitions[t].getDescription());
				//Remove the transition arc
				mutant.getAutomaton().removeTransition(transition);
				//Add the new mutant
				this.mutants.add(mutant);
			}
		}
		System.out.println(this.mutants.size());
		/*DEBUG*/
		//printMutants();
	}
	
	
	/**
	 * Destiny changed. This method changes the target state associated with each of the events
	 * by other existing states of FSM.
	 */
	public void TraDesStaAlt(){
		
		//automata.Transition[] transitions = this.automaton.getTransitions();
		automata.State[] states = this.automaton.getStates();
		automata.State[] statesDest = this.automaton.getStates();
		//All transitions
		for(int s = 0; s < states.length; s++){
			automata.Transition[] transitions = this.automaton.getTransitionsFromState(states[s]);
			for(int t = 0; t < transitions.length; t++){
				//All different destination states
				for(int sd = 0; sd < states.length; sd++){
					if(!statesDest[sd].getName().equals(transitions[t].getToState().getName())){
						//create a new mutant
						Mutant mutant = new Mutant(this.automaton);
						automata.Transition transition = mutant.getTransition(transitions[t].getToState().getName(), transitions[t].getFromState().getName());
						automata.State state = mutant.getState(statesDest[sd].getName());
						//Set the new destination state
						transition.setToState(state);
						//Add the new mutant
						this.mutants.add(mutant);
						
					}	
				}
			}
		}
		System.out.println(this.mutants.size());
		/*DEBUG*/
		//printMutants();
	}
	
	/**
	 * Event changed. It changes each event MEF by other events.
	 */
	public void TraEveAlt(){
		
		automata.Transition[] allTransitions = this.automaton.getTransitions();
		automata.State[] states = this.automaton.getStates();
		ArrayList<String> inputList = new ArrayList<String>();
		
		for(int t = 0; t < allTransitions.length; t++){
			MealyTransition t1 = (MealyTransition) allTransitions[t];
			if(!inputList.contains(t1.getLabel())){
				inputList.add(t1.getLabel());
			}
		}
		
		if(this.automaton.getTransitions().length == states.length * inputList.size()) return; //Complete FSM
			
		for(int s = 0; s < states.length; s++){
			automata.Transition[] transitions = this.getOrderTransition(states[s]);
			//Incomplete FSM
			if(transitions.length < inputList.size()){ 
				
				ArrayList<String> newInputList = getOrder(inputList);
								
				for(int t = 0; t < transitions.length; t++){
					MealyTransition t1 = (MealyTransition) transitions[t];
					newInputList.remove(t1.getLabel());
				}
					
				for(int t = 0; t < transitions.length; t++){
					MealyTransition t1 = (MealyTransition) transitions[t];
					Iterator<String> itInputList = newInputList.iterator();
					while(itInputList.hasNext()){
						String in = itInputList.next();
						if(!in.equals(t1.getLabel())){
							//Creates a new mutant
							Mutant mutant = new Mutant(this.automaton);
							//Finds the transition object 
							automata.Transition transition = mutant.getTransition(transitions[t].getToState().getName(), transitions[t].getFromState().getName());
							//Removes the old transition
							mutant.getAutomaton().removeTransition(transition);
							//Creates a new transition with the new output
							MealyTransition newTrans = new MealyTransition(transition.getFromState(), transition.getToState(), in, t1.getOutput());
							mutant.getAutomaton().addTransition(newTrans);
							//Adds the new mutant
							this.mutants.add(mutant);
						}
					}
				}
				
				
			}
		}
		System.out.println(this.mutants.size());
		/*DEBUG*/
		//printMutants();
		
	}
	
	/**
	 * Extra event. It is included in each of the FSM arc, each of the other existing events that
	 * do not cause the transition between the states related to the arc considered.
	 */
//	public void TraEveIns(){
//		
//		automata.Transition[] allTransitions = this.automaton.getTransitions();
//		automata.State[] states = this.automaton.getStates();
//		ArrayList<String> inputList = new ArrayList<String>();
//		
//		for(int t = 0; t < allTransitions.length; t++){
//			MealyTransition t1 = (MealyTransition) allTransitions[t];
//			if(!inputList.contains(t1.getLabel())){
//				inputList.add(t1.getLabel());
//			}
//		}
//		
//		if(this.automaton.getTransitions().length == states.length * inputList.size()) return; //Complete FSM
//			
//		for(int s = 0; s < states.length; s++){
//			automata.Transition[] transitions = this.getOrderTransition(states[s]);
//			//Incomplete FSM
//			if(transitions.length < inputList.size()){ 
//				
//				ArrayList<String> newInputList = getOrder(inputList);
//								
//				for(int t = 0; t < transitions.length; t++){
//					MealyTransition t1 = (MealyTransition) transitions[t];
//					newInputList.remove(t1.getLabel());
//				}
//					
//				for(int t = 0; t < transitions.length; t++){
//					MealyTransition t1 = (MealyTransition) transitions[t];
//					Iterator<String> itInputList = newInputList.iterator();
//					while(itInputList.hasNext()){
//						String in = itInputList.next();
//						if(!in.equals(t1.getLabel())){
//							//Creates a new mutant
//							Mutant mutant = new Mutant(this.automaton);
//							//printSingleMutant(mutant);
//							//Finds the transition object 
//							automata.Transition transition = mutant.getTransition(transitions[t].getToState().getName(), transitions[t].getFromState().getName());
//							//Creates a new transition with the new output
//							MealyTransition newTrans = new MealyTransition(transition.getFromState(), transition.getToState(), in, t1.getOutput());
//							mutant.getAutomaton().addTransition(newTrans);
//							//Adds the new mutant
//							this.mutants.add(mutant);
//						}
//					}
//				}
//				
//				
//			}
//		}
//		System.out.println(this.mutants.size());
//		/*DEBUG*/
//		//printMutants();
//	}
	
	/**
	 * Output missing. This operator removes the symbol output for each event.
	 */
	public void OutDel(){
		
	}
	
	/**
	 * State missing. This method removes a state, making the junction of two states into a 
	 * single, provided there is an arc connecting the same, redirecting all the the events
	 * pertinent to the ancient states the state again.
	 */
	public void StaDel(){
		
		automata.State[] states = this.automaton.getStates();
		automata.State initialState = this.automaton.getInitialState();
		//All non-initial states
		for(int i = 0; i < states.length; i++){
			if(!states[i].equals(initialState)){
				//Create a new mutant
				Mutant mutant = new Mutant(this.automaton);
				//Remove TransitionsFromState
				ArrayList<Transition> transFrom = mutant.getTransitionsFromState(states[i]);
				Iterator<Transition> itFrom = transFrom.iterator();
				while(itFrom.hasNext()){
					mutant.getAutomaton().removeTransition(itFrom.next());
				}
				//Remove TransitionsToState
				ArrayList<Transition> transTo = mutant.getTransitionsToState(states[i]);
				Iterator<Transition> itTo = transTo.iterator();
				while(itTo.hasNext()){
					mutant.getAutomaton().removeTransition(itTo.next());
				}
				//Remove State 
				mutant.getAutomaton().removeState(mutant.getState(states[i].getName()));
				//Add the new Mutant
				this.mutants.add(mutant);
			}
		}
		System.out.println(this.mutants.size());
		/*Debug*/ 
		//printMutants();
		
	}
	
	/**
	 * Event missing. It is excluded because an event that triggers the transition between two
	 * states
	 */
	public void TraEveDel(){
		
	}
	
	/**
	 * Changing the initial state of the FSM, so that in each mutant one of the other states
	 * becomes the starting state.
	 */
	public void IniStaAlt(){
		
		automata.State[] states = this.automaton.getStates();
		automata.State initialState = this.automaton.getInitialState();
		//All non-initial states
		for(int i = 0; i < states.length; i++){
			if(!states[i].equals(initialState)){
				//Create a new mutant
				Mutant mutant = new Mutant(this.automaton);
				//Set the new initial State
				mutant.setInitialState(mutant.getState(states[i].getName()));
				//Add the new Mutant
				this.mutants.add(mutant);
			}
		}
		System.out.println(this.mutants.size());
		/*Debug*/ 
		//printMutants();
	}
	
	
	
	
	/* DEBUG */
	public void printMutants(){
	
		Iterator<Mutant> iterator = this.mutants.iterator();
		System.out.println();
		while(iterator.hasNext()){
			System.out.println();
			Mutant mut = iterator.next();
			System.out.println(this.mutants.indexOf(mut)+" "+mut.getStatus());
			State[] state = mut.getStates();
			for(int i = 0; i < state.length; i++){
				Transition[] trans = mut.getOrderTransition(state[i]);
				for(int k =0 ; k <trans.length; k++){
					System.out.println(trans[k].toString());
				}
			}
			System.out.println("New Initial State: "+mut.getInitialState().getName());
			Iterator<String> itTC = mut.getTestCases().iterator();
			while(itTC.hasNext()){
				System.out.print(itTC.next()+" ");
			}
			System.out.println();
		}
		System.out.println(this.mutants.size());
		
	}
	
	private void printSingleMutant(Mutant mut){
		

		Transition[] trans = mut.getTransitions();
		for(int k =0 ; k <trans.length; k++){
			System.out.println(trans[k].toString());
		}
		System.out.println("New Initial State: "+mut.getInitialState().getName());
		System.out.println("** END **");
		
	}
	
	
	public Transition[] getOrderTransition(State state){
		
		Transition[] trans = this.automaton.getTransitionsFromState(state);

		int count = 0;
		
		for(int i = 0; i < trans.length; i++){			
			for(int j = i; j < trans.length; j++){
				MealyTransition t1 = (MealyTransition) trans[i].clone();
				MealyTransition t2 = (MealyTransition) trans[j].clone();
//				System.out.println((t1.getLabel()+" "+t2.getLabel()+" "+t1.getLabel().compareTo(t2.getLabel())));
				if(t1.getLabel().compareTo(t2.getLabel()) > 0){
					
					Transition aux = trans[i];
					trans[i] = trans[j];
					trans[j] = aux;
				}
			}
		}
		
//		for(int a= 0; a < trans.length; a++) System.out.println("order:"+trans[a].toString());
//		System.out.println();
		
		return trans; 
	}
	
	
	public ArrayList<String> getOrder(ArrayList<String> list){
		
			ArrayList<String> orderList = new ArrayList<String>(); 
			Object[] array = list.toArray();

			int count = 0;
			
			for(int i = 0; i < array.length; i++){
				String str1 = array[i].toString();
				
				for(int j = i; j < array.length; j++){
					String str2 = array[j].toString();
					if(str1.compareTo(str2) > 0){
						Object aux = array[i];
						array[i] = array[j];
						array[j] = aux;
					}
				}
			}
			
			for(int k = 0; k < array.length; k++ ){
				orderList.add(array[k].toString());
			}
			
			return orderList; 
	}
	

	private Automaton automaton;
	private ArrayList<Mutant> mutants;
	
	private HashMap<BigInteger, Mutant> mutantMap;
}
