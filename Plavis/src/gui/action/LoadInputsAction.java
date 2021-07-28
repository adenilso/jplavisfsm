package gui.action;

import file.BasicFileReader;
import file.BasicFileWriter;

import fsm4ws.WMethodGeneration;
import gui.environment.Environment;
import gui.sim.multiple.InputTableModel;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import mutants.Mutant;

import automata.Automaton;
import automata.CompletenessDetector;
import automata.ConnectivityDetector;
import automata.MinimalDetector;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.State;
import automata.Transition;
import automata.mealy.MealyTransition;

//import com.sun.corba.se.spi.orbutil.fsm.Action;

public class LoadInputsAction extends AbstractAction{
	
	public LoadInputsAction(Automaton automaton, Environment environment, JTable table, JComboBox jMethod, String strLocalDir){
		super("Load Inputs");
		this.automaton = automaton;
		this.environment = environment;
		this.jMethodList = jMethod;
		this.table = table;
		this.strLocalDir = strLocalDir;
		this.plavisEncode = writeFieldsPlavis(automaton);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}
		InputTableModel model = (InputTableModel) table.getModel();
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		/** LOGGER - End */
		
		if(jMethodList.getSelectedItem().equals("From File")){
		
			/** LOGGER - Begin */
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: FROM FILE - START");
			/** LOGGER - End */
			
			
			File fLocalDir = new File("").getAbsoluteFile();
			String strLocalDir = fLocalDir.getPath();
	    	String separator = "";
			BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
			
			if(configFile.open()){
				separator = configFile.readLine();
				
				/** LOGGER - Begin */
				this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: Config File = "+separator);
				/** LOGGER - End */
				
				if(separator.equalsIgnoreCase("DEFAULT"))
					separator = ",";
				else if(separator.equalsIgnoreCase("SPACE"))
					separator = " ";
				else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
					separator = "";
				configFile.close();
			}
			else{
				separator = ",";
			}
			
			JFileChooser ourChooser=new JFileChooser (System.getProperties().getProperty("user.dir"));
			
			int retval=ourChooser.showOpenDialog(null);
			File f=null;
			
			if (retval==JFileChooser.APPROVE_OPTION)
			{
				f=ourChooser.getSelectedFile();
//				try 
//				{
					String separatorFile = "";
					//Scanner sc=new Scanner(f);
					BasicFileReader bfr = new BasicFileReader(f.getAbsolutePath());
					
					int last=model.getRowCount()-1;
					boolean first = true;
					if(bfr.open()){
						//while (sc.hasNext())
						int option = JOptionPane.YES_OPTION;
						while(!bfr.endOfFile())
						{
							String temp = bfr.readLine(); //sc.next();
							
							
							if(first){
								separatorFile = temp;
								if(separatorFile.equalsIgnoreCase("DEFAULT"))
									separatorFile = ",";
								else if(separatorFile.equalsIgnoreCase("SPACE"))
									separatorFile = " ";
								else if(separatorFile.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
									separatorFile = "";
									
								option = JOptionPane.showConfirmDialog(null, "The \""+ separatorFile + "\" (from file) is going to be repleced by \"" + separator + "\"" ,
										"Separator Input", JOptionPane.YES_NO_OPTION);
								if(option == JOptionPane.NO_OPTION){
									model.setValueAt(temp, last, 0);
									last++;
								}
								
								first = false;		
							}
							else{
								if(option == JOptionPane.YES_OPTION) temp = temp.replaceAll(separatorFile, separator);
								model.setValueAt(temp, last, 0);
								last++;
							}
						

						}
					}
//				}
//				catch (FileNotFoundException e1) {
//					// TODO Auto-generate catch block
//					e1.printStackTrace();
//				}
			}
			/** LOGGER - Begin */
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: FROM FILE - END");
			/** LOGGER - End */
			
		}
		/*New code - Method Generation*/
//		else if(jMethodList.getSelectedItem().equals("Condado")){
//			System.out.println("=> Condado");
//		}
		else if(jMethodList.getSelectedItem().equals("W")){
			System.out.println("=> W");
			
			/** LOGGER - Begin */
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: W Method - START");
			/** LOGGER - End */
			
			
			//hasProblem?, Minimal, Complete, Initial Connected
		//	boolean completed = false;
			boolean[] problems = {false, false, false, false}; 
			State[] states;
		
			
			
			//=============================================
			//VERSÃO SEM GARANTIA DE MINIMALIDADE DA MEF!!!
			//=============================================
			int answerRed = JOptionPane.showConfirmDialog(null, "Do you want check if the FSM is reduced?", "Reduced Check", JOptionPane.YES_NO_OPTION);
			if(answerRed == JOptionPane.YES_OPTION){
				MinimalDetector md = new MinimalDetector();
				states = md.getEquivalentStates(automaton);
				if(states.length!=0){
					problems[0] = true; //hasProblem:
					problems[1] = true; //unreduced FSM
				}
				
				
			}
			//=============================================
			//VERSÃO SEM GARANTIA DE MINIMALIDADE DA MEF!!!
			//=============================================		
			
			
			//=============================================
			//VERSÃO COM GARANTIA DE MINIMALIDADE DA MEF!!!
			//=============================================
//			MinimalDetector md = new MinimalDetector();
//			states = md.getEquivalentStates(automaton);
//			if(states.length!=0){
//				problems[0] = true; //hasProblem:
//				problems[1] = true; //unreduced FSM
//			}
			//=============================================
			//VERSÃO COM GARANTIA DE MINIMALIDADE DA MEF!!!
			//=============================================
			
			Mutant mutant = new Mutant(this.automaton);
			CompletenessDetector cd = new CompletenessDetector();
			states = cd.getPartiallySpecifiedStates(automaton); 
			if(states.length!=0){				
				int answer = JOptionPane.showConfirmDialog(null, "Should FSM be automatically completed?", "Uncomplete FSM!", JOptionPane.YES_NO_OPTION);
				if(answer == JOptionPane.YES_OPTION){
					
					//Completar a MEF 
					automata.Transition[] allTransitions = this.automaton.getTransitions();
					automata.State[] statesFSM = this.automaton.getStates();
					ArrayList<String> inputList = new ArrayList<String>();
					//Create the input list
					for(int t = 0; t < allTransitions.length; t++){
						MealyTransition t1 = (MealyTransition) allTransitions[t];
						if(!inputList.contains(t1.getLabel())){
							inputList.add(t1.getLabel());
						}
					}
					
					if(this.automaton.getTransitions().length == statesFSM.length * inputList.size()) return; //Complete FSM
					//For all states	
					for(int s = 0; s < statesFSM.length; s++){
						automata.Transition[] transitions = this.getOrderTransition(statesFSM[s]);
						//Incomplete FSM
						if(transitions.length < inputList.size()){ 
							
							ArrayList<String> newInputList = getOrder(inputList);
							//The missing inputs		
							for(int t = 0; t < transitions.length; t++){
								MealyTransition t1 = (MealyTransition) transitions[t];
								newInputList.remove(t1.getLabel());
							}
							
							for(int t = 0; t < transitions.length; t++){
								MealyTransition t1 = (MealyTransition) transitions[t];
								Iterator<String> itInputList = newInputList.iterator();
								while(itInputList.hasNext()){
									String in = itInputList.next();
									if(!in.equals(t1.getLabel())){
										//Creates a new mutant
										//printSingleMutant(mutant);
										//Finds the transition object 
										automata.Transition transition = mutant.getTransition(transitions[t].getToState().getName(), transitions[t].getFromState().getName());
										//Creates a new transition with the new output (null) to get a complete FSM
										MealyTransition newTrans = new MealyTransition(transition.getFromState(), transition.getFromState(), in, "empty");
										mutant.getAutomaton().addTransition(newTrans);
									}
								}
							}						
						}
					}
					//completed = true;
					Transition[] trans = mutant.getTransitions();
					for(int k =0 ; k <trans.length; k++){
						System.out.println(trans[k].toString());
					}
					System.out.println("New Initial State: "+mutant.getInitialState().getName());
					System.out.println("** END **");
					automaton = mutant.getAutomaton();
					
				}
				else{
					problems[0] = true; //hasProblem:
					problems[2] = true; //Incomplete FSM
				}
				
			}
			ConnectivityDetector cod = new ConnectivityDetector();
			states = cod.getInitiallyDisconnectedStates(automaton);
			if(states.length!=0){
				problems[0] = true; //hasProblem:
				problems[3] = true; //Initially Disconnected FSM
			}
			
			if(problems[0]){
				String problemType = "";
				if(problems[1]) problemType += "Unreduced ";
				if(problems[2]) problemType += "Incomplet ";
				if(problems[3]) problemType += "Initially Disconnected ";
				JOptionPane.showMessageDialog(null, 
	                    "The W Method can not be applied to "+problemType+"FSM.\n" +
	                    "Select menu item Test to correct the problems.",
	                    "W Method", JOptionPane.ERROR_MESSAGE);
			}
			else{			
				WMethodGeneration wMethod = new WMethodGeneration(automaton);
				
				try 
				{
					int last=model.getRowCount()-1;
					ArrayList wSequences = wMethod.getSequences();
					Scanner sc = new Scanner(wSequences.toString());
					String separatorName = "";
					while (sc.hasNext())
					{
						String sequence = (String) sc.next();
						String editedSequence;
						editedSequence = sequence.substring(sequence.indexOf(",")+1);
//						editedSequence = editedSequence.replaceAll(",", "");
						if(editedSequence.endsWith(",")){
							editedSequence = editedSequence.substring(0, editedSequence.length()-1);
						}
						if(editedSequence.endsWith("]")){
							editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
						}
						
						File fLocalDir = new File("").getAbsoluteFile();
						String strLocalDir = fLocalDir.getPath();
						String separator = "";
						BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
										
						if(configFile.open()){
							separator = configFile.readLine();
							separatorName = separator;
							if(separator.equalsIgnoreCase("DEFAULT"))
								separator = ",";
							else if(separator.equalsIgnoreCase("SPACE"))
								separator = " ";
							else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
								separator = "";
							configFile.close();
						}
						else{
							separator = ",";
						}
						editedSequence = editedSequence.replaceAll(",", separator);
						
						model.setValueAt(editedSequence, last, 0);
						last++;
					}	
					
					/** LOGGER - Begin */
					this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: Config File = "+separatorName);
					/** LOGGER - End */
				}
				catch (Exception e1) {
					// TODO Auto-generate catch block
					e1.printStackTrace();
				}
			}	
			
			/** LOGGER - Begin */
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: W Method - END");
			/** LOGGER - End */
		}
		else if(jMethodList.getSelectedItem().equals("HSI")){
			
//			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: HSI Method - START");
//
//			this.plavisEncode = writeFieldsPlavisWithSeparator(automaton);
//			System.out.println("to HSI");
//			try{
//				File Dir = new File(this.strLocalDir);
//				Process p;
//				p = Runtime.getRuntime().exec("./fsm-hsi.sh", null, Dir);
//				p.waitFor();
//				System.out.println("./fsm-hsi <fsm.txt >out.txt");
//			}
//			catch(Exception eIE){
//				eIE.printStackTrace();
//			}
//			
//			int last=model.getRowCount()-1;
//			
//			BasicFileReader seqFile = new BasicFileReader(this.strLocalDir+"//fsmOUT.txt");
//							
//			ArrayList sequences = new ArrayList();
//			boolean hasSequences = true;
//			if(seqFile.open()){
//				while(!seqFile.endOfFile()){
//					String line = seqFile.readLine();
//					if(line.contains("*")){
//						hasSequences = false;
//						System.out.println("here");
//						break;
//					}
//					sequences.add(line);
//				}
//				seqFile.close();
//			}
//			if(hasSequences){
//				Scanner sc = new Scanner(sequences.toString());
//				String separatorName = "";
//				while (sc.hasNext())
//				{
//					String sequence = (String) sc.next();
//					String editedSequence = sequence.substring(sequence.indexOf("[")+1);
////					editedSequence = sequence.substring(sequence.indexOf("EnD")+3);
//					editedSequence = editedSequence.replaceAll("EnD", ",");
//					if(editedSequence.endsWith(",")){ //Para eliminar as vÃ­rgulas geradas pra separar as strings
//						editedSequence = editedSequence.substring(0, editedSequence.length()-1);
//					}
//					if(editedSequence.endsWith("]")){
//						editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
//					}
//					if(editedSequence.endsWith(",")){ //Para eliminar as vÃ­rgulas gerads pelo scanner
//						editedSequence = editedSequence.substring(0, editedSequence.length()-1);
//					}
//
//					System.out.println(editedSequence);
//					
//					File fLocalDir = new File("").getAbsoluteFile();
//					String strLocalDir = fLocalDir.getPath();
//					String separator = "";
//					BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
//									
//					if(configFile.open()){
//						separator = configFile.readLine();
//						separatorName = separator;
//						if(separator.equalsIgnoreCase("DEFAULT"))
//							separator = ",";
//						else if(separator.equalsIgnoreCase("SPACE"))
//							separator = " ";
//						else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
//							separator = "";
//						configFile.close();
//					}
//					else{
//						separator = ",";
//					}
//					editedSequence = editedSequence.replaceAll(",", separator);
//					
//					model.setValueAt(editedSequence, last, 0);
//					last++;
//				}
//			}
//			this.plavisEncode = writeFieldsPlavis(automaton);
//			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: HSI Method - END");
			
			
			
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: HSI Method - START");
			try{
				File Dir = new File(this.strLocalDir);
				System.out.println(strLocalDir);
				Process p;
				System.out.println("./hsi.sh "+automaton.getStates().length);
				p = Runtime.getRuntime().exec("./hsi.sh "+automaton.getStates().length, null, Dir);
				p.waitFor();
			}
			catch(Exception eIE){
				eIE.printStackTrace();
			}
			
			int last=model.getRowCount()-1;
			
			BasicFileReader seqFile = new BasicFileReader(this.strLocalDir+"//fsmOUT.txt");
							
			ArrayList sequences = new ArrayList();
			boolean hasSequences = true;
			if(seqFile.open()){
				while(!seqFile.endOfFile()){
					String line = seqFile.readLine();
					if(line.contains("*")){
						hasSequences = false;
						System.out.println("here");
						break;
					}
					sequences.add(line);
				}
				seqFile.close();
			}
			if(hasSequences){
				Scanner sc = new Scanner(sequences.toString());
				String separatorName = "";
				while (sc.hasNext())
				{
					String sequence = (String) sc.next();
					while(sc.hasNext() && (!sequence.endsWith(",") && !sequence.endsWith("]"))){
						sequence = sequence + "," + sc.next();
					}
					System.out.println(sequence);
					String editedSequence = sequence.substring(sequence.indexOf("[")+1);

					if(editedSequence.endsWith("]")){
						editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
					}
					if(editedSequence.endsWith(",")){ //Para eliminar as vÃ­rgulas gerads pelo scanner
						editedSequence = editedSequence.substring(0, editedSequence.length()-1);
					}
					if(editedSequence.startsWith(",")){
						editedSequence = editedSequence.substring(1, editedSequence.length());
					}
					System.out.println(editedSequence);
					
					File fLocalDir = new File("").getAbsoluteFile();
					String strLocalDir = fLocalDir.getPath();
					String separator = "";
					BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
									
					if(configFile.open()){
						separator = configFile.readLine();
						separatorName = separator;
						if(separator.equalsIgnoreCase("DEFAULT"))
							separator = ",";
						else if(separator.equalsIgnoreCase("SPACE"))
							separator = " ";
						else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
							separator = "";
						configFile.close();
					}
					else{
						separator = ",";
					}
					System.out.println(editedSequence);
					editedSequence = editedSequence.replaceAll(",", separator);
					System.out.println(editedSequence);
					model.setValueAt(editedSequence, last, 0);
					last++;
				}
			}
			
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: HSI Method - END");
			
			
			
			
		}
		else if(jMethodList.getSelectedItem().equals("SPY")){
			
			
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: SPY Method - START");
			try{
				File Dir = new File(this.strLocalDir);
				System.out.println(strLocalDir);
				Process p;
				System.out.println("./spy.sh "+automaton.getStates().length);
				p = Runtime.getRuntime().exec("./spy.sh "+automaton.getStates().length, null, Dir);
				p.waitFor();
			}
			catch(Exception eIE){
				eIE.printStackTrace();
			}
			
			int last=model.getRowCount()-1;
			
			BasicFileReader seqFile = new BasicFileReader(this.strLocalDir+"//fsmOUT.txt");
							
			ArrayList sequences = new ArrayList();
			boolean hasSequences = true;
			if(seqFile.open()){
				while(!seqFile.endOfFile()){
					String line = seqFile.readLine();
					if(line.contains("*")){
						hasSequences = false;
						System.out.println("here");
						break;
					}
					sequences.add(line);
				}
				seqFile.close();
			}
			if(hasSequences){
				Scanner sc = new Scanner(sequences.toString());
				String separatorName = "";
				while (sc.hasNext())
				{
					String sequence = (String) sc.next();
					while(sc.hasNext() && (!sequence.endsWith(",") && !sequence.endsWith("]"))){
						sequence = sequence + "," + sc.next();
					}
					System.out.println(sequence);
					String editedSequence = sequence.substring(sequence.indexOf("[")+1);

					if(editedSequence.endsWith("]")){
						editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
					}
					if(editedSequence.endsWith(",")){ //Para eliminar as vÃ­rgulas gerads pelo scanner
						editedSequence = editedSequence.substring(0, editedSequence.length()-1);
					}
					if(editedSequence.startsWith(",")){
						editedSequence = editedSequence.substring(1, editedSequence.length());
					}
					System.out.println(editedSequence);
					
					File fLocalDir = new File("").getAbsoluteFile();
					String strLocalDir = fLocalDir.getPath();
					String separator = "";
					BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
									
					if(configFile.open()){
						separator = configFile.readLine();
						separatorName = separator;
						if(separator.equalsIgnoreCase("DEFAULT"))
							separator = ",";
						else if(separator.equalsIgnoreCase("SPACE"))
							separator = " ";
						else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
							separator = "";
						configFile.close();
					}
					else{
						separator = ",";
					}
					System.out.println(editedSequence);
					editedSequence = editedSequence.replaceAll(",", separator);
					System.out.println(editedSequence);
					model.setValueAt(editedSequence, last, 0);
					last++;
				}
			}
			
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: SPY Method - END");
			
		}
		else if(jMethodList.getSelectedItem().equals("UIO")){
			
			int result = 0;
			this.plavisEncode = writeFieldsPlavisToUIO(automaton);
			
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: UIO Method - START");
			try{
				File Dir = new File(this.strLocalDir);
				System.out.println(strLocalDir);
				Process p;
				System.out.println("./uio fsm.txt > fsmOUT.txt");
				p = Runtime.getRuntime().exec("./uio.sh", null, Dir);
				result = p.waitFor();
			}
			catch(Exception eIE){
				eIE.printStackTrace();
			}
			
			if(result!=0){
				JOptionPane.showMessageDialog(null, 
	                    "The UIO Method can not be applied to this FSM.\n" +
	                    "MESSAGE ERROR: Graph is not strongly connected",
	                    "UIO Method", JOptionPane.ERROR_MESSAGE);
			}else{
			
				int last=model.getRowCount()-1;
				
				BasicFileReader seqFile = new BasicFileReader(this.strLocalDir+"//fsmOUT.txt");
								
				ArrayList sequences = new ArrayList();
				boolean hasSequences = true;
				if(seqFile.open()){
					while(!seqFile.endOfFile()){
						String line = seqFile.readLine();
						if(line.contains("*")){
							hasSequences = false;
							System.out.println("here");
							break;
						}
						sequences.add(line);
					}
					seqFile.close();
				}
				if(hasSequences){
					Scanner sc = new Scanner(sequences.toString());
					String separatorName = "";
					while (sc.hasNext())
					{
						String sequence = (String) sc.next();
						
						while(sc.hasNext() && (!sequence.endsWith(",") && !sequence.endsWith("]"))){
							sequence = sequence + "," + sc.next();
						}
						System.out.println(sequence);
						
						if(!sequence.startsWith("seq")) continue; // ignore the information lines
						
						String editedSequence = sequence.substring(sequence.indexOf("[")+1);
						editedSequence = editedSequence.replaceFirst("seq:,", "");
						
						if(editedSequence.endsWith("]")){
							editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
						}
						if(editedSequence.endsWith(",")){ //Para eliminar as vÃ­rgulas gerads pelo scanner
							editedSequence = editedSequence.substring(0, editedSequence.length()-1);
						}
						if(editedSequence.endsWith(",")){
							editedSequence = editedSequence.substring(0, editedSequence.length()-1);
						}
						System.out.println(editedSequence);
						
						File fLocalDir = new File("").getAbsoluteFile();
						String strLocalDir = fLocalDir.getPath();
						String separator = "";
						BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
										
						if(configFile.open()){
							separator = configFile.readLine();
							separatorName = separator;
							if(separator.equalsIgnoreCase("DEFAULT"))
								separator = ",";
							else if(separator.equalsIgnoreCase("SPACE"))
								separator = " ";
							else if(separator.equalsIgnoreCase("NOTHING") || separator.equalsIgnoreCase(""))
								separator = "";
							configFile.close();
						}
						else{
							separator = ",";
						}
						System.out.println(editedSequence);
						editedSequence = editedSequence.replaceAll(",", separator);
						System.out.println(editedSequence);
						model.setValueAt(editedSequence, last, 0);
						last++;
					}
				}
			}
			this.plavisEncode = writeFieldsPlavis(automaton);
			this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: UIO Method - END");
			
		}
		else if(jMethodList.getSelectedIndex()>=5){
			try{
				
				/** LOGGER - Begin */
				this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: NEW METHOD - "+ jMethodList.getSelectedItem().toString());
				/** LOGGER - End */
				
				System.out.println("New Method");
				File Dir = new File(this.strLocalDir);
				System.out.println(Dir.getPath());
				Process p;
				if(jMethodList.getSelectedItem().toString().endsWith(".jar")){
					p = Runtime.getRuntime().exec("java -jar "+this.jMethodList.getSelectedItem().toString()+" fsm.txt", null, Dir);
					p.waitFor();
					System.out.println("java -jar "+this.jMethodList.getSelectedItem().toString()+" fsm.txt");
				}
				else{
					p = Runtime.getRuntime().exec("./"+this.jMethodList.getSelectedItem().toString()+" fsm.txt", null, Dir);
					p.waitFor();
					System.out.println("./"+this.jMethodList.getSelectedItem().toString()+" fsm.txt");
				}

				int last=model.getRowCount()-1;
									
				BasicFileReader seqFile = new BasicFileReader(this.strLocalDir+"//fsmOUT.txt");
								
				ArrayList sequences = new ArrayList();
				boolean hasSequences = true;
				if(seqFile.open()){
					while(!seqFile.endOfFile()){
						String line = seqFile.readLine();
						if(line.contains("*")){
							hasSequences = false;
							System.out.println("here");
							break;
						}
						sequences.add(line);
					}
					seqFile.close();
				}
				if(hasSequences){
					Scanner sc = new Scanner(sequences.toString());
					while (sc.hasNext())
					{
						String sequence = (String) sc.next();
						String editedSequence = sequence;
						if(editedSequence.endsWith("]")){
							editedSequence = editedSequence.substring(0, editedSequence.indexOf("]"));
						}
						else{
							editedSequence = sequence.substring(0,sequence.indexOf(",")+1);
							editedSequence = sequence.substring(0,sequence.indexOf(",")+1);
							editedSequence = editedSequence.replaceAll("," , "");
							if(editedSequence.contains("[")){
								editedSequence = editedSequence.substring(editedSequence.indexOf("[")+1);
							}
						}
						model.setValueAt(editedSequence, last, 0);
						last++;
					}	
				
					
				//This is to verify if the set test is complete
					
//					//Process: ./n-complete initial1.txt < fsm1.txt
//					Process process = Runtime.getRuntime().exec("./n-complete.sh", null, Dir);
//					int i = process.waitFor();
//					System.out.println("n-complete: "+i);
//				
//					if(i==0){
//						JOptionPane.showMessageDialog(null, 
//			                    "The implemented Method generates a complete test set",
//			                    "Complete set", JOptionPane.INFORMATION_MESSAGE);
//					}
//					else{
//						JOptionPane.showMessageDialog(null, 
//			                    "The implemented Method does NOT generate a complete test set",
//			                    "Complete set", JOptionPane.ERROR_MESSAGE);
//					}
//					
//					OutputStream os = process.getOutputStream();
//				      
//			        InputStream is = process.getInputStream();
//			        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
//					//System.out.println(bf.readLine());
//			        
//			        process.destroy();
				}
				else{
					JOptionPane.showMessageDialog(null, 
		                    "There is no test set!",
		                    "Test set", JOptionPane.INFORMATION_MESSAGE);
				}
				
				/** LOGGER - Begin */
				this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Load Inputs: NEW METHOD - END");
				/** LOGGER - End */
				
			}
			catch(Exception eIE){
				eIE.printStackTrace();
			}
			
		}
//		else if(jMethodList.getSelectedItem().equals("n-complete")){
//			try{
//				File Dir = new File(this.strLocalDir);
//			
//				
//				
//			}
//			catch(Exception ex){
//			  	ex.printStackTrace();
//			}
//		}
		
		
	}
	
	
	private BasicFileWriter writeFieldsPlavis(Automaton auto) {
		// Add the states as subelements of the structure element.
		//State[] states = auto.getStates();
		
		File fLocalDir = new File("").getAbsoluteFile();
		String strLocalDir = fLocalDir.getPath()+"//Methods";
		File fMethodsDir = new File(strLocalDir);
		BasicFileWriter plavisFile = new BasicFileWriter(fMethodsDir.getPath()+"//fsm.txt");
		
		plavisFile.open();
		
		State initialState = auto.getInitialState();
		//try{
			Transition[] transFromInitialState = auto.getTransitionsFromState(initialState);
			for(int i = 0; i < transFromInitialState.length; i++){
				plavisFile.writeLine(transFromInitialState[i].getFromState().getName()+" -- "+
						transFromInitialState[i].getDescription().toString()+" -> "+
						transFromInitialState[i].getToState().getName());
				plavisFile.newLine();
			}
			
			Transition[] transitions = auto.getTransitions();
			for(int j = 0; j < transitions.length; j++){
				if(!transitions[j].getFromState().getName().equals(initialState.getName())){
					plavisFile.writeLine(transitions[j].getFromState().getName()+" -- "+
							transitions[j].getDescription().toString()+" -> "+
							transitions[j].getToState().getName());
					plavisFile.newLine();
				}
			}
//		}
//		catch(Exception e){
//			System.out.println("automato are not ready yet!");
//		}
			
		
//		//Header Plavis File
//		plavisFile.writeLine("estado FSMmain ou subestados ");	
//		for (int i = 0; i < states.length; i++){
//			if(i!=0)
//				plavisFile.writeLine(", ");
//			plavisFile.writeLine(states[i].getName());
//			if(auto.getInitialState().getName().equals(states[i].getName()))
//				plavisFile.writeLine(" def");
//        }
//		plavisFile.newLine();
//		
//		//Add the States
//        for (int i = 0; i < states.length; i++){
//			plavisFile.writeLine("estado "+states[i].getName()+" atomo\n");
//        	//System.out.println("estado "+states[i].getName()+" atomo");
//        }
//        plavisFile.writeLine(" ; \n");
//        //System.out.println(" ; ");
//        
//		// Add the transitions
//		//Transition[] transitions = auto.getTransitions();
//		
//		for (int i = 0; i < transitions.length; i++){
////			System.out.println("evento "+transitions[i].getDescription().toString().subSequence(0, 1)+
////					           " origem "+transitions[i].getFromState().getName()+
////					           " destino "+transitions[i].getToState().getName()+
////					           " acao { s"+transitions[i].getDescription().toString().subSequence(4, 5)+" }");
//			plavisFile.writeLine("evento "+transitions[i].getDescription().toString().subSequence(0, 1)+
//					           " origem "+transitions[i].getFromState().getName()+
//					           " destino "+transitions[i].getToState().getName()+
//					           " acao { s"+transitions[i].getDescription().toString().subSequence(4, 5)+" }\n");
//		}
//		plavisFile.writeLine(" ; \n");
//		//System.out.println(" ; ");
//		
		plavisFile.close();
	    
	    return plavisFile;
	}
	
