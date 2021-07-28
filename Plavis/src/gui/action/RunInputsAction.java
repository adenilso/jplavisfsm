package gui.action;

import file.BasicFileReader;
import grammar.Grammar;
import gui.environment.Environment;
import gui.environment.FrameFactory;
import gui.environment.GrammarEnvironment;
import gui.environment.Universe;
import gui.grammar.parse.BruteParsePane;
import gui.sim.multiple.InputTableModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import mutants.Mutant;
import mutants.Status;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.State;
import automata.Transition;
import automata.mealy.MealyConfiguration;
import automata.mealy.MealyMachine;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;
import javax.swing.JFrame;

public class RunInputsAction extends AbstractAction {

	
	public RunInputsAction(Environment environment, Automaton automaton, JTable table, ArrayList<Mutant> mutantsList, File file){
		super("Run Inputs");
		this.environment = environment;
		this.automaton = automaton;
		this.table = table;
		this.mutantsList = mutantsList;
		this.file = file;
		this.allTestCases = new ArrayList<String>();
		
		Iterator<Mutant> mutIt = mutantsList.iterator();
		while(mutIt.hasNext()){
			Iterator<String> tcIt = mutIt.next().getTestCases().iterator();
			while(tcIt.hasNext()){
				String tc = tcIt.next();
				if(!isAlreadyExecuted(tc)){
					allTestCases.add(tc);
				}
			}
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Run Inputs: START");
		/** LOGGER - End */
		
		ArrayList<String> currentTestCaseList = new ArrayList<String>();
		
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}
		InputTableModel model = (InputTableModel) table.getModel();
		BasicFileReader fileReader = null;
		if(file != null || file.length() > 0){
			String path = file.getAbsolutePath();
			path = path.replace(".txt", "_status.txt");
			fileReader = new BasicFileReader(path);
		}
		
			
		if(getObject() instanceof Automaton){
            Automaton currentAuto = (Automaton)getObject();
			AutomatonSimulator simulator = SimulatorFactory
					.getSimulator(currentAuto);
			String[][] inputs = model.getInputs();
            int uniqueInputs = inputs.length;
            int tapes = 1;
            if(model.isMultiple){
            	if (currentAuto instanceof TuringMachine) {
					 tapes = ((TuringMachine)currentAuto).tapes;
            	}
                uniqueInputs = getEnvironment().myTestStrings.size()/tapes;
            }
            
            
            currentTestCaseList.clear();
            int contExecution = 0;
            System.out.println("Execution: 0 de "+inputs.length+" (0%)");
            for (int r = 0; r < inputs.length; r++) {
            	
            	//Imprime progresso da execução
            	if(contExecution < ((r*100)/inputs.length) ) {
            		System.out.println();
            		System.out.println("Execution: "+r+" de "+inputs.length+" ("+(r*100)/inputs.length+"%)");
            		contExecution++;
            	}
            	if(r%5==0) System.out.print(".");
            	
            	if(r>0){
                    if(r%uniqueInputs==0){
                        currentAuto = (Automaton)getEnvironment().myObjects.get(r/uniqueInputs);
                       
                        simulator = SimulatorFactory.getSimulator(currentAuto);                         
                    }
                }
          
				Configuration[] configs = null;
				Object input = null;
				// Is this a Turing machine?
				if (currentAuto instanceof TuringMachine) {
					 
					configs = ((TMSimulator) simulator)
							.getInitialConfigurations(inputs[r]);
					input = inputs[r];
				} else { // If it's not a Turing machine.
					configs = simulator
							.getInitialConfigurations(inputs[r][0]);
					input = inputs[r][0];
				}
			//	if(input.toString().startsWith("#")) continue; //ignore the commented tests
				List associated = new ArrayList();
				int result = handleInput(currentAuto, simulator, configs, input, associated);
				Configuration c = null;
				String out = null;
				
				if (associated.size() != 0){
					c = (Configuration) associated.get(0);
					out = c.toString().substring(c.toString().lastIndexOf(":"));
					//System.out.println("O"+c.toString());
				}
				
				
				if( (!input.toString().startsWith("#")) && (!isAlreadyExecuted(currentTestCaseList, input.toString())) ){
					currentTestCaseList.add(new String(input.toString()));
					//System.out.println("ctc: "+new String(input.toString()));
				}

				if(!isAlreadyExecuted(inputs[r][0])){
					Iterator<Mutant> itMut = mutantsList.iterator();
					allTestCases.add(inputs[r][0]);
					while(itMut.hasNext()){
						Mutant mut = itMut.next();
		                
						AutomatonSimulator simulatorMut = SimulatorFactory.getSimulator(mut.getAutomaton());
		                                         
						
						Configuration[] configsMut = null;
						Object inputMut = null;
						// Is this a Turing machine?
						if (currentAuto instanceof TuringMachine) {
							 
							configsMut = ((TMSimulator) simulatorMut).getInitialConfigurations(inputs[r]);
							inputMut = inputs[r];
						} else { // If it's not a Turing machine.
							configsMut = simulatorMut.getInitialConfigurations(inputs[r][0]);
							inputMut = inputs[r][0];
						}
						List associatedMut = new ArrayList();
						int resultMut = handleInput(mut.getAutomaton(), simulatorMut,
								configsMut, inputMut, associatedMut);
						Configuration cMut = null;
						if (associated.size() != 0){
							cMut = (Configuration) associatedMut.get(0);
							String outMut = cMut.toString().substring(cMut.toString().lastIndexOf(":"));
							if(!outMut.equals(out)){
								mut.setStatus(Status.DEAD);
								mut.addTestCase(inputMut.toString());
								
							}
						}
						
					}
				}
                /*
                 * If it's a Moore or Mealy machine, the output should be
                 * the string not accept/reject.
                 */
                //MERLIN MERLIN MERLIN MERLIN MERLIN//
                if(getObject() instanceof MealyMachine)
                {
                    MealyConfiguration con = (MealyConfiguration) c;
                    model.setResult(r, con.getOutput(), con, 
                        getEnvironment().myTransducerStrings, (r%(uniqueInputs))*(tapes+1));
                }
				//currentCompare.add()
                else
                    model.setResult(r, RESULT[result], c, getEnvironment().myTransducerStrings, (r%(uniqueInputs))*(tapes+1));
			}
            /** LOGGER - Begin */
    		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Run Inputs: END");
    		/** LOGGER - End */
            
		}
		
		System.out.println("Test Section - Refresh");
		if(currentTestCaseList.size()!=allTestCases.size()){
			Iterator<String> itAll = allTestCases.iterator();
			while(itAll.hasNext()){
				String tc = itAll.next();
				System.out.println(tc);
				if(!isAlreadyExecuted(currentTestCaseList, tc)){
					Iterator<Mutant> itMut = mutantsList.iterator();
					while(itMut.hasNext()){
						Mutant mut = itMut.next();
						if(isAlreadyExecuted(mut.getTestCases(),tc)){
							mut.getTestCases().remove(tc);
							if(mut.getTestCases().size()==0){
								mut.setStatus(Status.ALIVE);
							}
						}
					}
				}
			}
			allTestCases = (ArrayList<String>) currentTestCaseList.clone();
		}
		
		
//		Iterator<Mutant> iterator = mutantsList.iterator();
		System.out.println();
//		while(iterator.hasNext()){
//			System.out.println();
//			Mutant mut = iterator.next();
//			System.out.println(mut.getStatus());
//			State[] state = mut.getStates();
//			for(int i = 0; i < state.length; i++){
//				Transition[] trans = mut.getOrderTransition(state[i]);
//				for(int k =0 ; k <trans.length; k++){
//					System.out.println(trans[k].toString());
//				}
//			}
//			System.out.println("New Initial State: "+mut.getInitialState().getName());
//			System.out.print("TC: ");
//			Iterator<String> itTC = mut.getTestCases().iterator();
//			while(itTC.hasNext()){
//				System.out.print(itTC.next()+" ");
//			}
//			System.out.println();
//		}
		System.out.println("Mutants number: "+mutantsList.size());
		
		
		float dead = 0;
		float alive = 0;
		float equivalent = 0;
		System.out.println("** Mutation **");
		Iterator<Mutant> itMut = mutantsList.iterator();
		while(itMut.hasNext()){
			Mutant mutant = itMut.next();
			if(mutant.isAlive()) alive++;
			else if(mutant.isDead()) dead++;
			else equivalent++;
		}
		System.out.println("Dead: "+dead);
		System.out.println("Alive: "+alive);
		System.out.println("Equivalent: "+equivalent);
		float score = dead/(mutantsList.size()-equivalent);
		System.out.println("** Score: "+score);
		
		/** LOGGER - Begin */
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Mutation: Dead = " + dead + "; Alive = " + alive + "; Equiv = " + equivalent);
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Mutation SCORE: " + score);
		/** LOGGER - End */
		
		//TESTE
		//FrameFactory.createFrame(new MealyMachine());
		
	}
	
	

