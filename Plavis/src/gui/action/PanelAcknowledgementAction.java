package gui.action;

import java.awt.event.ActionEvent;

import gui.environment.EnvironmentFrame;
import gui.environment.PanelAcknowledgement;

import javax.swing.AbstractAction;


public class PanelAcknowledgementAction extends AbstractAction{

	public PanelAcknowledgementAction(EnvironmentFrame frame){
		super("Acknowledgement");
		this.frame = frame;

	}
	
	public void actionPerformed(ActionEvent e){
		 new PanelAcknowledgement().setVisible(true);
	}
	
	
	
	public EnvironmentFrame frame;
}
