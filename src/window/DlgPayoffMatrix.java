package window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import games.Game;



/**
* This class allows to obtain a dialog parameter window with two buttons: OK and Cancel.
*
* @author  Juan C. Burguillo Rial
* @version 1.0
*/
class DlgPayoffMatrix extends JDialog implements ActionListener
{
private Label oLabel;
private	TextField[][] oMatTF = new TextField[5][5];		// Se inicializa en el constructor



/**
 * This is the Dialog constructor
 *
 * @param	oPadre 	Pointer to the parent
 * @param	sTit   	Dialog title
 * @param	bBool 	Tells if the window is modal (true) or not
 */
DlgPayoffMatrix (JFrame oPadre, String sTit, boolean bBool) {
  super (oPadre, sTit, bBool);

  setFont (new Font ("System", Font.BOLD, 10));
  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(new GridLayout(7,6));

  oLabel = new Label ("P1 vs P2", Label.CENTER);
  add (oLabel);
  
  for (int i=0; i<5; i++) {
	oLabel = new Label (""+i, Label.CENTER);
    add (oLabel);
  }

  for (int i=0; i<5; i++) {
    oLabel = new Label (""+i, Label.CENTER);
    add (oLabel);
    for (int j=0; j<5; j++) {
      oMatTF[i][j] = new TextField (""+Game.dPayMatrix[i][j][0]+" , "+Game.dPayMatrix[i][j][1], 4);
      add (oMatTF[i][j]);
    }
  }

  oLabel = new Label ("");
  add (oLabel);
  JButton oBut = new JButton ("OK");
  oBut.addActionListener (this);
  add (oBut);
  oLabel = new Label ("");
  add (oLabel);
  oLabel = new Label ("");
  add (oLabel);
  oBut  = new JButton ("Cancel");
  oBut.addActionListener (this);
  add (oBut);
  oLabel = new Label ("");
  add (oLabel);

  setSize(new Dimension(500, 250));
  setLocation (new Point (730, 200));
  setResizable(false);
  setVisible(true);
  }



/**
 * This method process all the events produced by this class
 *
 *	@param evt This is the event received
 */
public void actionPerformed (ActionEvent evt) {
	int iIndex;
	String sCad, sPayoff1, sPayoff2;
	
  if ("OK".equals (evt.getActionCommand())) {
    for (int i=0; i<5; i++)
      for (int j=0; j<5; j++) {
				sCad = oMatTF[i][j].getText();
				iIndex = sCad.indexOf (",");
				sPayoff1 = sCad.substring(0, iIndex);
				sPayoff2 = sCad.substring(iIndex+1);
				sPayoff1 = sPayoff1.trim();
				sPayoff2 = sPayoff2.trim();
				Game.dPayMatrix[i][j][0] = Double.parseDouble (sPayoff1);
				Game.dPayMatrix[i][j][1] = Double.parseDouble (sPayoff2);
      }

    dispose();
  }
  else if ("Cancel".equals (evt.getActionCommand()))
    dispose();
}


}	// from the class
