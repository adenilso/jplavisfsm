package fsm4ws.generationMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import fsm4ws.FiniteStateMachine;

public class RandomSequenceGenerator extends AbstractGenerator
{
	int numberOfSequences;
	int lengthOfSequence;
	
	public RandomSequenceGenerator(FiniteStateMachine fsm) {
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
			String newSeq = "EPSILON";
			for (int j = 0; j < lengthOfSequence; j++) 
			{
				newSeq += "," + selectRandomInput(fsm.getInputAlphabet()) + "/UNDEFINED";
			}
			sequences.add(newSeq);
		}
	}

	private String selectRandomInput(HashSet<String> inputAlphabet) 
	{
		Random random = new Random(System.nanoTime());
		int i = random.nextInt(inputAlphabet.size());
		
		return (String) inputAlphabet.toArray()[i];
	}
}
