package games.IPDNet;

import javax.swing.*;

import window.JPanelMainWindow;
import window.MainWindow;

import java.awt.*;
import java.awt.event.*;

import games.*;



/**
* This class allows to obtain a dialog parameter window with two buttons: OK and Cancel.
*
* @author  Juan C. Burguillo Rial
* @version 1.0
*/
public class DlgParam extends JDialog implements ActionListener, GameCONS
{
private MainWindow oMainWindow;

private Label oLabel;

private JTextField	oTdProbMutation,
										oTdProbNoise,
										oTdProbEnding,
										oTdPMOInterval,
										oTiNumGamesxEncounter,
										oTdOwnWeight,
										oTdProbImitation,
										oTiPMOsBufferSize,
										oTiDiffProbBuffer2Stop;

private Choice oChChangeModel, oChChangeType, oChChangeProbModel;


/**
 * This is the Dialog constructor
 *
 * @param	oPadre 	Pointer to the parent
 * @param	sTit   	Dialog title
 * @param	bBool 	Tells if the window is modal (true) or not
 */
public DlgParam (JFrame oParent, String sTit, boolean bBool) {
  super (oParent, sTit, bBool);

  oMainWindow = (MainWindow) oParent;
  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(new GridLayout(13,2));
  
	oLabel = new Label (" Exploration Mechanism:", Label.LEFT);
  add (oLabel);
  oChChangeType = new Choice();
  for (int i=0; i<sCHANGE_TYPE.length; i++)
    oChChangeType.add (sCHANGE_TYPE[i]);
  oChChangeType.select (Game.iChangeType);
  add (oChChangeType);
  
  oLabel = new Label (" Change Model:", Label.LEFT);
  add (oLabel);
  oChChangeModel = new Choice();
  for (int i=0; i<sCHANGE_MODEL.length; i++)
  	oChChangeModel.add (sCHANGE_MODEL[i]);
 	oChChangeModel.select (sCHANGE_MODEL[GameIPDNet.iChangeModel]);
  add (oChChangeModel);
  
  oLabel = new Label (" Probability Model:", Label.LEFT);
  add (oLabel);
  oChChangeProbModel = new Choice();
  for (int i=0; i<sPROB_MODEL.length; i++)
  	oChChangeProbModel.add (sPROB_MODEL[i]);
  oChChangeProbModel.select (GameIPDNet.iProbModel);
  add (oChChangeProbModel);
  
  oLabel = new Label (" Mutation Prob. [0,1]:", Label.LEFT);
  add (oLabel);
  oTdProbMutation = new JTextField(String.valueOf (Game.dProbMutation), 7);
  add (oTdProbMutation);
  
  oLabel = new Label (" Noise Prob. [0,1]:", Label.LEFT);
  add (oLabel);
  oTdProbNoise = new JTextField(String.valueOf(GameIPDNet.dProbNoise), 7);
  add (oTdProbNoise);
  
	oLabel = new Label (" Prob. Premature Ending [0,1]:", Label.LEFT);
  add (oLabel);
  oTdProbEnding = new JTextField(String.valueOf (GameIPDNet.dProbEnding), 7);
  add (oTdProbEnding);
    
  oLabel = new Label (" Discrete PMO Interval (0,1]:", Label.LEFT);
  add (oLabel);
  oTdPMOInterval = new JTextField(String.valueOf (GameIPDNet.dPMOInterval), 7);
  add (oTdPMOInterval);

  oLabel = new Label (" Num. Games x Encounter:", Label.LEFT);
  add (oLabel);
  oTiNumGamesxEncounter = new JTextField(String.valueOf (GameIPDNet.iNumGamesxEncounter), 7);
  add (oTiNumGamesxEncounter);
  
  oLabel = new Label (" Own Weight in Crossover:", Label.LEFT);
  add (oLabel);
  oTdOwnWeight = new JTextField(String.valueOf (GameIPDNet.dOwnWeight), 7);
  add (oTdOwnWeight);
  
  oLabel = new Label (" Prob. ChangeAction [0,1]:", Label.LEFT);
  add (oLabel);
  oTdProbImitation = new JTextField(String.valueOf (Game.dProbChangeAction), 7);
  add (oTdProbImitation);

  oLabel = new Label (" Buffer generations to Stop:", Label.LEFT);
  add (oLabel);
  oTiPMOsBufferSize = new JTextField(String.valueOf (GameIPDNet.iPMOsBufferSize), 7);
  add (oTiPMOsBufferSize);  

  oLabel = new Label (" Variation Percent to Stop [0,100]:", Label.LEFT);
  add (oLabel);
  oTiDiffProbBuffer2Stop = new JTextField(String.valueOf (GameIPDNet.iDiff_PMOsBuffer2Stop), 7);
  add (oTiDiffProbBuffer2Stop);
  
  JButton oBut = new JButton ("OK");
  oBut.addActionListener (this);
  add (oBut);
  oBut  = new JButton ("Cancel");
  oBut.addActionListener (this);
  add (oBut);
  
  setSize(new Dimension(500,800));
  
  setLocation (new Point (400, 0));
  setResizable(false);
  setVisible(true);
}



/**
 * This method process all the events produced by this class
 *
 *	@param evt This is the event received
 */
public void actionPerformed (ActionEvent evt) {

  if ("OK".equals (evt.getActionCommand())) {
  	  	
  	GameIPDNet.iChangeModel = oChChangeModel.getSelectedIndex();  	
  	Game.iChangeType = oChChangeType.getSelectedIndex();
  	GameIPDNet.iProbModel = oChChangeProbModel.getSelectedIndex();
  	  	  	
  	Game.dProbMutation = Double.parseDouble (oTdProbMutation.getText());
  	Game.dProbNoise = Double.parseDouble (oTdProbNoise.getText());   	
		GameIPDNet.dProbEnding = Double.parseDouble (oTdProbEnding.getText());
  	GameIPDNet.iNumGamesxEncounter = Integer.parseInt (oTiNumGamesxEncounter.getText());

    JPanelMainWindow.oJLabelPm.setText ("Pm: " + GameIPDNet.dProbMutation);
    JPanelMainWindow.oJLabelPn.setText ("Pn: " + GameIPDNet.dProbNoise);
    JPanelMainWindow.oJLabelPe.setText ("Pe: " + GameIPDNet.dProbEnding);
    JPanelMainWindow.oJLabelGames.setText ("Games: " + GameIPDNet.iNumGamesxEncounter);    
    
  	GameIPDNet.dOwnWeight = Double.parseDouble (oTdOwnWeight.getText());
  	GameIPDNet.dPMOInterval = Double.parseDouble (oTdPMOInterval.getText());  	
  	GameIPDNet.iPMOsBufferSize = Integer.parseInt (oTiPMOsBufferSize.getText());
  	GameIPDNet.iDiff_PMOsBuffer2Stop = Integer.parseInt (oTiDiffProbBuffer2Stop.getText());
  			
  	Game.dProbChangeAction = Double.parseDouble (oTdProbImitation.getText());
  }

  dispose();
}

}	// from the class
