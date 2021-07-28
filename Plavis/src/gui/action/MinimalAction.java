 /*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package gui.action;

import file.BasicFileWriter;
import grammar.Grammar;
//import grammar.parse.BruteParser;
//import grammar.parse.BruteParserEvent;
//import grammar.parse.BruteParserListener;
import automata.fsa.FSAStepByStateSimulator;
import automata.mealy.*;
import gui.JTableExtender;
import gui.SplitPaneFactory;
import gui.TableTextSizeSlider;
import gui.editor.ArrowDisplayOnlyTool;
import gui.editor.EditorPane;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.EnvironmentFactory;
import gui.environment.EnvironmentFrame;
import gui.environment.FrameFactory;
import gui.environment.GrammarEnvironment;
import gui.environment.Profile;
import gui.environment.Universe;
import gui.environment.EnvironmentFactory.EditorPermanentTag;
import gui.environment.tag.CriticalTag;
import gui.environment.tag.Tag;
import gui.grammar.GrammarInputPane;
import gui.grammar.parse.BruteParsePane;
import gui.grammar.parse.UnrestrictedTreePanel;
import gui.sim.TraceWindow;
import gui.sim.multiple.InputTableModel;
import gui.viewer.AutomatonPane;
import gui.viewer.SelectionDrawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.MinimalDetector;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.SimulatorFactory;
import automata.State;
import automata.Transition;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;

import fsm4ws.generationMethod.*;

/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction. This method can operate on any automaton.
 * 
 * @author Thomas Finley
 * @modified by Kyung Min (Jason) Lee
 */

public class MinimalAction extends MultipleSimulateAction {
	/**
	 * Instantiates a new <CODE>MultipleSimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public MinimalAction(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Highlight Equivalent States (unreduced FSM)");        

        this.automaton = automaton;
        this.environment = environment;
	}

	
	/**
	 * Returns the title for the type of compontent we will add to the
	 * environment.
	 * 
	 * @return in this base class, returns "Multiple Inputs"
	 */
	public String getComponentTitle() {
		return "unreduced FSM";
	}

	/**
	 * Provides an initialized multiple input table object.
	 * 
	 * @param obj
	 *            the automaton to provide the multiple input table for
	 * @return a table object for this automaton
	 * @see gui.sim.multiple.InputTableModel
	 */
	protected JTableExtender initializeTable(Object obj) {

		boolean toEquivalentTest = false;
		boolean multiple = false;
		int inputCount = 0;
        if(this.getEnvironment().myObjects!=null){
        	multiple = true;
        	inputCount = 1;
        }
        
        //System.out.println("In initialize:" + multiple);
        InputTableModel model = null;
        if(getObject() instanceof Automaton){
        	model = InputTableModel.getModel((Automaton)getObject(), multiple,true);
        }
       
		JTableExtender table = new JTableExtender(model, this);
		// In this regular multiple simulate pane, we don't care about
		// the outputs, so get rid of them.
		TableColumnModel tcmodel = table.getColumnModel();
		
		
		inputCount += model.getInputCount();
		for (int i = model.getInputCount(); i > 0; i--) {
			tcmodel.removeColumn(tcmodel.getColumn(inputCount));
		}
		if(multiple){
            ArrayList autos  = this.getEnvironment().myObjects;
            //System.out.println("In initialize: " + autos.size());
            ArrayList strings = this.getEnvironment().myTestStrings;
            int offset = strings.size();
            int row = 0;
            for(int m = 0; m < autos.size(); m++){      
                for(int k = 0; k < strings.size(); k++){
                    row = k+offset*m;
                    Object currentObj = autos.get(m);
                    if(currentObj instanceof Automaton){
                    	model.setValueAt(((Automaton)currentObj).getFileName(), row, 0); 
                    	model.setValueAt((String)strings.get(k), row, 1);                    	
                    }                
                }
                
            }
            while((model.getRowCount()-1) > (autos.size()*strings.size())){
            	model.deleteRow(model.getRowCount()-2);
            }
		}
		// Set up the last graphical parameters.
		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
		
		//table.setEnabled(false);
		table.setCellSelectionEnabled(false);
		
		return table;
	}
	
