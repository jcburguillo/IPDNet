
import java.util.Locale;
import java.util.Vector;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import window.MainWindow;
import file.IOFile;
import games.*;
import games.IPDNet.GameIPDNet;




/**
 * IPD on Networks in Batch Mode
 *
 * @author  Juan C. Burguillo Rial
 * @version 1.0
 */
public class bIPDNet implements GameCONS {
public Game oGame;


public static void main (String args[]) {
	
	bIPDNet oLauncher = new bIPDNet(); 
  
  Game.bBatchMode = true;
  
	oLauncher.oGame = new GameIPDNet ();
  //Game.iGameType = iIPDNET;
	Game.iGameType = iCLUSTERNET;
  
  MainWindow.oGame = oLauncher.oGame;
  oLauncher.vBatchRun (args);
  }




/**
 * This method contains the code to be executed in batch mode for the GameReputation
 */
private void vBatchRun (String[] args) {
	int iCounter=0;									// Counter to produce txt files to be processed by Python
	int iMaxCounter = 100;
  int iTimeHour, iTimeMin, iTimeSec;
  int iNumRuns = 100;							// Number of executions to average
  
  double dAux, dTimeINI, dTimeEND;
  double dAvgGlobalProfit = 0;
  double dAvgGen = 0;
  
  int[][] imGen = new int [iMaxCounter][iNumRuns];
  
  double[] dmAvgExplore = new double[Game.iNumActionProbs];
  double[] dmVarExplore = new double[Game.iNumActionProbs];
  double[] dmStdDevExplore = new double [Game.iNumActionProbs];
  double[] dTotAvgExplore = new double [Game.iNumActionProbs];
  double[][] dmProbAvgExplore = new double [iNumRuns][Game.iNumActionProbs];
    
  double[] dAvgGenPython = new double [iMaxCounter];
  double[] dAvgGlobalProfitxAgentxPython = new double [iMaxCounter];
  double[][] dmProbAvgResult4Python = new double [iMaxCounter][Game.iNumActionProbs];		// Up to iMaxCounter
  double[][] dmProbStdResult4Python = new double [iMaxCounter][Game.iNumActionProbs];		// Up to iMaxCounter
  
  
  
//-------------------------------- INI: BATCH IPDNet ---------------------------------------
  
  
  
  //MainWindow.iOutputMode = iOUTPUT_VERBOSE;
  MainWindow.iOutputMode = iOUTPUT_PYTHON;

  //Game.iGameType = iIPDNET;
  Game.iGameType = iCLUSTERNET;
  //Game.iChangeType = iCROSSOVER;
  Game.iChangeType = iIMITATION;
	GameIPDNet.iProbModel = iCONTINUOUS;
  GameIPDNet.dPMOInterval = 0.001;
	//GameIPDNet.iProbModel = iDISCRETE;
  //GameIPDNet.dPMOInterval = 1.0;

  //Game.dSpatialRadio = 1;
  //Game.dAvgNeighborhoodSize = 4;
  Game.dSpatialRadio = 1.5;
  Game.dAvgNeighborhoodSize = 8;
  
  GameIPDNet.iDiff_PMOsBuffer2Stop = 5;											// Stopping criterion in phase standard
  GameIPDNet.dRatio4ClusterDomination = 0.95;									// Stopping criterion for the cluster (domination)
  
  Game.iTotPosMatrix = 40000;					// First parameter gives the initial number of cells in the matrix
  //Game.iTotPosMatrix = 10000;					// First parameter gives the initial number of cells in the matrix
  if (args.length > 0)
  	Game.iTotPosMatrix = Integer.parseInt(args[0]);
  
  iNumRuns = 10;											// Second parameter provides the maximum number of runs
  if (args.length > 1)
  	iNumRuns = Integer.parseInt(args[1]);
  
  MainWindow.iGenStop = 10000;
  //MainWindow.iGenStop = 5000;
  if (args.length > 2)								// Third parameter provides the maximum number of gens. to stop
  	MainWindow.iGenStop = Integer.parseInt(args[2]);
  
  
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  PRINTING HEADER  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  System.out.println ("\n\n\n\nGame: " + sGAME_TYPE[GameIPDNet.iGameType] + "\t\t\tAgents: " + GameIPDNet.iTotPosMatrix +
  										"\t\t\t\tGamesxEncounter:" + GameIPDNet.iNumGamesxEncounter);
  System.out.println ("Gen: " + GameIPDNet.iNumGen + "\t\t\t\t\tNetType: " + sSHORT_COMPLEX_NET[GameIPDNet.iNetType] +
  		 							 "\t\t\t\tAvg. Neighborhood: " + String.format (Locale.ENGLISH, "%.2f", GameIPDNet.dAvgNeighbors)); 
  System.out.println ("ChangeType: " + sCHANGE_TYPE[GameIPDNet.iChangeType] + "  \t\tChangeModel: " +
  		 							  sCHANGE_MODEL[GameIPDNet.iChangeModel] + "\t\tProbModel: "+ sPROB_MODEL[GameIPDNet.iProbModel]);
  										
  System.out.println  ("Pn:" + GameIPDNet.dProbNoise + "   Pe:" + GameIPDNet.dProbEnding + "   Pm:" + GameIPDNet.dProbMutation +
  										"   Pca:" + String.format (Locale.ENGLISH, "%.2f", GameIPDNet.dProbChangeAction) +
  										"   Psr:" + String.format (Locale.ENGLISH, "%.2f", GameIPDNet.dProbSmallWorld) +
  									 	"   OwnWeight:" + GameIPDNet.dOwnWeight + "   Ipmo:" + GameIPDNet.dPMOInterval +
  									 	"   Radio:" + Game.dSpatialRadio + "   MateSize:" + Game.dAvgNeighborhoodSize);

  
  
  System.out.println ("\n\n >>> bIPDNet: STARTING THE BATCH ("+ iNumRuns + " RUNS) !!!        iGenStop: " + MainWindow.iGenStop);
  
  dTimeINI = System.currentTimeMillis();
  
  
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: LOOPs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  //for (int iNet=0; iNet<=3; iNet++) {     	// Networks: 0->SP	1->SW	 2->SF	3->RN (OJO: check dAvgNeighborhoodSize loop)
  for (int iNet=0; iNet<=0; iNet++) {     		// Networks: 0->SP	1->SW	 2->SF	3->RN (OJO: check dAvgNeighborhoodSize loop)
		Game.iNetType = iNet;
		//if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
		  System.out.print ("\n >>> Net: " + sCOMPLEX_NET[iNet]);

  //for (double dPrew=0.0; dPrew<=1.0; dPrew+=0.05) {     			// Rewiring: 1 YES, 0 NO
  for (double dPrew=0; dPrew<=0; dPrew+=1) {     						// Rewiring: 1 YES, 0 NO
  	Game.dProbRewiring = dPrew;
  	//Game.dProbSmallWorld = dPrew;
    //if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
    	//System.out.print ("\n dProbRewire: " + Game.dProbRewiring);
    	//System.out.print ("\n >>> dProbSmallWorld: " + Game.dProbSmallWorld);

  for (double dNSize=1.0; dNSize<=1.0; dNSize+=0.5) {     	// Spatial radio
  //for (double dNSize=4.0; dNSize<=4.0; dNSize*=2.0) {     // Social neighborhood size
  	//Game.dSpatialRadio = dNSize;
  	//Game.dAvgNeighborhoodSize = dNSize;
  	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
  	  System.out.print ("\n >>> dNSize: " + dNSize);
    
  //for (double dPm=0.005; dPm<1; dPm*=2.0) {     					// Mutation probability
  for (double dPm=0.01; dPm<=0.01; dPm+=0.1) {     				// Mutation probability
  	Game.dProbMutation = dPm;
  	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
  	  System.out.print ("\n >>> dProbMutation: " + String.format (Locale.ENGLISH, "%.3f", Game.dProbMutation));
  	  	  	
  //for (double dPe=0.0; dPe<=0.1; dPe+=0.01) {     					// Ending probability
  for (double dPe=0.1; dPe<=0.1; dPe+=0.1) {     					// Ending probability
  	GameIPDNet.dProbEnding = dPe;
  	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
  	  System.out.print ("\n >>> dProbEnding: " + String.format (Locale.ENGLISH, "%.3f", GameIPDNet.dProbEnding));
  	
  //for (double dPn=0.0; dPn<=0.2; dPn+=0.05) {     				// Noise probability
  for (double dPn=0.05; dPn<=0.05; dPn+=0.05) {     						// Noise probability
  	Game.dProbNoise = dPn;
  	//if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
  	  System.out.print ("\n >>> dProbNoise: " + String.format (Locale.ENGLISH, "%.3f", Game.dProbNoise));

  	
  	
  	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  	
	
  	// ---------------------------- Reseting the statistics ----------------------------
  
  dAvgGen = 0;
  dAvgGlobalProfit = 0;
  imGen = new int [iMaxCounter][iNumRuns];
  dmAvgExplore = new double [Game.iNumActionProbs];
  dmVarExplore = new double [Game.iNumActionProbs];
  dmStdDevExplore = new double [Game.iNumActionProbs];
  dTotAvgExplore = new double [Game.iNumActionProbs];
  dmProbAvgExplore = new double [iNumRuns][Game.iNumActionProbs];
      
  System.out.println("\n");
  
  
  for (int i=0; i<iNumRuns; i++) {     // Number of runs
    MainWindow.oGame.vNewGame();

    
    while (true) {
      MainWindow.oGame.vRunLoop();
      
      						// STOPPING CRITERIUM      	
      if (Game.iNumGen % MainWindow.iGenStopTMP == 0) {

      	String sString = "\n[Run: " + i + ", iGen: " + Game.iNumGen + "]: ";
    		for (int j=0; j<Game.iNumActionProbs; j++)
    			sString += "\tP("+j+")=" + Game.imProbs[j] + "%(" + (char) 177 + Game.imStDev[j]/2 +"%)";
    		
    		System.out.print (sString);
	    	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE)
	    		oGame.vPrintData ();

    		for (int y=0; y<Game.iCellV; y++)								// Even this is provided by Game.imProbs[], we use doubles for the final avgs.
		    for (int x=0; x<Game.iCellH; x++)
		    	for (int k=0; k<Game.iNumActionProbs; k++)		// Here we consider the 5 probabilities per agent: P0, P1, P2, P3, P4
		    		dmProbAvgExplore[i][k] += GameIPDNet.omStratPMO[x][y].dmProb[k];

	    	for (int k=0; k<Game.iNumActionProbs; k++) {		// Here we consider the 5 probabilities per agent: P0, P1, P2, P3, P4
    			dmProbAvgExplore[i][k] /= Game.iTotNumCells;
    			dTotAvgExplore[k] += dmProbAvgExplore[i][k];
	    	}
	    	
	    	if (Game.iGameType == iCLUSTERNET)
	    		imGen[iCounter][i] = Game.iNumGen - GameIPDNet.iGenSimStable;	    		
	    	else
	    		imGen[iCounter][i] += Game.iNumGen;

      	dAvgGen += imGen[iCounter][i];

	    	break;
      }

    }		// from while loop

  }   // for (int i=0; i<iNumRuns; i++)       // Iterations
  
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  END: RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  
  
  
  
  
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: CALCULATE STATISTICS AFTER ALL RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  dAvgGen = dAvgGen / iNumRuns;
  dAvgGlobalProfit = dAvgGlobalProfit / iNumRuns;
  
  for (int i=0; i<Game.iNumActionProbs; i++)
  	dmAvgExplore[i] = dTotAvgExplore[i] / iNumRuns;		// Average values per probability

  
  for (int i=0; i<Game.iNumActionProbs; i++) {
  	dmVarExplore[i] = 0;
  	for (int j=0; j<iNumRuns; j++) {
  		dAux = (double) (dmProbAvgExplore[j][i]) - dmAvgExplore[i];
  		dmVarExplore[i] += dAux * dAux;
  	}
  	
  	dmVarExplore[i] = dmVarExplore[i] / iNumRuns;
    dmStdDevExplore[i] = Math.sqrt (dmVarExplore[i]);																// standard deviation in exploration
  }

  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  END: CALCULATE STATISTICS AFTER ALL RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  
  

  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: PRINT AFTER RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  if (MainWindow.iOutputMode == iOUTPUT_VERBOSE) {
		System.out.println ("\n\n >>> Results after " + iNumRuns + " executions !!     dAvgGen: " + 
												String.format (Locale.ENGLISH, "%.2f", dAvgGen) +
												"     dAvgProfitxAgent: " + String.format (Locale.ENGLISH, "%.2f", GameIPDNet.dAvgProfitxAgent));
		
		
		System.out.println ("\n\nProbs(StdDev) after all runs \n----------------------------");
		for (int i=0; i<Game.iNumActionProbs; i++) {
			if (i > 0) System.out.print (", ");
		  System.out.print (String.format (Locale.ENGLISH, "%.2f", dmAvgExplore[i]) +
		  									"(" + String.format (Locale.ENGLISH, "%.2f", dmStdDevExplore[i]) + ")");
		}		
		
	  dTimeEND = System.currentTimeMillis();
	  iTimeMin = 0;
	  iTimeSec = (int) ((dTimeEND - dTimeINI) / 1000);
	  if (iTimeSec > 60) {
	  	iTimeMin = iTimeSec / 60;
	  	iTimeSec = iTimeSec - iTimeMin * 60;
	  }
	  
	  System.out.println ("\n\n\n--> TIME SINCE STARTING THE SIM: (" + iTimeMin + " mins, " + iTimeSec +" secs.) !!!\n\n");
		
  }
    
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  END: PRINT AFTER RUNs  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  

  			// Storing the results after a set of runs to produce the final Python table when changing a parameter
	for (int i=0; i<Game.iNumActionProbs; i++) {
		dmProbAvgResult4Python [iCounter][i] = dmAvgExplore[i];
		dmProbStdResult4Python [iCounter][i] = dmStdDevExplore[i];
	}
	dAvgGenPython [iCounter] = dAvgGen;
	dAvgGlobalProfitxAgentxPython [iCounter] = GameIPDNet.dAvgProfitxAgent;
	
	iCounter++;
	if (iCounter == iMaxCounter) {
		System.out.println ("\n ERROR: iCounter == iMaxCounter ");
		System.exit(-1);
	}
  
	
	
	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: CLOSING LOOPS  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  System.out.println("\n");
  }		//	for (double dPn=0.00; dPn<=0.00; dPn+=0.05) { 					// Noise probability
  System.out.println();
  }		//	for (double dPe=0.0; dPe<=1.0; dPe+=0.1) {     					// Ending probability
  System.out.println();
  }		//	for (double dPm=0.0; dPm<=1.0; dPm+=0.1) {     					// Mutation probability
  System.out.println("\n");
  }		//	  for (double dNSize=1.0; dNSize<1.1; dNSize+=1.0) {  	// Spatial radio
  System.out.println();
  }		//	for (double dPr=0; dPr<=0; dPr+=1) {     								// Rewiring probability
  System.out.println();
  }		//	for (int iNet=0; iNet<=3; iNet++) {											// Networks: 0->SP	1->SW	2->SF	3->RN

	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  END: CLOSING LOOPS  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  
  
  
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  INI: PRINT FINAL SUMMARY FOR PYTHON  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  

