package games.IPDNet;

import games.Cell;
import games.Game;
import machineLearning.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import window.DlGraphics;
import window.MainWindow;
import window.WindowCONS;



/**
 * This class is used as a strut to keep data stored per strat, when printing them
 */
class StratStats {
	String sStrat;											// String with the sequence of probabilities for this strat
	int iPop = 0;												// Popularity of this strategy
	int iNumGames = 0;									// Number of games played by this strategy
	int iNumCs = 0;											// Number of times it played C
	double dPayoff = 0;									// Payoff obtained
	double dAvgPayoff = 0;								// Only can be calculated at the end
	int[] iNumTimes_MO_State = new int[4];	// Avg. number of times this strat has been in CC(0), CD(1), DC(2), DD(3)

	
StratStats (String sStratTMP) {
	sStrat = sStratTMP;
}

}




/**
  * This class implements the Spatial Iterated Prisoner's Dilemma with Probabilistic Memory One strategies
  *		- Cooperator (C)
  *		- Defector (D)
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class GameIPDNet extends Game implements WindowCONS
{
// IOFile.java, MainWindow.java, DlgCluster.java
public static Strat_PMO oClusterStratPMO;																			// Central checking strat (when used) 
public static Strat_PMO[][] omStratPMO = new Strat_PMO [iCellH][iCellV];				// PM_1 Strats for all Matrix cells

//bIPDNet.java, MainWindow.java
public static int iNumGamesxEncounter = 50;								// Number of consecutive interactions between each 2 players
public static double dProbEnding = 0;											// Prob. of ending a game after each round

// bIPDNet.java
public static int iGenSimStable = 0;											// Generations when the simulation becomes stable
public static int iChangeModel = iNOT_BEST;								// To decide the change in the actions/strategies
public static int iProbModel = iCONTINUOUS;								// Using discrete or continuous probabilities
public static double dAvgNeighbors = 0;										// Average number of neighbors per agent in the net
public static double dAvgProfitxAgent = 0;								// Profit per agent after each generation
public static double dPMOInterval = 1.0;									// Interval  (0,1] to generate discrete strats
public static double dOwnWeight = 0.5;										// Own weight for selecting probs, when mating with a best neighbor

//bIPDNet.java, DlgCluster.java
public static double dRatio4ClusterDomination = 0.95;					// Amount of CC cells (over 1) to pass the checking phase

// VisorSerieInteger.java
public static int iDiff_PMOsBuffer2Stop = 10;							// Less than V % of change in all average probabilities in last N gens --> STOP !
public static int iPMOsBufferSize = 100;

private static int iMaxNumGen2StartCluster = 1000;				// If the previous conditions do not happen the cluster starts at this gen.

private static boolean bSimStable = false;
private static int iMaxDiff_PMOsBuffer = 0;
private static int[] imSumAvg_PMOsBuffer;									// This is used to stop the simulation when there are no changes
private static int iPrevNumClusterCells = 0;							// Previous number of cells using the checking strat
private static int iNumClusterCells = 0;									// Number of cells using the checking strat
private static int iReward = 0;														// Reward for the previous strat
private static int iNumPopStrats = 0;											// Number of popular strategies
private static int iNumStrats2Print = 0;									// The most popular strats to print or use in the clusters
private static StratStats[] omPopStratStats;																	// List of popular strategies
private static int[][] imCounterStateDynamics = new int [4][4];								// State dynamics based on changing frequencies
private static int[][] imStratChanges = new int [32][32];											// Movements among strats
private static Strat_PMO[][] omNewStrat_PMO = new Strat_PMO [iCellH][iCellV];	// PM_1 New Strats to update synchronously all Matrix cells
private static Vector ovClusterCells = (Vector) new Vector (1,1);							// Vector with the present checking strat agents

private static Brain oHiveBrain;													// This is the brain containing a SOM network with LA states													



/**
  * This is the graphical class constructor
  *
  */
public GameIPDNet (MainWindow oVentAux) {
  this();
  oMainWindow = oVentAux;
}




/**
  * This is the textual class constructor
  */
public GameIPDNet () {
	super ();
	
	//iChangeType = iCROSSOVER;
	iChangeType = iIMITATION;
	iChangeModel = iNOT_BEST;
	//iProbModel = iDISCRETE;
	iProbModel = iCONTINUOUS;
  
	iNumActionProbs = 5;				// Here we consider the 5 probabilities: P0, P1, P2, P3, P4
  MainWindow.iGenStop = 5000;
  MainWindow.iLastNGen = 1000;
  
  iPMOsBufferSize = 100;
  
  //iDiff_PMOsBuffer2Stop = 5;
  iDiff_PMOsBuffer2Stop = 10;

	dProbMutation = 0.01;
	dProbNoise = 0.0;
	dProbEnding = 0.1;
	//dPMOInterval = 1.0;
	dPMOInterval = 0.001;

	dProbChangeAction = 1.0;
	
	iTotPosMatrix = 10000;				// Initial number of cells in the matrix
	dSpatialRadio = 1;						// 1 -> 4 neighbors, 1.5 -> -> 8 neighbors, 2 -> 12 neighbors, 3 -> 28 neighbors
	dAvgNeighborhoodSize = 4;			// Neighborhood Size for complex nets
//	dSpatialRadio = 1.5;					// 1 -> 4 neighbors, 1.5 -> -> 8 neighbors, 2 -> 12 neighbors, 3 -> 28 neighbors
//	dAvgNeighborhoodSize = 8;			// Neighborhood Size for complex nets
	iNumGamesxEncounter = 50;	
	
  oVTextAction = new Vector<String> (1,1);
  oVTextAction.add ("Po");										// P0: Probability of C at the initial round
  oVTextAction.add ("P1_cc");									// P1: Probability of C after [C,C] == Pr
  oVTextAction.add ("P2_cd");									// P2: Probability of C after [C,D] == Ps
  oVTextAction.add ("P3_dc");									// P3: Probability of C after [D,C] == Pt
  oVTextAction.add ("P4_dd");									// P4: Probability of C after [D,D] == Pp
  
	dPayMatrix[0][0][0] = 1;	// dP							// T > R > P > S    &    2 * R > T + S  
	dPayMatrix[0][1][0] = 5;	// dT
	dPayMatrix[1][0][0] = 0;	// dS
	dPayMatrix[1][1][0] = 3;	// dR

  dPayMatrix[0][0][1] = dPayMatrix[0][0][0];
  dPayMatrix[0][1][1] = dPayMatrix[1][0][0];
  dPayMatrix[1][0][1] = dPayMatrix[0][1][0];
  dPayMatrix[1][1][1] = dPayMatrix[1][1][0];
  
  //oHiveBrain = new Brain (6400, iNumActionProbs);		// 6.400 samples (generations): 400 neurons with 5 probs [+ CC + BC?] ratios
  oHiveBrain = new Brain (400, iNumActionProbs);		// 400 samples (generations): 100 neurons with 5 probs [+ CC + BC?] ratios
  //oHiveBrain = new Brain (25, iNumActionProbs);			// 25 samples (generations): 25 neurons with 5 probs [+ CC + BC?] ratios
}



