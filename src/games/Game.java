package games;

import java.util.Vector;

import window.MainWindow;
import window.WindowCONS;


/**
  * This is the basic class to create new games
  *
  * @author  Juan C. Burguillo Rial
  * @version 3.0
  */
public abstract class Game implements GameCONS, WindowCONS
{
protected MainWindow oMainWindow;

public static int iNewGame = 0;					  			// Number of times a games has been restarted
public static int iMaxRandomAttempts = 5;				// Max number of attempts to select something
public static boolean bBatchMode = false;	  		// Indicates BATCH mode (bCellNet) or not (CellNet)


// #################### This values are accessed from DlgConfiguration and DlgPayoffMatrix ####################
public static int iGameType = -1;             	// Type of games we are using
public static int iChangeType = iIMITATION;   	// Change type: Imitation or learning (Stats, LA, QL, etc.)
public static int iAlgorithm = 0;		  	  			// Algorithms used by some games(Optimization, Prediction)
public static int iTotPosMatrix = 400;	  	  	// Number of possible cells in the spatial matrix
public static int iCellH = 20;                 	// Horizontal cells in the grid
public static int iCellV = 20;                 	// Vertical cells in the matrix
public static int iTotNumCells = 0;           	// Real number of cells (the grid can have empty cells (null))
public static int iTotNumLinks = 0;           	// Real number of unidirectional links among cells
																								// NOTE: in games with bidirectional links this number must be divided by two !!!
public static int iNGenIncubation = 0;        	// Number of generations before starting infection
public static double dAvgInterxCell = 1.25;			// Avg. number of times that a cell interacts in each games generation
public static double dSpatialRadio = 1.0;		    // Radio of the neighborhood
public static double dProbMutation = 0.0;       // Probability of mutation
public static double dProbNoise = 0.0;					// Probability that an action is modified by noise
public static double dProbChangeAction = 1.0;   // Probability of imitation
public static double dProbJoinCoa = 0.25;      	// Probability of joining to a coalition
public static double dProbRebellion = 0.0;      // Probability that a cell rebels in a coalition
public static double dProbInfection = 0.1;      // Probability of infecting a neighbor cell
public static double dProbEmpty = 0.0;		  		// Probability of having an empty cell
public static double dNumGen2ChangeAction = 1.0;// Time scale for action changes
public static double dNumGen2Rewire = 1.0;	  	// Time scale for partner switching
public static double dProbResource = 0.5;				// Probability of having a resource
public static boolean bCoalitions = false;    	// Do we play with or without coalitions?
public static boolean bHolons = false;    			// Do we play with or without coalition hierarchies, i.e., holons?
public static boolean bUseNowaksW = false;			// Do we use W from Nowaks paper?

public static Cell[][] oCellMatrix = new Cell [iCellH][iCellV];	// Cell Matrix accessed from VisorWorld
public static Cell[] oCellLine = new Cell [iCellH*iCellV];
public static Vector oCellVector = new Vector (1,1);			// Matrix cells ordered in a sequential vector

public static int iNumTypes = 3;			  			// (0) Independent, (1) in coalition or (2) leader
public static int iNumActionProbs = 2;				// In this framework used for the Probs: P(0)...P(4)
public static double[][][] dPayMatrix = new double[5][5][2];
/*   C  D	// Payoff matrix initialized at the constructor
     1  0
C 1  R  S
D 0  T  P				*/
public static double dT = 3.5;			// (T)emptation in DC [with T>R>P>S and R > S+T]
public static double dR = 3.0;			// (R)eward in CC
public static double dP = 0.5;			// (P)unishment in DD
public static double dS = 0.0;			// (S)ucker in CD
// #################### Previous values are accessed from DlgConfiguration and DlgPayoffMatrix ##################

// ############################ This values are accessed from Parameter Windows ##########################
public static boolean bMovement = false;	// Indicates if cell can move
public static boolean bRandomTaxShare = true;	// Indicates if taxes are random
public static boolean bStorePayoff = false;	// Indicates if payoff is accumulated by cells
public static int iWorldShow = 0;           // Indicates what is shown on the map
public static int iNumGen = 0;              // Number of generations
public static int iStatsBufferSize = 5;    	// Buffer length for storing cell statistics
public static int iNumChanges = 0;					// Number of cells that have changed a certain parameter
public static String sTextStateBar="";      // Text to be shown in the state bar
public static int iNumNetStats = 4;					// Number of network statistics to show in the graphics window

public static int[] imCellsAction = new int [iNumActionProbs];				// Contains number of cells per action
public static int[] imCellsType = new int [iNumTypes];						// Contains number of cells per type
public static int[] imMovAction = new int [iNumActionProbs];					// Contains number of movements per action
public static double[] dmProfitAction = new double [iNumActionProbs];	// Profit per action
public static double[] dmProfitType = new double [iNumTypes];	 		// Profit per cell type

public static Vector<Vector> oVFrecActions = new Vector<Vector> (1,1);  	// Action frequency to display graphs
public static Vector<Vector> oVFrecTypes = new Vector<Vector> (1,1);	  	// Type frequency to display graphs
public static Vector<Vector> oVFrecMov = new Vector<Vector> (1,1);	  		// Movement frequency to display graphs
public static Vector<Vector>  oVNetStats = new Vector<Vector> (1,1);			// Network statistics to display graphs
public static Vector oVNetCCStats = new Vector (1,1);	  									// Cluster Coefficient per generation
public static Vector oVTextAction = new Vector (1,1);	  									// Textual data per action for the graphics
public static Vector oVTextCellType = new Vector (1,1);	  								// Textual data per type for the graphics
public static Vector oVGlobalProfit = new Vector (1,1);	  								// Profit per generation
public static Vector<Vector> oVProfitAction = new Vector<Vector> (1,1);	  // Profit per action
public static Vector<Vector> oVProfitType = new Vector<Vector> (1,1);	  	// Profit per cell type
public static Vector oVChangesxGen = new Vector (1,1);    								// Number of cells changing per generation

//------------------- Used by Coalition Games --------------------------------------
public static boolean bShowLeaders=false;         // Show leader cells in white color
public static int iNumCellRec = 0;                // Number of cells with extra resources per generation
public static int iMaxStrategies = iMETA_STRAT;   // Maximum number of strategies allowed
public static double dLeaderTax = 0.0;            // Leader tax over the income [0,1]
public static double dLeaderShare = 0.0;          // Sharing of benefits obtained by the leader [0,1]
public static double dCellIncomeGen = 1.0;        // Cell incomes per generation
public static double dCellCostGen = 1.0;          // Cell costs per generation
public static double dMinCellPayoff = 0.0;            // Minimum payoff obtained by a cell
public static double dMaxCellPayoff = 0.0;            // Maximum payoff obtained by a cell
public static int[] imProbs = new int [5];                   				// Probability values [number of probabilities]
public static int[] imStDev = new int [5];                   				// Standard deviation values [number of probabilities]
public static Vector oVCoaLeaders = new Vector (1,1);           				// Contains the leaders of the generation coalitions
public static Vector ovAvgTaxCoaCell = new Vector (1,1);        				// Avg. tax per coalition cell
public static int[] iNumCellStrat = new int [iMETA_STRAT];            	// Number of cells using a strategy
public static Vector oVFrecActionInsideCoa = new Vector (1,1);       		// Frequency of actions inside a group
public static Vector<Vector> oVCellsxStrat = new Vector<Vector> (1,1); 	// To store data about the number of cells per strategy
//-----------------------------------------------------------------------------------------------------

//------------------- Used by Complex Network Games ------------------------------
public static int iNetType = 0;                  	// Network type: 0-Spatial, 1-Small-World, 2-Scale-Free, 3-Random Net
public static int iInitialNodesNet = 2;          	// Initial number of cells in Scale Free and Random Network
public static double dAvgNeighborhoodSize = 4.0;  // Average number of neighbors per cell
public static double dProbSmallWorld = 0.1;       // Probability of creating new links in Small World
public static double dProbRewiring = 0; 	     		// Probability of rewiring
public static double dProbRewireRandom = 0.5;     // Probability of changing a link randomly (1) or selecting the best (0)
public static double dGlobalProfit = 0; 		  		// Global profit per generation
public static int iNumIntervals=20;               // Number of intervals for displaying network graphs
public static int iNumValInterval=1;              // Number of values per interval to display network graphs
//-----------------------------------------------------------------------------------------------------------


// ############################ Previous public static values are accessed from Parameter Windows ######################



public Game() {
	if (Game.bBatchMode)
		iCellH = (int) Math.sqrt (iTotPosMatrix);
	else {
	  MainWindow.iCellSize = MainWindow.iMapSize / (int) Math.sqrt (iTotPosMatrix);
	  iCellH = MainWindow.iMapSize/MainWindow.iCellSize;	// Horizontal cells in the matrix
	}
  iCellV = iCellH;              											// Vertical cells in the matrix
  iTotPosMatrix = iCellH * iCellV;          					// Number of potential cells
  
  iNumCellStrat = new int [iMETA_STRAT];            	// Number of cells using a strategy
  dProbEmpty = 0;

  oCellMatrix = new Cell [iCellH][iCellV];
  for (int x=0; x<iCellH; x++)
  for (int y=0; y<iCellV; y++)
    oCellMatrix[x][y] = null;
}



/**
  * This method sets up the games when pressing "New".
  */
public void vNewGame () {
	if (Game.bBatchMode)
		iCellH = (int) Math.sqrt (iTotPosMatrix);
	else {
	  MainWindow.iCellSize = MainWindow.iMapSize / (int) Math.sqrt (iTotPosMatrix);
	  iCellH = MainWindow.iMapSize / MainWindow.iCellSize;
	}
  iCellV = iCellH;
  iTotPosMatrix = iCellH * iCellV;  
  iNumGen = 0;
  iTotNumCells = 0;

  oCellMatrix = new Cell [iCellH][iCellV];
  for (int x=0; x<iCellH; x++)
    for (int y=0; y<iCellV; y++)
      oCellMatrix[x][y] = null;

  iNumCellStrat = new int [iMETA_STRAT];

  imCellsAction = new int [iNumActionProbs];
  imCellsType = new int [iNumTypes];
  imMovAction = new int [iNumActionProbs];
  dmProfitAction = new double [iNumActionProbs];
  dmProfitType = new double [iNumTypes];
  
  oVFrecTypes = new Vector<Vector> (1,1);
  oVProfitType = new Vector<Vector> (1,1);
  for (int i=0; i<iNumTypes; i++) {
		oVFrecTypes.add (new Vector (1,1));
		oVProfitType.add (new Vector (1,1));
  }
  
  oVFrecActions = new Vector<Vector> (1,1);
  oVFrecMov = new Vector<Vector> (1,1);
  oVProfitAction = new Vector<Vector> (1,1);
  for (int i=0; i<iNumActionProbs; i++) {
    oVFrecActions.add (new Vector (1,1));
    oVFrecMov.add (new Vector (1,1));
    oVProfitAction.add (new Vector (1,1));
  }

  oVNetStats = new Vector<Vector> (1,1);
  for (int i=0; i<iNumNetStats; i++)
  	oVNetStats.add (new Vector (1,1));
  
  oVNetCCStats = new Vector (1,1);

  oVCellsxStrat = new Vector<Vector> (1,1);
  for (int i=0; i<iMETA_STRAT; i++)
    oVCellsxStrat.add (new Vector (1,1));

  oVChangesxGen = new Vector (1,1);
  oVGlobalProfit = new Vector (1,1);
  ovAvgTaxCoaCell = new Vector (1,1);  
}



/**
 * This opens all graphical windows used by this game
 */
public void vOpenALLWindows() {	
}




/**
  * This method creates a sequential vector and a sequential line from the matrix of cells.
  * Note: This method must be called before using the next ones for the networks.
  */
public void vCells2Vector_Line (Cell[][] oMatCellAux) {
	oCellVector = new Vector<Cell> (1,1);
  oCellLine = new Cell [iCellH*iCellV];
  
  int iAux=0;
  for (int y=0; y<iCellV; y++)
  for (int x=0; x<iCellH; x++) {
  	oCellLine[iAux++] = oMatCellAux[x][y];
  	oCellVector.add (oMatCellAux[x][y]);
  }
}



/**
  * This method sets up the neighbors of a cell in a lattice as a function of the radio
  */
public void vSetNeighborsSpatialRadio () {
  int xAux, yAux, iRadio = (int) Math.ceil (dSpatialRadio);
  double dDist=0;
  
  iTotNumLinks = 0;
  
  for (int x=0; x<iCellH; x++)
  for (int y=0; y<iCellV; y++) {
    if (oCellMatrix[x][y] == null)
      continue;                          // If the cell is empty go to next

    Vector<Cell> ovNeighbors = new Vector<Cell> (1,1);
    for (int v=-iRadio; v<=iRadio; v++)
    for (int h=-iRadio; h<=iRadio; h++) {
      dDist = Math.sqrt (h*h + v*v);
      if ( (dDist > dSpatialRadio) || (dDist == 0)) continue;
      xAux = x+h;
      yAux = y+v;

      if (xAux < 0)						// Toroidal world
        xAux += iCellH;
      else if (xAux >= iCellH)
        xAux -= iCellH;

      if (yAux < 0)
        yAux += iCellV;
      else if (yAux >= iCellV)
        yAux -= iCellV;

      if (oCellMatrix[xAux][yAux] != null)												// Not an empty cell
      	if (ovNeighbors.indexOf(oCellMatrix[xAux][yAux]) == -1)		// Not already added
      		ovNeighbors.add (oCellMatrix[xAux][yAux]);							// Added as a new neighbor
    }

    oCellMatrix[x][y].vSetNeighbors (ovNeighbors);
	  iTotNumLinks += ovNeighbors.size();
  }

}




/**
  * This method sets the neighbors per cell creating a Small World net.
  * Note: it assumes that there are no empty cells(null) and that the radio is less than the number of cells
  */
public void vSetNeighborsSmallWorld () {
  int iAux, iRadio = (int) (dAvgNeighborhoodSize / 2.0);
  Vector<Cell> ovNeighbors;

            // Initializing links with lateral networks
  for (int i=0; i<oCellLine.length; i++) {
    ovNeighbors = new Vector<Cell> (1,1);

    for (int h=-iRadio; h<=iRadio; h++) {
      if (h==0) continue;                                     // Cell itself is not a neighbor
      iAux = i + h;
      if (iAux < 0)
        iAux += oCellLine.length;
      else if (iAux >= oCellLine.length)
        iAux -= oCellLine.length;
      ovNeighbors.add (oCellLine[iAux]);
    }

    oCellLine[i].vSetNeighbors (ovNeighbors);
  }

  if (dProbSmallWorld == 0) return;									// If there is no initial rewiring, we do not rewire
  else vCreateSmallWorldNet();
  
  iTotNumLinks = 0;
  for (int i=0; i<oCellLine.length; i++)
  	iTotNumLinks += oCellLine[i].oVGetNeighbors().size();

}



/**
  * This method reconfigure a network to reconnect them as a Small World net.
  */
public void vCreateSmallWorldNet () {
  int iAux;
  Vector<Cell> ovNeighbors, ovMatesMate;
  Cell oCell;
  
            // Changing links
  for (int i=0; i<oCellLine.length; i++) {
    ovNeighbors = oCellLine[i].oVGetNeighbors ();

    for (int j=0; j<ovNeighbors.size(); j++) {                      // Over the neighbors j of cell i
      if (Math.random() >= dProbSmallWorld) continue;                // If dProb doesn't happen jump to next
      
      do {
        iAux = (int) (Math.random() * (double) oCellLine.length);   // Searching for a new neighbor
      } while ((iAux == i) || (iIsNeighbor (oCellLine[iAux], ovNeighbors) > -1));
      
      oCell = ovNeighbors.elementAt (j);
      ovMatesMate = oCell.oVGetNeighbors();
      if (ovMatesMate.size() == 1) continue;		// If neighbor j only has 1 neighbor (i) then it is not deleted and continue
        
      ovMatesMate.removeElement (oCellLine[i]);                   // Remove i as a neighbor of j
      ovNeighbors.removeElementAt (j);                            // Remove j as a neighbor of i
      ovNeighbors.add (oCellLine[iAux]);                          // Add iAux as a neighbor of i
      ovMatesMate = oCellLine[iAux].oVGetNeighbors ();            // Asking iAux for his neighbors
      ovMatesMate.add (oCellLine[i]);                             // Adding i among iAux's neighbors
    }

  }

}





/**
  * This method determines the neighbors of a cell using a Scale Free network
  */
public void vSetNeighborsScaleFree () {

  int iNumEdgesTot, iVecino, iNumNeighborsAux, iHalfAvgNeighborhoodSize = (int) (dAvgNeighborhoodSize / 2.0);
  int iNumCellsTot = iCellH*iCellV;
  double dProb, dProbTot;
  Vector<Cell> ovNeighbors, ovMatesMate;
  int[] iMatDegCell = new int [iNumCellsTot];
  double[] dmProbMate;

  
  iNumEdgesTot = 0;
  for (int i=0; i<iInitialNodesNet; i++) {									// Linking the initial nodes
    ovNeighbors = oCellLine[i].oVGetNeighbors();
    ovMatesMate = oCellLine[i+1].oVGetNeighbors();
    ovNeighbors.add (oCellLine[i+1]);
    ovMatesMate.add (oCellLine[i]);
    iMatDegCell[i+1]++;
    iMatDegCell[i]++;
    iNumEdgesTot += 2;																			// Note we have 2 new unidirectional links
  }


  for (int i=(iInitialNodesNet+1); i<iNumCellsTot; i++) {		// Connecting new nodes with previous ones
    ovNeighbors = new Vector<Cell> (1,1);

    iNumNeighborsAux = iHalfAvgNeighborhoodSize;
    if (i < iHalfAvgNeighborhoodSize)												// if we have less initial nodes than half neighborhood
      iNumNeighborsAux = i;

    dmProbMate = new double [i];
    for (int j=0;j<i; j++)																	// Setting up the probabilities to link with previous nodes
      dmProbMate[j] = ( (double) iMatDegCell[j]) / (double) iNumEdgesTot;


    for (int j=0; j<iNumNeighborsAux; j++) {                // Over the number of neighbors to choose
      do {
        dProb = Math.random();
        dProbTot = dmProbMate[0];
        iVecino = -1;
        for (int k=0; k<i; k++) {
          if (dProb < dProbTot) {
            iVecino = k;																		// We select this node
            break;
          }
          dProbTot += dmProbMate[k+1];
        }
      } while (iIsNeighbor (oCellLine[iVecino], ovNeighbors) > -1);

      ovNeighbors.add (oCellLine[iVecino]);									// We setup new neighbors
      ovMatesMate = oCellLine[iVecino].oVGetNeighbors();
      ovMatesMate.add (oCellLine[i]);
      iMatDegCell[i]++;
      iMatDegCell[iVecino]++;
      iNumEdgesTot += 2;																		// NOTE: we have 2 new unidirectional links !!!
    }

    oCellLine[i].vSetNeighbors(ovNeighbors);
  }
  
  iTotNumLinks = 0;
  for (int i=0; i<oCellLine.length; i++)
  	iTotNumLinks += oCellLine[i].oVGetNeighbors().size();
  
}




/**
  * This method sets up the neighbors of a cell using a Random Network
  * Note: it assumes that there are no empty cells (null)
  */
public void vSetNeighborsRandomNetwork () {
  int iAux, iHalfAvgNeighborhoodSize = (int) (dAvgNeighborhoodSize / 2.0);
  Vector<Cell> ovNeighbors, ovMatesMate;

  for (int i=0; i<oCellLine.length; i++) {															// For all the nodes
    ovNeighbors = oCellLine[i].oVGetNeighbors();
    for (int j=0; j<iHalfAvgNeighborhoodSize; j++) {										// We add (dAvgNeighborhoodSize/2) links per node
      do {
        iAux = (int) (Math.random() * (double) oCellLine.length);       // Searching for a new neighbor
      } while ((iAux == i) || (iIsNeighbor (oCellLine[iAux], ovNeighbors) > -1));
      ovNeighbors.add (oCellLine[iAux]);
      ovMatesMate = oCellLine[iAux].oVGetNeighbors();
      ovMatesMate.add (oCellLine[i]);																		// NOTE: we have 2 new unidirectional links !!!
    }

  }

  iTotNumLinks = 0;
  for (int i=0; i<oCellLine.length; i++)
  	iTotNumLinks += oCellLine[i].oVGetNeighbors().size();

}











/**
  * This method checks if a cell is already a neighbor
  */
private int iIsNeighbor (Cell oCellAux, Vector<Cell> ovNeighborsAux) {
  for (int k=0; k<ovNeighborsAux.size(); k++)           // Checking that is not in the neighbor list
    if (oCellAux == ovNeighborsAux.elementAt (k))
      return k;
      
  return -1;    // If it arrives here, then it is not a neighbor
}


/**
  * Contains the code to be executed per cycle in a thread
  */
public abstract void vRunLoop(); // Every games must extend this



/**
 * Calculates distance among two cells in a toroidal world
 */
public static double dCalcDistanceToroidal (Cell oCell1, Cell oCell2) {
  return dCalcDistanceToroidal (oCell1.iGetPosX(), oCell1.iGetPosY(), oCell2.iGetPosX(), oCell2.iGetPosY());
}


/**
  * Calculates distance among two points in a toroidal world
  */
public static double dCalcDistanceToroidal (int x1, int y1, int x2, int y2) {
  int iDistX1, iDistX2, iDistX;
  int iDistY1, iDistY2, iDistY;
  double dDistance;
  
  if (x1 < x2) {
    iDistX1 = x2 - x1;
    iDistX2 = x1 + iCellH - x2;
    }
  else {
    iDistX1 = x1 - x2;
    iDistX2 = x2 + iCellH - x1;
  }
  
  if (iDistX1 < iDistX2)
    iDistX = iDistX1;
  else
    iDistX = iDistX2;


  if (y1 < y2) {
    iDistY1 = y2 - y1;
    iDistY2 = y1 + iCellV - y2;
    }
  else {
    iDistY1 = y1 - y2;
    iDistY2 = y2 + iCellV - y1;
  }
  
  if (iDistY1 < iDistY2)
    iDistY = iDistY1;
  else
    iDistY = iDistY2;

  dDistance = Math.sqrt (iDistX*iDistX + iDistY*iDistY);
  
  return dDistance;
  }



/**
 * Calculates the distance among two cells
 */
public static double dCalcDistance (Cell oCell1, Cell oCell2) {
  return dCalcDistance (oCell1.iGetPosX(), oCell1.iGetPosY(), oCell2.iGetPosX(), oCell2.iGetPosY());
}


/**
  * Calculates distance among two cells
  */
public static double dCalcDistance (int x1, int y1, int x2, int y2) {
  double iDistX;
  double iDistY;
  double dDistance;
  
  iDistX = x1 - x2;
  iDistY = y1 - y2;

  dDistance = Math.sqrt (iDistX*iDistX + iDistY*iDistY);
  
  return dDistance;
  }



/**
 * Prints data related with the simulation
 */
public void vPrintData ()
{}



/**
 * States the basic graphic values for all games
 */
protected void vSetGraphicValues() {
  Vector oVect, oVectMov, oVecType, oVectProfit, oVectorNetStats;

/*
  oVGlobalProfit.add (new Double (dGlobalProfit));														// Storing the profit accumulated by this generation
  while (oVGlobalProfit.size() > MainWindow.iLastNGen)
  	oVGlobalProfit.removeElementAt (0);

  oVChangesxGen.addElement (new Integer (100 * iNumChanges) / iTotNumCells);	// Normalizing and storing action changes per generation
  while (oVChangesxGen.size() > MainWindow.iLastNGen)
  	oVChangesxGen.removeElementAt (0);

	for (int i=0; i<iNumActionProbs; i++) {
    oVectMov = (Vector) oVFrecMov.elementAt (i);
    oVectProfit = (Vector) oVProfitAction.elementAt (i);
    if (imCellsAction[i] > 0) {
      oVectMov.add (new Integer(100 * imMovAction[i] / imCellsAction[i]));
      oVectProfit.add (new Double (dmProfitAction[i] / (double) imCellsAction[i]));
    }
    else {
      oVectMov.add (new Integer(0));
      oVectProfit.add (new Double (0));
    }
    while (oVectMov.size() > MainWindow.iLastNGen)
      oVectMov.removeElementAt (0);
    while (oVectProfit.size() > MainWindow.iLastNGen)
      oVectProfit.removeElementAt (0);
    
    oVect = (Vector) oVFrecActions.elementAt (i);
    oVect.add (new Integer(100 * imCellsAction[i] / iTotNumCells));
    while (oVect.size() > MainWindow.iLastNGen)
      oVect.removeElementAt (0);
  }
  
  for (int i=0; i<iNumTypes; i++) {
  	oVecType = (Vector) oVFrecTypes.elementAt(i);
  	oVecType.add (new Integer(100 * imCellsType[i] / iTotNumCells));
    while (oVecType.size() > MainWindow.iLastNGen)
      oVecType.removeElementAt (0);
  }
*/
  
  if (MainWindow.omDlGraph[MainWindow.iNET_STATS] != null) {
    
  	if (MainWindow.omDlGraph[MainWindow.iNET_STATS].isVisible())
    	NetStats.vCalcBasicStats();
	  
    for (int i=0; i<iNumNetStats; i++) {
	    oVectorNetStats = (Vector) oVNetStats.elementAt (i);
	    switch(i) {
	      case 0:	oVectorNetStats.add (new Double(NetStats.dMean)); break;
	      case 1:	oVectorNetStats.add (new Double(NetStats.dStdDeviation)); break;
	      case 2:	oVectorNetStats.add (new Double(NetStats.iRange)); break;
	      case 3:	oVectorNetStats.add (new Double(NetStats.iMode));break;
	    }
	    while (oVectorNetStats.size() > MainWindow.iLastNGen)
	      oVectorNetStats.removeElementAt (0);
    
    }
    
  }
  
  if (MainWindow.omDlGraph[MainWindow.iNET_CC_STATS] != null) { 
		
  	if (MainWindow.omDlGraph[MainWindow.iNET_CC_STATS].isVisible())
		  NetStats.vCalcClusterCoef();
		
		oVNetCCStats.add (new Double (NetStats.dCC));			// Storing the cluster coefficient in this generation
		while (oVNetCCStats.size() > MainWindow.iLastNGen)
		  oVNetCCStats.removeElementAt (0);
		
  }

}


}	// from the class