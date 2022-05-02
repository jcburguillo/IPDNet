package window;

import java.awt.*;
import java.util.Vector;

import games.*;


public class VisorNeighborsxAction extends Visor
{
  private boolean bDraw=false;

public VisorNeighborsxAction (MainWindow oVentAux) {
  super(oVentAux);
  }

public void paint (Graphics g) {
  int iAux, iFrecMax;
  int iX1, iX2, iY2;
  int[] imMaxVecAcc = new int [Game.iNumActionProbs];
  int[][] imFrecNeighbors = new int [Game.iNumActionProbs][Game.iNumActionProbs];
  int iAccionPropia, iAccionVecino;
  Cell oCell, oVecino;
  Vector ovNeighbors;

  g.setColor (Color.black);
  g.drawLine (50, 50, 50, 300);
  g.drawLine (50, 300, 350, 300);
  g.drawString ("100", 22, 50);
  g.drawLine (45, 50, 55, 50);
  g.drawString ("75", 25, 113);
  g.drawLine (45, 113, 55, 113);
  g.drawString ("50", 25, 175);
  g.drawLine (45, 175, 55, 175);
  g.drawString ("25", 25, 238);
  g.drawLine (45, 238, 55, 238);
  for (int i=0; i<(Game.iNumActionProbs+1); i++) {
    iAux = 50 + (300 * i) / Game.iNumActionProbs;
    g.drawLine (iAux, 295, iAux, 305);
    }

  g.drawString ("Neighbours per Action (%)", 150, 20);
  for (int i=0; i<(Game.iNumActionProbs); i++) {
    iAux = 85 + (300 * i) / Game.iNumActionProbs;
    g.drawString ((String) Game.oVTextAction.elementAt(i), iAux, 320);
    }

  		// Reset all matrixes
  for (int i=0; i<Game.iNumActionProbs; i++)
    imMaxVecAcc[i] = 0;

  		//Setting the number of neighbors per cell type (adding all values)
  for (int y=0; y<Game.iCellV; y++)
    for (int x=0; x<Game.iCellH; x++)
      if (Game.oCellMatrix[x][y] == null)
        continue;
      else {
        bDraw = true;
        oCell = Game.oCellMatrix[x][y];
        iAccionPropia = oCell.iGetAction();
        ovNeighbors = oCell.oVGetNeighbors();
        for (int i=0; i<ovNeighbors.size(); i++) {
          oVecino = (Cell) ovNeighbors.elementAt (i);
          iAccionVecino = oVecino.iGetAction();
          imFrecNeighbors[iAccionPropia][iAccionVecino]++;
        }
      }


  if (!bDraw) return;


  iFrecMax=0;
  for (int i=0; i<Game.iNumActionProbs; i++) {
    for (int j=0; j<Game.iNumActionProbs; j++)
      imMaxVecAcc[i] += imFrecNeighbors[i][j];                    // Determino el total de vecinos para cada acción

          // Determino los porcentajes de vecinos para cada acción
    if (imMaxVecAcc[i] > 0)
      for (int j=0; j<Game.iNumActionProbs; j++) {
        imFrecNeighbors[i][j] = 100 * imFrecNeighbors[i][j] / imMaxVecAcc[i];
        if (imFrecNeighbors[i][j] > iFrecMax)
          iFrecMax = imFrecNeighbors[i][j];
      }
  }


  iAux = Game.iNumActionProbs*Game.iNumActionProbs;
  if (iFrecMax > 0) {
    iX1 = 50;
    for (int i=0; i<Game.iNumActionProbs; i++)
      for (int j=0; j<Game.iNumActionProbs; j++) {
        iX2 = 50 + (300 * (1+j+i*Game.iNumActionProbs)) / iAux;
        iY2 = 300 - (250 * imFrecNeighbors[i][j]) / 100;
        switch (j) {
          case 0: g.setColor (Color.black); break;
          case 1: g.setColor (Color.green); break;
          case 2: g.setColor (Color.red); break;
          case 3: g.setColor (Color.blue);
        }

        g.fillRect (iX1+2, iY2, 15, 300-iY2);
        iX1 = iX2;
      }
    }

  g.setColor (Color.black);
  g.drawString ("Max Freq. = " + iFrecMax, 40, 40);
  }

}		// de la clase
