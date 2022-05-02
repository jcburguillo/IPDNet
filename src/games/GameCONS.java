package games;

/**
  * Contains constant definitions for Game
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public interface GameCONS
{
final int iNULL = -13;				// Value used for comparisons

final int iDEFECT = 0;
final int iCOOPERATE = 1;

final int iOUTPUT_NONE = 0;
final int iOUTPUT_VERBOSE = 1;
final int iOUTPUT_PYTHON = 2;


      // Strategy types
final int iIMITATION = 0;                   // Using basic memetics with the best neighbor
final int iCROSSOVER = 1;										// Evolutive mating by tournament
final int iMETA_STRAT = 2;									// Combining the previous strategies

final int iNOT_BEST = 0;
final int iWORST_THAN_AVG = 1;
final int iWORST_OF_ALL = 2;

final int iDISCRETE = 0;
final int iCONTINUOUS = 1;

final int iIPDNET = 0;
final int iCLUSTERNET = 1;


final String sGAME_TYPE[] =	{
	"0. IPD Net",
	"1. Cluster Net"
};


final String sCHANGE_MODEL[] = {
	"0. Not the Best",
	"1. Worst than Avg.",
	"2. The Worst"
};


final String sCHANGE_TYPE[] = {
		"0. Imitation",													// Imitating the selected neighbor
	 	"1. Crossover",													// Mating with the selected neighbor
	};


final String sPROB_MODEL[] = {
		"0. Discrete",
	 	"1. Continuous",												// Discrete or Continuous probabilities
	};


final String sCOMPLEX_NET[] = {
	"0. Spatial Net",
	"1. Small World Net",
	"2. Scale Free Net",
	"3. Random Net",
	"4. Spatial Small World Net"
};


final String sSHORT_COMPLEX_NET[] = {
	"0. SP",
	"1. SW",
	"2. SF",
	"3. RN",
	"4. SP+SW"
};

}

