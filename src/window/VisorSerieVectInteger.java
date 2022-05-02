package window;

import games.*;

import java.awt.*;
import java.util.Vector;


/**
  * This class allows to visualize a vector that contains series of Integers
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class VisorSerieVectInteger extends Visor implements WindowCONS
{
private static final long serialVersionUID = 1L;
private int iTipoGrafica;
private int iMax = 100;     		// Using iMax to normalize graphics

public VisorSerieVectInteger (MainWindow oVentAux, int iTipo) {
  super(oVentAux);
  oWindow = oVentAux;
  iTipoGrafica = iTipo;
  }

public void paint (Graphics g) {
  int iGenIni=0;
  Integer oInt;
  Vector oVVect = new Vector(1,1);
  Vector<Integer> oVSerie;
  
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

  switch (iTipoGrafica) {              
    case iFREQxACTION:
   		g.drawString ("Frequency of Cells x Action", 130, 20);
    	oVVect = Game.oVFrecActions;
    	break;            
  }

  oVSerie = (Vector) oVVect.firstElement();
  if ( (oVVect == null) || (oVSerie.size() == 0)) return;
  
  iGenIni = Game.iNumGen - MainWindow.iLastNGen;
  if (iGenIni < 0)
    iGenIni = 0;
  int iGenSize = Game.iNumGen - iGenIni - 1;
  if ( (iGenSize == 0) || (oVSerie.size() < iGenSize) ) return;
  
  
  int iX1, iY1, iX2, iY2;
  for (int j=0; j<oVVect.size(); j++) {
    oVSerie = (Vector) oVVect.elementAt (j);
	  if (oVSerie.size() == 0) continue;
    
    switch (j) {
    	case 0: g.setColor (Color.black); break;
    	case 1: g.setColor (Color.red); break;
    	case 2: g.setColor (Color.orange); break;
    	case 3: g.setColor (Color.blue); break;
    	case 4: g.setColor (Color.green);
    }

    iX1 = 50; iY1 = 175; iX2 = 50; iY2 = 175;
    for (int i=0; i<iGenSize; i++) {
      iX2 = 50 + (300 * (i+1)) / iGenSize;
      oInt = (Integer) oVSerie.elementAt(i);
      iY2 = 300 - (250 * oInt.intValue()) / iMax;	// Using iMax to normalize
      g.drawLine (iX1, iY1, iX2, iY2);
      iX1 = iX2;
      iY1 = iY2;
      }
    
    g.drawString ((String) Game.oVTextAction.elementAt(j), iX2+2, iY2);
          
    }   // for (int j=0; j<oVVect.size(); j++)
    
  	g.setColor (Color.black);
    g.drawString ("IniG = "+iGenIni, 50, 315);
    g.drawString ("EndG = "+Game.iNumGen, 310, 315);
    
  }   // public void paint (Graphics g)

}   // from the class






