package gui.action;

import gui.SplitPaneFactory;
import gui.TableTextSizeSlider;
import gui.action.MultipleSimulateAction.MultiplePane;
import gui.editor.ArrowDisplayOnlyTool;
import gui.environment.Environment;
import gui.environment.tag.CriticalTag;
import gui.viewer.AutomatonPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import mutants.Mutant;
import mutants.Status;

import automata.Automaton;
import automata.State;
import automata.Transition;

public class ShowMutantsAction extends AbstractAction{

	public ShowMutantsAction(Environment environment, Automaton automaton, ArrayList<Mutant> mutantsList){
		super("Show Mutants");
		this.automaton = automaton;
		this.environment = environment;
		this.mutantsList = mutantsList;
		this.indexOfCurrentMutant = 0;
	}
	
	public void actionPerformed(ActionEvent e){
		
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Show Mutants");
		/** LOGGER - End */
		
		panel = new JPanel(new BorderLayout());
		JToolBar bar = new JToolBar();
	
		textPane = new JTextPane();
		textPane.setContentType("text/html");  
		textPane.setEditable(false);
		panel.add(textPane, BorderLayout.CENTER);
		panel.add(new JScrollPane(textPane), BorderLayout.CENTER);
		panel.add(bar, BorderLayout.SOUTH);
		
		//Write the "configuration" of the first Mutant
		setIndexOfCurrentMutant(0);
		writeText(getIndexOfCurrentMutant());
		Mutant mut = mutantsList.get(getIndexOfCurrentMutant());
		
		ap = new AutomatonPane((Automaton) mut.getAutomaton());
    	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
    	split = SplitPaneFactory.createSplit(getEnvironment(), true,
			0.34, ap, panel);
    	mp = new MultiplePane(split);
    	getEnvironment().add(mp, "Mutants", new CriticalTag() {
		});
		getEnvironment().setActive(mp);
		
		
		//Menu Bar
		bar.add(new AbstractAction("<<"){
    		public void actionPerformed(ActionEvent arg0) {
    			setIndexOfCurrentMutant(0);
    			showMutant(0);			
    		}
		});
		bar.add(new AbstractAction("<"){
    		public void actionPerformed(ActionEvent arg0) {
    			if(getIndexOfCurrentMutant() > 0){
    				setIndexOfCurrentMutant(getIndexOfCurrentMutant() - 1);
    				showMutant(getIndexOfCurrentMutant());
    			}
    		}
		});
		
		bar.add(new AbstractAction("< (Alive)"){
			public void actionPerformed(ActionEvent arg0) {
				if(getIndexOfCurrentMutant() > 0){
    				setIndexOfCurrentMutant(getIndexOfCurrentMutant() - 1);
					while(getMutantsList().get(getIndexOfCurrentMutant()).isDead() && getIndexOfCurrentMutant() > 0){
						setIndexOfCurrentMutant(getIndexOfCurrentMutant() -  1);
					}
				}
				showMutant(getIndexOfCurrentMutant());
			}
		});
		
		bar.add(new AbstractAction("(Alive) >"){
			public void actionPerformed(ActionEvent arg0) {
				if(getIndexOfCurrentMutant() < (getMutantsList().size() - 1)){ 
					setIndexOfCurrentMutant(getIndexOfCurrentMutant() +  1);
					while(getMutantsList().get(getIndexOfCurrentMutant()).isDead() && getIndexOfCurrentMutant() < (getMutantsList().size() - 1)){
						setIndexOfCurrentMutant(getIndexOfCurrentMutant() +  1);
					}
				}
				showMutant(getIndexOfCurrentMutant());
			}
		});
		
		bar.add(new AbstractAction(">"){
    		public void actionPerformed(ActionEvent arg0) {
    			if(getIndexOfCurrentMutant() < (getMutantsList().size() - 1) ){
    				setIndexOfCurrentMutant(getIndexOfCurrentMutant() +  1);
    				showMutant(getIndexOfCurrentMutant());
    			}
    		}
		});
		bar.add(new AbstractAction(">>"){
    		public void actionPerformed(ActionEvent arg0) {
    			setIndexOfCurrentMutant(getMutantsList().size()-1);
    			showMutant(getMutantsList().size()-1);
    		}
		});
		bar.add(new AbstractAction("Refresh"){
    		public void actionPerformed(ActionEvent arg0) {
    			showMutant(getIndexOfCurrentMutant());
    			
    			/** LOGGER - Begin */
    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
    			getEnvironment().getLogArea().append("\n" + sdf.format(new Date()) + " - Show Mutant: REFRESH");
    			/** LOGGER - End */
    		}
		});
        
	}
	
