package window;

import java.awt.*;
import java.awt.event.*;

import games.*;
import games.IPDNet.*;

import file.IOFile;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;





/**
  * This is the main frame of the tool.
  *
  * @author  Juan C. Burguillo-Rial
  * @version 3.0
  */
public class MainWindow extends JFrame implements
	WindowCONS, GameCONS, ActionListener, MouseMotionListener, WindowListener, ItemListener, Runnable
{
public static int iOutputMode = iOUTPUT_VERBOSE;
//public static int iOutputMode = iOUTPUT_PYTHON;

public static Game oGame;				              // Contains the games to play
private boolean bApplet;			            		// Tells if is an applet or an application
private	Thread oProcess = null;			      		// This object will contain the execution Thread
private boolean bRunThread = false;		      	// Tells if the thread is running or not
private boolean bInsideThread = false;		  	// Tells if the execution is still inside the Thread
public boolean bEndThread = true;  		      	// Finishes execution

public int iDelayMilisecs = 0;           			// Waiting time in milliseconds
public int iNGenRepaint = 1;			        		// Number of generations to repaint

public static int iMapSize = 1000;         		// Map size in pixels for painting the cells
public static int iCellSize;               		// Cell size in pixels per cell
public static int iGenStop = 10000; 					// Generation to stop the simulation
public static int iGenStopTMP = iGenStop; 		// To be used with Step execution
public static int iLastNGen = 100;        		// To visualize only the last N generations in graphic windows
public static Menu oMenuGames;
public static MenuItem oMIPayMatrix;

public static MenuItem[] oMIWindow = new MenuItem[iNUM_GRAPHIC_WINDOWS];  		// Elements from MenuItem
public static DlGraphics[] omDlGraph = new DlGraphics[iNUM_GRAPHIC_WINDOWS]; 	// Graphics (the first 3 are non used)

public static DlgInfoCell oDlgInfoCell;				// Information about a particular cell in the cell world
public static DlgInfoBrain 	oDlgInfoSOM;				// Information about a particular cell in the SOM grid





/**
  * This is the constructor of the MainWindow class.
  *
  * @param	bBool   Indicates if this is an applet (true) or not
  */
public MainWindow (boolean bBool) {
  super(" Game: ");

  bApplet = bBool;		// if it is an applet, then receives true
  addWindowListener (this);

  setBackground (Color.lightGray);
  setForeground (Color.black);

  MenuBar oMB = new MenuBar();

  Menu oMenu = new Menu("File");
  MenuItem oMI = new MenuItem ("Load Matrix");
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Save Matrix");
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Load Cluster Strat", new MenuShortcut('L'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Save Cluster Strat", new MenuShortcut('S'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Import Network", new MenuShortcut('I'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Export Network", new MenuShortcut('E'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Print Data", new MenuShortcut('P'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Quit", new MenuShortcut('Q'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMB.add (oMenu);


  oMenuGames = new Menu ("Modes");
  for (int i=0; i<Game.sGAME_TYPE.length; i++) {
    oMI = new MenuItem (sGAME_TYPE[i]);
    oMI.addActionListener (this);
    oMenuGames.add(oMI);
    }
  oMB.add (oMenuGames);
  
  oMenu = new Menu("Options");
  oMI = new MenuItem ("Configuration", new MenuShortcut('C'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("Network Param", new MenuShortcut('N'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  
  oMI = new MenuItem ("-");
  oMenu.add(oMI);
  
  oMIPayMatrix = new MenuItem ("Payoff Matrix", new MenuShortcut('M'));
  oMIPayMatrix.addActionListener (this);
  oMenu.add(oMIPayMatrix);
  
  oMI = new MenuItem ("-");
  oMenu.add(oMI);
  
  oMI = new MenuItem ("Game Param", new MenuShortcut('G'));
  oMI.addActionListener (this);
  oMenu.add(oMI);

  oMI = new MenuItem ("Cluster Param", new MenuShortcut('K'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  
  oMB.add (oMenu);
  
  
  
  oMenu = new Menu("Window");
 
  oMIWindow[iREDRAW] = new MenuItem ("ReDraw", new MenuShortcut('0'));
  oMIWindow[iREDRAW].addActionListener (this);
  oMenu.add(oMIWindow[iREDRAW]);
  
  oMI = new MenuItem ("-");
  oMenu.add(oMI);

  oMIWindow[iOPEN_ALL] = new MenuItem ("Open ALL", new MenuShortcut('1'));
  oMIWindow[iOPEN_ALL].addActionListener (this);
  oMenu.add(oMIWindow[iOPEN_ALL]);

  oMIWindow[iCLOSE_ALL] = new MenuItem ("Close ALL", new MenuShortcut('2'));
  oMIWindow[iCLOSE_ALL].addActionListener (this);
  oMenu.add(oMIWindow[iCLOSE_ALL]);

  
  oMI = new MenuItem ("-");
  oMenu.add(oMI);

  oMIWindow[iNODE_DEGREE_DIST] = new MenuItem ("Node Degree Distribution", new MenuShortcut('3'));
  oMIWindow[iNODE_DEGREE_DIST].addActionListener (this);
  oMenu.add(oMIWindow[iNODE_DEGREE_DIST]);
  oMIWindow[iNET_STATS] = new MenuItem ("Net Basic Stats", new MenuShortcut('4'));
  oMIWindow[iNET_STATS].addActionListener (this);
  oMenu.add(oMIWindow[iNET_STATS]);
  oMIWindow[iNET_CC_STATS] = new MenuItem ("Net CC Stats", new MenuShortcut('5'));
  oMIWindow[iNET_CC_STATS].addActionListener (this);
  oMenu.add(oMIWindow[iNET_CC_STATS]);

  oMI = new MenuItem ("-");
  oMenu.add(oMI);
  
  oMIWindow[iFREQxACTION] = new MenuItem ("Graph Freq. x Action");
  oMIWindow[iFREQxACTION].addActionListener (this);
  oMenu.add(oMIWindow[iFREQxACTION]);
  oMIWindow[iCHANGESxGEN] = new MenuItem ("Graph Changes x Gen");
  oMIWindow[iCHANGESxGEN].addActionListener (this);
  oMenu.add(oMIWindow[iCHANGESxGEN]);  
  oMIWindow[iGLOBAL_PROFIT] = new MenuItem ("Graph Profit");
  oMIWindow[iGLOBAL_PROFIT].addActionListener (this);
  oMenu.add(oMIWindow[iGLOBAL_PROFIT]);
  oMIWindow[iPROBSTDEV_HISTOGRAM] = new MenuItem ("Graph Prob. Histogram");
  oMIWindow[iPROBSTDEV_HISTOGRAM].addActionListener (this);
  oMenu.add(oMIWindow[iPROBSTDEV_HISTOGRAM]);

  oMB.add (oMenu);

  oMIPayMatrix.setEnabled (false);
  for (int i=iFREQxACTION; i<oMIWindow.length; i++)
    oMIWindow[i].setEnabled (false);

  oMenu = new Menu("Help");
  CheckboxMenuItem oCBMI = new CheckboxMenuItem ("Comments Verbose", true);
	oCBMI.addItemListener (this);
  oMenu.add(oCBMI);
  oMI = new MenuItem ("IPDNet Help", new MenuShortcut('H'));
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMI = new MenuItem ("About");
  oMI.addActionListener (this);
  oMenu.add(oMI);
  oMB.add (oMenu);
  //oMB.setHelpMenu (oMenu);

  setMenuBar(oMB);

  Game.iGameType = iIPDNET;
  //Game.iGameType = iCLUSTERNET;
  
	oGame = new GameIPDNet (this);
  
	setTitle (" IPDNet:   " + sGAME_TYPE[Game.iGameType] + " Mode");		// Must be here, before initializing the games

  setLayout(new GridLayout (1,1));
  JPanelMainWindow oJGridMainWindow = new JPanelMainWindow (this);
  add (oJGridMainWindow);
  
  JPanelMainWindow.oJLabelStatusData.setText("                                  NumGen: "+Game.iNumGen+"     N: "+Game.iTotPosMatrix);
  JPanelMainWindow.oJLabelPm.setText ("Pm: " + GameIPDNet.dProbMutation);
  JPanelMainWindow.oJLabelPn.setText ("Pn: " + GameIPDNet.dProbNoise);
  JPanelMainWindow.oJLabelPe.setText ("Pe: " + GameIPDNet.dProbEnding);
  JPanelMainWindow.oJLabelNet.setText ("Net: " + sSHORT_COMPLEX_NET[Game.iNetType]);
  JPanelMainWindow.oJLabelGames.setText ("Games: " + GameIPDNet.iNumGamesxEncounter);
  JPanelMainWindow.oJLabelBreakPoint.setText ("#GenStop: " + MainWindow.iGenStop);
  JPanelMainWindow.vActivateButtons();
  
  setSize (new Dimension (iMapSize+125,iMapSize+50)); 									 // Window size
    
  setResizable (false);
  setLocation (new Point (0,0));			// Window position
  setVisible (true);									// We make the window visible
  
  oDlgInfoCell = new DlgInfoCell (this, "Cell Information", false);
  oDlgInfoSOM = new DlgInfoBrain (this, "SOM Information", false);
  
  
  vSetupThread();
}


//----------------------- INI: Methods related with WindowListener interface -------------
public void windowClosing(WindowEvent e)
  {vQuit();}
public void windowClosed(WindowEvent e) {}
public void windowOpened(WindowEvent e)	{}
public void windowActivated(WindowEvent e) {}
public void windowDeactivated(WindowEvent e) {}
public void windowDeiconified(WindowEvent e) {}
public void windowIconified(WindowEvent e) {}
//----------------------- END: Methods related with WindowListener interface -------------

//-------------------- INI: Methods related with MouseMotionListener interface -------------
public void mouseMoved (MouseEvent oME){}
public void mouseDragged (MouseEvent oME){}
//-------------------- END: Methods related with MouseMotionListener interface -------------





/**
 * This method receives and processes the changes in the CheckMenuItem
 *
 *	@param evt the generated event
 */
public void itemStateChanged (ItemEvent evt) {
	if (evt.paramString().indexOf ("Comments Verbose") > 0) {
  	if (iOutputMode == iOUTPUT_VERBOSE)
  		iOutputMode = iOUTPUT_PYTHON;
  	else
  		iOutputMode = iOUTPUT_VERBOSE;
  }		
}





/**
  * This method receives and processes the events in the objects from this class
  *
  *	@param evt the generated event
  */
public void actionPerformed (ActionEvent evt) {
  String sCommand = evt.getActionCommand();

  if ("Load Matrix".equals (sCommand)) {
    IOFile.vLoadMatrix (this, Game.oCellMatrix);
    vMatrixReloaded();
    return;
  }
  
  else if ("Save Matrix".equals (sCommand)) {
    IOFile.vSaveMatrix (this, Game.oCellMatrix);
    return;
  }

  else if ("Load Cluster Strat".equals (sCommand)) {
    IOFile.vLoadStrat (this);
    return;
  }

  else if ("Save Cluster Strat".equals (sCommand)) {
    IOFile.vSaveStrat (this);
    return;
  }
  
  else if ("Import Network".equals (sCommand)) {
    IOFile.vImportNetwork (this, Game.oCellMatrix);
    JPanelMainWindow.oWorldGrid.repaint();
    return;
  }
  else if ("Export Network".equals (sCommand)) {
    IOFile.vExportNetwork (this, Game.oCellMatrix);
    return;
  }
  
  else if ("Print Data".equals (sCommand)) {
    oGame.vPrintData ();
    return;
  }

  else if ("Quit".equals (sCommand))
    vQuit();

  else if ("Configuration".equals (sCommand)) {
  	if (bRunThread)
  		vStopThread();
    new DlgConfiguration (this, "Configuration", true);
    JPanelMainWindow.oWorldGrid.repaint();
  }
  
  else if ("Network Param".equals (sCommand)) {
  	if (bRunThread)
  		vStopThread();
    new DlgNetParam (this, "Network Parameters", true);
    JPanelMainWindow.oWorldGrid.repaint();
  }

  else if ("Payoff Matrix".equals (sCommand)) {
  	if (bRunThread)
  		vStopThread();
    new DlgPayoffMatrix (this, "Payoff Matrix", true);
    JPanelMainWindow.oWorldGrid.repaint();
  }
 
  else if ("Game Param".equals (sCommand)) {
  	if (bRunThread)
  		vStopThread();
  	new DlgParam (this, " Parameters", true);
    JPanelMainWindow.oWorldGrid.repaint();
  }
  
  else if ("Cluster Param".equals (sCommand)) {
  	if (bRunThread)
  		vStopThread();
    new DlgCluster (this, "Cluster Parameters", true);
    JPanelMainWindow.oWorldGrid.repaint();
  }
  
  else if ("ReDraw".equals (sCommand))
    JPanelMainWindow.oWorldGrid.repaint();

  else if ("Open ALL".equals (sCommand))
    oGame.vOpenALLWindows();

  else if ("Close ALL".equals (sCommand)) {
    for (int i=iFREQxACTION; i<omDlGraph.length; i++)
      if (MainWindow.omDlGraph[i] != null) {
        MainWindow.omDlGraph[i].dispose();       // Disposing graphic windows to be enabled by every game
        MainWindow.omDlGraph[i] = null;
      }
  }
  
  else if ("Node Degree Distribution".equals (sCommand)) {
	  if (omDlGraph[iNODE_DEGREE_DIST] == null)
		  omDlGraph[iNODE_DEGREE_DIST] = new DlGraphics (this, " IPDNet: Node Degree Distribution", false, iNODE_DEGREE_DIST);
      omDlGraph[iNODE_DEGREE_DIST].setVisible(true);
    }

  else if ("Net Basic Stats".equals (sCommand)) {
	  if (omDlGraph[iNET_STATS] == null)
		  omDlGraph[iNET_STATS] = new DlGraphics (this, " IPDNet: Network Basic Statistics", false, iNET_STATS);
      omDlGraph[iNET_STATS].setVisible(true);
    }
  
  else if ("Net CC Stats".equals (sCommand)) {
	  if (omDlGraph[iNET_CC_STATS] == null)
		  omDlGraph[iNET_CC_STATS] = new DlGraphics (this, " IPDNet: Network Cluster Coefficient", false, iNET_CC_STATS);
      omDlGraph[iNET_CC_STATS].setVisible(true);
    }
  
  else if ("Graph Freq. x Action".equals (sCommand)) {
	  if (omDlGraph[iFREQxACTION] == null)
		  omDlGraph[iFREQxACTION] = new DlGraphics (this, " IPDNet: Freq. x Action", false, iFREQxACTION);
      omDlGraph[iFREQxACTION].setVisible(true);
    }
  
  else if ("Graph Changes x Gen".equals (sCommand)) {
	  if (omDlGraph[iCHANGESxGEN] == null)
		  omDlGraph[iCHANGESxGEN] = new DlGraphics (this, " IPDNet: Max. Change in PMO", false, iCHANGESxGEN);
      omDlGraph[iCHANGESxGEN].setVisible(true);
    }
  
  else if ("Graph Profit".equals (sCommand)) {
	  if (omDlGraph[iGLOBAL_PROFIT] == null)
		  omDlGraph[iGLOBAL_PROFIT] = new DlGraphics (this, " IPDNet: Profit", false, iGLOBAL_PROFIT);
      omDlGraph[iGLOBAL_PROFIT].setVisible(true);
    }
  
  else if ("Graph Prob. Histogram".equals (sCommand)) {
	  if (omDlGraph[iPROBSTDEV_HISTOGRAM] == null)
		  omDlGraph[iPROBSTDEV_HISTOGRAM] = new DlGraphics (this, " IPDNet: Prob. Histogram", false, iPROBSTDEV_HISTOGRAM);
      omDlGraph[iPROBSTDEV_HISTOGRAM].setVisible(true);
    }
    
  else if ("IPDNet Help".equals (sCommand))
  	new DialogHelp (this, "IPDNet Help", false);

  else if ("About".equals (sCommand)) {
    vStopThread();
    String sAux[] ={"IPD Network (version 2021)", "© Juan C. Burguillo", "e-mail: J.C.Burguillo@uvigo.es", "December 2021"};
    new DialogOK (this, "About", true, sAux);
    }

  else {
    for (int j=0; j<Game.sGAME_TYPE.length; j++)
      if (sGAME_TYPE[j].equals (sCommand)) {
      	Game.iGameType = j;
      	setTitle (" IPDNet:   " + sGAME_TYPE[Game.iGameType] + " Mode");
        break;
      }
    }
    
}




/**
  * This method creates a new thread when the matrix of cells is reloaded
  */
public void vMatrixReloaded() {                    	// Restarts the thread
  vEndThread ();
  while (bInsideThread) {           				// Waiting for the end of the actual cycle
    try {Thread.sleep(10);}							// Introducing a certain delay
    catch (InterruptedException oIE) {}
    }
  bRunThread = false;
   
  JPanelMainWindow.oWorldGrid.repaint();	            // Repainting the world
  oDlgInfoCell.setVisible (false);
  oDlgInfoSOM.setVisible (false);
  JPanelMainWindow.oJLabelStatusData.setText("                                  NumGen: "+Game.iNumGen+"     N: "+Game.iTotPosMatrix);
  JPanelMainWindow.oJButtonGoStop.setText("GO");
  for (int i=1; i<omDlGraph.length; i++)                 // Repainting graphic windows
    if (omDlGraph[i] != null) omDlGraph[i].oVisor.repaint();
  }



/**
  * This method creates a new thread
  */
public void vSetupThread() {                    	// Starts the thread
  vEndThread ();
  while (bInsideThread) {           							// Waiting for the end of the actual cycle
    try {Thread.sleep(10);}												// Introducing a certain delay
    catch (InterruptedException oIE) {}
    }
  bRunThread = false;
  
  oGame.vNewGame();
  oMenuGames.setEnabled (true);
  
  //GameIPDNet.vRestartValuesBrain();												// Reseting some values for a new run
    
  JPanelMainWindow.oWorldGrid.repaint();	                // Repainting the world
  JPanelMainWindow.oJLabelStatusData.setText("                                  NumGen: "+Game.iNumGen+"     N: "+Game.iTotPosMatrix);
  JPanelMainWindow.oJButtonGoStop.setText("GO");
  for (int i=1; i<omDlGraph.length; i++)                 // Repainting graphic windows
    if (omDlGraph[i] != null) omDlGraph[i].oVisor.repaint();  
}



/**
 * This method starts/continues the thread
 */
public void vGoThread () {
  iGenStopTMP = iGenStop;
  oMenuGames.setEnabled (false);
  if (oProcess == null)
    vStartThread();
  else if (bEndThread == false)
    bRunThread = true;
  
  JPanelMainWindow.oJButtonGoStop.setText("STOP");
}



/**
  * This methods starts the thread
  */
public void vStartThread() {
  if (!bRunThread) {
    bEndThread = false;
    bRunThread = true;
    oProcess = new Thread (this);
    oProcess.start();
  }
}


/**
 * This methods makes the thread go step by step
 */
public void vStepThread () {
  oMenuGames.setEnabled (false);
  JPanelMainWindow.oJButtonGoStop.setText("GO");
  if (oProcess == null) {
    iGenStopTMP = 1;
    vStartThread();
  }
  else if (bEndThread == false) {
    iGenStopTMP = Game.iNumGen + 1;
    bRunThread = true;
  }
}


/**
  * This method stops the thread
  */
public void vStopThread () {
  JPanelMainWindow.oJButtonGoStop.setText("GO");
  bRunThread = false;
}


/**
 * This method finishes the thread
 */
public void vEndThread () {
  bEndThread = true;
  bRunThread = true;
  if (!Game.bBatchMode)
    oProcess = null;
}



/**
 * This method exits the program
 */
private void vQuit () {
  vEndThread();
  for (int i=0; i<oMIWindow.length; i++)
    if (omDlGraph[i] != null) {
  	  omDlGraph[i].dispose();
  	  omDlGraph[i] = null;
    }
  while (bInsideThread) {           		// Waiting for the end of the actual execution cycle
    try {Thread.sleep(10);}				// Introducing a certain delay
    catch (InterruptedException oIE) {}
  }
  dispose();
  try {Thread.sleep(500);}				// Introducing a certain delay to avoid window functions
  catch (InterruptedException oIE) {}
  System.exit(0);
}



/**
  * This method contains the code to be executed as a thread
  */
public void run() {
  bInsideThread = true;
  vNormalRun ();
  bInsideThread = false;
  }		// de run





/**
  * This method contains the code executed in normal mode
  */
private void vNormalRun () {

  while (!bEndThread) {    

  	oGame.vRunLoop();
    
    if (Game.iNumGen % iNGenRepaint == 0) {	     // We update the windows every iNGenRepaint
      if (oDlgInfoCell.isVisible()) oDlgInfoCell.repaint();
      if (oDlgInfoSOM.isVisible()) oDlgInfoSOM.repaint();
      JPanelMainWindow.oWorldGrid.repaint();	                			// Repainting the present world
      JPanelMainWindow.oJLabelStatusData.setText ("                                  "+Game.sTextStateBar);	// Repainting the status line
      for (int i=1; i<omDlGraph.length; i++)                 				// Repainting graphic windows
        if (omDlGraph[i] != null) omDlGraph[i].oVisor.repaint();
      
      if (iDelayMilisecs > 0)
      	try {Thread.sleep(iDelayMilisecs);}				// Introducing a certain delay in the display  (iDelayMilisecs)
      	catch (InterruptedException oIE) {}
    }
    
				// If there is a STOP the simulation stops
    if ( (Game.iNumGen > 0) && (Game.iNumGen % iGenStopTMP == 0) ) {
      vStopThread();
      oGame.vPrintData();
      String sCad = JPanelMainWindow.oJLabelStatusData.getText() + "   -->   STOP !";
      JPanelMainWindow.oJLabelStatusData.setText (sCad);
      JPanelMainWindow.oWorldGrid.repaint();
    }
    
    while (!bRunThread) {
      try {Thread.sleep(100);}                  	// We introduce a certain delay if stopped
      catch (InterruptedException oIE) {}
	  	}
      	
  }		// from while (!bEndThread) {
  bRunThread = false;
}




}   // from MainWindow class
