package games;

import java.util.Vector;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
  * This is the basic class to model a cell
  *
  * @author  Juan C. Burguillo Rial
  * @version 3.0
  */
public class Cell
{
protected String sIdCell;										// String identifier of the Cell
protected boolean bOwnsResource = false;		// Indicate if this cell owns any abstract resource	
protected int iPosX;											  // X cell's position on the grid
protected int iPosY;											  // Y cell's position on the grid
protected int iAction;                                            // Present action (defect, cooperate, etc.) to be used
protected int iNewAction;                                         // New Action
protected int iCellType;                                          // Cell type (independent, coa member, leader, etc.)
protected int iNewCellType;                                       // New Cell type
protected int iStrategy;                                      		// Strategy (defector, cooperator, etc.) used by this cell
protected int iNewStrategy;                                      	// New Strategy
protected int iStrategyType;                                      // Strategy (imitation, learning, etc.) used by this cell
protected int iAliveNeighbors;																		// Used in LIFE games
protected int iColor;																							// Used in Sat Ball games
protected double dPayoff;                                         // Instant income per generation
protected double dPayoffNeighbors;                                // Instant neighbors income per generation
protected Vector<Double> ovBufferPayoff = new Vector<Double> (1,1);                // Stores the last payoffs in a buffer
protected double dPayoffBuffer;                                   // Sum of the incomes in a buffer of generations
protected double dPayoffTot;                                      // Total income over all generations
protected double dPayoffGen=Game.dCellIncomeGen;                  // Income per generation
protected double dCostGen=Game.dCellCostGen;                      // Costs per generation
protected Vector<Cell> ovNeighbors = new Vector<Cell> (1,1);      // Neighbors of this cell

protected double dGlobalValMax = -Double.MAX_VALUE;
protected double dGlobalValMin = Double.MAX_VALUE;
protected double dAllGlobalValMax = -Double.MAX_VALUE;
protected double dAllGlobalValMin = Double.MAX_VALUE;


// BEGIN ------------------- Statistics ------------------------------
protected int iNumCooperations;                                   // Number of cooperation actions in the last generation
protected int iNumGamesxGen;                                      // Number of interactions (turns) per generation
public int iTimes_MO_State[] = new int[4];												// Times this strat has been in CC(0), CD(1), DC(2), DD(3)
// END --------------------- Statistics ------------------------------


// BEGIN ------------------- Complex Networks ------------------------------
protected int iIdPajek = 0;                                       // Identifier to exchange data with Pajek
protected double dMaxPayoff = 0;                                  // It is the maximum payoff per cell, used for rewiring
protected Cell oCellMinPayoff=null;								  							// Cell with the minimum payoff
// END --------------------- Complex Networks ------------------------------









public Cell (int x, int y, int iActionAux) {
  this (x, y, 0, iActionAux);
}


public Cell (int x, int y, int iCellTypeAux, int iActionAux) {
  this (""+x+","+y, iCellTypeAux, iActionAux);
  iPosX = x;
  iPosY = y;
}


public Cell (String sIdAux, int iCellTypeAux, int iActionAux) {
	int iIndex;
	String sAux;
	
  sIdCell = new String (sIdAux);
  iIndex = sIdCell.indexOf (",");
  sAux = sIdCell.substring (0, iIndex);					// Taking the initial substring
  iPosX = Integer.parseInt (sAux);
  sAux = sIdCell.substring (iIndex+1);					// Taking the final substring
  iPosY = Integer.parseInt (sAux);

  iCellType = iCellTypeAux;
  iNewCellType = iCellTypeAux;
  iAction = iActionAux;
  iNewAction = iActionAux;
  iAliveNeighbors = 0;
  iColor = -1;
  iNumGamesxGen = 0;
  
  if (Game.iChangeType == Game.iMETA_STRAT)						// With Meta-strats cells have personal strategies
  	iStrategyType = (int) (Math.random() * (double) (Game.iMaxStrategies - 1));		// Initially we do not consider iHIVE_BRAIN
  else  
  	iStrategyType = Game.iChangeType;									// All cells share the same strategy

  dPayoff = 0;
  dPayoffBuffer = 0;
  dPayoffTot = 0;
  dPayoffNeighbors = 0;
  for (int i=0; i<Game.iStatsBufferSize; i++)
    ovBufferPayoff.addElement (new Double (dPayoff));
  }



public String sGetID ()
  {return sIdCell;}
  
public int iGetPosX ()
  {return iPosX;}

public int iGetPosY ()
  {return iPosY;}

public void vSetNewPos (int x, int y) {
  iPosX = x;
  iPosY = y;
}

public int iGetStringPosX () {
  int iIndex = sIdCell.indexOf (",");
  String sAux = sIdCell.substring (0, iIndex);					// Taking the initial substring
  return Integer.parseInt (sAux);
  }

public int iGetStringPosY () {
  int iIndex = sIdCell.indexOf (",");
  String sAux = sIdCell.substring (iIndex+1);					// Taking the final substring
  return Integer.parseInt (sAux);
  }
  
public int iGetCellType ()
  {return iCellType;}

public int iGetNewCellType ()
  {return iNewCellType;}

public void vSetCellType (int iAux)
  {iCellType = iAux;}

public void vSetNewCellType (int iAux)
  {iNewCellType = iAux;}

public void vUpdateCellType ()
  {iCellType = iNewCellType;}

public int iGetStrategyType ()
  {return iStrategyType;}

public int iGetStrategy ()
{return iStrategy;}

public int iGetAction ()
  {return iAction;}

public int iGetNewAction ()
  {return iNewAction;}

public int iGetColor()
	{return iColor;}

public void vSetColor (int iColorAux)
	{iColor = iColorAux;}

public int iGetAliveNeighbors()
  {return iAliveNeighbors;}

public void vSetAliveNeighbors (int iAliveNeighborsAux)
  {iAliveNeighbors = iAliveNeighborsAux;}

public void vUpdateAction ()
  {iAction = iNewAction;}

public void vSetNewAction (int iAux)
  {iNewAction = iAux;}

public void vSetAction (int iAux)
  {iAction = iAux;}

public void vSetStrategyType (int iStrategyTypeAux)
{iStrategyType = iStrategyTypeAux;}

public void vSetStrategy (int iAux)
{iStrategy = iAux;}

public void vSetNumCooperations (int iNumCoopAux)
{iNumCooperations = iNumCoopAux;}

public int iGetNumCooperations ()
{return iNumCooperations;}

public void vSetNumGamesPlayed (int iNumTurnsAux)
{iNumGamesxGen = iNumTurnsAux;}

public int iGetNumGamesPlayed ()
{return iNumGamesxGen;}



public void vSetPayoff (double dAux)
  {dPayoff = dAux;}


public void vAddPayoffs (double dPayoffAux, double dPayoffNeighborsAux) {
  dPayoff += dPayoffAux;
  dPayoffNeighbors += dPayoffNeighborsAux;
  }



public void vResetCellData () {
	dPayoffTot += dPayoff;
  //if (dTotPayoff < 0) dTotPayoff = 0;

  dPayoff = dPayoffGen - dCostGen;                	// Initial value per generation
  dPayoffNeighbors = 0;
  iNumCooperations = 0;
  iNumGamesxGen = 0;
  iTimes_MO_State = new int[4];
  }




public void vUpdateBufferPayoff () {
  dPayoffBuffer += dPayoff;  	
  
  ovBufferPayoff.addElement (new Double (dPayoff));
  while (ovBufferPayoff.size() > Game.iStatsBufferSize) {	// If there is more, delete the first
    Double oDouble = (Double) ovBufferPayoff.elementAt(0);
    dPayoffBuffer -= oDouble.doubleValue();
    ovBufferPayoff.removeElementAt (0);
    }
}


public double dGetPayoff () {
  if (Game.bStorePayoff)
    return dPayoffTot;
  else
    return dPayoff;
  }

public double dGetPayoffAvgNeighborsSize () {
    return (dPayoff / ovNeighbors.size()) ;
  }

public double dGetPayoffAvgNumGamesxGen () {
  return (dPayoff / iNumGamesxGen) ;
}

public double dGetPayoffBuffer ()
{return dPayoffBuffer;}

public double dGetPayoffBufferAvgNeighborsSize ()
	{return (dPayoffBuffer / ovNeighbors.size());}

public double dGetPrevPayoff () {
  Double oDouble = (Double) ovBufferPayoff.lastElement();
  return oDouble.doubleValue();
}

public double dGetTotPayoff ()
  {return dPayoffTot;}

public double dGetPayoffGen ()
  {return dPayoffGen;}

public void vAddPayoffGen (double dInc)
  {dPayoffGen += dInc;}



