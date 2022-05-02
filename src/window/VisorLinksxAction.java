package window;

import java.awt.*;
import java.util.Vector;

import games.*;


public class VisorLinksxAction extends Visor
{
  private boolean bDraw=false;

public VisorLinksxAction (MainWindow oVentAux) {
  super(oVentAux);
  }

public void paint (Graphics g) {
  int iAux, iFrecMax;
  int iX1, iY1, iX2, iY2;
  int[] imNumCelAcc = new int [Game.iNumActionProbs];
  int[] imNumVecAcc = new int [Game.iNumActionProbs];
  int[] imMedVecAcc = new int [Game.iNumActionProbs];
  int iAccionPropia;
  Cell oCell;
  Vector<?> ovNeighbors;

  g.setColor (Color.black);
  g.drawLine (50, 50, 50, 300);
  g.drawLine (50, 300, 350, 300);
  g.drawString ("1", 30, 50);
  g.drawLine (45, 50, 55, 50);
  g.drawString (".75", 25, 113);
  g.drawLine (45, 113, 55, 113);
  g.drawString (".5", 25, 175);
  g.drawLine (45, 175, 55, 175);
  g.drawString (".25", 25, 238);
  g.drawLine (45, 238, 55, 238);
  for (int i=0; i<(Game.iNumActionProbs+1); i++) {
    iAux = 50 + (300 * i) / Game.iNumActionProbs;
    g.drawLine (iAux, 295, iAux, 305);
    }

  g.drawString ("Average Links per Action", 170, 20);
  
  for (int i=0; i<(Game.iNumActionProbs); i++) {
    iAux = 85 + (300 * i) / Game.iNumActionProbs;
    g.drawString ((String) Game.oVTextAction.elementAt(i), iAux, 320);
    }
  
       // Reset all the matrixes
  for (int i=0; i<Game.iNumActionProbs; i++) {
    imNumCelAcc[i] = 0;
    imNumVecAcc[i] = 0;
    imMedVecAcc[i] = 0;
  }

      // Setting the number of neighbors per cell type (adding all values)
  for (int y=0; y<Game.iCellV; y++)
    for (int x=0; x<Game.iCellH; x++)
      if (Game.oCellMatrix[x][y] == null)
        continue;
      else {
        bDraw = true;
        oCell = Game.oCellMatrix[x][y];
        iAccionPropia = oCell.iGetAction();
        ovNeighbors = oCell.oVGetNeighbors();
        imNumCelAcc[iAccionPropia]++;
        imNumVecAcc[iAccionPropia] += ovNeighbors.size();
      }


  if (!bDraw) return;

  iFrecMax = 0;
  for (int i=0; i<Game.iNumActionProbs; i++) {
    imMedVecAcc[i] = imNumVecAcc[i] / imNumCelAcc[i];
    if (imMedVecAcc[i] > iFrecMax)
      iFrecMax = imMedVecAcc[i];
  }

 
 
  if (iFrecMax > 0) {
    iX1 = 50; iY1 = 300;
    for (int i=0; i<Game.iNumActionProbs; i++) {
      iX2 = 50 + (300 * (i+1)) / Game.iNumActionProbs;
      iY2 = 300 - (250 * imMedVecAcc[i]) / iFrecMax;
      g.drawLine (iX1, iY1, iX1, iY2);
      g.drawLine (iX1, iY2, iX2, iY2);
      g.drawLine (iX2, iY2, iX2, 300);
      iX1 = iX2;
      iY1 = iY2;
      }
    }


  g.drawString ("Max Freq. = " + iFrecMax, 40, 40);
  }

}		// from the class
