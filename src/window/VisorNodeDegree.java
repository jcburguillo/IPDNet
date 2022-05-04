package window;

import java.awt.*;

import games.*;


public class VisorNodeDegree extends Visor
{

public VisorNodeDegree (MainWindow oVentAux) {
  super(oVentAux);
  }

public void paint (Graphics g) {
  int iAux, iMaxFrec, iTamBar = 700 / Game.iNumIntervals;;
  int iLinks, iInterval, iNumLinksTot;
  int iX1, iX2, iY2;
  int[] imNumCellsInterval = new int [Game.iNumIntervals];
  Cell oCell;

  g.setColor (Color.black);
  g.drawLine (50, 50, 50, 300);
  g.drawLine (50, 300, 750, 300);
  g.drawString ("1.0", 20, 55);
  g.drawLine (45, 50, 55, 50);
  g.drawString ("0.75", 17, 117);
  g.drawLine (45, 113, 55, 113);
  g.drawString ("0.5", 20, 180);
  g.drawLine (45, 175, 55, 175);
  g.drawString ("0.25", 17, 243);
  g.drawLine (45, 238, 55, 238);
  for (int i=1; i<(Game.iNumIntervals+1); i++) {
    iAux = 50 + (700 * i) / Game.iNumIntervals;
    g.drawLine (iAux, 295, iAux, 305);
    }

  for (int i=0; i<Game.iNumIntervals; i++) {
    iAux = 48 + iTamBar/2 + 700*i/Game.iNumIntervals - (int) (3.0 * Math.log10 ((double) (i+1)) );
    if (i < (Game.iNumIntervals-1))
      g.drawString (""+i, iAux, 310);
    else
      g.drawString (">>", iAux, 310);
    }

  g.drawString ("Links", 760, 310);
  g.drawString ("Node Degree", 360, 20);

  			//Setting the number of neighbors per cell type (adding all values)
  iNumLinksTot=0;
  for (int y=0; y<Game.iCellV; y++)
    for (int x=0; x<Game.iCellH; x++) {
      oCell = Game.oCellMatrix[x][y];
      if (oCell == null)
        continue;
      else {
      	iLinks = 0;
	    	if (oCell.oVGetNeighbors() != null)
	        iLinks = oCell.oVGetNeighbors().size();	    	  

	    	iNumLinksTot += iLinks;
        iInterval = iLinks / Game.iNumValInterval;
        if (iInterval >= Game.iNumIntervals)
          iInterval = Game.iNumIntervals - 1;
        imNumCellsInterval[iInterval]++;
      }
    }


  iMaxFrec=0;
  for (int i=0; i<Game.iNumIntervals; i++)
    if (imNumCellsInterval[i] > iMaxFrec)
      iMaxFrec = imNumCellsInterval[i];

  if (iMaxFrec == 0) return; 

  iX1 = 50;
  for (int i=0; i<Game.iNumIntervals; i++) {
    iX2 = 50 + (700 * (1+i)) / Game.iNumIntervals;
    iY2 = 300 - (250 * imNumCellsInterval[i] / iMaxFrec);
    g.fillRect (iX1+2, iY2, (iTamBar-2), 300-iY2);
    iX1 = iX2;
    }

  g.drawString ("Max Freq. = " + iMaxFrec, 20, 40);
  g.drawString ("Total Number of Links: " + iNumLinksTot, 325, 343);
  g.drawString ("Values x Interval: "+Game.iNumValInterval, 650, 40);
    
  g.setColor (Color.blue);
  g.drawString ("Nodes", 760, 325);
  for (int i=0; i<Game.iNumIntervals; i++) {
  	iAux = 45 + iTamBar/2 + 700*i/Game.iNumIntervals - (int) (4.0 * Math.log10 ((double) (imNumCellsInterval[i]+1)) );
  	if (i%2 == 0)
  		g.drawString (""+imNumCellsInterval[i], iAux, 320);
  	else
  		g.drawString (""+imNumCellsInterval[i], iAux, 330);
    
  }

  }     // public void paint (Graphics g)

}   