/**
  * This method is started when we press "New" button
  *
  */
public void vNewGame() {
  super.vNewGame();
  
  //				OJO: This line restarts the Brain in each new Game
  //oHiveBrain = new Brain (6400, iNumActionProbs);		// 6.400 samples (generations): 400 neurons with 5 probs [+ CC + BC?] ratios
  //oHiveBrain = new Brain (400, iNumActionProbs);		// 400 samples (generations): 100 neurons with 5 probs [+ CC + BC?] ratios
  //oHiveBrain = new Brain (25, iNumActionProbs);			// 25 samples (generations): 25 neurons with 5 probs [+ CC + BC?] ratios
  
  imStratChanges = new int [32][32];
  omStratPMO = new Strat_PMO [iCellH][iCellV];
  omNewStrat_PMO = new Strat_PMO [iCellH][iCellV];  
  for (int x=0; x<iCellH; x++)						// Initializing the cell matrix depending on the games and actions
  for (int y=0; y<iCellV; y++)
  	omStratPMO[x][y] = new Strat_PMO ();
  
  for (int x=0; x<iCellH; x++)						// Initializing the cell matrix depending on the games and actions
  for (int y=0; y<iCellV; y++) {	
	  imCellsType[0]++;
	  imCellsAction[0]++;
	  iTotNumCells++;
	  oCellMatrix[x][y] = new Cell (""+x+","+y, 0, 0);
	}

  vCells2Vector_Line (oCellMatrix);				// Creates a line vector and a 1D matrix to enable random access

  switch (iNetType) {
		case 0:   vSetNeighborsSpatialRadio (); break;
		case 1:   vSetNeighborsSmallWorld (); break;
		case 2:   vSetNeighborsScaleFree (); break;
		case 3:   vSetNeighborsRandomNetwork (); break;
		case 4:		vSetNeighborsSpatialRadio (); vCreateSmallWorldNet(); break;
	}

  if ( (iNewGame == 0) && (!Game.bBatchMode) )
  	vOpenALLWindows();
  
  iNewGame++;
	bSimStable = false;
	iGenSimStable = 0;
  MainWindow.iGenStopTMP = MainWindow.iGenStop;
    
	imSumAvg_PMOsBuffer = new int[iNumActionProbs];
	oClusterStratPMO = new Strat_PMO((int) Math.floor (Math.random() * 8.0));					// Starts with a random basic strat
	oClusterStratPMO.bClusterStrat = true;
  
  dAvgNeighbors = (double) iTotNumLinks / (double) iTotNumCells;

  iNumClusterCells = 0;
  ovClusterCells = (Vector) new Vector (1,1);
}






/**
 * This opens all graphical windows used by this game
 */
public void vOpenALLWindows() {
	MainWindow.oMIPayMatrix.setEnabled (true);
	
	MainWindow.oMIWindow[iFREQxACTION].setEnabled (true);
	MainWindow.oMIWindow[iCHANGESxGEN].setEnabled (true);
	MainWindow.oMIWindow[iGLOBAL_PROFIT].setEnabled (true);
	MainWindow.oMIWindow[iPROBSTDEV_HISTOGRAM].setEnabled (true);

	
	if (MainWindow.omDlGraph[iNET_STATS] == null)
		MainWindow.omDlGraph[iNET_STATS] = new DlGraphics (oMainWindow, " IPDNet: Network Basic Statistics", false, iNET_STATS);
  MainWindow.omDlGraph[iNET_STATS].setVisible(false);
  
  if (MainWindow.omDlGraph[iNET_CC_STATS] == null)
		MainWindow.omDlGraph[iNET_CC_STATS] = new DlGraphics (oMainWindow, " IPDNet: Network Cluster Coefficient", false, iNET_CC_STATS);
  MainWindow.omDlGraph[iNET_CC_STATS].setVisible (false);
	
	if (MainWindow.omDlGraph[iFREQxACTION] == null)
		MainWindow.omDlGraph[iFREQxACTION] = new DlGraphics (oMainWindow, " IPDNet: Frequency per Action", false, iFREQxACTION);
  MainWindow.omDlGraph[iFREQxACTION].setVisible (true);		// OJO
  
  if (MainWindow.omDlGraph[iCHANGESxGEN] == null)
  	MainWindow.omDlGraph[iCHANGESxGEN] = new DlGraphics (oMainWindow, " IPDNet: Action Changes", false, iCHANGESxGEN);
  MainWindow.omDlGraph[iCHANGESxGEN].setVisible (true);		// OJO
  
  if (MainWindow.omDlGraph[iGLOBAL_PROFIT] == null)
  	MainWindow.omDlGraph[iGLOBAL_PROFIT] = new DlGraphics (oMainWindow, " IPDNet: Profit", false, iGLOBAL_PROFIT);
  MainWindow.omDlGraph[iGLOBAL_PROFIT].setVisible (false);  
  
  if (MainWindow.omDlGraph[iPROBSTDEV_HISTOGRAM] == null)
  	MainWindow.omDlGraph[iPROBSTDEV_HISTOGRAM] = new DlGraphics (oMainWindow, " IPDNet: Prob. Histogram", false, iPROBSTDEV_HISTOGRAM);
  MainWindow.omDlGraph[iPROBSTDEV_HISTOGRAM].setVisible (false);
}






public static Strat_PMO dGetStrat_PMO (int x, int y) {
	return omStratPMO[x][y];
}



public static double dGetStrat_PMO (int x, int y, int i) {
	return omStratPMO[x][y].dmProb[i];
}







/**
  * This method contains the code executed in parallel
  */
public void vRunLoop() {	
  iNumGen++;                              						// Increasing the number of generations (games iterations)
  iNumChanges = 0;
  imCounterStateDynamics = new int[4][4];
  omNewStrat_PMO = new Strat_PMO [iCellH][iCellV];
  
  for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++) {
    oCellMatrix[x][y].vResetCellData ();													// Put new income to zero
    if (omStratPMO[x][y].bClusterStrat)													// Updating the strat in cluster cells
    	omStratPMO[x][y].vSetProbs (oClusterStratPMO.dmProb);
    omNewStrat_PMO[x][y] = new Strat_PMO (omStratPMO[x][y]);	 	// By defect, we kept the previous strategies
  }
  
	for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++)													// Every cell plays a game with its neighbors
  	vCalcNeighborPayoffs (x,y);													// Calculates the payoff for the interacting cells
    	
	for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++)												
    if (Math.random() < dProbChangeAction)
    	vChangeStrat_PMO (x, y);   												// Setting strats for independent and cluster agents
  
  omStratPMO = omNewStrat_PMO;												// Updates synchronously to the new strats	
  iPrevNumClusterCells = iNumClusterCells;
  
  vCalculateStatistics();  
  vEvaluateMode ();
  
	if ( (iGameType == iCLUSTERNET) && (ovClusterCells.size() > 0) )
		vProduceNewStrat_Cluster ();

  sTextStateBar = "Gen: "+ iNumGen + "   Agents: "+ iTotNumCells + "   Links: " + iTotNumLinks +
			"   Avg. Neighbors: " + String.format (Locale.ENGLISH, "%.2f", dAvgNeighbors) + "   [";
  sTextStateBar += sCHANGE_TYPE[iChangeType] +", " + sPROB_MODEL[iProbModel] + "]";
  if (iNumClusterCells > 0)
  	sTextStateBar += "     Num. CC: " + iNumClusterCells + " (" + (iNumClusterCells - iPrevNumClusterCells) + ")";

}		// from vRunLoop()









