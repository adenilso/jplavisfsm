package gui.action;

import java.awt.event.ActionEvent;

import gui.environment.ConfigurationEnvironment;
import gui.environment.EnvironmentFrame;

import javax.swing.AbstractAction;


public class ConfigurationAction extends AbstractAction{

	public ConfigurationAction(EnvironmentFrame frame){
		super("Input Separator");
		this.frame = frame;

	}
	
	public void actionPerformed(ActionEvent e){
		 new ConfigurationEnvironment(frame).setVisible(true);
	}
	
	
	
	public EnvironmentFrame frame;
}