	private BasicFileWriter writeFieldsPlavisWithSeparator(Automaton auto) {
		// Add the states as subelements of the structure element.
		//State[] states = auto.getStates();
		
		File fLocalDir = new File("").getAbsoluteFile();
		String strLocalDir = fLocalDir.getPath()+"//Methods";
		File fMethodsDir = new File(strLocalDir);
		BasicFileWriter plavisFile = new BasicFileWriter(fMethodsDir.getPath()+"//fsm.txt");
		System.out.println(fMethodsDir.getPath()+"/fsm.txt");
		if (plavisFile.open()) System.out.println("open");
		
		State initialState = auto.getInitialState();
		try{
			Transition[] transFromInitialState = auto.getTransitionsFromState(initialState);
			for(int i = 0; i < transFromInitialState.length; i++){
				plavisFile.writeLine(transFromInitialState[i].getFromState().getName()+" -- "+
						transFromInitialState[i].getDescription().toString().subSequence(0, 1)+"EnD / "+
						transFromInitialState[i].getDescription().toString().subSequence(4, 5)+" -> "+
						transFromInitialState[i].getToState().getName());
				plavisFile.newLine();
			}
			
			Transition[] transitions = auto.getTransitions();
			for(int i = 0; i < transitions.length; i++){
				if(!transitions[i].getFromState().equals(initialState)){
					plavisFile.writeLine(transitions[i].getFromState().getName()+" -- "+
							transitions[i].getDescription().toString().subSequence(0, 1)+"EnD / "+
							transitions[i].getDescription().toString().subSequence(4, 5)+" -> "+
							transitions[i].getToState().getName());
					plavisFile.newLine();
				}
			}
		}
		catch(Exception e){
			System.out.println("automato are not ready yet!");
		}
		plavisFile.close();
	    return plavisFile;
	}
	
	
	private BasicFileWriter writeFieldsPlavisToUIO(Automaton auto) {
		// Add the states as subelements of the structure element.
		//State[] states = auto.getStates();
		
		File fLocalDir = new File("").getAbsoluteFile();
		String strLocalDir = fLocalDir.getPath()+"//Methods";
		File fMethodsDir = new File(strLocalDir);
		BasicFileWriter plavisFile = new BasicFileWriter(fMethodsDir.getPath()+"//fsm.txt");
		System.out.println(fMethodsDir.getPath()+"//fsm.txt");
		if (plavisFile.open()) System.out.println("to UIO");
		
		State initialState = auto.getInitialState();
		try{
			Transition[] transFromInitialState = auto.getTransitionsFromState(initialState);
			for(int i = 0; i < transFromInitialState.length; i++){
				plavisFile.writeLine(transFromInitialState[i].getFromState().getName()+" -- "+
						transFromInitialState[i].getDescription().toString()+" -> "+
						transFromInitialState[i].getToState().getName()+";");
				plavisFile.newLine();
			}
			
			Transition[] transitions = auto.getTransitions();
			for(int i = 0; i < transitions.length; i++){
				if(!transitions[i].getFromState().equals(initialState)){
					plavisFile.writeLine(transitions[i].getFromState().getName()+" -- "+
							transitions[i].getDescription().toString()+" -> "+
							transitions[i].getToState().getName()+";");
					plavisFile.newLine();
				}
			}
		}
		catch(Exception e){
			System.out.println("automato are not ready yet!");
		}
		plavisFile.close();
	    return plavisFile;
	}
	
