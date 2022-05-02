package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
  * This class offers a window to put strings and an OK button to leave.
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class DialogOK extends JDialog implements ActionListener, KeyListener
{
/**
  * This is the constructor
  *
  * @param	oParent   Pointer to the object that creates this window
  * @param	sTit      Title of this window
  * @param	bBool     Tells if it is modal (true) or not
  * @param	sCad	  Contains the different strings to show in the window
  */
public DialogOK(JFrame oParent, String sTit, boolean bBool, String sCad[])
  {
  super (oParent, sTit, bBool);

  setLayout(new GridLayout(sCad.length+4,1));
  add (new Label("",Label.CENTER));
  for (int i=0; i<sCad.length; i++)
    add (new Label(sCad[i],Label.CENTER));

  add (new Label("",Label.CENTER));
  Panel oPanel = new Panel();
  oPanel.setLayout (new GridLayout(1,3));
  oPanel.add (new Label("",Label.CENTER));
  JButton oOK = new JButton ("OK");
  oOK.addActionListener (this);
  oOK.addKeyListener(this);
  oPanel.add (oOK);
  oPanel.add (new Label("",Label.CENTER));
  add (oPanel);
  add (new Label("",Label.CENTER));
  setSize(new Dimension(70+50*sCad.length, 200));
  setLocation (new Point (300, 200));
  setResizable(false);
  setVisible(true);
  }



/**
 * This method receives and process the events generated by this class
 *
 *	@param evt This is the event parameter generated
 */
public void actionPerformed (ActionEvent evt)
  {
  if ("OK".equals (evt.getActionCommand()))
    dispose();
  }

public void keyTyped (KeyEvent e) {}
public void keyReleased (KeyEvent e) {}
public void keyPressed (KeyEvent e) {
	if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
		dispose();
}
}	// de la clase DialogOK