private void vEvaluateMode () {		
	
	switch (iGameType) {
	
		case iIPDNET:							// Exploration shares the next code with exploitation
		  if ( (iNumGen > iPMOsBufferSize) && (iMaxDiff_PMOsBuffer <= iDiff_PMOsBuffer2Stop) )
				MainWindow.iGenStopTMP = Game.iNumGen;
		  break;

		case iCLUSTERNET:
			if (  ( (iNumGen > iPMOsBufferSize) && (iMaxDiff_PMOsBuffer < iDiff_PMOsBuffer2Stop) ) ||
					(iNumGen > iMaxNumGen2StartCluster)  ){
				bSimStable = true;
				if (iGenSimStable == 0)
					iGenSimStable = iNumGen;
			}
			
			iReward = iNumClusterCells - iPrevNumClusterCells;

			if (iNumClusterCells > dRatio4ClusterDomination * iTotNumCells)						// Majority of checking cells
				MainWindow.iGenStopTMP = Game.iNumGen;
			
			else if ( (iNumClusterCells == 0) && bSimStable )	{										// If stable and no cluster -> set cluster
				oHiveBrain.vResetMemory();
				iReward = -1;																												// To force a new strat
				vSetCluster();
				vProduceNewStrat_Cluster();
			}		
	}

}





/**
 	* Set the cluster seed on the grid
 	*/
private void vSetCluster() {
	int x, y;
	Vector ovNeighbors;
	
	do {
		x = (int) Math.floor (Math.random() * (double) iCellH);
		y = (int) Math.floor (Math.random() * (double) iCellV);
 		ovNeighbors = (Vector) oCellMatrix[x][y].oVGetNeighbors();
	} while (ovNeighbors.size() < dAvgNeighbors);

	ovClusterCells = (Vector) new Vector (1,1);
	omStratPMO[x][y] = new Strat_PMO (oClusterStratPMO);			// Setting the initial cluster cell
	ovClusterCells.add (oCellMatrix[x][y]);
/*
	for (int i=0; i<ovNeighbors.size(); i++) {								// Including its neighbors as cluster members
		Cell oMate = (Cell) ovNeighbors.elementAt(i);
		x = oMate.iGetPosX();
		y = oMate.iGetPosY();
		omStratPMO[x][y] = new Strat_PMO (oClusterStratPMO);
		ovClusterCells.add (oCellMatrix[x][y]);
	}
*/	
  iNumClusterCells = ovClusterCells.size();
}









/**
  * This method updates the income of a cell playing with one of its neighbors.
	*
	*	@param x 	Horizontal coordinate of the cell
	*	@param y 	Vertical coordinate of the cell
  */
private void vCalcNeighborPayoffs (int x, int y) {
  Cell oCell=oCellMatrix[x][y], oNeighbor;
  Vector<Cell> ovNeighbors = oCell.oVGetNeighbors();
  
  for (int i=0; i<ovNeighbors.size(); i++) {  	  	
  	oNeighbor = ovNeighbors.elementAt (i);
  	vCalc2PlayerPayoffs (oCell, oNeighbor);  	
  }   
}




/**
  * This method updates the income of a two cells playing with one on one for a series of iNumGamesxEncounter.
	*
	*	@param oPlayer1 	First player
	*	@param oPlayer2 	Second player
  */
private void vCalc2PlayerPayoffs (Cell oPlayer1, Cell oPlayer2) {
  int iPlayer1Action=0, iPlayer2Action=0, x1, y1, x2, y2, iNumCsPlayer1, iNumCsPlayer2, iNumGamesPlayer1, iNumGamesPlayer2;
  String sMemoryPlayer1="", sMemoryPlayer2="", sPrevMemoryPlayer1="", sPrevMemoryPlayer2="";
  
  iNumCsPlayer1 = oPlayer1.iGetNumCooperations();
  iNumGamesPlayer1 = oPlayer1.iGetNumGamesPlayed();
	x1 = oPlayer1.iGetPosX();
	y1 = oPlayer1.iGetPosY();
  
	iNumCsPlayer2 = oPlayer2.iGetNumCooperations();
	iNumGamesPlayer2 = oPlayer2.iGetNumGamesPlayed();
	x2 = oPlayer2.iGetPosX();
	y2 = oPlayer2.iGetPosY();
	
	sMemoryPlayer1 = "";
	sMemoryPlayer2 = "";
	sPrevMemoryPlayer1 = "";
	sPrevMemoryPlayer2 = "";
	for (int k=0; k<iNumGamesxEncounter; k++) {
  	
		iPlayer1Action = omStratPMO[x1][y1].iGetAction (sMemoryPlayer1);
		if (Math.random() < dProbNoise)
			iPlayer1Action = 1 - iPlayer1Action;				// The Cell action is flipped due to noise 
			
  	iPlayer2Action = omStratPMO[x2][y2].iGetAction (sMemoryPlayer2);  	  
	  if (Math.random() < dProbNoise)
	  	iPlayer2Action = 1 - iPlayer2Action;				// The Mate action is flipped due to noise
	  	  
                   // We adjust payment for both, in the IPD games this means a double payment
	  oPlayer1.vAddPayoffs (dPayMatrix[iPlayer1Action][iPlayer2Action][0], dPayMatrix[iPlayer1Action][iPlayer2Action][1]);
	  oPlayer2.vAddPayoffs (dPayMatrix[iPlayer1Action][iPlayer2Action][1], dPayMatrix[iPlayer1Action][iPlayer2Action][0]);
	  
	  if (iPlayer1Action == iDEFECT)
	  	sMemoryPlayer1 = "D";
	  else {
	  	sMemoryPlayer1 = "C";
	  	iNumCsPlayer1++;
	  }
	  
	  if (iPlayer2Action == iDEFECT)
	  	sMemoryPlayer1 += "D";
	  else {
	  	sMemoryPlayer1 += "C";
	  	iNumCsPlayer2++;
	  }
	  
	  sMemoryPlayer2 = "" + sMemoryPlayer1.charAt(1) + sMemoryPlayer1.charAt(0);
	  
	  switch (sMemoryPlayer1) {
	  	case "CC":	oPlayer1.iTimes_MO_State[0]++; break;
	  	case "CD":	oPlayer1.iTimes_MO_State[1]++; break;
	  	case "DC":	oPlayer1.iTimes_MO_State[2]++; break;
	  	case "DD":	oPlayer1.iTimes_MO_State[3]++; break;
	  }
	  
	  switch (sMemoryPlayer2) {
	  	case "CC":	oPlayer2.iTimes_MO_State[0]++; break;
	  	case "CD":	oPlayer2.iTimes_MO_State[1]++; break;
	  	case "DC":	oPlayer2.iTimes_MO_State[2]++; break;
	  	case "DD":	oPlayer2.iTimes_MO_State[3]++; break;
	  }

	  if (k > 0) {
		  vUpdateFreqStateDynamics (sMemoryPlayer1, sPrevMemoryPlayer1);
		  vUpdateFreqStateDynamics (sMemoryPlayer2, sPrevMemoryPlayer2);
	  }
	  
	  iNumGamesPlayer1++;
	  iNumGamesPlayer2++;

	  sPrevMemoryPlayer1 = sMemoryPlayer1;
	  sPrevMemoryPlayer2= sMemoryPlayer2;

	  if (Math.random() < dProbEnding) break;
	  
	}	// for (int k=0; k<iNumGamesxEncounter; k++) {
	
	oPlayer2.vSetNumCooperations(iNumCsPlayer2);
	oPlayer2.vSetNumGamesPlayed(iNumGamesPlayer2);

  oPlayer1.vSetNumCooperations(iNumCsPlayer1);
  oPlayer1.vSetNumGamesPlayed(iNumGamesPlayer1);

}







