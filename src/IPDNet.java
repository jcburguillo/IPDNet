import java.awt.*;
import java.awt.event.*;

import games.Game;
import window.*;


/**
 * Cellular Networks
 *
 * @author  Juan C. Burguillo Rial
 * @version 1.0
 */
public class IPDNet extends java.applet.Applet implements ActionListener
{
private MainWindow oVM;

/**
  * This method establishes the initial conditions in the applet.
  */
public void init() {
	Button oBoton = new Button ("Open IPDNet");
	oBoton.addActionListener (this);
  add(oBoton);
	oBoton = new Button ("Close IPDNet");
	oBoton.addActionListener (this);
  add(oBoton);
  oVM = new MainWindow (true);
  }


/**
  * This method processes the events generated by this object
	*
	*	@param evt This is the generated event
  */
public void actionPerformed (ActionEvent evt) {
	if ("Open IPDNet".equals (evt.getActionCommand())) {
		if (!oVM.isShowing()) oVM.setVisible(true);
    }
  else if ("Close IPDNet".equals (evt.getActionCommand())) {
		if (oVM.isShowing()) oVM.setVisible(false);
    }
  }


public String getAppletInfo() {
	return "IPD Network. Version 2021 ©Juan C. Burguillo. e-mail: J.C.Burguillo@uvigo.es, December 2021";
	}


public static void main (String args[]) {
	IPDNet oLauncher = new IPDNet();			// If it enters here it is not an applet, but an application
	Game.bBatchMode = false;
	oLauncher.oVM = new MainWindow (false);
	}


}	// from CellNet
