package window;

import java.awt.*;

import games.*;


public class VisorNeighborsxSize extends Visor
{
  private boolean bDraw=false;

public VisorNeighborsxSize (MainWindow oVentAux) {
  super(oVentAux);
  }

public void paint (Graphics g) {
  int iAux, iFrecMax, iTamBar;
  int iIntervalo;
  int iX1, iX2, iY2;
  int[] imNumCeldasIntervalo = new int [Game.iNumIntervals];
  int[][] imFrecAccIntervalo = new int [Game.iNumIntervals][Game.iNumActionProbs];
  int iAccionPropia, iNumNeighbors;

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
  for (int i=0; i<(Game.iNumIntervals+1); i++) {
    iAux = 50 + (300 * i) / Game.iNumIntervals;
    g.drawLine (iAux, 295, iAux, 305);
    }

  for (int i=0; i<Game.iNumIntervals; i++) {
    iAux = 45 + (150 + 300 * i) / Game.iNumIntervals;
    if (i < (Game.iNumIntervals-1))
      g.drawString (""+i*Game.iNumValInterval, iAux, 310);
    else
      g.drawString (">"+(i-1)*Game.iNumValInterval, iAux, 310);
    }

  g.drawString ("Actions per Neighborhood Size (%)", 130, 20);


  				//Setting the number of neighbors per cell type (adding all values)
  for (int y=0; y<Game.iCellV; y++)
    for (int x=0; x<Game.iCellH; x++)
      if (Game.oCellMatrix[x][y] == null)
        continue;
      else {
        bDraw = true;
        iAccionPropia = Game.oCellMatrix[x][y].iGetAction();
        iNumNeighbors = Game.oCellMatrix[x][y].oVGetNeighbors().size();
        iIntervalo = iNumNeighbors / Game.iNumValInterval;
        if (iIntervalo >= Game.iNumIntervals)
          iIntervalo = Game.iNumIntervals - 1;
        imFrecAccIntervalo [iIntervalo][iAccionPropia]++;
        imNumCeldasIntervalo[iIntervalo]++;
      }

  if (!bDraw) return;

  iFrecMax=0;
  for (int i=0; i<Game.iNumIntervals; i++) {
    iAux = 0;
    for (int j=0; j<Game.iNumActionProbs; j++)
      iAux += imFrecAccIntervalo[i][j];

    if (iAux > 0)
      for (int j=0; j<Game.iNumActionProbs; j++) {
        imFrecAccIntervalo[i][j] = 100 * imFrecAccIntervalo[i][j] / iAux;
        if (imFrecAccIntervalo[i][j] > iFrecMax)
          iFrecMax = imFrecAccIntervalo[i][j];
      }
  }


  if (iFrecMax > 0) {
    iX1 = 50;
    iAux = Game.iNumIntervals * Game.iNumActionProbs;
    iTamBar = 300 / (iAux+1);
    for (int i=0; i<Game.iNumIntervals; i++)
      for (int j=0; j<Game.iNumActionProbs; j++) {
        iX2 = 50 + (300 * (1+j+i*Game.iNumActionProbs)) / iAux;
        iY2 = 300 - (250 * imFrecAccIntervalo[i][j]) / 100;
        switch (j) {
          case 0: g.setColor (Color.black); break;
          case 1: g.setColor (Color.green); break;
          case 2: g.setColor (Color.red); break;
          case 3: g.setColor (Color.blue);
        }

        g.fillRect (iX1+2, iY2, iTamBar, 300-iY2);
        iX1 = iX2;
      }
    }

  g.setColor (Color.black);
  g.drawString ("Max Freq. = " + iFrecMax, 40, 40);
  g.drawString ("(Number of Cells per Interval)", 140, 30);
  for (int i=0; i<Game.iNumIntervals; i++) {
    iAux = 43 + (150 + 300 * i) / Game.iNumIntervals;
    g.drawString ("("+imNumCeldasIntervalo[i]+")", iAux, 320);
  }

}     // from paint (Graphics g)

}   // from the class
