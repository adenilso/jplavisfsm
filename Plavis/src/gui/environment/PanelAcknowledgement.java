package gui.environment;

import java.awt.Component;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class PanelAcknowledgement extends JFrame {

    public PanelAcknowledgement() {
    	super("JPlavidFSM - About");
    	this.setAlwaysOnTop(true);
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelSponsored = new javax.swing.JPanel();
        buttonFomentoL = new javax.swing.JButton();
        buttonFomentoC = new javax.swing.JButton();
        buttonFomentoR = new javax.swing.JButton();
        panelSupported = new javax.swing.JPanel();
        buttonInstL = new javax.swing.JButton();
        buttonInstC = new javax.swing.JButton();
        buttonInstR = new javax.swing.JButton();
        panelPowered = new javax.swing.JPanel();
        buttonToolL = new javax.swing.JButton();
        buttonToolC = new javax.swing.JPanel();
        panelAuthors = new javax.swing.JPanel();
        buttonToolR = new javax.swing.JButton();
        //buttonToolR = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        panelSponsored.setBorder(javax.swing.BorderFactory.createTitledBorder("Sporsored by:"));
        panelSponsored.setLayout(new java.awt.GridBagLayout());

        buttonFomentoL.setText("");
        buttonFomentoL.setBorderPainted(false);
        buttonFomentoL.setContentAreaFilled(false);
        buttonFomentoL.setFocusPainted(false);
        buttonFomentoL.setFocusable(false);
        buttonFomentoL.setRequestFocusEnabled(false);
        buttonFomentoL.setRolloverEnabled(false);
        buttonFomentoL.setIcon(new ImageIcon(getClass().getResource("/pictures/fapesp.jpg")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panelSponsored.add(buttonFomentoL, gridBagConstraints);

        buttonFomentoC.setText("");
        buttonFomentoC.setBorderPainted(false);
        buttonFomentoC.setContentAreaFilled(false);
        buttonFomentoC.setFocusPainted(false);
        buttonFomentoC.setFocusable(false);
        buttonFomentoC.setRequestFocusEnabled(false);
        buttonFomentoC.setRolloverEnabled(false);
        buttonFomentoC.setIcon(new ImageIcon(getClass().getResource("/pictures/CAPESlogo.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panelSponsored.add(buttonFomentoC, gridBagConstraints);

        buttonFomentoR.setText("");
        buttonFomentoR.setBorderPainted(false);
        buttonFomentoR.setContentAreaFilled(false);
        buttonFomentoR.setFocusPainted(false);
        buttonFomentoR.setFocusable(false);
        buttonFomentoR.setRequestFocusEnabled(false);
        buttonFomentoR.setRolloverEnabled(false);
        buttonFomentoR.setIcon(new ImageIcon(getClass().getResource("/pictures/cnpq.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panelSponsored.add(buttonFomentoR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(panelSponsored, gridBagConstraints);

        panelSupported.setBorder(javax.swing.BorderFactory.createTitledBorder("Supported by"));
        panelSupported.setLayout(new java.awt.GridBagLayout());

        buttonInstL.setText("");
        buttonInstL.setBorderPainted(false);
        buttonInstL.setContentAreaFilled(false);
        buttonInstL.setFocusPainted(false);
        buttonInstL.setFocusable(false);
        buttonInstL.setRequestFocusEnabled(false);
        buttonInstL.setRolloverEnabled(false);
        buttonInstL.setIcon(new ImageIcon(getClass().getResource("/pictures/ICMClogo.jpg")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panelSupported.add(buttonInstL, gridBagConstraints);

        buttonInstC.setText("");
        buttonInstC.setBorderPainted(false);
        buttonInstC.setContentAreaFilled(false);
        buttonInstC.setFocusPainted(false);
        buttonInstC.setFocusable(false);
        buttonInstC.setRequestFocusEnabled(false);
        buttonInstC.setRolloverEnabled(false);
        buttonInstC.setIcon(new ImageIcon(getClass().getResource("/pictures/inctsec.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panelSupported.add(buttonInstC, gridBagConstraints);

        buttonInstR.setText("");
        buttonInstR.setBorderPainted(false);
        buttonInstR.setContentAreaFilled(false);
        buttonInstR.setFocusPainted(false);
        buttonInstR.setFocusable(false);
        buttonInstR.setRequestFocusEnabled(false);
        buttonInstR.setRolloverEnabled(false);
        buttonInstR.setIcon(new ImageIcon(getClass().getResource("/pictures/INPElogo.jpg")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panelSupported.add(buttonInstR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(panelSupported, gridBagConstraints);

        panelAuthors.setBorder(javax.swing.BorderFactory.createTitledBorder("Authors:")); 
        panelAuthors.setLayout(new java.awt.GridBagLayout());

        buttonToolL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonToolL.setLayout(new java.awt.GridBagLayout());
        buttonToolL.setBorderPainted(false);
        buttonToolL.setContentAreaFilled(false);
        buttonToolL.setFocusPainted(false);
        buttonToolL.setFocusable(false);
        buttonToolL.setRequestFocusEnabled(false);
        buttonToolL.setRolloverEnabled(false);
        JTextPane textPane1 = new JTextPane();
		textPane1.setContentType("text/html");  
		textPane1.setEditable(false);
        String str = "<html><center><b>Arineiza Cristina Pinheiro</b> <br> <i>arineiza at icmc.usp.br</i> " + "<br><b> & </b>"+
        		     "<br> <b>Adenilso da Silva Simão</b><br><i>adenilso at icmc.usp.br</i> </center></html>";
        textPane1.setText(str);
        textPane1.setSize(200, 200);
        textPane1.setEditable(false);
        
        buttonToolL.add(textPane1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 50.0;
        panelAuthors.add(buttonToolL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(panelAuthors, gridBagConstraints);
        
        
        
        
        panelPowered.setBorder(javax.swing.BorderFactory.createTitledBorder("Powered by:")); 
        panelPowered.setLayout(new java.awt.GridBagLayout());


        buttonToolR.setText("");
        buttonToolR.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonToolR.setBorderPainted(false);
        buttonToolR.setContentAreaFilled(false);
        buttonToolR.setFocusPainted(false);
        buttonToolR.setFocusable(false);
        buttonToolR.setRequestFocusEnabled(false);
        buttonToolR.setRolloverEnabled(false);
        
        buttonToolR.setIcon(new ImageIcon(getClass().getResource("/pictures/jflapLogo.jpg")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        panelPowered.add(buttonToolR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
     //   gridBagConstraints.
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 50.0;
        gridBagConstraints.weighty = 50.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 1);
        add(panelPowered, gridBagConstraints);
        
        
        
        
        
        setSize(600,600);
        setVisible(true);
    }

    private javax.swing.JButton buttonFomentoR;
    private javax.swing.JButton buttonFomentoC;
    private javax.swing.JButton buttonFomentoL;
    private javax.swing.JButton buttonInstC;
    private javax.swing.JButton buttonInstL;
    private javax.swing.JButton buttonInstR;
    private javax.swing.JButton buttonToolR;
    private javax.swing.JPanel buttonToolC;
    private javax.swing.JButton buttonToolL;
    private javax.swing.JPanel panelAuthors;
    private javax.swing.JPanel panelPowered;
    private javax.swing.JPanel panelSponsored;
    private javax.swing.JPanel panelSupported;

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new PanelAcknowledgement().setVisible(true);
//            }
//        });
//    }
}