	public Environment getEnvironment() {
		return environment;
	}


	public Automaton getObject() {
		return automaton;
	}

	
	protected int handleInput(Automaton automaton,
			AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput, List associatedConfigurations) {
		JFrame frame = Universe.frameForEnvironment(getEnvironment());
		// How many configurations have we had?
		int numberGenerated = 0;
		// When should the next warning be?
		int warningGenerated = WARNING_STEP;
		Configuration lastConsidered = configs[configs.length - 1];
		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, frame)) {
					associatedConfigurations.add(lastConsidered);
					return 2;
				}
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}
			// Get the next batch of configurations.
			ArrayList next = new ArrayList();
			for (int i = 0; i < configs.length; i++) {
				lastConsidered = configs[i];
				if (configs[i].isAccept()) {
					associatedConfigurations.add(configs[i]);
					return 0;
				} else {
					next.addAll(simulator.stepConfiguration(configs[i]));
				}
			}
			configs = (Configuration[]) next.toArray(new Configuration[0]);
		}
		associatedConfigurations.add(lastConsidered);
		return 1;
	}

	protected boolean confirmContinue(int generated, Component component) {
		int result = JOptionPane.showConfirmDialog(component, generated
				+ " configurations have been generated.  "
				+ "Should we continue?");
		return result == JOptionPane.YES_OPTION;
	}
	
	private boolean isAlreadyExecuted(String testCase){
		
		Iterator<String> itTC = allTestCases.iterator();
		while(itTC.hasNext()){
			String tc = itTC.next();
			if(testCase.equals(tc)){ 
				return true;
			}
		}
		return false;
	}
	
	private boolean isAlreadyExecuted(ArrayList<String> list, String testCase){
		
		Iterator<String> itList = list.iterator();
		while(itList.hasNext()){
			String str = itList.next();
			if(testCase.equals(str)){ 
				return true;
			}
		}
		return false;
	}

	private Environment environment;
	private Automaton automaton;
	private JTable table;
	private ArrayList<Mutant> mutantsList;
	private File file;
	private boolean isLoaded = false;
	protected static final int WARNING_STEP = 500;
	
	private static String[] RESULT = { "Accept", "Reject", "Cancelled" };
	
	private ArrayList<String> allTestCases;
}
