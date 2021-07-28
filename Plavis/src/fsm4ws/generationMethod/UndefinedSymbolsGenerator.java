package fsm4ws.generationMethod;

import java.util.ArrayList;
import java.util.HashSet;

import fsm4ws.FiniteStateMachine;
import fsm4ws.State;
import tool.fsm.auxiliar.TestingTreeSetsConstructor;

public class UndefinedSymbolsGenerator extends AbstractGenerator
{

	public UndefinedSymbolsGenerator(FiniteStateMachine fsm) {
		super(fsm);
	}

	@Override
	public void generateSequences() 
	{
		sequences = new ArrayList<String>();
		TestingTreeSetsConstructor ttsc = new TestingTreeSetsConstructor(fsm);
		ArrayList<String> stateCover = ttsc.getStateCover();
		HashSet<String> inputSet = fsm.getInputAlphabet();
		for(String st_seq : stateCover)
		{
			State st = fsm.nextStateWithSequence(fsm.getInitialState(), st_seq);
			for(String in : inputSet)
				if(! st.isDefinedForInput(in))
					sequences.add(st_seq + "," + in);
		}
	}

}
