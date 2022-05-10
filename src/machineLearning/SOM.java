package machineLearning;

import java.util.Locale;

import games.Game;
import games.GameCONS;
import games.IPDNet.GameIPDNet;
import window.MainWindow;



/**
  * This class provides a SOM neural network
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class SOM implements GameCONS
{
	// WorldGrid.java, DlgInfoBrain.java
public static int iGridSide;												// Side of the SOM 2D grid
public static int iCellSize;												// Size in pixels of a SOM neuron in the grid
public static int[][] iNumTimesBMU;									// Number of times a cell has been a BMU
public static int[] iBMU_Pos = new int[2];					// BMU position in the grid

private int iInputSize;															// Size of the input vector
private int iRadio;																	// BMU radio to modify neurons
private double dLearnRate = Brain.dINILearnRate;		// Learning rate for this SOM
private double[] dBMU_Vector = null;								// BMU state
private double[][][] dGrid;													// SOM square grid + vector state per neuron



/**
 * This is the class constructor that creates the 2D SOM grid
 * 
 * @param iSideAux 	the square side
 * @param iInputSizeAux		the dimensions for the input data
 * 
 */
public SOM (int iSideAux, int iInputSizeAux) {
	iInputSize = iInputSizeAux;
	iGridSide = iSideAux;
	iCellSize = MainWindow.iMapSize / iGridSide;
	iRadio = iGridSide / 10;
	dBMU_Vector = new double[iInputSize];
	dGrid = new double [iGridSide][iGridSide][iInputSize];
	iNumTimesBMU = new int[iGridSide][iGridSide];
	
	vResetValues();
}




public void vResetValues() {
	dLearnRate = Brain.dINILearnRate;
	iNumTimesBMU = new int[iGridSide][iGridSide];
	iBMU_Pos[0] = -1;
	iBMU_Pos[1] = -1;
	
	for (int i=0; i<iGridSide; i++)										// Initializing the SOM grid/network
		for (int j=0; j<iGridSide; j++)
			for (int k=0; k<iInputSize; k++)
				dGrid[i][j][k] = Math.random();	
}


public double[] dvGetBMU_Vector() {
	return dBMU_Vector;
}

public double dGetLearnRate() {
	return dLearnRate;
}

public double[] dGetNeuronWeights (int x, int y) {
	return dGrid[x][y];
}



/**
 * This is the main method that returns the coordinates of the BMU and trains its neighbors
 * 
 * @param dmInput		contains the input vector
 * @param bTrain		training or testing phases
 * 
 */
public String sGetBMU (double[] dmInput, boolean bTrain)
	{
	int x=0, y=0;
	double dNorm, dNormMin = Double.MAX_VALUE;
	String sReturn;
	
	
	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE) {
		System.out.print ("\n\n\n\n-------------------- SOM -------------------\ndmInput: \t");
		for (int k=0; k<iInputSize; k++) 
	  	System.out.print ("  " + String.format (Locale.ENGLISH, "%.5f", dmInput[k]) );
	}

	
	for (int i=0; i<iGridSide; i++)												// Finding the BMU
		for (int j=0; j<iGridSide; j++) {
			dNorm = 0;
			for (int k=0; k<iInputSize; k++)	  								// Calculating the norm
				dNorm += (dmInput[k] - dGrid[i][j][k]) * ((dmInput[k] - dGrid[i][j][k]));
			
			if (dNorm < dNormMin) {
				dNormMin = dNorm; 
				x = i;
				y = j;
			}
		}																							// Leaving the loop with the x,y positions for the BMU
	
	
	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE) {
		System.out.print ("\ndBMU_pre: \t");
		for (int k=0; k<iInputSize; k++) 
	  	System.out.print ("  " + String.format (Locale.ENGLISH, "%.5f", dGrid[x][y][k]) );
	}
	
	
	dLearnRate = 1 / (1 + 0.1 * iNumTimesBMU[x][y]);

	if (bTrain) {
		int xAux=0;
		int yAux=0;
		for (int v=-iRadio; v<=iRadio; v++)							// Adjusting the neighborhood
			for (int h=-iRadio; h<=iRadio; h++) {
				xAux = x+h;
				yAux = y+v;
				
				if (xAux < 0)																// Assuming a torus world
					xAux += iGridSide;
				else if (xAux >= iGridSide)
					xAux -= iGridSide;
	
				if (yAux < 0)
					yAux += iGridSide;
				else if (yAux >= iGridSide)
					yAux -= iGridSide;
	
				for (int k=0; k<iInputSize; k++)
					dGrid[xAux][yAux][k] += dLearnRate * (dmInput[k] - dGrid[xAux][yAux][k]) / (1 + v*v + h*h);
		}	
	}
	
	
	if (MainWindow.iOutputMode == iOUTPUT_VERBOSE) {
		System.out.print ("\ndBMU_post: \t");
		for (int k=0; k<iInputSize; k++) 
	  	System.out.print ("  " + String.format (Locale.ENGLISH, "%.5f", dGrid[x][y][k]) );
	}

	
	sReturn = "" + x + "," + y;
	iBMU_Pos[0] = x;
	iBMU_Pos[1] = y;
	dBMU_Vector = dGrid[x][y].clone();
	iNumTimesBMU[x][y]++;
	
	return sReturn;
}


}	// from the class SOM


