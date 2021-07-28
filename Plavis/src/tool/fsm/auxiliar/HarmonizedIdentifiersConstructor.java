package tool.fsm.auxiliar;

import java.util.ArrayList;
import java.util.HashMap;

import fsm4ws.FiniteStateMachine;
import fsm4ws.State;


public class HarmonizedIdentifiersConstructor {
	private ArrayList<String> W;
	private FiniteStateMachine fsm;
	private HashMap<State, ArrayList<String>> Hs;
	
	public void setFsm(FiniteStateMachine fsm) {
		this.fsm = fsm;
	}
	
	public void setW(ArrayList<String> w) {
		W = w;
	}
	
	public void construct() {
		Hs = new HashMap<State, ArrayList<String>>();
		
		for(State state : fsm.getStates()) {
			ArrayList<String> hseqs = new ArrayList<String>();
			
			//start with the W
			for(String wseq : W)
				hseqs.add(wseq);
	
			Hs.put(state, hseqs);
		}

		//try to minimize keeping the properties
		reduce();
	}
	
	public ArrayList<String> getHi(State state) {
		return Hs.get(state);
	}
	
	private void reduce() {
		//greedy algorithm - try to remove all sequences and select the biggest.
		
		int bigger_size = -1;
		State bigger_state = null;
		int bigger_index = -1;
		
		for(State state : fsm.getStates())
		{
			ArrayList<String> Hi = getHi(state);
			
			for(int i = 0; i < Hi.size(); i++)
			{
				String seq = Hi.remove(i);
				
				if(isSeparatingFamily() && seq.length() > bigger_size) {
					bigger_size = seq.length();
					bigger_state = state;
					bigger_index = i;
				}
				
				Hi.add(i, seq);
			}
		}
		
		if(bigger_size != -1) {
			ArrayList<String> Hi = getHi(bigger_state);
			Hi.remove(bigger_index);
			reduce();
		}
		
	}
	
	public boolean isSeparatingFamily() {
		ArrayList<State> states = fsm.getStates();				//for each pair of states
		for (int i = 0; i < states.size(); i++) {
			for (int j = i+1; j < states.size(); j++) {
				State si = states.get(i);
				State sj = states.get(j);
				
				ArrayList<String> Hi = getHi(si);
				ArrayList<String> Hj = getHi(sj);
				
				boolean hasAlfa = false;
				
				for(String beta : Hi){
					for(String gama : Hj) {
						//alfa
						for(String alfa : getCommonPrefixes(beta, gama)) {
							if(! fsm.nextOutput(si, alfa).equals(fsm.nextOutput(sj, alfa))) {
								hasAlfa = true;
								break;
							}
						}
						
						if(hasAlfa)
							break;
					}
					if(hasAlfa)
						break;
				}
				
				if(!hasAlfa)
					return false;
			}
		}
		
		return true;
	} 
	
	private ArrayList<String> getCommonPrefixes(String a, String b) {
		ArrayList<String> ret = new ArrayList<String>();
		
		//System.out.println("----");
		//System.out.println(a + " - " + b);
		String as[] = a.split(",");
		String bs[] = b.split(",");
		String curr = "";
		for(int i = 0; i < as.length && i < bs.length; i++) {
			if(as[i].equals(bs[i])) {
				if(i == 0)
					curr = as[i];
				else
					curr += "," + as[i];
				//System.out.println(curr);
				ret.add(curr);
			}
			else
				break;
		}
		
		//System.out.println("----");
		return ret;
	}
}