	public Transition[] getOrderTransition(State state){
		
		Transition[] trans = this.automaton.getTransitionsFromState(state);

		int count = 0;
		
		for(int i = 0; i < trans.length; i++){			
			for(int j = i; j < trans.length; j++){
				MealyTransition t1 = (MealyTransition) trans[i].clone();
				MealyTransition t2 = (MealyTransition) trans[j].clone();
//				System.out.println((t1.getLabel()+" "+t2.getLabel()+" "+t1.getLabel().compareTo(t2.getLabel())));
				if(t1.getLabel().compareTo(t2.getLabel()) > 0){
					
					Transition aux = trans[i];
					trans[i] = trans[j];
					trans[j] = aux;
				}
			}
		}
		
//		for(int a= 0; a < trans.length; a++) System.out.println("order:"+trans[a].toString());
//		System.out.println();
		
		return trans; 
	}
	
	
	public ArrayList<String> getOrder(ArrayList<String> list){
		
			ArrayList<String> orderList = new ArrayList<String>(); 
			Object[] array = list.toArray();

			int count = 0;
			
			for(int i = 0; i < array.length; i++){
				String str1 = array[i].toString();
				
				for(int j = i; j < array.length; j++){
					String str2 = array[j].toString();
					if(str1.compareTo(str2) > 0){
						Object aux = array[i];
						array[i] = array[j];
						array[j] = aux;
					}
				}
			}
			
			for(int k = 0; k < array.length; k++ ){
				orderList.add(array[k].toString());
			}
			
			return orderList; 
	}
	
	
	private Automaton automaton;
	private Environment environment;
	private JTable table;
	private JComboBox jMethodList;
	private String strLocalDir;
	private BasicFileWriter plavisEncode;

}
