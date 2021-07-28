package tool.ds;

import java.util.ArrayList;

import fsm4ws.State;

public class Node {
	private State state;
	private ArrayList<Node> children = new ArrayList<Node>();
	private ArrayList<String> labels = new ArrayList<String>();
	
	public Node(State st) {
		state = st;
	}
	
	public State getState() {
		return state;
	}
	
	public void addChild(Node n, String label) {
		children.add(n);
		labels.add(label);
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public ArrayList<String> getLabels() {
		return labels;
	}
}
