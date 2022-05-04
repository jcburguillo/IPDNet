package window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Locale;

import games.*;
import games.IPDNet.*;


/**
	* This class offers information about a particular cell clicked over the Cell World.
	*
	* @author  Juan C. Burguillo Rial
	* @version 1.0
	*/
public class DlgInfoCell extends JDialog implements ActionListener, KeyListener
{
public static boolean bShowNeighbors = false;

private Cell oCell;

private JLabel	oJLabelID;
private JLabel	oJLabelPayoff;
private JLabel	oJLabelNeighbors;										// Alive cells around
private JLabel	oJLabelStrat_PMO[] = new JLabel[5];	// Probabilities
private JLabel	oJLabelBasicStrat;
private JLabel	oJLabelZDStrat;



/**
  * This is the constructor
  *
  * @param	oParent Pointer to the object that creates this window
  * @param	sTit   Title of this window
  * @param	bBool  Tells if it is modal (true) or not
  */
DlgInfoCell (JFrame oParent, String sTit, boolean bBool) {
  super (oParent, sTit, bBool);

  setBackground (Color.lightGray);
  setForeground (Color.black);
  
	setLayout (new GridLayout (12,2));
  
  JLabel oJLabel = new JLabel (" Cell Id:", JLabel.LEFT);
  add (oJLabel);
  oJLabelID = new JLabel ("", JLabel.CENTER);
  add (oJLabelID);

	oJLabel = new JLabel (" Neighbors:", JLabel.LEFT);
  add (oJLabel);
  oJLabelNeighbors = new JLabel ("", JLabel.CENTER);
  add (oJLabelNeighbors);

  oJLabel = new JLabel (" Payoff:", JLabel.LEFT);
  add (oJLabel);
  oJLabelPayoff = new JLabel ("", JLabel.CENTER);
  add (oJLabelPayoff);
  

	oJLabel = new JLabel (" Basic Strat:", JLabel.LEFT);
	add (oJLabel);
	oJLabelBasicStrat = new JLabel ("", JLabel.CENTER);
  add (oJLabelBasicStrat);
	
	oJLabel = new JLabel (" ZD Strat:", JLabel.LEFT);
	add (oJLabel);
	oJLabelZDStrat = new JLabel ("", JLabel.CENTER);
	add (oJLabelZDStrat);
	
	for (int i=0; i<5; i++) {
	  oJLabelStrat_PMO[i] = new JLabel ("", JLabel.CENTER);
	  switch (i) {
		  case 0:
  			oJLabel = new JLabel (" P0 == Po(C):", JLabel.LEFT);
  			break;
  			
  		case 1:
  			oJLabel = new JLabel (" P1 == Pcc(C) == Pr(C):", JLabel.LEFT);
  			oJLabel.setForeground (Color.red);
  			oJLabelStrat_PMO[i].setForeground (Color.red);
  			break;
  		
  		case 2:
  			oJLabel = new JLabel (" P2 == Pcd(C) == Ps(C):", JLabel.LEFT);
  			oJLabel.setForeground (Color.orange);
  			oJLabelStrat_PMO[i].setForeground (Color.orange);
  			break;
  		
  		case 3:
  			oJLabel = new JLabel (" P3 == Pdc(C) == Pt(C):", JLabel.LEFT);
  			oJLabel.setForeground (Color.blue);
  			oJLabelStrat_PMO[i].setForeground (Color.blue);
  			break;
  		
  		case 4:
  			oJLabel = new JLabel (" P4 == Pdd(C) == Pp(C):", JLabel.LEFT);
  			oJLabel.setForeground (Color.green);
  			oJLabelStrat_PMO[i].setForeground (Color.green);
  			break;
	  }
	  add (oJLabel);
	  add (oJLabelStrat_PMO[i]);
	}


  JButton oJButton = new JButton ("Grid");
  oJButton.addActionListener (this);
  oJButton.addKeyListener(this);
  add (oJButton);
  oJButton = new JButton ("Neighbors");
  oJButton.addActionListener (this);
  oJButton.addKeyListener(this);
  add (oJButton);
  oJButton = new JButton ("GET Checking Strat");
  oJButton.addActionListener (this);
  oJButton.addKeyListener(this);
  add (oJButton);
  oJButton = new JButton ("SET Checking Strat");
  oJButton.addActionListener (this);
  oJButton.addKeyListener(this);
  add (oJButton);
  
  

	setSize (new Dimension (350,550)); 
  setLocation (new Point (MainWindow.iMapSize + 220, 100));
  setResizable(false);
  setVisible(false);
  }






/**
 * Called from the game to update the new values
 *
 */
public void repaint() {
  vUpdate (oCell);
}



/**
 * This method updates all info labels
 *
 *  @param oCellAux		The cell to be updated
 */
public void vUpdate (Cell oCellAux) {
	oCell = oCellAux;
	int x = oCell.iGetPosX(), y = oCell.iGetPosY();
	int iBasicStrat = GameIPDNet.omStratPMO[x][y].iGet_Basic_Strat();

	String sCC = "      ";

	if (GameIPDNet.omStratPMO[oCell.iGetPosX()][oCell.iGetPosY()].bClusterStrat)
  	sCC = "  (CC)";
	  
 	oJLabelID.setText ("("+oCell.sGetID()+")");
  oJLabelPayoff.setText (String.format (Locale.ENGLISH, "%.3f", oCell.dGetPayoffAvgNumGamesxGen()) );
  
  oJLabelNeighbors.setText ("" + oCell.oVGetNeighbors().size());
  
	if (iBasicStrat < 8)
		oJLabelBasicStrat.setText (" == " + Strat_PMO.sBasicStrats[iBasicStrat] + sCC);
	else
		oJLabelBasicStrat.setText (" -> " +
							Strat_PMO.sBasicStrats [Strat_PMO.iFindCloserBasicStrat (GameIPDNet.omStratPMO[x][y].dmGet_Prob(), 1.0)] + sCC);
	
	int iZDStrat = GameIPDNet.omStratPMO[x][y].iGet_ZD_Strat();
	oJLabelZDStrat.setText (Strat_PMO.sZDStrats[iZDStrat]);
	
  for (int i=0; i<5; i++)
    oJLabelStrat_PMO[i].setText("" + String.format (Locale.ENGLISH, "%.3f",
    														GameIPDNet.dGetStrat_PMO (x, y, i) ) );
  
  setVisible (true);  
}




/**
  * This method receives and process the events generated by this class
  *
  *	@param evt This is the event parameter generated
  */
public void actionPerformed (ActionEvent evt) {
	int x = oCell.iGetPosX(), y = oCell.iGetPosY();

	if ("Grid".equals (evt.getActionCommand()))
		bShowNeighbors = false;

	else if ("Neighbors".equals (evt.getActionCommand()))
		bShowNeighbors = true;

	else if ("GET Checking Strat".equals (evt.getActionCommand())) {
		GameIPDNet.oClusterStratPMO = new Strat_PMO (GameIPDNet.dGetStrat_PMO (x, y));
		GameIPDNet.oClusterStratPMO.bClusterStrat = true;
	}
		
	else if ("SET Checking Strat".equals (evt.getActionCommand())) {
 		GameIPDNet.omStratPMO[x][y] = new Strat_PMO (GameIPDNet.oClusterStratPMO);
 		Vector ovNeighbors = (Vector) GameIPDNet.oCellMatrix[x][y].oVGetNeighbors();
 		for (int i=0; i<ovNeighbors.size(); i++) {
 			Cell oMate = (Cell) ovNeighbors.elementAt(i);
 			GameIPDNet.omStratPMO[oMate.iGetPosX()][oMate.iGetPosY()] = new Strat_PMO (GameIPDNet.oClusterStratPMO);
 		} 		
	}
		
	repaint();
	JPanelMainWindow.oWorldGrid.repaint();
}





public void keyTyped (KeyEvent e) {}
public void keyReleased (KeyEvent e) {}
public void keyPressed (KeyEvent e) {
	int x = oCell.iGetPosX();
	int y = oCell.iGetPosY();
	
	if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
		setVisible(false);
	
	else {
		if (e.getKeyCode()==KeyEvent.VK_LEFT) {
			if (x > 0) x--;
			else x = Game.iCellH - 1;
		}
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if (x < (Game.iCellH - 1) ) x++;
			else x = 0;
		}
		else if (e.getKeyCode()==KeyEvent.VK_UP) {
			if (y > 0) y--;
			else y = Game.iCellV - 1;
		}
		else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
			if (y < (Game.iCellV - 1)) y++;
			else y = 0;
		}
		
		oCell = GameIPDNet.oCellMatrix[x][y];
		JPanelMainWindow.oWorldGrid.vSetInfoCell (oCell);
		repaint();
		JPanelMainWindow.oWorldGrid.repaint();
	}	

}

}	// from the class