/**
 * This method updates the times a state has been visited by the cell or the neighbor
 *
	*	@param sMemory 			The present memory
	*	@param sPrevMemory 	The previous memory
 */
private void vUpdateFreqStateDynamics (String sMemory, String sPrevMemory) {
  switch (sPrevMemory) {
		case "CC":	switch (sMemory) {
							  	case "CC":	imCounterStateDynamics[0][0]++; break;
							  	case "CD":	imCounterStateDynamics[0][1]++; break;
							  	case "DC":	imCounterStateDynamics[0][2]++; break;
							  	case "DD":	imCounterStateDynamics[0][3]++; break;		  							
								};
								break;
		case "CD":	switch (sMemory) {
							  	case "CC":	imCounterStateDynamics[1][0]++; break;
							  	case "CD":	imCounterStateDynamics[1][1]++; break;
							  	case "DC":	imCounterStateDynamics[1][2]++; break;
							  	case "DD":	imCounterStateDynamics[1][3]++; break;		  							
								};
								break;
		case "DC":	switch (sMemory) {
							  	case "CC":	imCounterStateDynamics[2][0]++; break;
							  	case "CD":	imCounterStateDynamics[2][1]++; break;
							  	case "DC":	imCounterStateDynamics[2][2]++; break;
							  	case "DD":	imCounterStateDynamics[2][3]++; break;		  							
								};
								break;
		case "DD":	switch (sMemory) {
							  	case "CC":	imCounterStateDynamics[3][0]++; break;
							  	case "CD":	imCounterStateDynamics[3][1]++; break;
							  	case "DC":	imCounterStateDynamics[3][2]++; break;
							  	case "DD":	imCounterStateDynamics[3][3]++; break;		  							
								};
								break;		  
	}
}





/**
  * This method selects the probabilistic memory-one strategy used by a cell in these games
  *
	*	@param x 	Horizontal coordinate of the cell
	*	@param y 	Vertical coordinate of the cell
  */
private void vChangeStrat_PMO (int x, int y) {
  double dPayoffCell, dPayoffNeighbor;
  double dPayoffBestNeighbor = -Double.MAX_VALUE;                         // Initialize the best neighbor
  double dPayoff2ndBestNeighbor = -Double.MAX_VALUE;                      // Initialize the 2nd best neighbor
  double dPayoffWorstNeighbor = Double.MAX_VALUE;                         // Initialize the worst neighbor
  double dAvgPayoffNeighbors = 0;                         								// Initialize the average payoff
  double dPayoff2Compare = 0;
  Cell	oBestNeighbor = null,
				o2ndBestNeighbor = null,
				oCell = oCellMatrix[x][y],
				oTournamentWinner = null;
  Vector<Cell> ovNeighbors = oCell.oVGetNeighbors();

  if (ovNeighbors.size() == 0) return;
  
  dPayoffCell = oCellMatrix[x][y].dGetPayoffAvgNumGamesxGen();
  
  Vector ovNeighborsAux = (Vector) ovNeighbors.clone();
  while (ovNeighborsAux.size() > 0) {
		int iAux = (int) (Math.random () * (double) ovNeighborsAux.size());
		Cell oNeighbor = (Cell) ovNeighborsAux.elementAt (iAux);
		ovNeighborsAux.removeElementAt (iAux);
		
		dPayoffNeighbor = oNeighbor.dGetPayoffAvgNumGamesxGen();
				
    if (dPayoffNeighbor > dPayoffBestNeighbor) {
    	dPayoff2ndBestNeighbor = dPayoffBestNeighbor;
    	o2ndBestNeighbor = oBestNeighbor;
		  dPayoffBestNeighbor = dPayoffNeighbor;
		  oBestNeighbor = oNeighbor;
		}
    
    else if (dPayoffNeighbor > dPayoff2ndBestNeighbor) {
		  dPayoff2ndBestNeighbor = dPayoffNeighbor;
		  o2ndBestNeighbor = oNeighbor;
		}

    if (dPayoffNeighbor < dPayoffWorstNeighbor)
	    dPayoffWorstNeighbor = dPayoffNeighbor;
    
    dAvgPayoffNeighbors += dPayoffNeighbor;
  }

  dAvgPayoffNeighbors = dAvgPayoffNeighbors / ((double) ovNeighbors.size());		// Average income for the neighbors
  
  
	oTournamentWinner = oBestNeighbor;
  if ( (Math.random() < 0.5) && (o2ndBestNeighbor != null) )
  	oTournamentWinner = o2ndBestNeighbor;
  
  
  switch (iChangeModel) {
  	case iNOT_BEST:									dPayoff2Compare = dPayoffBestNeighbor; break;
  	case iWORST_THAN_AVG:						dPayoff2Compare = dAvgPayoffNeighbors; break;
  	case iWORST_OF_ALL:							dPayoff2Compare = dPayoffWorstNeighbor; break;
  }
  

	if (iChangeType == iCROSSOVER)											// Genetics (Always crossover, or population stays stable)
  	vProduceNewStrat_PMO (x, y, oTournamentWinner, dOwnWeight);
  else if (dPayoffCell < dPayoff2Compare)					  	// Memetics
  	vProduceNewStrat_PMO (x, y, oBestNeighbor, 0);		// We do mating with dOwnWeight = 0, so we are doing imitation
  	
	
	for (int i=0; i<iNumActionProbs; i++) {
		if (omNewStrat_PMO[x][y].dmProb[i] != omStratPMO[x][y].dmProb[i]) {
  		int iCellStrat = omStratPMO[x][y].iGetDecimalDiscreteStrat();
  		int iNewStrat = omNewStrat_PMO[x][y].iGetDecimalDiscreteStrat();
  		imStratChanges[iCellStrat][iNewStrat]++;
			iNumChanges++;
			break;
		}
	}	
}





