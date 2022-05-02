package machineLearning;

import java.io.Serializable;
import java.util.Vector;
import games.Game;
import games.IPDNet.Strat_PMO;


/**
  * This is the basic class to store the values and actions for a certain state
  *
  * @author  Juan C. Burguillo Rial
  * @version 3.0
  */
public class StratsxState implements Serializable
{
private int iTimesState = 0;
private int iSuccessTimes = 0;
private String sState;																								// The state as a string with SOM x,y values
private int iNumStrats = 8;																						// Present actions (PMO_strats) stored at this state
private int[] imSuccessTimesStrat = new int[Brain.iMaxNumStrats];			// Number of times this strat has been successful
private int[] imTimesStrat = new int[Brain.iMaxNumStrats];						// Number of times this strat has been used
private double dLearnRate = Brain.dINILearnRate;											// Learning rate: present value
public static final double dDecFactor = 0.95;	  	  									// To multiply LearnRate to reduce its value
private double[] dmValueStrat = new double[Brain.iMaxNumStrats];			// Stores Values per action
private double[][] dmStrat = new double[Brain.iMaxNumStrats][Game.iNumActionProbs];			// Prob. strat. per strat



StratsxState (String sAuxState) {
  sState = sAuxState;
  
  vResetValues();
}



public void vResetValues() {
	dLearnRate = Brain.dINILearnRate;
	iTimesState = 0;
	iSuccessTimes = 0;
	iNumStrats = 8;
	imTimesStrat = new int[Brain.iMaxNumStrats];
	for (int i=0; i<=5; i++) {
  	dmValueStrat[i] = 1;																			// Initial 6 basic strategies (Spiteful, TFT, Pavlov) with value 1
  	for (int j=0; j<Game.iNumActionProbs; j++)								// 5 PMO probs
  		dmStrat[i][j] = Strat_PMO.dmProbBasicStrats[i+1][j];		// The 6 basic strats
  }
	
	dmValueStrat[6] = 1;																				// All-D in 6 with value 1
	for (int j=0; j<Game.iNumActionProbs; j++)									// 5 PMO probs
		dmStrat[6][j] = 0;																				// All-D
	dmValueStrat[7] = 1;																				// All-C in 7 with value 1
	for (int j=0; j<Game.iNumActionProbs; j++)									// 5 PMO probs
		dmStrat[7][j] = 1;																				// All-C
}



public void vResetLearningRate() {
	dLearnRate = Brain.dINILearnRate;
}


public String sGetState() {
  return sState;
}


public double dGetLearningRate() {
  return dLearnRate;
}


public int iGetNumStrats () {
  return iNumStrats;
}


public double dGetValStrat (int iStratPMO) {
  return dmValueStrat[iStratPMO];
}


public double[] dmGetValStrats () {
	return dmValueStrat.clone();
}

public int iGetTimesState() {
	return iTimesState;
}

public int iGetSuccessTimes() {
	return iSuccessTimes;
}

public double[] dmGetStrat (int iStrat) {
	return dmStrat[iStrat].clone();
}

public int[] imGetTimesStrat() {
	return imTimesStrat;
}

public int[] imGetSuccessTimesStrat() {
	return imSuccessTimesStrat;
}



public int iGetStratMaxVal () {
	int iMaxValAction = -1;
	double dMaxVal = -Double.MAX_VALUE;
	
	for (int i=0; i<iNumStrats; i++)
		if (dMaxVal <= dmValueStrat[i]) {
			if ( (dMaxVal == dmValueStrat[i]) && (Math.random() < 0.5) )		// Randomly selecting among equals
				continue;
			dMaxVal = dmValueStrat[i];
			iMaxValAction = i;
		}
		
  return iMaxValAction;
}




public double dGetMaxVal () {
	double dMaxVal = -Double.MAX_VALUE;
	
	for (int i=0; i<iNumStrats; i++)
		if (dMaxVal < dmValueStrat[i])
			dMaxVal = dmValueStrat[i];
		
  return dMaxVal;
}



public double dGetMinVal () {
	double dMinVal = Double.MAX_VALUE;
	
	for (int i=0; i<Brain.iMaxNumStrats; i++)
		if (dmValueStrat[i] < dMinVal)
			dMinVal = dmValueStrat[i];
		
  return dMinVal;
}



public double dGetTotVal () {
	double dTotVal=0;
	for (int i=0; i<iNumStrats; i++)
		dTotVal += dmValueStrat[i];
  return dTotVal;
}






public void vUpdateValStrat (int iStrat, boolean bSuccessful) {
  iTimesState++;
  imTimesStrat[iStrat]++;
  
	if (bSuccessful) {
		iSuccessTimes++;
		imSuccessTimesStrat[iStrat]++;
	}

	dmValueStrat[iStrat] = (0.5 + 1.4 * Math.log (1.0 + (double) imTimesStrat[iStrat]) * ((double) imSuccessTimesStrat[iStrat]) )
													/ (double) imTimesStrat[iStrat];
}





public int iSetNewProbs (double[] dmProbs) {
	boolean bEqual;
	int iPos = 0;
	double dLowestValue = Double.MAX_VALUE;
	
	for (int i=0; i<iNumStrats; i++) {
		bEqual = true;
		for (int j=0; j<Game.iNumActionProbs; j++)
			if (dmStrat[i][j] != dmProbs[j]) {					// Checking if we already have it
				bEqual = false;
				break;
			}
		if (bEqual) return i;													// We have found such strat, so we return its index
	}
 	

	if (iNumStrats < Brain.iMaxNumStrats) {					// Still room for new strats
		iPos = iNumStrats;
		iNumStrats++;
	}
	
	else
		for (int i=6; i<iNumStrats; i++) 							// Otherwise, we identify the last-4 strat with the lowest Value
			if (dmValueStrat[i] <= dLowestValue) {
				dLowestValue = dmValueStrat[i];							
				iPos = i;																	// Position with the lowest value
			}
	
	imTimesStrat[iPos] = 0;													// We reset the times we use it
	imSuccessTimesStrat[iPos] = 0;									// We reset the successful times used
	dmValueStrat[iPos] = 2.0;												// New strat values are set initially to 2.0 to promote its initial selection							
	for (int i=0; i<Game.iNumActionProbs; i++)
		dmStrat[iPos][i] = dmProbs[i];
	
	return iPos;
}


}		// from class ActionsxState
