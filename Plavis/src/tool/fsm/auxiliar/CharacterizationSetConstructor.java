package tool.fsm.auxiliar;

import java.util.ArrayList;
import java.util.HashSet;
import fsm4ws.FiniteStateMachine;
import fsm4ws.State;

public class CharacterizationSetConstructor 
{
	FiniteStateMachine fsm;
	ArrayList<String> wset;
	
	public CharacterizationSetConstructor(FiniteStateMachine fsm) {
		this.fsm = fsm;
		build();
	}

	public void build() 
	{
		wset = new ArrayList<String>();
		
		for(State si : fsm.getStates())
			for(State sj : fsm.getStates())
				if(si != sj)
				{
					String dseq = distiguishSeq(si, sj);
					if(! wset.contains(dseq))
						wset.add(dseq);
				}
	}
	
	private String distiguishSeq(State si, State sj) 
	{
		int k = 1;
		int n = fsm.getNumberOfStates();
		
		while(k < (n*(n-1)/2))
		{
			for(String x : getInputSeqWithLength(k))
			{
				if(fsm.separe(x, si, sj))
					return x;
			}
			k++;
			/*
			initInputs(k);
			
			while(hasNextInput())
			{
				String x = nextInput();
				if(fsm.separe(x, si, sj))
					return x;

			}
			k++;*/			
		}
		
		if(k == (n*(n-1)/2))
			System.out.println("ERROR: no minimal machine");
			
		return null;
	}
	
	public void initInputs(int k)
	{
		
	}
	
	public boolean hasNextInput()
	{
		return true;
	}
	
	public String nextInput()
	{
		return null;
	}
	
	public ArrayList<String> getInputSeqWithLength(int k)
	{
		ArrayList<String> ret = new ArrayList<String>();		
		HashSet<String> Li = fsm.getInputAlphabet();

		for(String in : Li)
			ret.add(in);
		
		for (int i = 2; i <= k; i++) 
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(String seq : ret)
				for(String in : Li)
					temp.add(seq + "," + in);
				
			ret = temp;	
		}
		return ret;
	}	
		
	public ArrayList<String> getWset() {
		return wset;
	}
}