package window;

import games.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Vector;


/**
  * This class allows to visualize a vector that contains vectors with Double values
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class VisorSerieVectDouble extends Visor implements WindowCONS
{
private int iTipoGrafica;


public VisorSerieVectDouble (MainWindow oVentAux, int iTipo) {
  super(oVentAux);
  oWindow = oVentAux;
  iTipoGrafica = iTipo;
  }

public void paint (Graphics g) {
  int iGenIni=0,
  		iGenSize = 0;
  double dAux=0, dSizeMax;
  double dMin = Double.MAX_VALUE;
  double dMax = -Double.MAX_VALUE;
  Double oDouble;
  Vector oVVect=null, oVSerie;
  DecimalFormat oDF5 = new DecimalFormat("0.00000");
  
  switch (iTipoGrafica) {
  
  	case iNET_STATS:
  		g.drawString ("Network Connectivity Statistics", 140, 20);
  		oVVect = Game.oVNetStats;
  		break;
    
  }

  oVSerie = (Vector) oVVect.firstElement();
  if ( (oVVect == null) || (oVSerie.size() == 0)) return;

  iGenIni = Game.iNumGen - MainWindow.iLastNGen;
  if (iGenIni < 0)
    iGenIni = 0;
  iGenSize = Game.iNumGen - iGenIni - 1;
  if ( (iGenSize == 0) || (oVSerie.size() < iGenSize) ) return;

  if (Game.iNumGen > 0)
	for (int j=0; j<oVVect.size(); j++) {
	  oVSerie = (Vector) oVVect.elementAt (j);
	  for (int i=0; i<oVSerie.size(); i++) {
	    oDouble = (Double) oVSerie.elementAt(i);
	    dAux = oDouble.doubleValue();
	    if (dAux > dMax)
	      dMax = dAux;
	    if (dAux < dMin)
	      dMin = dAux;
	    }
	}


  dSizeMax = dMax - dMin;
  if (dSizeMax == 0) dSizeMax = Double.MIN_VALUE;                   // To avoid division by zero
  
  g.setColor (Color.black);
  g.drawLine (50, 50, 50, 300);										// Vertical line: Y axis
  g.drawString ("1", 30, 50);
  g.drawLine (45, 50, 55, 50);
  g.drawLine (45, 113, 55, 113);
  g.drawLine (45, 175, 55, 175);
  g.drawLine (45, 238, 55, 238);
  g.drawLine (45, 300, 55, 300);
  if (dMax > -Double.MAX_VALUE) {
    g.drawString ("Max. Value = " + oDF5.format(dMax), 145, 315);
    g.drawString ("Max. Value = " + oDF5.format(dMax), 40, 40);
  }
  if (dMin < Double.MAX_VALUE)
    g.drawString ("Min. Value = " + oDF5.format(dMin), 145, 325);
  g.drawString ("Ini = "+iGenIni, 30, 315);
  g.drawString ("End = "+Game.iNumGen, 320, 315);
  
    
  
  for (int j=0; j<oVVect.size(); j++) {
	  
	  oVSerie = (Vector) oVVect.elementAt (j);
	  if (oVSerie.size() == 0) continue;
    
	  int iX1, iY1, iX2, iY2;
	  if (iTipoGrafica != iNET_STATS)
	  	g.setColor(new Color(-1677215 * (-5 + j)));
	  else switch (j) {
	  	case 0: 	g.setColor (Color.black); break;
	  	case 1: 	g.setColor (Color.green); break;
	  	case 2: 	g.setColor (Color.red); break;
	  	case 3: 	g.setColor (Color.blue); break;
	  	default: 	g.setColor(Color.orange);
	  }
	  
	  iX1 = 50; iY1 = 175; iX2 = 50; iY2 = 175;
	  for (int i=0; i<iGenSize; i++) {
	    iX2 = 50 + (300 * (i+1)) / iGenSize;
	    oDouble = (Double) oVSerie.elementAt(i);
      iY2 = (int) (300.0 - (250.0 * (oDouble.doubleValue()-dMin)) / dSizeMax);     	// Using dSizeMax to normalize
	    g.drawLine (iX1, iY1, iX2, iY2);
	    iX1 = iX2;
	    iY1 = iY2;
	    }
	
	  switch (iTipoGrafica) {
	  	case iNET_STATS:
	  		switch (j) {
	  			case 0: g.drawString ("Mean", iX2+2, iY2-10); break;
	  			case 1: g.drawString ("StDev", iX2+2, iY2-10); break;
	  			case 2: g.drawString ("Range", iX2+2, iY2-10); break;
	  			case 3: g.drawString ("Mode", iX2+2, iY2-10); break;
	  		}
	  		break;  		
	  }
	  
  }   // for (int j=0; j<oVVect.size(); j++)


}   // public void paint (Graphics g)

}   // from the class






