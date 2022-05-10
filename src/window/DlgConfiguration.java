package window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import games.*;



/**
	* This class allows to obtain a dialog parameter window with two buttons: OK and Cancel.
	*
	* @author  Juan C. Burguillo Rial
	* @version 1.0
	*/
class DlgConfiguration extends JDialog implements ActionListener, GameCONS
{
private MainWindow oMainWindow;
private Label oLabel;
private JTextField  oTiGenStop,
                    oTiMiliSeg,
                    oTiLastNGen,
                    oTiNGenRePaint,
                    oTiBufferStat;



/**
  * This is the Dialog constructor
  *
  * @param	oPadre 	Pointer to the parent
  * @param	sTit   	Dialog title
  * @param	bBool 	Tells if the window is modal (true) or not
  */
DlgConfiguration (JFrame oPadre, String sTit, boolean bBool) {
  super (oPadre, sTit, bBool);
  oMainWindow = (MainWindow) oPadre;

  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(new GridLayout(6,2));
    
  oLabel = new Label (" Break (in generations):", Label.LEFT);
  add (oLabel);
  oTiGenStop = new JTextField(String.valueOf(MainWindow.iGenStop), 7);
  add (oTiGenStop);

  oLabel = new Label (" Delay in MiliSeconds per Frame:", Label.LEFT);
  add (oLabel);
  oTiMiliSeg = new JTextField(""+oMainWindow.iDelayMilisecs, 7);
  add (oTiMiliSeg);
   
  oLabel = new Label (" Num. Generations to RePaint:", Label.LEFT);
  add (oLabel);
  oTiNGenRePaint = new JTextField(""+oMainWindow.iNGenRepaint, 7);
  add (oTiNGenRePaint);

  oLabel = new Label (" Last N Generations (Graphics):", Label.LEFT);
  add (oLabel);
  oTiLastNGen = new JTextField(String.valueOf(MainWindow.iLastNGen), 7);
  add (oTiLastNGen);
      
  oLabel = new Label (" Buffer Size per Cell:", Label.LEFT);
  oLabel.setForeground (Color.blue);
  add (oLabel);
  oTiBufferStat = new JTextField (""+Game.iStatsBufferSize, 7);
  add (oTiBufferStat);


  JButton oBut = new JButton ("OK");
  oBut.addActionListener (this);
  add (oBut);
  oBut  = new JButton ("Cancel");
  oBut.addActionListener (this);
  add (oBut);

  setSize(new Dimension(500,300));
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
    MainWindow.iGenStop = Integer.parseInt (oTiGenStop.getText());
    JPanelMainWindow.oJLabelBreakPoint.setText ("#GenStop: " + MainWindow.iGenStop);
    oMainWindow.iDelayMilisecs = Integer.parseInt (oTiMiliSeg.getText());
    oMainWindow.iNGenRepaint = Integer.parseInt (oTiNGenRePaint.getText());
    MainWindow.iLastNGen = Integer.parseInt (oTiLastNGen.getText());    
    Game.iStatsBufferSize = Integer.parseInt (oTiBufferStat.getText());

    dispose();
    }

  else if ("Cancel".equals (evt.getActionCommand()))
    dispose();
}



}	// from class DlgConfiguration
