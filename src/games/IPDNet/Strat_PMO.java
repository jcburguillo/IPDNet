package games.IPDNet;

import games.GameCONS;
import java.io.Serializable;



/**
 * This class stores the probabilistic memory-one strategies to define the strategy of a Cell, depending on the previous action,
 * and returning the action (C or D) to be played next. It uses the next probabilities:
 *
 *	dProb[0]: Probability of C in the initial round.
 *	dProb[1]: Probability of C after [C,C] also Pcc or also Pr (R: Reward)
 *	dProb[2]: Probability of C after [C,D] also Pcd or also Ps (S: Sucker) 
 *	dProb[3]: Probability of C after [D,C] also Pdc or also Pt (T: Temptation)
 *	dProb[4]: Probability of C after [D,D] also Pcc or also Pp (P: Punishment)
 *
 * Note: the first action in Pxy is the own one (x) and the other is the opponent one (y)
 *
 * @author  Juan C. Burguillo-Rial
 * @version 1.0
 */
public class Strat_PMO implements GameCONS, Serializable
{
public final static int iZD_STRAT = 0;
public final static int iZD_EQUALIZER_STRAT = 1;
public final static int iZD_EXTORTIONER_STRAT = 2;
	
public boolean bClusterStrat = false;			// Describes if this is a strategy to be checked

public double[] dmProb = new double[5];



					// 8 Basic Strategies	{P0,P1,P2,P3,P4}
public static final double[][] dmProbBasicStrats =
								{{0,0,0,0,0},			// All D 			== 0
								 {0,1,0,0,0},			// d.Spiteful == 2
								 {1,1,0,0,0},			// c.Spiteful == 3
								 {0,1,0,1,0},			// d.TFT			== 10   a.k.a.   Mistrust
								 {1,1,0,1,0},			// c.TFT			== 11
								 {0,1,0,0,1},			// d.Pavlov		== 18
								 {1,1,0,0,1},			// c.Pavlov		== 19
								 {1,1,1,1,1}};		// All C			== 31

public static final String[] sBasicStrats =
								{"All D",
								 "d.Spiteful",
								 "c.Spiteful",
								 "d.TFT",
								 "c.TFT",
								 "d.Pavlov",
								 "c.Pavlov",
								 "All C",
								 ""};

public static final String[] sZDStrats =
								{"ZD",
								 "Equalizer",
								 "Extortioner",
								 ""};



/**
  * This is the class constructor creating a random strategy
  */
public Strat_PMO () {
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++)													// For P0...P4
		dmProb[i] = Math.random();
	
	if (GameIPDNet.iProbModel == iDISCRETE)
		dmProb = dmDiscretizeStrat (dmProb, GameIPDNet.dPMOInterval);
}



/**
 * This is the class constructor creating a new basic strategy
 */
public Strat_PMO (int iBasicStrat) {
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++)													// For P0...P4
		dmProb[i] = dmProbBasicStrats[iBasicStrat][i];
}




/**
 * This is the class constructor initializing with a certain set of probs.
	*
	*	@param dProbAux 	The strategy transferred
 */
public Strat_PMO (double[] dProbAux) {
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++)
		dmProb[i] = dProbAux[i];
}



/**
  * This is the class constructor initializing with a certain strategy
	*
	*	@param oStrat_PMO 	The strategy transferred
  */
public Strat_PMO (Strat_PMO oStrat_PMO) {
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++)
		dmProb[i] = oStrat_PMO.dmProb[i];
	
	if (oStrat_PMO.bClusterStrat)
		bClusterStrat = true;
	else
		bClusterStrat = false;
}




public int iGet_Basic_Strat () {
	return iIdentify_Basic_Strat (dmProb);
}


public int iGet_ZD_Strat () {
	return iIdentify_ZD_Strat (dmProb);
}


public double[] dmGet_Prob () {
	return dmProb;
}


public void vSetProbs (double[] dmProbAux) {
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++)
		dmProb[i] = dmProbAux[i];
}




/**
	* This method returns the decimal value of a strategy, converting it to a pure PMO strategy
	*/
public int iGetDecimalDiscreteStrat () {
	int iDecValue=0, iPow=1;
	
	for (int i=0; i<5; i++) {
		iDecValue += (int) (Math.round(dmProb[i]) * iPow);
		iPow *= 2;
	}

	return iDecValue;
}



/**
 	* This method changes to new strategy
	*
	*	@param iStrat 	The index of the new basic strategy
	*/
public void vSetBasicProb (int iStrat) {
		dmProb = dmProbBasicStrats[iStrat];
}





/**
 	* This method discretizes a strategy
	*
	*	@param oStrat_PMO 	The strategy to be discretized
	*/