/**
 * This method produces a strat_pmo from a cell and its best neighbor using mating/imitation and mutation
 *
 *	@param	x 							Horizontal coordinate of the cell
 *	@param	y 							Vertical coordinate of the cell
 *	@param	oBestNeighbor  	Best neighbor cell to mate with
 *	@param	dOwnWeightAux		Relative weight when mating a neighbor (0: imitation, 1: staying equal)
 */
private void vProduceNewStrat_PMO (int x, int y, Cell oBestNeighbor, double dOwnWeightAux) {
	int xN = oBestNeighbor.iGetPosX();
	int yN = oBestNeighbor.iGetPosY();

	
	if (omStratPMO[xN][yN].bClusterStrat)								// Cluster strats are imitated without mutation
		omNewStrat_PMO[x][y] = new Strat_PMO (oClusterStratPMO);

	else {																							// The imitated neighbor is not a cluster strat
		omNewStrat_PMO[x][y].bClusterStrat = false;
		
		// ------------------------------ BEGIN: Crossover ------------------------------
		for (int i=0; i<iNumActionProbs; i++)								// Crossover with the mate
			if (Math.random() > dOwnWeightAux)								// We take this BestNeighbor prob. (by defect we keep the previous one)
				omNewStrat_PMO[x][y].dmProb[i] = omStratPMO[xN][yN].dmProb[i];
		// ------------------------------ END: Crossover --------------------------------
						
		// ------------------------------ BEGIN: Mutation ---------------------------------
		if (Math.random() < dProbMutation) {							// A mutation only affects one of the 5 probs: P0 to P4 of the child
			double dAux = Math.random();
			if (dAux < 0.2) omNewStrat_PMO[x][y].dmProb[0] = Math.random();
			else if (dAux < 0.4) omNewStrat_PMO[x][y].dmProb[1] = Math.random();
			else if (dAux < 0.6) omNewStrat_PMO[x][y].dmProb[2] = Math.random();
			else if (dAux < 0.8) omNewStrat_PMO[x][y].dmProb[3] = Math.random();
			else omNewStrat_PMO[x][y].dmProb[4] = Math.random();			
		// ------------------------------ END: Mutation -----------------------------------
		}
		
		if (iProbModel == iDISCRETE)
			omNewStrat_PMO[x][y].dmProb = Strat_PMO.dmDiscretizeStrat (omNewStrat_PMO[x][y].dmProb, dPMOInterval);

		}
	
}			// from vProduceNewStrat_PMO







/**
 * Produces the new strat to be used by the cluster in the next generation
 */
private void vProduceNewStrat_Cluster () {
	int x, y, iNumBorderStrats, iIndex;
	double[] 	dmTotProbBorderMates = new double[iNumActionProbs],
						dmInput = new double[iNumActionProbs],
						dmNewClusterStratPMO = oClusterStratPMO.dmGet_Prob();
	Cell oMate;
	Vector ovBorderCells = new Vector (1,1);
	HashMap<String, Integer> oMapHashPMO = new HashMap <String, Integer>();
	StratStats oStratStats, oStratStatsTMP;
	StratStats[] omBorderStratStats = new StratStats[iTotNumCells];		// List of popular border strategies
	
	
	
	for (int i=0; i<ovClusterCells.size(); i++) {
		Cell oClusterCell = (Cell) ovClusterCells.elementAt(i);
		Vector ovNeighbors = (Vector) oClusterCell.oVGetNeighbors();
		for (int j=0; j<ovNeighbors.size(); j++) {
			oMate = (Cell) ovNeighbors.elementAt(j);
			x = oMate.iGetPosX();
			y = oMate.iGetPosY();
			if (! omStratPMO[x][y].bClusterStrat)
				ovBorderCells.add (oMate);																	// Composing a vector with all the external border strats
		}
	}
	
	
	if (ovBorderCells.size() > 0 ) {
		
		iIndex = 0;																												// Removing oMates which are duplicated in ovBorderCells
		do {
			oMate = (Cell) ovBorderCells.elementAt(iIndex);
			int iDuplicated = ovBorderCells.indexOf (oMate, (iIndex+1)); 
			if (iDuplicated > 0)
				ovBorderCells.removeElementAt(iDuplicated);
			else {
				x = oMate.iGetPosX();
				y = oMate.iGetPosY();
				for (int j=0; j<iNumActionProbs; j++)
					dmTotProbBorderMates[j] += omStratPMO[x][y].dmProb[j];
				iIndex++;
			}
		} while (iIndex < ovBorderCells.size());
		
		
				// Building the dmInput vector for oClusterBrain with the border avg. probs.
		for (int i=0; i<iNumActionProbs; i++)
			dmInput[i] = dmTotProbBorderMates[i] / (double) ovBorderCells.size();
	
	
	
		if (iReward < 0) {																							// Changing dmClusterStratPMO, otherwise stays the same
			
			iNumBorderStrats = 0;
			for (int i=0; i<ovBorderCells.size(); i++) {									// Identifying the (integrated) border strats
				oMate = (Cell) ovBorderCells.elementAt(i);
				x = oMate.iGetPosX();
				y = oMate.iGetPosY();
		  	String sStrat = "[";
		  	for (int j=0; j<iNumActionProbs; j++) {
		  		sStrat += String.format (Locale.ENGLISH, "%.1f", omStratPMO[x][y].dmProb[j]); 		// Note: we integrate to 1 decimal
		  		if (j < (iNumActionProbs-1)) sStrat += "|";
		  	}
		  	sStrat += "]";
		  	
		    if (oMapHashPMO.containsKey (sStrat)) {											// If we already have the sStrat
		      Integer oInteger = oMapHashPMO.get (sStrat);
		      int iTMP = oInteger.intValue();
		    	oStratStats = omBorderStratStats[iTMP];
		    }
		    else {																											// If not we add it
		    	oStratStats = new StratStats(sStrat);
		      oMapHashPMO.put (sStrat, new Integer(iNumBorderStrats));
		      omBorderStratStats[iNumBorderStrats] = oStratStats;
		      iNumBorderStrats++;
		    }
		
		    oStratStats.iPop++;
		    oStratStats.iNumGames += oMate.iGetNumGamesPlayed();
		    oStratStats.dPayoff += oMate.dGetPayoff();
		  }
		  
			
									// Calculating the average payoff per (integrated) border strat
			for (int i=0; i<iNumBorderStrats-1; i++)
				omBorderStratStats[i].dAvgPayoff = omBorderStratStats[i].dPayoff / (double) omBorderStratStats[i].iNumGames;
			
			
			
					// Reordering the border strats as a function of their popularity
			int iCounter;
		  do {
		  	iCounter = 0;
		  	for (int i=0; i<iNumBorderStrats-1; i++)
		  		if (omBorderStratStats[i].iPop < omBorderStratStats[i+1].iPop) {
	//	  		if (omBorderStratStats[i].dAvgPayoff < omBorderStratStats[i+1].dAvgPayoff) {
		  			oStratStatsTMP = omBorderStratStats[i];
		  			omBorderStratStats[i] = omBorderStratStats[i+1];
		  			omBorderStratStats[i+1] = oStratStatsTMP;
		  			iCounter++;
		  		}
		  } while (iCounter > 0);
		  
		  
		  		// Reordering the top pop border strats as a function of their AvgPayoff (Important !!!)
		  int iTopPopAvgPayoff = 3;
		  if (iNumBorderStrats < iTopPopAvgPayoff)
		  	iTopPopAvgPayoff = iNumBorderStrats;
			  do {
			  	iCounter = 0;
			  	for (int i=0; i<iTopPopAvgPayoff-1; i++)
			  		if (omBorderStratStats[i].dAvgPayoff < omBorderStratStats[i+1].dAvgPayoff) {
			  			oStratStatsTMP = omBorderStratStats[i];
			  			omBorderStratStats[i] = omBorderStratStats[i+1];
			  			omBorderStratStats[i+1] = oStratStatsTMP;
			  			iCounter++;
			  		}
			  } while (iCounter > 0);
		
	
		  		// By defect the cluster pics the best AvgPayoff from the top pop border strats
		  iIndex = 0;
		  if ( (iTopPopAvgPayoff > 1) && (Math.random() < 0.1) )											// Eps-greedy
		  	iIndex = 1 + (int) (Math.random() * (double) (iTopPopAvgPayoff - 2));
		  dmNewClusterStratPMO = dmGetProbsFromString (omBorderStratStats[iIndex].sStrat);
		  
	/*		  
		  iIndex = 0;																// By defect the cluster pics the best AvgPayoff from the top pop strats
			if (Math.random() < 0.5) {								// Picks random among the other top AvgPayoffs from the top pop strats
			  if ( (iTopPopAvgPayoff > 1) && (Math.random() < 0.5) )											// Eps-greedy
			  	iIndex = 1 + (int) (Math.random() * (double) (iTopPopAvgPayoff - 2));
			  dmNewClusterStratPMO = dmGetProbsFromString (omBorderStratStats[iIndex].sStrat);
				
			}
			else {																					// Picks random among the these 6 basic strats: x.Spiteful, x.TFT, x.Pavlov
				iIndex = 1 + (int) Math.round (Math.random() * 5.0);
				dmNewClusterStratPMO = Strat_PMO.dmProbBasicStrats[iIndex];
			}
			
			oClusterStratPMO.vSetProbs (dmNewClusterStratPMO);
	*/
		}
	
	}		// if (ovBorderCells.size() > 0 )

	oClusterStratPMO.vSetProbs (oHiveBrain.dmGetNewStrat_State_SOM_RL (dmInput, dmNewClusterStratPMO, iReward, true));
	
	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
		System.out.println ("     iNumClusterCells: " + iNumClusterCells + " (" + (iNumClusterCells - iPrevNumClusterCells) + ")");
  
}








