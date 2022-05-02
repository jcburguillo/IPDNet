package window;

import games.Game;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Vector;


public class VisorSerieDouble extends Visor implements WindowCONS 
{
private final double dTHOUSAND = 1000;
private final double dMILLION = 1000000;
private int iGraphType;

public VisorSerieDouble (MainWindow oVentAux, int iGraphTypeAux) {
  super(oVentAux);
  iGraphType = iGraphTypeAux;
  }

public void paint (Graphics g) {
  DecimalFormat oDF2 = new DecimalFormat("0.00");
  Vector oVect=null;
    
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
  g.drawLine (125, 295, 125, 305);
  g.drawLine (200, 295, 200, 305);
  g.drawLine (275, 295, 275, 305);
  g.drawLine (350, 295, 350, 305);
  
  
  switch (iGraphType) {
  	case iNET_CC_STATS:   	g.drawString ("Clustering Coefficient", 145, 20);
									  				oVect = Game.oVNetCCStats;
									  				g.setColor (Color.blue);
									  				break;

    case iGLOBAL_PROFIT:   	g.drawString ("Avg. Profit x Agent x Game", 130, 20);
								            oVect = Game.oVGlobalProfit;
								            g.setColor (Color.green);
								            break;
  }
  
  if (oVect == null) return;

  if (oVect.size() > 0) {
    Double oDouble;
    double dAux, dMax, dMin, dAvg=0;
    int iX1, iY1, iX2, iY2;
    
    int iGenIni = Game.iNumGen - MainWindow.iLastNGen;
    if (iGenIni < 0)
      iGenIni = 0;
    int iGenSize = Game.iNumGen - iGenIni - 1;
    if ( (iGenSize == 0) || (oVect.size() < iGenSize) ) return;

    dAvg = 0;
    dMin =  Double.MAX_VALUE;         				// I use the minimum value obtained for normalizing
    dMax = -Double.MAX_VALUE;         				// I use the maximum value obtained for normalizing
    
    for (int i=0; i<iGenSize; i++) {
      oDouble = (Double) oVect.elementAt(i);
      dAux = oDouble.doubleValue();
      dAvg += dAux;
      if (dAux > dMax)
        dMax = dAux;
      if (dAux < dMin)
        dMin = dAux;
      }
    dAvg = dAvg / iGenSize;

    if (dMax == 0) return;

    iX1 = 50; iY1 = 175; iX2 = 50; iY2 = 175;
    if (dMax - dMin > 0)
	    for (int i=0; i<iGenSize; i++) {
	      iX2 = 50 + (300 * (i+1)) / iGenSize;
	      oDouble = (Double) oVect.elementAt(i);
	      iY2 = (int) ( 300.0 - 250.0 * (oDouble.doubleValue() -dMin) / (dMax - dMin) );
	      g.drawLine (iX1, iY1, iX2, iY2);
	      iX1 = iX2;
	      iY1 = iY2;
	      }
    
    g.setColor (Color.black);
    g.drawString ("Ini = "+iGenIni, 30, 315);
    g.drawString ("End = "+Game.iNumGen, 310, 315);
        
    g.drawString ("Max. Value = " + oDF2.format(dMax), 40, 40);
    g.drawString ("Max. Value = " + oDF2.format(dMax), 125, 315);
    g.drawString ("Avg. Value = " + oDF2.format(dAvg), 125, 325);
    g.drawString ("Min. Value = " + oDF2.format(dMin), 125, 335);

    }

  }

}		// from the class






