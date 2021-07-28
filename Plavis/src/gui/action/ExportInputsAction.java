package gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import mutants.Mutant;

import file.BasicFileReader;
import file.BasicFileWriter;
import gui.environment.Environment;
import gui.sim.multiple.InputTableModel;




public class ExportInputsAction extends AbstractAction {

	public ExportInputsAction(Environment environment, JTable table, File file, ArrayList<Mutant> MutantsList){
		super("Save Test Section");
		this.table = table;
		this.file = file;
		this.MutantsList = MutantsList;
		this.environment = environment;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		/** LOGGER - Begin */
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " -Save Test Section: " + file.getAbsolutePath() + " - START");
		/** LOGGER - End */
		
		InputTableModel model = (InputTableModel) table.getModel();
		String[][] inputs = model.getInputs();

		//This part cleans empty cells of the input table
		try {
			// Make sure any recent changes are registered.
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException exception) {
			// We weren't editing anything, so we're OK.
		}       
		model.clear();
		try 
		{
			int last=model.getRowCount()-1;
			for(int i = 0; i<inputs.length; i++){
				if(!inputs[i][0].isEmpty()){
					model.setValueAt(inputs[i][0], last, 0);
					last++;
				}
			}		
		}
		catch (Exception e1) {
			// TODO Auto-generate catch block
			e1.printStackTrace();
		}
		model = (InputTableModel) table.getModel();
		inputs = model.getInputs();
		
		
		//Now, there isn't empty cells
        int inputsLength = inputs.length;
		if(inputsLength>0){
			File file = null;
			
			File fLocalDir = new File("").getAbsoluteFile();
			String strLocalDir = fLocalDir.getPath();
	    	String separator = "";
			BasicFileReader configFile = new BasicFileReader(strLocalDir+"//config.txt");
							
			if(configFile.open()){
				separator = configFile.readLine();
			}
			else{
				separator = "DEFAULT";
			}
			
			//New part
			if(this.file!=null){
				BasicFileWriter f = null;	
				BasicFileWriter fStatus = null;	
				try{
					String name = this.file.getCanonicalPath();
					
					if(!name.endsWith(".ptf")){
						name +=".ptf";
					}
					System.out.println(name);
					
					f = new BasicFileWriter(name);
					try{
						f.open();
						f.writeLine(separator);
						f.newLine();
						for(int i = 0; i<inputs.length; i++){
							f.writeLine(inputs[i][0]);
							f.newLine();
						}
						f.close();
					}					
					catch(Exception e2)
					{
						System.out.println("ERROR");
					}
					
					String nameStatus;
					nameStatus = name.replaceFirst(".ptf", "_status.ptsf");
					fStatus = new BasicFileWriter(nameStatus);
					try{
						fStatus.open();
						ArrayList<String> InputsList = new ArrayList<String>();
						for(int i = 0; i<inputs.length; i++){
							InputsList.add(inputs[i][0]);
						}
						
						Iterator<Mutant> itMut = this.MutantsList.iterator();
						while(itMut.hasNext()){
							Mutant mut = itMut.next();
							String line = mut.getStatus()+"";
							Iterator<String> itTC = mut.getTestCases().iterator();
							//System.out.println(mut.getTestCases().size());
							while(itTC.hasNext()){
								line = line.concat(" "+InputsList.indexOf(itTC.next()));								
							}
							fStatus.writeLine(line);
							fStatus.newLine();
						}
						fStatus.close();
					}
					catch(Exception e3)
					{
						System.out.println("ERROR STATUS");
					}
					
					
				}
				catch(IOException ioe){
					System.out.println("File not found: "+ioe.toString());
				}
				
			}
			else{
				//Original Method
				while (file == null){
					JFileChooser ourChooser = new JFileChooser();
					int retval=ourChooser.showSaveDialog(null);
					BasicFileWriter f = null;					
					if (retval==JFileChooser.CANCEL_OPTION) break;
					if (retval==JFileChooser.APPROVE_OPTION)
					{
						ourChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						
						try
						{
							String name = ourChooser.getSelectedFile().getCanonicalPath();
							file = new File(name);
							
							if (file.exists()) {
								int result = JOptionPane.showConfirmDialog(null, "Overwrite "
										+ file.getName() + "?");
								switch (result) {
								case JOptionPane.CANCEL_OPTION:
									break;
								case JOptionPane.NO_OPTION:
									file = null;
									break;
								default:
									if(!name.endsWith(".ptf")) name +=".ptf";
									System.out.println(name);
									f = new BasicFileWriter(name);
									try{
										f.open();
										for(int i = 0; i<inputs.length; i++){
											f.writeLine(inputs[i][0]);
											f.newLine();
										}
										f.close();
									}
									catch(Exception e2)
									{
										System.out.println("ERROR");
									}
									
								}
							}
							else{
								if(!name.endsWith(".ptf")) name +=".ptf";
								System.out.println(name);
								f = new BasicFileWriter(name);
								try{
									f.open();
									for(int i = 0; i<inputs.length; i++){
										f.writeLine(inputs[i][0]);
										f.newLine();
									}
									f.close();
								}
								catch(Exception e2)
								{
									System.out.println("ERROR");
								}
							}
						}
						catch (Exception e1) {
							// TODO Auto-generate catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
		else{
			System.out.println("empty");
		}
		
		/** LOGGER - Begin */
		this.environment.getLogArea().append("\n" + sdf.format(new Date()) + " - Save Test Section: END");
		/** LOGGER - End */
	}
	
	
	private JTable table;
	private File file;
	private ArrayList<Mutant> MutantsList;
	private Environment environment;
	
	
}
