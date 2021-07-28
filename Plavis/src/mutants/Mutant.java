package mutants;

import java.util.ArrayList;
import automata.Automaton;
import automata.State;
import automata.Transition;
import automata.mealy.MealyMachine;
import automata.mealy.MealyTransition;

public class Mutant{

	public Mutant(Automaton original) {
		super();
		this.status = Status.ALIVE; //alive
		this.automaton = (Automaton)original.clone();
		this.original = original;		
		this.testCases = new ArrayList<String>();
	}

	public ArrayList<String> getTestCases(){
		return this.testCases;
	}
	
	public void addTestCase(String testCaseLine){
		this.testCases.add(testCaseLine);
	}
	
	public void setInitialState(automata.State initialState){
		this.automaton.setInitialState(initialState);
	}
	
	public automata.State getInitialState(){
		return this.automaton.getInitialState();
	}
	
	public automata.State[] getStates(){
		return this.automaton.getStates();
	}
	
	public automata.Transition[] getTransitions(){
		return this.automaton.getTransitions();
	}
	
	public ArrayList<automata.Transition> getTransitionsFromState(automata.State state){
		
		ArrayList<automata.Transition> transitions = new ArrayList<automata.Transition>();
		automata.Transition[] aux;
	
		aux = this.automaton.getTransitions();
		for(int i = 0; i < aux.length; i++){
			if(aux[i].getFromState().getName().equals(state.getName())){
				transitions.add(aux[i]);
				//System.out.println("I: "+transitions[index].toString());
			}
		}
		return transitions;
	}
	
	public ArrayList<automata.Transition> getTransitionsToState(automata.State state){
		
		ArrayList<automata.Transition> transitions = new ArrayList<automata.Transition>();
		automata.Transition[] aux;
	
		aux = this.automaton.getTransitions();
		for(int i = 0; i < aux.length; i++){
			if(aux[i].getToState().getName().equals(state.getName())){
				transitions.add(aux[i]);
				//System.out.println("I: "+transitions[index].toString());
			}
		}
		return transitions;
	}
	
	public Automaton getAutomaton() {
		return this.automaton;
	}
	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isDead(){
		if(this.status == Status.DEAD) return true;
		return false;
	}
	
	public boolean isAlive(){
		if(this.status == Status.ALIVE) return true;
		return false;
	}
	
	public boolean isEquivalent(){
		if(this.status == Status.EQUIVALENT) return true;
		return false;
	}
	
	public State getState(String name){
		State[] states = this.automaton.getStates();
		for(int i = 0; i < states.length; i++){
			if(states[i].getName().equals(name)){
				return states[i];
			}
		}
		return null;
	}
	
	public Transition getTransition(String nameToState, String nameFromState){
		Transition[] transitions = this.automaton.getTransitions();
		for(int i = 0; i < transitions.length; i++){
			if(transitions[i].getToState().getName().equals(nameToState) &&
					transitions[i].getFromState().getName().equals(nameFromState)){
				return transitions[i];
			}
		}
		return null;
	}
	
	public Transition getTransition(String nameToState, String nameFromState, String description){
		Transition[] transitions = this.automaton.getTransitions();
		for(int i = 0; i < transitions.length; i++){
			if(transitions[i].getToState().getName().equals(nameToState) &&
			   transitions[i].getFromState().getName().equals(nameFromState) &&
			   transitions[i].getDescription().equals(description)
					){
				return transitions[i];
			}
		}
		return null;
	}
	
	
//	public Transition[] getOrderTransition(){
//		
//		Transition[] trans = this.automaton.getTransitions().clone();
//		Transition[] transMut = this.automaton.getTransitions();
//		Transition[] transOriginal = this.original.getTransitions();
//		int count = 0;
//		
//		for(int iOriginal = 0; iOriginal < transOriginal.length; iOriginal++){
//			MealyTransition tOri = (MealyTransition) transOriginal[iOriginal];
//			for(int iMut = 0; iMut < transMut.length; iMut++){
//				MealyTransition tMut = (MealyTransition) transMut[iMut];
//				System.out.println(iOriginal+" "+tOri.toString());
//				System.out.println(iMut+" "+tMut.toString());
//				
//				System.out.println((tMut.getFromState().getName().equals(tOri.getFromState().getName())) &&
//				   (tMut.getToState().getName().equals(tOri.getToState().getName())) &&
//				   (tMut.getLabel().equals(tOri.getLabel())) &&
//				   (tMut.getOutput().equals(tOri.getOutput())));
//				
//				if((tMut.getFromState().getName().equals(tOri.getFromState().getName())) &&
//				   (tMut.getToState().getName().equals(tOri.getToState().getName())) &&
//				   (tMut.getLabel().equals(tOri.getLabel())) &&
//				   (tMut.getOutput().equals(tOri.getOutput())) ){
//					trans[count] = tMut;
//					count++;
//					//System.out.println("Order");
//					break;
//				}
//			}
//		}
//		return trans; 
//	}
	
	
	public Transition[] getOrderTransition(State state){
		
		//	Transition[] transAns = this.automaton.getTransitionsFromState(state).clone();
			Transition[] trans = this.automaton.getTransitionsFromState(state);

			int count = 0;
			
			for(int i = 0; i < trans.length; i++){
				MealyTransition t1 = (MealyTransition) trans[i].clone();
			//	System.out.println("f1:"+t1.toString());
				
				for(int j = i; j < trans.length; j++){
					MealyTransition t2 = (MealyTransition) trans[j].clone();
			//		System.out.println("f2:"+t2.toString());
					if(t1.getLabel().compareTo(t2.getLabel()) > 0){
						Transition aux = trans[i];
						trans[i] = trans[j];
						trans[j] = aux;
					}
				}
			}
			
			
//			for(int a= 0; a < trans.length; a++) System.out.println("order:"+trans[a].toString());
//			System.out.println();
			
			return trans; 
		}
	
	private Automaton original;
	private Automaton automaton;
	private int status; //0: alive; 1: dead; 2: equivalent
	private ArrayList<String> testCases;
	
	
}