	System.out.println ("\n\n\n### Final Results for Python ###");
	
	System.out.print ("\n Probability results: each line P0...P4 over the x-axis parameter:");
	for (int i=0; i<Game.iNumActionProbs; i++) {
		System.out.println ();
		for (int j=0; j<iCounter; j++) {
		  System.out.print (String.format (Locale.ENGLISH, "%.2f", dmProbAvgResult4Python [j][i]));
		  if (j < iCounter - 1)
		  	System.out.print (", ");
		}
	}

	
	System.out.print ("\n\n\n Std. results: each line P0...P4 over the x-axis parameter:");
	for (int i=0; i<Game.iNumActionProbs; i++) {
		System.out.println ();
		for (int j=0; j<iCounter; j++) {
		  System.out.print (String.format (Locale.ENGLISH, "%.2f", dmProbStdResult4Python [j][i]));
		  if (j < iCounter - 1)
		  	System.out.print (", ");
		}
	}

	
	System.out.println ("\n\n\n Generations:");
	for (int j=0; j<iCounter; j++) {
		System.out.println ();
		for (int i=0; i<iNumRuns; i++) {
		  System.out.print ("" + imGen[j][i]);
		  if (i < iNumRuns - 1)
		  	System.out.print (" & ");
		}
	}
		

