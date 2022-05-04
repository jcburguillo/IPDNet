package machineLearning;

import java.util.HashMap;
import java.util.Locale;

import games.GameCONS;
import games.IPDNet.GameIPDNet;
import games.IPDNet.Strat_PMO;
import window.MainWindow;


/**
  * This is the basic class to use several machine learning algorithms (statistics, LA, QL, etc.).
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class Brain implements GameCONS
{
final static int iMaxNumStrats = 10;													// Max. Num. strats per state, initially 8 basic strats
private int iLastStrat = -1;																	// Last action taken
private static int iSuccessTimes = 0;																// Success obtained
private static int iTimes = 0;																				// Times tried


//--------------------------------- INI: SOM --------------------------
private static SOM oSOM;                         							// SOM Grid
//--------------------------------- END: SOM --------------------------
//--------------------------------- INI: LA --------------------------
private static HashMap<String,StratsxState> oHashMapStratsxState;
//--------------------------------- END: LA --------------------------

private StratsxState oLastStratsxState=null, oPresentStratsxState=null;      						// LA states per SOM


//BEGIN ------------------ Learning ----------------------
public static final double dINILearnRate = 1.0;	  			// Learning rate: initial value
public static final double dMINLearnRate = 0.01;				// Min. value allowed for LearnRate
public static final double dEpsilonGreedy = 0.95;				// Epsilon value for using e-greedy
//END -------------------- Learning ----------------------



public Brain (int iSamples, int iInputSize) {
	int iGridSide = (int) Math.sqrt (5.0 * Math.sqrt ( (double) iSamples) );
	oSOM = new SOM (iGridSide, iInputSize);
	oHashMapStratsxState = new HashMap<String,StratsxState>();
}




public void vResetMemory () {
  oLastStratsxState = null;
  iLastStrat = -1;
}


public void vRestartValues_SOM_LA () {
	iSuccessTimes = 0;
	iTimes = 0;
	oSOM.vResetValues();	
	for (HashMap.Entry<String,StratsxState> oItemStratPMOxState : oHashMapStratsxState.entrySet()) {
		StratsxState oStratPMOxState = oItemStratPMOxState.getValue();
		oStratPMOxState.vResetValues();
	}
}


public StratsxState oGetLastActionsxState () {
  return oLastStratsxState;
}



/**
 * This method uses a SOM to identify the state and then Quality Learning (QL) to decide the action:
 *
 * @param dmInput contains a double vector with the present input state
 * @param iReward tells if we must reinforce a certain action (PMO strat) for that state
 * @param bTrain indicates if we are still updating the SOM states (training phase) 
 */
public double[] dmGetNewStrat_State_SOM_RL (double[] dmInput, double[] dmNewStrat, int iReward, boolean bTrain) {
	String sBMU = oSOM.sGetBMU (dmInput, bTrain);
	
  return dmGetNewStrat_State_RL (sBMU, dmNewStrat, iReward);
}






/**
  * This method is used to implement a Reinforcement Learning model (RL):
  *
  * @param sBMU contains the present SOM BMU as a String
  * @param dmNewStrat contains the new strat suggested: the previous or a new one to include among the actions (if not already in).
  * @param iReward tells if we must reinforce the last strat (so keeping it)
  */