/**
 * Printing data related with the simulation
 */
public void vPrintData () {
	HashMap<String, Integer> oHashMapPMO = new HashMap <String, Integer>();
	int iTMP, iCounter, iNumCells, iTotSum = 0;
	int iTotCounter_Row[], iTotCounter_Column[];
	String sStrat, sPrintString;
	Integer oInteger;
	StratStats oStratStats, oStratStatsTMP;
	
	omPopStratStats = new StratStats[iTotNumCells];
	
	// Indexing all the strats that we have
	iNumPopStrats = 0;
  for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++) {
  	double[] dProbDiscrete = Strat_PMO.dmDiscretizeStrat (omStratPMO[x][y].dmProb, dPMOInterval);
  	sStrat = "[";
  	for (int i=0; i<iNumActionProbs; i++) {
  		sStrat += String.format (Locale.ENGLISH, "%.1f", dProbDiscrete[i]); 
  		if (i < (iNumActionProbs-1)) sStrat += "|";
  	}
  	sStrat += "]";
  	
    if (oHashMapPMO.containsKey (sStrat)) {							// If we already have the sStrat
      oInteger = oHashMapPMO.get (sStrat);
      iTMP = oInteger.intValue();
    	oStratStats = omPopStratStats[iTMP];
    }
    else {																							// If not we add it
    	oStratStats = new StratStats(sStrat);
      oHashMapPMO.put (sStrat, new Integer(iNumPopStrats));
      omPopStratStats[iNumPopStrats] = oStratStats;
      iNumPopStrats++;
    }

    oStratStats.iPop++;
    oStratStats.iNumGames += oCellMatrix[x][y].iGetNumGamesPlayed();
    oStratStats.iNumCs += oCellMatrix[x][y].iGetNumCooperations();
    oStratStats.dPayoff += oCellMatrix[x][y].dGetPayoff();
    for (int i=0; i<4; i++)
    	oStratStats.iNumTimes_MO_State[i] += oCellMatrix[x][y].iTimes_MO_State[i];
  }
  

	
	// Reordering the strats as a function of their popularity
  do {
  	iCounter = 0;
  	for (int i=0; i<iNumPopStrats-1; i++)
  		if (omPopStratStats[i].iPop < omPopStratStats[i+1].iPop) {  			  			
  			oStratStatsTMP = omPopStratStats[i];
  			omPopStratStats[i] = omPopStratStats[i+1];
  			omPopStratStats[i+1] = oStratStatsTMP;
  			iCounter++;
  		}
  } while (iCounter > 0);
  
    
  
  System.out.println ("\n\nMode: " + sGAME_TYPE[iGameType] + "\t\t\tAgents: " + iTotNumCells +
  										"\t\t\t\tGamesxEncounter:" + iNumGamesxEncounter);
  System.out.println ("ChangeType: " + sCHANGE_TYPE[iChangeType] + "\t\tChangeModel: " + sCHANGE_MODEL[iChangeModel] +
  										"\t\tProbModel: "+ sPROB_MODEL[iProbModel]);
  System.out.println ("NetType: " + sSHORT_COMPLEX_NET[iNetType] + "\t\t\t\tAvg. Neighborhood: " +
  										String.format (Locale.ENGLISH, "%.2f", dAvgNeighbors) + "\t\t\tGen: " + iNumGen);
  
  System.out.println  ("Pn:" + dProbNoise + "   Pe:" + dProbEnding + "   Pm:" + dProbMutation +
  										"   Pca:" + String.format (Locale.ENGLISH, "%.2f", dProbChangeAction) +
  										"   Psr:" + String.format (Locale.ENGLISH, "%.2f", GameIPDNet.dProbSmallWorld) +
  									 	"   OwnWeight:" + dOwnWeight + "   Ipmo:" + dPMOInterval);

  
  iNumCells = 0;
  iNumStrats2Print = iNumPopStrats;
  if (iNumStrats2Print > 32)
  	iNumStrats2Print = 32;
  System.out.println("\nPopularity of the Top " + iNumStrats2Print +
  									 " strats (P0,P1,P2,P3,P4) == (P0,Pcc,Pcd,Pdc,Pdd) == (P0,Pr,Ps,Pt,Pp)");
  
  System.out.println("\nRank [P0 |P1 |P2 |P3 |P4 ]  Pop (Rel,Tot)      Name   \t\t    Avg.Payoff \tC.Rate \tFcc \tFcd \tFdc \tFdd");
  int iPercent, iAccumPercent = 0;
  for (int i=0; i<iNumStrats2Print; i++) {
  	sPrintString = "";
  	if (i<9)
  		sPrintString += "0";					// To have 2-digit values like 01, 02, ...
  	iPercent = 100*omPopStratStats[i].iPop / iTotNumCells;
  	iAccumPercent += iPercent;
  	sPrintString += ""+(i+1)+".  "+omPopStratStats[i].sStrat+"  "+omPopStratStats[i].iPop+" (" + iPercent
  									+ "%," + iAccumPercent + "%)";
  	while (sPrintString.length() < 43)		// To organize in columns the last strat % data
  		sPrintString += " ";
  	sPrintString += sGetStratName (omPopStratStats[i].sStrat);
  	while (sPrintString.length() < 64)		// To organize in columns the last strat % data
  		sPrintString += " ";
  	sPrintString += "\t" + String.format (Locale.ENGLISH, "%.2f", (double) omPopStratStats[i].dPayoff / (double) omPopStratStats[i].iNumGames);
  	sPrintString += "\t" + String.format (Locale.ENGLISH, "%.2f", (double) omPopStratStats[i].iNumCs / (double) omPopStratStats[i].iNumGames); 
    for (int j=0; j<4; j++)
    	sPrintString += "\t" + String.format (Locale.ENGLISH, "%.2f", (double) omPopStratStats[i].iNumTimes_MO_State[j] / (double) omPopStratStats[i].iNumGames);
  	iNumCells += omPopStratStats[i].iPop;
  	System.out.println (sPrintString);
  }
  
  System.out.println ("\nNum. Agents printed: " + iNumCells + " ("+ (100*iNumCells/iTotNumCells) +
  										"%)          MaxProbChange: " + iMaxDiff_PMOsBuffer +"%");
  
  
  System.out.print ("\nAvg. Probs.(" + (char) 177 + "St.Dev.):");
	for (int i=0; i<iNumActionProbs; i++)
		System.out.print ("   P("+i+")=" + imProbs[i] + "%(" + (char) 177 + imStDev[i]/2 +"%)");
    	
	
	
	iTotSum = 0;
	iTotCounter_Row = new int[4];
	iTotCounter_Column = new int[4];
	for (int j=0; j<4; j++)							// Iterating over the rows
		for (int i=0; i<4; i++) {					// Iterating over the columns
			iTotCounter_Row[j] += imCounterStateDynamics[j][i];
			iTotCounter_Column[i] += imCounterStateDynamics[j][i];
			iTotSum += imCounterStateDynamics[j][i];
		}

	System.out.println ("\n\nFrequency of State Dynamics per Generation: " + iNumGen);
	System.out.print ("        CC    CD    DC    DD");
	for (int j=0; j<4; j++) {						// Iterating over the rows
		switch (j) {
			case 0:	System.out.print ("\nCC ->"); break;
			case 1:	System.out.print ("\nCD ->"); break;
			case 2:	System.out.print ("\nDC ->"); break;
			case 3:	System.out.print ("\nDD ->"); break;
		}
				
		for (int i=0; i<4; i++)						// Iterating over the columns
			System.out.print ("  " + String.format (Locale.ENGLISH, "%.2f", (double) imCounterStateDynamics[j][i] / (double) iTotSum) );

		System.out.print ("  =  " + String.format (Locale.ENGLISH, "%.2f", (double) iTotCounter_Row[j] / (double) iTotSum) );
	}

	System.out.print ("\n--------------------------------------\nProb:");
	for (int i=0; i<4; i++)							// Iterating over the columns
		System.out.print ("  " + String.format (Locale.ENGLISH, "%.2f", (double) iTotCounter_Column[i] / (double) iTotSum) );
	System.out.print("\n\n\n\n");
	
	
	