	public void performAction(Component source){
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Properties: Minimal");
		/** LOGGER - End */	
		
		if(getObject() instanceof Automaton){
			if (((Automaton)getObject()).getInitialState() == null) {
				JOptionPane.showMessageDialog(source,
						"Simulation requires an automaton\n"
								+ "with an initial state!", "No Initial State",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
       table = initializeTable(getObject());
       table.setEnabled(false);

		if(((InputTableModel)table.getModel()).isMultiple){
			getEnvironment().remove(getEnvironment().getActive());
			
		}
		
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(new JScrollPane(table), BorderLayout.CENTER);

		panel.add(new TableTextSizeSlider(table), BorderLayout.NORTH);
	
       
        myPanel = panel;
		// Set up the final view.
        Object finObject = getObject();
        if(finObject instanceof Automaton){
        	SelectionDrawer drawer = new SelectionDrawer(automaton);
    		MinimalDetector m = new MinimalDetector();
    		State[] eqStates = m.getEquivalentStates(automaton);
    		for (int i = 0; i < eqStates.length; i++)
    			drawer.addSelected(eqStates[i]);
        	AutomatonPane ap = new AutomatonPane(drawer);
        	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
        	JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
				0.5, ap, panel);
        	MultiplePane mp = new MultiplePane(split);
        	getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
    		});
    		getEnvironment().setActive(mp);
    		
    		InputTableModel model = (InputTableModel) table.getModel();
            try 
    		{
    			int last=model.getRowCount()-1;
    			Scanner sc = new Scanner(eqStates.toString());
    			
    			//while (sc.hasNext())
    			for(int i = 0; i < eqStates.length; i+=2)
    			{
    				String state1 = eqStates[i].getName();
    				String state2 = eqStates[i+1].getName();;
    				String editedSequence = "{"+state1+", "+state2+"}";
    				model.setValueAt(editedSequence, last, 0);
    				
    				last++;
    			}	
    		}
    		catch (Exception e1) {
    			// TODO Auto-generate catch block
    			e1.printStackTrace();
    		}
    		
        }	
               
	}
	
	private int getMachineIndexBySelectedRow(JTable table){
		InputTableModel model = (InputTableModel) table.getModel();
        int row = table.getSelectedRow();
        if(row < 0) return -1;
        String machineFileName = (String)model.getValueAt(row, 0);
        return getMachineIndexByName(machineFileName);
	}
	
	public int getMachineIndexByName(String machineFileName){
	        ArrayList machines = getEnvironment().myObjects;
	        if(machines == null) return -1;
	        for(int k = 0; k < machines.size(); k++){            
	            Object current = machines.get(k);
	            if(current instanceof Automaton){
	            	Automaton cur = (Automaton)current;
	            	if(cur.getFileName().equals(machineFileName)){
	            		return k;
	                }
	            }
	            else if(current instanceof Grammar){
	            	Grammar cur = (Grammar)current;
	            	if(cur.getFileName().equals(machineFileName)){
	            		return k;
	                }
	            }
	            
	        }
	        return -1;
	}
	
	/**
	 * Handles the creation of the multiple input pane.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		performAction((Component)e.getSource());		
	}

	/**
	 * @param machineFileName
     * 
     */
 
    /**
	 * This auxillary class is convenient so that the help system can easily
	 * identify what type of component is active according to its class.
	 */
	public class MultiplePane extends JPanel {
		public MultiplePane(JSplitPane split) {
			super(new BorderLayout());
			add(split, BorderLayout.CENTER);
			mySplit = split;
		}
		public JSplitPane mySplit = null;
	}
	
	
	protected JTable table = null;
    
    protected JPanel myPanel = null;
	
	private Automaton automaton;
	
	private Environment environment;
	
}