public double[] dmGetNewStrat_State_RL (String sBMU, double[] dmNewStrat, int iReward) {
  int iNewStrat = -1;
 
  iTimes++;
  
  if (oHashMapStratsxState.containsKey (sBMU)) {								// Searching if we already have the SOM state
    oPresentStratsxState = oHashMapStratsxState.get (sBMU);
  } else {    																									// If not we add it
    oPresentStratsxState = new StratsxState (sBMU);
    oHashMapStratsxState.put (sBMU, oPresentStratsxState);
  }
  
  
  
  if (iReward > 0) {																						// Successful strat: reinforcing and keeping it
	  if (oLastStratsxState != null)															// Reinforcing last strat
	  	oLastStratsxState.vUpdateValStrat (iLastStrat, true);
	  
		if (oPresentStratsxState == oLastStratsxState)
			iNewStrat = iLastStrat;
		else
			iNewStrat = oPresentStratsxState.iSetNewProbs (dmNewStrat);
		
		iSuccessTimes++;
  }
  
  
  
  else if (iReward < 0) {												// Previous non successful strat, selecting a new one
  	
  	if (oLastStratsxState != null)
  		oLastStratsxState.vUpdateValStrat (iLastStrat, false);

  	
  	if (Math.random() < 0.25)										// With 25% prob. we include and select a new border strat
  		iNewStrat = oPresentStratsxState.iSetNewProbs (dmNewStrat);
  	
  	else {																			// With 75% uses an already stored strat
		  double[] dValStratPMO = oPresentStratsxState.dmGetValStrats();
		  double dTotVal = oPresentStratsxState.dGetTotVal();
		  do {																			// Avoiding to repeat the same wrong strat in the same state
			  double dValRandom = Math.random() * dTotVal;
			  double dValStrats = 0;
			  for (int i=0; i<oPresentStratsxState.iGetNumStrats(); i++) {
					dValStrats += dValStratPMO[i];
					if (dValRandom <= dValStrats) {
					  iNewStrat = i;
					  break;
					}
			  }
		  }	while ( (oPresentStratsxState == oLastStratsxState) && (iNewStrat == iLastStrat) );

	  	dmNewStrat = oPresentStratsxState.dmGetStrat (iNewStrat);
  	}  		
  }
  
    
  else {																				// If (iReward == 0) we keep the strat, without updating the previous state
  	if (oLastStratsxState != null)
  		dmNewStrat = oLastStratsxState.dmGetStrat (iLastStrat);

  	iNewStrat = oPresentStratsxState.iSetNewProbs (dmNewStrat);
  }
  
  
  oLastStratsxState = oPresentStratsxState;
  iLastStrat = iNewStrat;
  
  
  if (MainWindow.iOutputMode == iOUTPUT_VERBOSE) {
	  String sString2Print = sPrint_RL_StratsxState (oLastStratsxState, iNewStrat);
	  sString2Print += "\n---> Strat selected: " + iNewStrat;
		System.out.print (sString2Print);
  }	
	
  return dmNewStrat;
}






/**
 * This method is used to get information about a certain SOM neuron:
 *
 * @param x the x position in the SOM grid
 * @param y the y position in the SOM grid
 * @return  a String with the information requested
 */

public static String sPrintInfoBrain (int x, int y) {
	String sBMU = "" + x + "," + y;
	
	String sReturn = "\n-------------------- SOM Neuron [" + x + "," + y + "] -------------------\nWeigths:   ";
	double[] dmWeights = oSOM.dGetNeuronWeights (x, y);
	for (int i=0; i<dmWeights.length; i++)
		sReturn += "   " + String.format (Locale.ENGLISH, "%.3f", dmWeights[i]);
	sReturn += "\nBMU times: " + oSOM.iNumTimesBMU[x][y];
	
	if (oHashMapStratsxState.containsKey (sBMU)) {
		StratsxState oStratsxState = oHashMapStratsxState.get (sBMU);
		sReturn += sPrint_RL_StratsxState (oStratsxState, -1);
	}
	
	return sReturn;
}



public static String sPrint_RL_StratsxState (StratsxState oStratxState, int iNewStrat) {
	String sReturn;
	int[] imSuccessTimesStratPMO = oStratxState.imGetSuccessTimesStrat();
	int[] imTimesStratPMO = oStratxState.imGetTimesStrat();
	double[] dmValStratPMO = oStratxState.dmGetValStrats();
	
	sReturn = "\n\n-------------------- LA --------------------";
	sReturn += "\nNeuron: [" + oStratxState.sGetState() + "]   TotVal: " +
			String.format (Locale.ENGLISH, "%.2f", oStratxState.dGetTotVal()) +
			"   iNumStratsPMO: " + oStratxState.iGetNumStrats() +
			"   Success/Times: (" + oStratxState.iGetSuccessTimes() + "/" +  oStratxState.iGetTimesState() + ")";
	
	for (int i=0; i<dmValStratPMO.length; i++) {
		sReturn += "\nStrat: " + i + "   Value: " +
	  									String.format (Locale.ENGLISH, "%.3f", dmValStratPMO[i]) + "   PMOs: ";
	  double[] dmStratPMO  = oStratxState.dmGetStrat (i);
	  for (int j=0; j<GameIPDNet.iNumActionProbs; j++) 
	  	sReturn += "  " + dmStratPMO[j];
	  
	  sReturn += "   State Success/Times: (" + imSuccessTimesStratPMO[i] + "/" + imTimesStratPMO[i] + ")";
	  if (i == iNewStrat)
	  	sReturn += " <";
	  if (i < 6)
	  	sReturn += "\t" + Strat_PMO.sBasicStrats[i+1];
	}
	
	sReturn += "\nTOTAL Success/Times: (" + iSuccessTimes + "/" + iTimes + ") = " + 100*iSuccessTimes/iTimes + "%";
	
	return sReturn;
}


}	// from the class Brain