/*
	iTotSum = 0;
	iTotCounter_Row = new int[32];
	iTotCounter_Column = new int[32];
	int iMaxRow = 0, iMaxColumn=0, iIndexMaxRow = -1, iIndexMaxColumn = -1; 
	for (int j=0; j<32; j++)							// Iterating over the rows
		for (int i=0; i<32; i++) {					// Iterating over the columns
			iTotCounter_Row[j] += imStratChanges[j][i];
			iTotCounter_Column[i] += imStratChanges[j][i];
			iTotSum += imStratChanges[j][i];
		}
	
	for (int i=0; i<32; i++) {							// Iterating over rows and columns
		if (iTotCounter_Row[i] > iMaxRow) {
			iMaxRow = iTotCounter_Row[i];
			iIndexMaxRow = i;
		}
		if (iTotCounter_Column[i] > iMaxColumn) {
			iMaxColumn = iTotCounter_Column[i];
			iIndexMaxColumn = i;
		}
	}

	System.out.println ("Matrix of Strat imitation with: [0]ALL-D  [2]dSpiteful  [3]cSpiteful  [10]dTFT  [11]cTFT  [18]dPavlov  [19]cPavlov  [31]All-C");
	System.out.println ("-------------------------------------------------------------------------------------------------------------------------");
	for (int i=0; i<32; i++)											// Columns
		if (i == iIndexMaxColumn)
			System.out.print ("\t[" + i + "]<-");
		else
			System.out.print ("\t[" + i + "]");
	
	for (int j=0; j<32; j++) {										// Rows
		System.out.print ("\n[" + j + "] ->");
		for (int i=0; i<32; i++)										// Columns
			System.out.print ("\t" + String.format (Locale.ENGLISH, "%.3f", (double) imStratChanges[j][i] / (double) iTotSum));
		System.out.print ("  =  " + String.format (Locale.ENGLISH, "%.3f", (double) iTotCounter_Row[j] / (double) iTotSum) + "\t[" + j +"]");
		if (j == iIndexMaxRow) System.out.print (" <-");
	}
	
	System.out.print ("\n---------------------------------------------------------------------------------------------------------------------------------\nStats:");
	for (int i=0; i<32; i++) {							// Iterating over the columns
		System.out.print ("\t" + String.format (Locale.ENGLISH, "%.3f", (double) iTotCounter_Column[i] / (double) iTotSum));
		if (i == iIndexMaxColumn) System.out.print ("<-");
	}
	System.out.print("  -> " + iTotSum + " Changes\n\n\n\n");
	
*/
	
}



