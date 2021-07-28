package fsm4ws;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteStateMachine {
	private HashSet<String> inputAlphabet, outputAlphabet;
	
	private ArrayList<State> states;
	private ArrayList<Transition> transitions;
	private State initialState = null;
	
	public FiniteStateMachine() {
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		inputAlphabet = new HashSet<String>();
		outputAlphabet = new HashSet<String>();
	}
	
	public State getInitialState() {
		return initialState;
	}
	
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}
	
	public int getNumberOfStates() {
		return states.size();
	}
	
	public int getNumberOfTransitions() {
		return transitions.size();
	}
	
	public void addState(State ns) {
		states.add(ns);
	}
	
	public void addTransition(Transition nt) {
		transitions.add(nt);
		inputAlphabet.add(nt.getInput());
		outputAlphabet.add(nt.getOutput());
	}

	public ArrayList<State> getStates() {
		return states;
	}
	
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public HashSet<String> getOutputAlphabet() {
		return outputAlphabet;
	}
	
	public HashSet<String> getInputAlphabet() {
		return inputAlphabet;
	}
	
	public State nextState(State current, String inputsymbol) 
	{
		for(Transition ot : current.getOut()) {
			if(ot.getInput().equals(inputsymbol))
				return ot.getOut();
		}
		
		return null;
	}
	
	public String nextStateOut(State current, String inputsymbol) 
	{
		for(Transition ot : current.getOut()) {
			if(ot.getInput().equals(inputsymbol))
				return ot.getOutput();
		}
		
		return null;
	}
	
	public State nextStateWithSequence(State current, String sequence) 
	{
		String symbols[] = sequence.split(",");
		if(sequence.equals("") || sequence.equals("EPSILON"))		//epsilon
			return current;
		
		State curr = current;
		for(String symb : symbols) {
			if(! symb.equals("EPSILON"))
				curr = nextState(curr, symb);
		}
			
		return curr;
	}
	
	public String nextOutput(State current, String sequence) 
	{
		String symbols[] = sequence.split(",");
		if(sequence.equals(""))		//epsilon
			return "";
		String out = "";
		State curr = current;
		for(String symb : symbols) {
			if(out.equals(""))
				out = nextStateOut(curr, symb);
			else
				out = out + "," + nextStateOut(curr, symb);
			
			curr = nextState(curr, symb);
		}
			
		return out;
	}

	public boolean isDefinedSeq(String x, State state)
	{
		String ins[] = x.split(",");
		State current = state;
		for (int i = 0; i < ins.length; i++) 
		{	
			State next = nextState(current, ins[i]);
			if(next == null)
				return false;
			
			current = next;
		}		
		return true;
	}	
	
	public boolean separe(String x, State si, State sj) 
	{
		if((! isDefinedSeq(x, si)) || (! isDefinedSeq(x, sj)))
			return false;
		
		//separe
		String in[] = x.split(",");
		State current_si = si;
		State current_sj = sj;
		for (int i = 0; i < in.length; i++) 
		{
			if(! nextOutput(current_si, in[i]).equals(nextOutput(current_sj, in[i])))
				return true;
			
			current_si = nextState(current_si, in[i]);
			current_sj = nextState(current_sj, in[i]);			
		}
		
		return false;
	}

	public String getIOSequence(String s) 
	{
		String in[] = s.split(",");
		String ret = "";
		State current = getInitialState();
		for (int i = 1; i < in.length; i++) 
		{
			if("".equals(ret))
				ret += in[i] + "/";
			else
				ret += "," + in[i] + "/";

			if(nextStateOut(current, in[i]) == null)
				ret += "UNDEFINED";
			else
				ret += nextStateOut(current, in[i]);			
			
			current = nextState(current, in[i]);
		}
		return ret;
	}
}
