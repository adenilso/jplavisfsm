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

import gui.editor.ArrowDisplayOnlyTool;
import gui.environment.Environment;
import gui.environment.tag.CriticalTag;
import gui.viewer.AutomatonPane;
import gui.viewer.SelectionDrawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import automata.Automaton;
import automata.CompletenessDetector;
import automata.ConnectivityDetector;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.State;

/**
 * This is the action used to highlight nondeterministic states.
 * 
 * @author Thomas Finley
 */

public class ConnectivityAction extends AutomatonAction {
	/**
	 * Instantiates a new <CODE>NondeterminismAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public ConnectivityAction(Automaton automaton, Environment environment) {
		super("Highlight Inicially Disconnected States", null);
		this.automaton = automaton;
		this.environment = environment;
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Properties: Conectivity");
		/** LOGGER - End */	
		
		SelectionDrawer drawer = new SelectionDrawer(automaton);
		//MUDAR!!
		ConnectivityDetector d = new ConnectivityDetector();
		State[] nd = d.getInitiallyDisconnectedStates(automaton);
		for (int i = 0; i < nd.length; i++)
			drawer.addSelected(nd[i]);
		AutomatonPane ap = new AutomatonPane(drawer);
		ConnectivityPane pane = new ConnectivityPane(ap);
		environment.add(pane, "Connectivity", new CriticalTag() {
		});
		environment.setActive(pane);
	}

	/**
	 * This action is only applicable to automaton objects.
	 * 
	 * @param object
	 *            the object to test for being an automaton
	 * @return <CODE>true</CODE> if this object is an instance of a subclass
	 *         of <CODE>Automaton</CODE>, <CODE>false</CODE> otherwise
	 */
	public static boolean isApplicable(Object object) {
		return object instanceof Automaton;
	}

	/**
	 * A class that exists to make integration with the help system feasible.
	 */
	private class ConnectivityPane extends JPanel {
		public ConnectivityPane(AutomatonPane ap) {
			super(new BorderLayout());
			ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
			add(ap, BorderLayout.CENTER);
			add(new JLabel("Initially Disconnected States are highlighted."),
					BorderLayout.NORTH);
		}
	}

	/** The automaton this simulate action runs simulations on! */
	private Automaton automaton;

	/** The environment that the simulation pane will be put in. */
	private Environment environment;
}