/**
 * Get the name of the strat, given a certain string
 * 
 *	@param	sStrat		The string sequence of the strat
 */
private String sGetStratName (String sStrat) {
	int iBasicStrat, iZDStrat;
	double[] dmProb = new double[iNumActionProbs];
	String sReturn = "", sExtra = "\t";

	dmProb = dmGetProbsFromString (sStrat);
	
	iBasicStrat = Strat_PMO.iIdentify_Basic_Strat (dmProb);
	if (iBasicStrat < 8)
		sReturn = " == " + Strat_PMO.sBasicStrats[iBasicStrat];
	else if (dPMOInterval <= 0.5)
		sReturn = " -> " + Strat_PMO.sBasicStrats [Strat_PMO.iFindCloserBasicStrat (dmProb, dPMOInterval)];
	
	iZDStrat = Strat_PMO.iIdentify_ZD_Strat (dmProb); 
	if (iZDStrat < 3) sExtra = " (" + Strat_PMO.sZDStrats[iZDStrat] + ")";

	return (sReturn + sExtra);
}



/**
 * Get the probabilities from a certain String strat
 * 
 *	@param	sStrat		The string sequence of the unidentified strat
 */
public static double[] dmGetProbsFromString (String sStrat) {
	int iIndex;
	double[] dmProb = new double[iNumActionProbs];
	String sAux;

	sAux = sStrat.substring(1,sStrat.length()-1);		// removing parenthesis !!!
	for (int i=0; i<iNumActionProbs-1; i++) {
		iIndex = sAux.indexOf('|');
		dmProb[i] = Double.valueOf (sAux.substring (0, iIndex));
		sAux = sAux.substring(iIndex+1);
	}
	dmProb[iNumActionProbs-1] = Double.valueOf (sAux);
		
	return dmProb;
}





/**
 * This method calculates the game statistics
 *
 */
public void vCalculateStatistics () {
	//super.vSetGraphicValues();												// OJO: can be avoided if we do not use complex net graphs
	
	ovClusterCells = (Vector) new Vector (1,1);
  dGlobalProfit = 0; dAvgProfitxAgent = 0;
  iNumCellStrat = new int [Game.iMaxStrategies];			// In case we use meta-strategies
  for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++) {
  	dGlobalProfit += oCellMatrix[x][y].dGetPayoff();
  	dAvgProfitxAgent += oCellMatrix[x][y].dGetPayoffAvgNumGamesxGen();
 		iNumCellStrat [oCellMatrix[x][y].iGetStrategyType()]++;
  	if (omStratPMO[x][y].bClusterStrat)
  		ovClusterCells.add (oCellMatrix[x][y]);
	}
  iNumClusterCells = ovClusterCells.size();
  dAvgProfitxAgent = dAvgProfitxAgent / iTotNumCells;	// Avg. profit per agent in this generation
  

	double[] dmAvgProb = new double[iNumActionProbs];
	for (int y=0; y<iCellV; y++)
	for (int x=0; x<iCellH; x++)
		for (int i=0; i<iNumActionProbs; i++)								// Here we add the values of the 5 probabilities: P0, P1, P2, P3, P4
			dmAvgProb[i] += omStratPMO[x][y].dmProb[i];

	Vector oVect;
	for (int i=0; i<iNumActionProbs; i++) {
		oVect = (Vector) oVFrecActions.elementAt (i);				// Here we calculate the averages to obtain the oVFrecActions values
		dmAvgProb[i] /= (double) iTotNumCells;
		imProbs[i] = (int) (100.0 * dmAvgProb[i]);				// This value will be used for the probability histogram
		oVect.add (new Integer (imProbs[i]));
    while (oVect.size() > MainWindow.iLastNGen)
      oVect.removeElementAt (0);
		}
	
	Integer oInteger;
	int iDiff, iAvgProbBuffer;
	iMaxDiff_PMOsBuffer = 0;
	for (int i=0; i<iNumActionProbs; i++) {
		oVect = (Vector) oVFrecActions.elementAt (i);					// Here we calculate the averages to obtain the oVFrecActions values.	
		oInteger = (Integer) oVect.elementAt (oVect.size() - 1);
		imSumAvg_PMOsBuffer[i] += oInteger.intValue();
		
		if (iNumGen > iPMOsBufferSize) {
			oInteger = (Integer) oVect.elementAt (oVect.size() - iPMOsBufferSize);
			imSumAvg_PMOsBuffer[i] -= oInteger.intValue();
			iAvgProbBuffer = imSumAvg_PMOsBuffer[i] / iPMOsBufferSize;
			for (int j=1; j<iPMOsBufferSize; j++) {
				oInteger = (Integer) oVect.elementAt (oVect.size() - j);
				iDiff = Math.abs (oInteger.intValue() - iAvgProbBuffer);
				if (iDiff > iMaxDiff_PMOsBuffer)
					iMaxDiff_PMOsBuffer = iDiff;
			}
		}
		
		else {
			iAvgProbBuffer = imSumAvg_PMOsBuffer[i] / iNumGen;
			for (int j=0; j<oVect.size(); j++) {
				oInteger = (Integer) oVect.elementAt (j);
				iDiff = Math.abs (oInteger.intValue() - iAvgProbBuffer);
				if (iDiff > iMaxDiff_PMOsBuffer)
					iMaxDiff_PMOsBuffer = iDiff;
			}
		}
		
	}		// for (int i=0; i<iNumActionProbs; i++) {
	
	oVChangesxGen.add (new Integer (iMaxDiff_PMOsBuffer));
  while (oVChangesxGen.size() > MainWindow.iLastNGen)
  	oVChangesxGen.removeElementAt (0);
	
  oVGlobalProfit.add (new Double (dAvgProfitxAgent));		// Storing the avg. profit accumulated per agent in this generation
  while (oVGlobalProfit.size() > MainWindow.iLastNGen)
  	oVGlobalProfit.removeElementAt (0);

  
  
	double[] dmVariance = new double[iNumActionProbs];
	for (int y=0; y<iCellV; y++)
	for (int x=0; x<iCellH; x++)
		for (int i=0; i<iNumActionProbs; i++) {
			double dAux = omStratPMO[x][y].dmProb[i] - dmAvgProb[i];
			dmVariance[i] += dAux * dAux;										// Here we calculate the variance of the 5 probabilities: P0, P1, P2, P3, P4
		}
	
	double[] dmStdDeviation = new double[iNumActionProbs];
	for (int i=0; i<iNumActionProbs; i++) {
		dmVariance[i] /=  (double) iTotNumCells;
		dmStdDeviation[i] = Math.sqrt (dmVariance[i]);			// Here we calculate the standard deviation of the 5 probs: P0, P1, P2, P3, P4
		imStDev[i] = ( (int) (100.0 * dmStdDeviation[i]) );	// This value will be used for the standard deviation in the probability histogram
	}

}	// from public void vCalculateStatistics () {



}	// from the class
