package games;

import java.io.Serializable;


/**
  * This class calculates the statistics of the network in every round
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class NetStats implements Serializable
{
private static int iAux, imMode[];
private static double dAux2;

public static int iMax, iMin, iRange, iMode, iValMode;
public static double dMean, dVariance, dStdDeviation, dCC;


NetStats () {
  iMax = -Integer.MAX_VALUE;
  iMin = Integer.MAX_VALUE;
  iRange = 0;
  iMode = 0;
  iValMode = 0;
  imMode = new int [Game.iTotPosMatrix];
  dMean = 0;
  dVariance = 0;
  dStdDeviation = 0;
  dCC = 0;
}



/** Calculates the basic network statistics concerning the links per node: range, mode,
 * mean and standard deviation.
 */
public static void vCalcBasicStats() {
  iMax = -Integer.MAX_VALUE; iMin = Integer.MAX_VALUE; iRange = 0;
  iMode = 0; iValMode = 0; imMode = new int [Game.iTotPosMatrix];
  dMean = 0; dVariance = 0; dStdDeviation = 0;
  
  for (int i=0; i<Game.iTotPosMatrix; i++) {
	Cell oCell = (Cell) Game.oCellVector.elementAt(i);
	if (oCell != null) {
	  iAux = oCell.ovNeighbors.size();
	  dMean += (double) iAux;
	  if (iAux > iMax) iMax = iAux;
	  if (iAux < iMin) iMin = iAux;
	  imMode[iAux]++;
	}
  }
  
  iRange = iMax - iMin;
  dMean = dMean / Game.oCellVector.size();
  
  
  for (int i=0; i<Game.iTotPosMatrix; i++) {
		Cell oCell = (Cell) Game.oCellVector.elementAt(i);
		if (oCell != null) {
		  iAux = oCell.ovNeighbors.size();
		  dAux2= (double) iAux - dMean;
		  dVariance += dAux2 * dAux2;
		}
		
		if (iValMode < imMode[i]) {
		  iValMode = imMode[i];
		  iMode = i;
		}
  }
  
  dVariance = dVariance / Game.oCellVector.size();
  dStdDeviation = Math.sqrt(dVariance);  
}


/** Calculates the clustering coefficient of the network.  
 */
public static void vCalcClusterCoef() {
  int iNumPaths2=0, iNumPaths2CC=0;
  
  dCC = 0;
  for (int i=0; i<Game.iTotPosMatrix; i++) {
		Cell oCell = (Cell) Game.oCellVector.elementAt(i);
		for (int j=0; j<oCell.ovNeighbors.size(); j++) {
		  Cell oCellVec = (Cell) oCell.ovNeighbors.elementAt(j);
		  for (int k=0; k<oCellVec.ovNeighbors.size(); k++) {
				Cell oCell2Vec = (Cell) oCellVec.ovNeighbors.elementAt(k);
			  iNumPaths2++;
				for (int m=0; m<oCell2Vec.ovNeighbors.size(); m++) {
			    Cell oCell3Vec = (Cell) oCell2Vec.ovNeighbors.elementAt(m);
				  if (oCell == oCell3Vec) {
						iNumPaths2CC++;
						break;
				  }
				}
		  }
	  }
  }
  
  if (iNumPaths2 > 0)
    dCC = (double) iNumPaths2CC / (double) iNumPaths2;  
}
	
	
}	// from the class NetStats