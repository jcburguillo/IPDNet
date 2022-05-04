package games.IPDNet;

import javax.swing.*;

import window.JPanelMainWindow;
import window.MainWindow;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import games.*;



/**
* This class allows to obtain a dialog parameter window with two buttons: OK and Cancel.
*
* @author  Juan C. Burguillo Rial
* @version 1.0
*/
public class DlgCluster extends JDialog implements ActionListener, KeyListener, GameCONS
{
private Label oLabel;

private JTextField	oTdP0,
										oTdP1,
										oTdP2,
										oTdP3,
										oTdP4,
										oTiMaxCheckingTimes,
										oTdCC_Cells2PassChecking;



/**
 * This is the Dialog constructor
 *
 * @param	oPadre 	Pointer to the parent
 * @param	sTit   	Dialog title
 * @param	bBool 	Tells if the window is modal (true) or not
 */
public DlgCluster (JFrame oParent, String sTit, boolean bBool) {
  super (oParent, sTit, bBool);

  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(new GridLayout(7,2));

  oLabel = new Label (" P0 [0,1]:", Label.LEFT);
  add (oLabel);
  oTdP0 = new JTextField (String.valueOf (GameIPDNet.oClusterStratPMO.dmProb[0]), 7);
  add (oTdP0);

  oLabel = new Label (" P1 [0,1]:", Label.LEFT);
  add (oLabel);
  oTdP1 = new JTextField (String.valueOf (GameIPDNet.oClusterStratPMO.dmProb[1]), 7);
  add (oTdP1);

  oLabel = new Label (" P2 [0,1]:", Label.LEFT);
  add (oLabel);
  oTdP2 = new JTextField (String.valueOf (GameIPDNet.oClusterStratPMO.dmProb[2]), 7);
  add (oTdP2);

  oLabel = new Label (" P3 [0,1]:", Label.LEFT);
  add (oLabel);
  oTdP3 = new JTextField (String.valueOf (GameIPDNet.oClusterStratPMO.dmProb[3]), 7);
  add (oTdP3);

  oLabel = new Label (" P4 [0,1]:", Label.LEFT);
  add (oLabel);
  oTdP4 = new JTextField (String.valueOf (GameIPDNet.oClusterStratPMO.dmProb[4]), 7);
  add (oTdP4);

  oLabel = new Label (" Ratio to Pass Checking [0,1]:", Label.LEFT);
  add (oLabel);
  oTdCC_Cells2PassChecking = new JTextField (String.valueOf (GameIPDNet.dRatio4ClusterDomination), 7);
  add (oTdCC_Cells2PassChecking);  
    
  JButton oButSET = new JButton ("OK");
  oButSET.addActionListener (this);
  add (oButSET);
  JButton oButCancel  = new JButton ("Cancel");
  oButCancel.addActionListener (this);
  add (oButCancel);
    
  setSize(new Dimension(500,450));
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
  	
  	GameIPDNet.oClusterStratPMO.bClusterStrat = true;
  	GameIPDNet.oClusterStratPMO.dmProb[0] = Double.parseDouble (oTdP0.getText());
  	GameIPDNet.oClusterStratPMO.dmProb[1] = Double.parseDouble (oTdP1.getText());
  	GameIPDNet.oClusterStratPMO.dmProb[2] = Double.parseDouble (oTdP2.getText());
  	GameIPDNet.oClusterStratPMO.dmProb[3] = Double.parseDouble (oTdP3.getText());
  	GameIPDNet.oClusterStratPMO.dmProb[4] = Double.parseDouble (oTdP4.getText());
  	GameIPDNet.dRatio4ClusterDomination = Double.parseDouble (oTdCC_Cells2PassChecking.getText());
  }

  dispose();
}




public void keyTyped (KeyEvent e) {}
public void keyReleased (KeyEvent e) {}
public void keyPressed (KeyEvent e) {
	if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
		dispose();
}

}	// from the class