public static double[] dmDiscretizeStrat (double[] dProbAux, double dPMOIntervalAux) {
	double[] dProbDiscrete = new double[5];
	double dSum, dPrecision = 1e-5;
	
	for (int i=0; i<GameIPDNet.iNumActionProbs; i++) {													// For P0...P4
		dSum = 0;
		dProbDiscrete[i] = -1;

		do {
			if (dProbAux[i] < dSum + 0.5 * dPMOIntervalAux) {
				dProbDiscrete[i] = dSum;
				break;
			}
			dSum += dPMOIntervalAux;
		} while (dSum < 1);
		
		if (dProbDiscrete[i] < 0)												// Not found in the intervals
			dProbDiscrete[i] = 1;		
		else if (dProbDiscrete[i] < dPrecision)					// Very close to zero
			dProbDiscrete[i] = 0;
		else if (dProbDiscrete[i] > (1 - dPrecision))		// Very close to one
			dProbDiscrete[i] = 1;		
	}
	
	return dProbDiscrete;
}






/**
 * Get the closer basic strat from a certain one
 * 
 *	@param	dProbAux		The probabilities of the unidentified strat
 */
public static int iFindCloserBasicStrat (double[] dProbAux, double dPMOIntervalAux) {
	int 		iCloserStrat = 8;
	double 	dAux,
					dNorm,
					dMinDistance = Double.MAX_VALUE,
					dProbDiscrete[] = dmDiscretizeStrat (dProbAux, GameIPDNet.dPMOInterval);
	
	
	for (int j=0; j<dmProbBasicStrats.length; j++) {
		dNorm = 0;
		for (int i=0; i<GameIPDNet.iNumActionProbs; i++) {
			dAux = dProbDiscrete[i] - dmProbBasicStrats[j][i];
			dNorm += dAux*dAux;
		}
		if (dNorm < dMinDistance) {
			dMinDistance = dNorm;
			iCloserStrat = j;
		}
	}
	
	return iCloserStrat;
}






/**
 	* This method identifies if given a certain strategy it belongs to the basic 8 ones
	*
	*	@param dProbAux 	The probabilities of the strategy
	*/
public static int iIdentify_Basic_Strat (double[] dProbAux) {
	boolean bFound;
	int iBasicStratAux = 8;
	double[] dProbDiscrete = dmDiscretizeStrat (dProbAux, GameIPDNet.dPMOInterval);
	
	for (int j=0; j<dmProbBasicStrats.length; j++) {
		bFound = true;
		for (int i=0; i<GameIPDNet.iNumActionProbs; i++)
			if (dProbDiscrete[i] != dmProbBasicStrats[j][i]) {
				bFound = false;
				break;
			}
		
		if (bFound) {
			iBasicStratAux = j;
			break;
		}		
	}
	
	return iBasicStratAux;
}


	
/**
	* This method identifies if given a certain strategy it belongs to any of the 3 ZD ones.
	* This numbers are only valid for the classic matrix payoff values: 5, 3, 1, 0
	*
	*	@param dProbAux 	The probabilities of the strategy
	*/
public static int iIdentify_ZD_Strat (double[] dProbAux) {
	int iZDStratAux = 3;		// Not a ZD strat
	double dA, dB, dC;
	double[] dProbDiscrete = dmDiscretizeStrat (dProbAux, GameIPDNet.dPMOInterval);
	
	dA = dProbDiscrete[2]/15.0 + 4.0*dProbDiscrete[3]/15.0 - dProbDiscrete[4]/3.0 - 1.0/15.0;
	dB = 4.0 * dProbDiscrete[2]/15.0 + dProbDiscrete[3]/15.0 - dProbDiscrete[4]/3.0 - 4.0/15.0;
	dC = -dProbDiscrete[2]/3.0 - dProbDiscrete[3]/3.0 + 5.0*dProbDiscrete[4]/3.0 + 1.0/3.0;
	
	if (dProbDiscrete[1] == (1.0 + 3.0*dA + 3.0*dB + dC))		// ZD
		iZDStratAux = iZD_STRAT;
	if ( (dA == 0) && (dB != 0) )												// Equalizer
		iZDStratAux = iZD_EQUALIZER_STRAT;
	if (dA + dB + dC == 0)															// Extortioner
		iZDStratAux = iZD_EXTORTIONER_STRAT;

	return iZDStratAux;
}



/**
* This method gets an action from the strategy stored, given a certain memory of the last actions (PMO)
*
*	@param sMemory 	The actions last performed by this cell (1st) and the mate cell (2nd).
*/
public int iGetAction (String sMemory) {
	int iAction=iDEFECT;
	int iIndex=0;

	if (sMemory.equals(""))
		iIndex = 0;
	else if (sMemory.equals("CC"))
		iIndex = 1;
	else if (sMemory.equals("CD"))
		iIndex = 2;
	else if (sMemory.equals("DC"))
		iIndex = 3;
	else if (sMemory.equals("DD"))
		iIndex = 4;
	else
		System.out.println("Error StratPM_1.iGetAction(String sMemory="+sMemory+")");

	if (Math.random() < dmProb[iIndex])
		iAction = iCOOPERATE;
	
	return iAction;
}

}		// from class StratPM_1 implements GameCONS






