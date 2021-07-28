package fsm4ws;

public class Transition {
	private String input, output;
	private State in, out;
	
	public Transition(State in, State out, String input, String output) {
		this.in = in;
		this.in.addOutTransition(this);
		this.out = out;
		this.out.addInTransition(this);
		this.input = input;
		this.output = output;
	}
	
	public State getIn() {
		return in;
	}
	
	public State getOut() {
		return out;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setIn(State in) {
		this.in = in;
	}
	
	public void setOut(State out) {
		this.out = out;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
}