	System.out.println ("\n\n\n Avg. generations:");
	for (int j=0; j<iCounter; j++) {
	  System.out.print (String.format (Locale.ENGLISH, "%.2f", dAvgGenPython [j]));
	  if (j < iCounter - 1)
	  	System.out.print (", ");
	}
	
	
	System.out.println ("\n\n\n Avg. profit per agent:");
	for (int j=0; j<iCounter; j++) {
	  System.out.print (String.format (Locale.ENGLISH, "%.2f", dAvgGlobalProfitxAgentxPython [j]));
	  if (j < iCounter - 1)
	  	System.out.print (", ");
	}
    
  // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  END: PRINT FINAL SUMMARY FOR PYTHON  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

  
  
  
  
  dTimeEND = System.currentTimeMillis();
  iTimeHour = 0;
  iTimeMin = 0;
  iTimeSec = (int) ((dTimeEND - dTimeINI) / 1000);
  if (iTimeSec > 60) {
  	iTimeMin = iTimeSec / 60;
  	iTimeSec = iTimeSec - iTimeMin * 60;
  }
  if (iTimeMin > 60) {
  	iTimeHour = iTimeMin / 60;
  	iTimeMin = iTimeMin - iTimeHour * 60;
  }
  
  System.out.println ("\n\n\n--> END OF THE BATCH EXECUTION. DURATION: " + iTimeHour + " hours, " + iTimeMin + " mins, " + iTimeSec +" secs !!!\n");

/*  
  try {
      Clip oSound = AudioSystem.getClip();
      oSound.open (AudioSystem.getAudioInputStream (new File("./sound/cuckoo.wav")));
      oSound.start();
      while (oSound.isRunning())
        Thread.sleep(1000);
      oSound.close();
  } catch (Exception e) {
      System.out.println ("" + e);
  }
*/  

  

}   // private void vBatchRun (String[] args) {


//-------------------------------- END: BATCH IPDNet ---------------------------------------


}	// public class bIPDNet implements GameCons