      // Social Net
public void vAddNeighbor (Cell oCell)
  {ovNeighbors.add (oCell);}

public void vSetNeighbors (Vector<Cell> oVec)
  {ovNeighbors = oVec;}

public Vector oVGetNeighbors ()
  {return ovNeighbors;}

public int iGetNeighborhoodSize ()
{return ovNeighbors.size();}

public void vSetIdPajek (int iId)
  {iIdPajek = iId;}

public int iGetIdPajek()
  {return iIdPajek;}

public double dGetMaxPayoff()
  {return dMaxPayoff;}

public void vSetMaxPayoff (double dPayMax)
  {dMaxPayoff = dPayMax;}


public boolean bIsNeighbor (Cell oNeighbor) {
  for (int i=0; i<ovNeighbors.size(); i++) {
    Cell oVec = (Cell) ovNeighbors.elementAt(i);
    if (oVec == oNeighbor)
      return true;
  }
  return false;
}

public void vSetCellMinPayoff (Cell oCell) {
  oCellMinPayoff = oCell;
}

public Cell oGetCellMinPayoff() {
  return oCellMinPayoff;
}


public void vCheckNewResource() {	
	if (Math.random() < Game.dProbResource)							// This cell has some resource in this interaction
		bOwnsResource = true;
	else
		bOwnsResource = false;
}


public boolean bOwnsResource() {
	return bOwnsResource;
}







/**
 * This method is used to imitate probabilistically a memetic majority behavior.
 * It is basically a probabilistic TFT adapted to neighborhoods. 
 */
public void vGetNewActionProbTFT() {
 if ( Math.random() < ((double) iNumCooperations / (double) ovNeighbors.size()) )
   iNewAction = 1;		// Cooperation
 else
   iNewAction = 0;		// Defection
 }






/**
  * This method generate brain statistics for this cell.
  * WARNING: It must be called at the end of the iteration (before deciding every new action).
  */
public void vBrainGenerateStats() {
  double dAux;

  if (dPayoff > dGlobalValMax)
    dGlobalValMax = dPayoff;
  if (dPayoff < dGlobalValMin)
    dGlobalValMin = dPayoff;

  dAux = dPayoff + dPayoffNeighbors;
  if (dAux > dAllGlobalValMax)
    dAllGlobalValMax = dAux;
  if (dAux < dAllGlobalValMin)
    dAllGlobalValMin = dAux;

}




/**
 * This method is used to output data to Pajek/Gephi depending on the games
 */
public double dGetOutput4ExternalTool () {
  return dPayoff;
}



}	// from the class Cell


