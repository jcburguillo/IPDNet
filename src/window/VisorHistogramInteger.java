package window;

import games.*;

import java.awt.*;


public class VisorHistogramInteger extends Visor implements WindowCONS
{
int iGraphicType;

public VisorHistogramInteger (MainWindow oVentAux, int iGraphicTypeAux) {
  super(oVentAux);
  iGraphicType = iGraphicTypeAux;
  }

public void paint (Graphics g) {
  int iAux, iMaxVal=0;
  int iX1, iX2, iY2, iZ2;

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
  
  for (int i=0; i<5; i++) {
  	g.drawString (""+i, 80+i*55, 315);
  }  
  
	g.drawString ("Probability Histogram", 160, 20);
	
  for (int i=0; i<(Game.imProbs.length+1); i++) {
    iAux = 50 + (300 * i) / Game.imProbs.length;
    g.drawLine (iAux, 295, iAux, 305);
  }


  iX1 = 50;
  for (int i=0; i<Game.imProbs.length; i++) {
    iX2 = 50 + (300 * (i+1)) / Game.imProbs.length;
    iY2 = 300 - 250 * Game.imProbs[i] / 100;
    iZ2 = 250 * Game.imStDev[i] / 200;									// We divide the std. dev. in two segments centered over the avg. value
    if (Game.imProbs[i] > iMaxVal)
      iMaxVal = Game.imProbs[i];
    switch (i) {
    	case 0:	g.setColor (Color.black); break;
    	case 1:	g.setColor (Color.red); break;
    	case 2:	g.setColor (Color.orange); break;
    	case 3:	g.setColor (Color.blue); break;
    	case 4:	g.setColor (Color.green); break;
    }
    g.fillRect (iX1+2, iY2, iX2-iX1-4, 300-iY2);				// Drawing the average probability with a bar
    g.setColor (Color.gray);
    g.drawRect (iX1+2, iY2-iZ2, iX2-iX1-4, 2*iZ2);			// Drawing the std. deviation with a gray rectangle
    iX1 = iX2;
    }

  g.setColor (Color.black);
  g.drawString ("Max value = " + iMaxVal, 40, 40);
  }


}		// from the class






