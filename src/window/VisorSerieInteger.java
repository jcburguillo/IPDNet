package window;

import java.awt.*;
import java.util.Vector;

import games.Game;
import games.IPDNet.GameIPDNet;


public class VisorSerieInteger extends Visor implements WindowCONS
{
int iTipoGrafica;

public VisorSerieInteger (MainWindow oVentAux, int iTipoGraficaAux) {
  super (oVentAux);
  iTipoGrafica = iTipoGraficaAux;
  }

public void paint (Graphics g) {
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

  
  switch (iTipoGrafica) {
    case iCHANGESxGEN:
    	g.drawString ("Max. Change in PMOs", 140, 15);
    	g.setColor (Color.red);
    	g.drawString ("< " + GameIPDNet.iDiff_PMOsBuffer2Stop + "% in " + GameIPDNet.iPMOsBufferSize + " gens to STOP !", 125, 30);
    	g.setColor (Color.black);		
    	oVect = Game.oVChangesxGen;
    	break;            
  }
  
  if (oVect == null) return;
  
  if (oVect.size() > 0) {
    Integer oInteger;
    int iX1, iY1, iX2, iY2;
    int iValue=0, iAvg=0, iMax=0, iMin=100;
    
    int iGenIni = Game.iNumGen - MainWindow.iLastNGen;
    if (iGenIni < 0)
      iGenIni = 0;
    int iGenSize = Game.iNumGen - iGenIni - 1;
    if (iGenSize == 0) return;

    g.setColor (Color.blue);
    iX1 = 50; iY1 = 175; iX2 = 50; iY2 = 175;
    for (int i=0; i<iGenSize; i++) {
      iX2 = 50 + (300 * (i+1)) / iGenSize;
      oInteger = (Integer) oVect.elementAt(i);
      iValue = oInteger.intValue();
      iAvg += iValue;
      if (iValue > iMax) iMax = iValue;
      if (iValue < iMin) iMin = iValue;
      iY2 = 300 - (250 * iValue) / 100;
      g.drawLine (iX1, iY1, iX2, iY2);
      iX1 = iX2;
      iY1 = iY2;
      }
  	iAvg = iAvg / iGenSize;

    g.setColor (Color.black);
    
    g.drawString ("Max. = 100%", 40, 40);
    g.drawString ("Max. Value = " + iMax + "  %", 130, 315);
    g.drawString ("Last Value = " + iValue + "  %", 130, 325);
    g.drawString ("Min. Value = " + iMin + "  %", 130, 335);
    g.drawString ("IniG = "+iGenIni, 50, 315);
    g.drawString ("EndG = "+Game.iNumGen, 310, 315);
    }

  }

}		// from the class VisorSerieInteger






