package fsm4ws;

import java.util.ArrayList;

public class State {
	private String label;

	private ArrayList<Transition> in, out;
	
	public State(String l) {
		label = l;
		in = new ArrayList<Transition>();
		out = new ArrayList<Transition>();
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public ArrayList<Transition> getIn() {
		return in;
	}

	public void setIn(ArrayList<Transition> in) {
		this.in = in;
	}

	public ArrayList<Transition> getOut() {
		return out;
	}

	public void setOut(ArrayList<Transition> out) {
		this.out = out;
	}
	
	public void addInTransition(Transition t) {
		in.add(t);
	}

	public void addOutTransition(Transition t) {
		out.add(t);
	}

	public boolean isDefinedForInput(String input) 
	{
		for(Transition t : out)
		{
			if(t.getInput().equals(input))
				return true;
		}
		return false;
	}

}
