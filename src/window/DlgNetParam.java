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
class DlgNetParam extends JDialog implements ActionListener, GameCONS
{
private MainWindow oMainWindow;
private Label oLabel;
private JTextField  oTiNetType,
										oTiMatrixCells,
										oTProbSmallNets,
                    oTRadio,
                    oTNumNeighborsIni,
                    oTInitialNodesNet,
                    oTNumIntervals,
                    oTNumValIntervalo;
private Choice oChNetType;



/**
 * This is the Dialog constructor
 *
 * @param	oPadre 	Pointer to the parent
 * @param	sTit   	Dialog title
 * @param	bBool 	Tells if the window is modal (true) or not
 */
DlgNetParam (JFrame oPadre, String sTit, boolean bBool) {
  super (oPadre, sTit, bBool);
  oMainWindow = (MainWindow) oPadre;

  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(new GridLayout(9,2));

  oLabel = new Label (" Network Type:", Label.LEFT);
  add (oLabel);

  
  if (!oMainWindow.bEndThread) {     										// If the thread is executing, then it can not change
  	oTiNetType = new JTextField (sCOMPLEX_NET[Game.iNetType], 7);
  	oTiNetType.setEditable (false);
    add (oTiNetType);
  } else {
    oChNetType = new Choice();
    for (int i=0; i<sCOMPLEX_NET.length; i++)
      oChNetType.add (sCOMPLEX_NET[i]);
    oChNetType.select (Game.iNetType);
    add (oChNetType);
  }

  oLabel = new Label (" Matrix Cells [4, 40.000]:", Label.LEFT);
  add (oLabel);
  oTiMatrixCells = new JTextField(""+Game.iTotPosMatrix, 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
    oTiMatrixCells.setEditable (false);
  add (oTiMatrixCells);

  oLabel = new Label (" Neighbor Radix (Spatial Net):", Label.LEFT);
  add (oLabel);
  oTRadio = new JTextField(String.valueOf(Game.dSpatialRadio), 7);
  if (!oMainWindow.bEndThread)			// If the thread is running we can not change it
    oTRadio.setEditable (false);
  add (oTRadio);
  
  oLabel = new Label (" Avg. Neighbourhood Size (Complex Nets):", Label.LEFT);
  add (oLabel);
  oTNumNeighborsIni = new JTextField(String.valueOf (Game.dAvgNeighborhoodSize), 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
  	oTNumNeighborsIni.setEditable (false);
  add (oTNumNeighborsIni);

  oLabel = new Label (" Initial Rewire Prob (Small World):", Label.LEFT);
  add (oLabel);
  oTProbSmallNets = new JTextField(String.valueOf (Game.dProbSmallWorld), 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
  	oTProbSmallNets.setEditable (false);
  add (oTProbSmallNets);

  oLabel = new Label (" Initial Nodes (Scale Free):", Label.LEFT);
  add (oLabel);
  oTInitialNodesNet = new JTextField(String.valueOf (Game.iInitialNodesNet), 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
  	oTInitialNodesNet.setEditable (false);
  add (oTInitialNodesNet);

  oLabel = new Label (" Graph Node-Degree Intervals (Max. 40):", Label.LEFT);
  add (oLabel);
  oTNumIntervals = new JTextField(String.valueOf (Game.iNumIntervals), 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
  	oTNumIntervals.setEditable (false);
  add (oTNumIntervals);

  oLabel = new Label (" Graph Node-Degree Values x Interval:", Label.LEFT);
  add (oLabel);
  oTNumValIntervalo = new JTextField(String.valueOf (Game.iNumValInterval), 7);
  if (!oMainWindow.bEndThread)       										// If the thread is executing, then it can not change
  	oTNumValIntervalo.setEditable (false);
  add (oTNumValIntervalo);
  

  JButton oBut = new JButton ("OK");
  oBut.addActionListener (this);
  add (oBut);
  oBut  = new JButton ("Cancel");
  oBut.addActionListener (this);
  add (oBut);

  setSize(new Dimension(650,500));
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
    Game.iNetType = oChNetType.getSelectedIndex();
    Game.dSpatialRadio = Double.parseDouble (oTRadio.getText());
    Game.dAvgNeighborhoodSize = Double.parseDouble (oTNumNeighborsIni.getText());
    Game.dProbSmallWorld = Double.parseDouble (oTProbSmallNets.getText());
    Game.iInitialNodesNet = Integer.parseInt (oTInitialNodesNet.getText());
    Game.iNumIntervals = Integer.parseInt (oTNumIntervals.getText());
    Game.iNumValInterval = Integer.parseInt (oTNumValIntervalo.getText());
    
    int iMatrixCells = Integer.parseInt (oTiMatrixCells.getText());
    if ( (iMatrixCells >= 4) && (iMatrixCells <= 40000) )
    	Game.iTotPosMatrix = iMatrixCells;      
      
    JPanelMainWindow.oJLabelNet.setText ("Net: "+sSHORT_COMPLEX_NET[Game.iNetType]);
    
    if (oMainWindow.bEndThread)       				// If the thread is not executing, then we do a setup to modify the network
    	oMainWindow.vSetupThread();
  }

  dispose();
}



}	// from the class
