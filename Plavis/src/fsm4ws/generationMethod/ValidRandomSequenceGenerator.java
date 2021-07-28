package fsm4ws.generationMethod;

import java.util.ArrayList;
import java.util.Random;

import fsm4ws.FiniteStateMachine;
import fsm4ws.State;
import fsm4ws.Transition;

public class ValidRandomSequenceGenerator extends AbstractGenerator
{
	int numberOfSequences;
	int lengthOfSequence;
	
	public ValidRandomSequenceGenerator(FiniteStateMachine fsm) {
		super(fsm);
	}

	@Override
	public void generateSequences() 
	{
		numberOfSequences = 50;
		lengthOfSequence = 10; 
		sequences = new ArrayList<String>();
		System.out.println(numberOfSequences);
		for (int i = 0; i < numberOfSequences; i++) 
		{
			State current = fsm.getInitialState();
			String newSeq = "EPSILON";
			for (int j = 0; j < lengthOfSequence; j++) 
			{
				Transition t = selectRandomTransition(current);
				newSeq += "," + t.getInput();
				current = t.getOut();
			}
			sequences.add(newSeq);
		}
	}

	private Transition selectRandomTransition(State current) 
	{
		ArrayList<Transition> transitions = current.getOut();
		Random random = new Random(System.nanoTime());
		int i = random.nextInt(transitions.size());
		
		return transitions.get(i);
	}	
}
