package fsm4ws.generationMethod;

import java.util.ArrayList;

import fsm4ws.FiniteStateMachine;
import tool.fsm.auxiliar.CharacterizationSetConstructor;
import tool.fsm.auxiliar.TestingTreeSetsConstructor;

public class WMethod 
{
	FiniteStateMachine fsm;
	ArrayList<String> sequences;
	
	public WMethod(FiniteStateMachine fsm) 
	{	
		this.fsm = fsm;
		generateSequences();
	}

	public ArrayList<String> getSequences() {
		return sequences;
	}	
	
	private void generateSequences() 
	{
		sequences = new ArrayList<String>();
		TestingTreeSetsConstructor ttc = new TestingTreeSetsConstructor(fsm);
		CharacterizationSetConstructor csconst = new CharacterizationSetConstructor(fsm);
		
		for(String tseq : ttc.getTransitionCover())
		{
			for(String wseq : csconst.getWset())
			{
			
				String newseq = tseq + "," + wseq;
				if(! sequences.contains(newseq)){
					sequences.add(newseq);
				}
					
			}
		}
		//remove proper prefixes
		sequences = getSequencesWithoutPrefixes();
	}
		
	public ArrayList<String> getSequencesWithoutPrefixes() 
	{
		ArrayList<String> noPrefixes = new ArrayList<String>();
		for(String s : sequences) 
		{
			boolean isPrefix = false;
			for(String pref : sequences)
				if(! s.equals(pref) && pref.startsWith(s) && pref.substring(s.length()).contains(",")) 
				{
					isPrefix = true;
					break;
				}
			
			if(! isPrefix){
				noPrefixes.add(s);
				System.out.println(s);
			}
		}
		return noPrefixes;
	}	
}
