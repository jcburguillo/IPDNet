package window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;

import games.*;
import games.IPDNet.*;
import machineLearning.SOM;


public class WorldGrid extends Visor implements WindowCONS {
  private boolean bMouseClicked = false;
  private Cell oInfoCell = null;
	
public WorldGrid(MainWindow oVentAux) {
  super(oVentAux);
  }


public void mouseClicked (MouseEvent oME) {
  Cell[][] oCellMatrix;
  Point oPoint = oME.getPoint();
  
  if (Game.iWorldShow == iWORLD_SHOW_SOM)
  	MainWindow.oDlgInfoSOM.vUpdate ((int) oPoint.getX()/SOM.iCellSize, (int) oPoint.getY()/SOM.iCellSize);
  	
  else {
	  oCellMatrix = Game.oCellMatrix;
	  bMouseClicked = true;
	  oInfoCell = oCellMatrix	[(int) (oPoint.getX() / (double) MainWindow.iCellSize)]
	  												[(int) (oPoint.getY() / (double) MainWindow.iCellSize)];
	  MainWindow.oDlgInfoCell.vUpdate (oInfoCell);
	  repaint();
  }
}



public void vSetInfoCell (Cell oCellAux) {
	oInfoCell = oCellAux;
}


public void paint (Graphics oGraphics)
 {update (oGraphics);}


public void update (Graphics oGraphics) {
	Image offImage = createImage (oGraphics.getClipBounds().width, oGraphics.getClipBounds().height);
  Graphics offg = offImage.getGraphics();

  
  if (Game.iWorldShow == iWORLD_SHOW_SOM)
    vDrawSOM (offg);
  
  else {
	  if (oWindow.oGame.iNewGame > 0)
	  	vDrawCells (offg, GameIPDNet.oCellMatrix);
	
	  if ( ( (bMouseClicked) && (oInfoCell != null) ) || (MainWindow.oDlgInfoCell.isVisible()) )
	  	vDrawCellNeighbors (offg, oInfoCell);
  }  
  

  oGraphics.drawImage (offImage, 0, 0, this);
  
  bMouseClicked = false;

}



/** Draws the cells' color for NON-coalition games 
 * 
 * @param offg  			The Graphics object used to paint
 * @param oCellMatrix	The matrix with the cells we are painting
 */
private void vDrawCells (Graphics offg, Cell[][] oCellMatrix) {
	boolean bNoPureStrat = false;
	int iColor = 8;
  Cell oCell;
  
  if (GameIPDNet.omStratPMO == null) return;

  int xAux=0, yAux=0;
  for (int y=0; y<Game.iCellV; y++) {
    for (int x=0; x<Game.iCellH; x++) {
      oCell = oCellMatrix[x][y];

      if (oCell == null)                                     // Empty cell
        offg.setColor (Color.blue);
      
      else {
      	
      	if (GameIPDNet.omStratPMO[x][y] == null) return;
      	
      	switch (Game.iWorldShow) {
		      case iWORLD_SHOW_PROBS:					iColor = iGetColorCell_Show_Probs (x,y); break;
	        case iWORLD_SHOW_BASIC_STRATS:	iColor = GameIPDNet.omStratPMO[x][y].iGet_Basic_Strat(); break;
	        case iWORLD_SHOW_ZD_STRATS:			iColor = iGetColorCell_Show_Prob_ZD_Strats (x,y); break;
	        case iWORLD_SHOW_PROB_C:				iColor = iGetColorCell_Show_Prob_C (x,y); break;
      	}
      	
      	bNoPureStrat = false;
      	if ( (Game.iWorldShow == iWORLD_SHOW_BASIC_STRATS) && (iColor > 7) ) {
      		bNoPureStrat = true;
       		iColor = Strat_PMO.iFindCloserBasicStrat (GameIPDNet.omStratPMO[x][y].dmGet_Prob(), 1.0);
      	}
      	 	
      	switch (iColor) {
      		case 0:   offg.setColor (Color.black); break;		// All D				// ZD
      		case 1:   offg.setColor (Color.orange); break;	// d.Spiteful		// Extortioner	// P1
	        case 2:   offg.setColor (Color.red); break;			// c.Spiteful										// P2 
	        case 3:   offg.setColor (Color.cyan); break;		// d.TFT
	        case 4:   offg.setColor (Color.blue); break;		// c.TFT				// Equalizer		// P3
	        case 5:   offg.setColor (Color.yellow); break;	// d.Pavlov
	        case 6:   offg.setColor (Color.green); break;		// c.Pavlov											// P4
	        case 7:   offg.setColor (Color.white); break;		// All C
	        case 8:   offg.setColor (Color.gray); break;		// Unknown			// Not a ZD
	      }
      }
      
      offg.fillRect (xAux, yAux, MainWindow.iCellSize, MainWindow.iCellSize); 
      
      if (!GameIPDNet.omStratPMO[x][y].bClusterStrat && (MainWindow.iCellSize > 2) ) {
      	if (bNoPureStrat) {
      		offg.setColor (Color.darkGray);
      		offg.drawRect (xAux+1, yAux+1, MainWindow.iCellSize-2, MainWindow.iCellSize-2);
      	}
      	else
      		offg.setColor (Color.darkGray);
      		
      	offg.drawRect (xAux, yAux, MainWindow.iCellSize, MainWindow.iCellSize);
	    }
      

      xAux += MainWindow.iCellSize;
      }
    xAux=0;
    yAux += MainWindow.iCellSize;
    }

  }







/** Get the cell's color depending on the higher probability P1..P4 
 * 
 * @param x			 Horizontal coordinate of the cell
 * @param y			 Vertical coordinate of the cell
 */
private int iGetColorCell_Show_Probs (int x, int y) {
	int iIndexMax=0, iColor;
	double dProbMax=0;  

	for (int i=1; i<5; i++)
		if (GameIPDNet.dGetStrat_PMO(x, y, i) > dProbMax) {
			dProbMax = GameIPDNet.dGetStrat_PMO(x, y, i);
			iIndexMax = i;
		}
		else if (GameIPDNet.dGetStrat_PMO(x, y, i) == dProbMax) {
			if (Math.random() < 0.5) {
				dProbMax = GameIPDNet.dGetStrat_PMO(x, y, i);
				iIndexMax = i;
			}
		}
	
	iColor = iIndexMax;
	if (iIndexMax == 3)
		iColor = 4;		// Blue, used by basic TFT
	else if (iIndexMax == 4)
		iColor = 6;		// Green, used by basic Pavlov
	
	return iColor;
}




/** Get the cell's color depending on the average cooperation prob.: (P1+..+P4)/4 
 * 
 * @param x			 Horizontal coordinate of the cell
 * @param y			 Vertical coordinate of the cell
 */

private int iGetColorCell_Show_Prob_ZD_Strats (int x, int y) {
	int iAux, iColor = 8;		// Not anyone (gray)
	
	iAux = GameIPDNet.omStratPMO[x][y].iGet_ZD_Strat();
	
	if (iAux == Strat_PMO.iZD_STRAT)										// ZD strat (black)
		iColor = iAux;
	if (iAux == Strat_PMO.iZD_EQUALIZER_STRAT)					// Equalizer (blue)
		iColor = 4;
	else if (iAux == Strat_PMO.iZD_EXTORTIONER_STRAT)		// Extortioner (red)
		iColor = 1;
	return iColor;
}



/** Get the cell's color depending on the average cooperation prob.: (P1+..+P4)/4 
 * 
 * @param x			 Horizontal coordinate of the cell
 * @param y			 Vertical coordinate of the cell
 */
private int iGetColorCell_Show_Prob_C (int x, int y) {
	double dAvgProbC=0;
	  
	for (int i=1; i<5; i++)
		dAvgProbC +=  GameIPDNet.dGetStrat_PMO(x, y, i);
	dAvgProbC = dAvgProbC / 4.0;
	
	if (dAvgProbC < 0.5)			// Higher defection (black)
		return 0;
	else											// Higher cooperation (green)
		return 6;
}




/** When a cell is clicked on the grid, we draw the colors of the neighbors of a cell 
 * 
 * @param offg  The Graphics object used to paint
 * @param oCell	The Cell we are observing
 */
private void vDrawCellNeighbors (Graphics offg, Cell oCell) {
  int xAux=0, yAux=0;
  Cell oCellAux;
  Vector oVect = new Vector (1,1);
  
  offg.setColor (Color.pink);														// Then we paint the cell in pink
  xAux = oCell.iGetPosX() * MainWindow.iCellSize;
  yAux = oCell.iGetPosY() * MainWindow.iCellSize;
  offg.fillRect (xAux, yAux, MainWindow.iCellSize, MainWindow.iCellSize);  
  
  oVect = oCell.oVGetNeighbors();

  if (DlgInfoCell.bShowNeighbors) {
	  offg.setColor (Color.magenta);											// Painting the neighborhood members in magenta
	  for (int i=0; i<oVect.size(); i++) {
	    oCellAux = (Cell) oVect.elementAt (i);
	    xAux = oCellAux.iGetPosX() * MainWindow.iCellSize;
	    yAux = oCellAux.iGetPosY() * MainWindow.iCellSize;
	   	offg.fillRect (xAux, yAux, MainWindow.iCellSize, MainWindow.iCellSize);
	  }
  }
  
}






private void vDrawSOM (Graphics offg) {
	int xAux=0, yAux=0;
	int iCellSOMSize = MainWindow.iMapSize / SOM.iGridSide;
	
  for (int y=0; y<SOM.iGridSide; y++) {
    for (int x=0; x<SOM.iGridSide; x++) {
    	int iAux = (SOM.iNumTimesBMU[x][y] + 9) / 10;			// We draw colors in multiples of 10
    	
    	if ( (SOM.iBMU_Pos[0] == x) && (SOM.iBMU_Pos[1] == y) )
    			offg.setColor (Color.yellow);
		  
    	else switch (iAux) {
		  	case 5:		offg.setColor (Color.gray); break;		// 41 or more times being the BMU
		  	case 4:		offg.setColor (Color.green); break;
		  	case 3:		offg.setColor (Color.orange); break;
		  	case 2:		offg.setColor (Color.red); break;
		  	case 1:		offg.setColor (Color.blue); break;
		  	case 0:		offg.setColor (Color.black); break;		// Not a single BMU
		  	default:	offg.setColor (Color.white);					// More than 50 times BMU
		  }
    	
    	offg.fillRect (xAux, yAux, iCellSOMSize, iCellSOMSize);
      if (iCellSOMSize > 2) {
        offg.setColor (Color.darkGray);
        offg.drawRect (xAux, yAux, iCellSOMSize, iCellSOMSize);
      }

    	
      xAux += iCellSOMSize;
    }
    xAux=0;
    yAux += iCellSOMSize;
  }
}



}		// from the class VisorWorld






