package window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import machineLearning.Brain;


/**
	* This class offers information about a particular cell clicked over the Cell World.
	*
	* @author  Juan C. Burguillo Rial
	* @version 1.0
	*/
public class DlgInfoBrain extends JDialog implements ActionListener
{
private int imCellPosX = 0;
private int imCellPosY = 0;

private JTextArea oJTextArea;



/**
  * This is the constructor
  *
  * @param	oParent Pointer to the object that creates this window
  * @param	sTit   Title of this window
  * @param	bBool  Tells if it is modal (true) or not
  */
DlgInfoBrain (JFrame oParent, String sTit, boolean bBool) {
  super (oParent, sTit, bBool);

  setBackground (Color.lightGray);
  setForeground (Color.black);
  
	setLayout (new GridLayout (1,1));	

  oJTextArea = new JTextArea ();
  oJTextArea.setEditable(false);
  oJTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
  add (oJTextArea);
 
	setSize (new Dimension (1100,500)); 
  setLocation (new Point (MainWindow.iMapSize + 220, 100));
  setResizable(true);
  setVisible(false);
  }






/**
 * Called from the game to update the new values
 *
 */
public void repaint() {
  vUpdate (imCellPosX, imCellPosY);
}



/**
 * This method updates all info about this SOM cell and its associated LA
 *
 *  @param imCellPosX		Horizontal position of the SOM cell
 *  @param imCellPosY		Vertical position of the SOM cell
 */
public void vUpdate (int x, int y) {
	imCellPosX = x;
	imCellPosY = y;
	oJTextArea.setText (Brain.sPrintInfoBrain (x,y));	
  setVisible (true);  
}




/**
  * This method receives and process the events generated by this class
  *
  *	@param evt This is the event parameter generated
  */
public void actionPerformed (ActionEvent evt) {
	if ("OK".equals (evt.getActionCommand()))
		setVisible (false);
}


}	// from the class