	protected void writeText(int indexOfMutant){
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<html>");
		strBuilder.append("<br><center><b style=color:red>** MUTATION **</b> </center>");
		
		float dead = 0;
		float alive = 0;
		float equivalent = 0;

		
		Iterator<Mutant> itMut = mutantsList.iterator();
		while(itMut.hasNext()){
			Mutant mutant = itMut.next();
			if(mutant.isAlive()) alive++;
			else if(mutant.isDead()) dead++;
			else equivalent++;
		}
		
		DecimalFormat df = new DecimalFormat("0");
		strBuilder.append("<br><center><i>Total:</i> "+mutantsList.size()+"\n");
		strBuilder.append("<br>Dead: "+df.format(dead)+"\n");
		strBuilder.append(" - Alive: "+df.format(alive)+"\n");
		strBuilder.append(" - Equivalent: "+df.format(equivalent)+"\n");
		float score = dead/(mutantsList.size()-equivalent);
		DecimalFormat df2 = new DecimalFormat("0.00");
		strBuilder.append("<br><b>SCORE: "+df2.format(score)+"</b>\n");
		
		strBuilder.append("<p>============================</center>\n\n\n");
		
		Mutant mut = mutantsList.get(indexOfMutant);
		
		if(mut.getStatus() == Status.ALIVE) strBuilder.append("<br><b>"+ (indexOfCurrentMutant+1) +": Alive</b><br>\n\n");
		if(mut.getStatus() == Status.DEAD) strBuilder.append("<br><b>"+ (indexOfCurrentMutant+1) +": Dead<br></b>\n\n");
		if(mut.getStatus() == Status.EQUIVALENT) strBuilder.append("<br><b>"+ (indexOfCurrentMutant+1) +": Equivalent</b><br>\n\n");
		
		
		strBuilder.append("<br><i>Initial State: </i>"+mut.getInitialState().getName()+"\n");
		State[] state = mut.getStates();
		for(int i = 0; i < state.length; i++){
			Transition[] trans = mut.getOrderTransition(state[i]);
			for(int k =0 ; k <trans.length; k++){
				strBuilder.append("<br>"+trans[k].getFormatedTransition()+"\n");
			}
		}
		
		strBuilder.append("<br><br><i>Test Cases: </i>");
		Iterator<String> itTC = mut.getTestCases().iterator();
		String tc = null;
		while(itTC.hasNext()){
			strBuilder.append("<br>   "+itTC.next());
		}
		strBuilder.append("</html>");
		textPane.setText(strBuilder.toString());
		
	}
	

	protected void showMutant(int indexOfMutant){
		
		writeText(indexOfMutant);
		
		Mutant mut = mutantsList.get(indexOfMutant);
		ap = new AutomatonPane((Automaton) mut.getAutomaton());

    	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
    	JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
    			0.34, ap, panel);

    	MultiplePane mp1 = new MultiplePane(split);
    	mp1.remove(split);
    	mp1.add(split);

    	getEnvironment().remove(mp);
    	getEnvironment().add(mp1, "Mutants", new CriticalTag() {
		});
		getEnvironment().setActive(mp1);
		mp = mp1;

	}
	

	public class MultiplePane extends JPanel {
		public MultiplePane(JSplitPane split) {
			super(new BorderLayout());
			add(split, BorderLayout.CENTER);
			mySplit = split;
		}
		public JSplitPane mySplit = null;
	}
	
	protected Object getObject() {
		if(automaton != null)return automaton;
		return null;
	}
	
	protected ArrayList<Mutant> getMutantsList(){
		return this.mutantsList;
	}
	
	protected int getIndexOfCurrentMutant() {
		return indexOfCurrentMutant;
	}

	protected void setIndexOfCurrentMutant(int indexOfCurrentMutant) {
		this.indexOfCurrentMutant = indexOfCurrentMutant;
	}

	protected Environment getEnvironment() {
		return environment;
	}
	
	protected void setEnvironment(Environment newEnv) {
		environment = newEnv;
	}
	
	private Automaton automaton;
	private Environment environment;
	private ArrayList<Mutant> mutantsList;
	private int indexOfCurrentMutant = 0;
	private JTextPane textPane;
	private JPanel panel;
	private AutomatonPane ap;
	private JSplitPane split;
	private MultiplePane mp;
}
