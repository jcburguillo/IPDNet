package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

		// The next imports are used to save the graphic window as a .png fine
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
  * This class make appear dialog windows with graphics and an OK button
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class DlGraphics extends JDialog implements ActionListener, WindowCONS
{
Visor oVisor;
JButton oJButtonSave, oJButtonOK;
private static int iNumSaves = 0;


/**
 * This is the constructor
 *
 * @param	oParent Pointer to the parent
 * @param	sTit   	Dialog title
 * @param	bBool 	Tells if the window is modal (true) or not
 * @param iGraphicType	This is the type of the graphic window that will appear
 */
public DlGraphics (JFrame oParent, String sTit, boolean bBool, int iGraphicType) {
  super (oParent, sTit, bBool);

  GridBagLayout oGBL = new GridBagLayout();
  GridBagConstraints oGBCons = new GridBagConstraints();

  setBackground (Color.lightGray);
  setForeground (Color.black);

  setLayout(oGBL);

  oGBCons.fill = GridBagConstraints.BOTH;					// Crece a lo ancho y alto
  oGBCons.weightx = 1.0;									// Entre [0.0, 1.0]
  oGBCons.weighty = 1.0;									// Entre [0.0, 1.0]
  oGBCons.gridheight = GridBagConstraints.RELATIVE;
  oGBCons.gridwidth = GridBagConstraints.REMAINDER;
    
  switch (iGraphicType) {

    case iNODE_DEGREE_DIST:
		  VisorNodeDegree oVisorNodeDegree = new VisorNodeDegree ((MainWindow) oParent);
		  oGBL.setConstraints (oVisorNodeDegree, oGBCons);
		  add(oVisorNodeDegree);
		  oVisor = oVisorNodeDegree;
		  break;

    case iNET_STATS:
      VisorSerieVectDouble oVisorNetStats = new VisorSerieVectDouble ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorNetStats, oGBCons);
      add(oVisorNetStats);
      oVisor = oVisorNetStats;
      break;
      
    case iNET_CC_STATS:
      VisorSerieDouble oVisorNetCCStats = new VisorSerieDouble ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorNetCCStats, oGBCons);
      add(oVisorNetCCStats);
      oVisor = oVisorNetCCStats;
      break;
	  
    case iCHANGESxGEN:
      VisorSerieInteger oVisorChangesxGen = new VisorSerieInteger ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorChangesxGen, oGBCons);
      add (oVisorChangesxGen);
      oVisor = oVisorChangesxGen;
      break;
            
    case iGLOBAL_PROFIT:
      VisorSerieDouble oVisorGlobalProfit = new VisorSerieDouble ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorGlobalProfit, oGBCons);
      add(oVisorGlobalProfit);
      oVisor = oVisorGlobalProfit;
      break;
      
    case iPROBSTDEV_HISTOGRAM:
      VisorHistogramInteger oVisorProbStDev = new VisorHistogramInteger ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorProbStDev, oGBCons);
      add(oVisorProbStDev);
      oVisor = oVisorProbStDev;
      break;
      
      
    default:
      VisorSerieVectInteger oVisorSerieVectInteger = new VisorSerieVectInteger ((MainWindow) oParent, iGraphicType);
      oGBL.setConstraints (oVisorSerieVectInteger, oGBCons);
      add(oVisorSerieVectInteger);
      oVisor = oVisorSerieVectInteger;
  }


  Panel oPanel = new Panel();
  oPanel.setForeground (Color.black);
  oPanel.setLayout (new GridLayout(1,2));
  oJButtonSave = new JButton ("Save");
  oJButtonSave.addActionListener (this);
  oPanel.add (oJButtonSave);
  oJButtonOK = new JButton ("OK");
  oJButtonOK.addActionListener (this);
  oPanel.add (oJButtonOK);
  add (oPanel);

  
  
  if (iGraphicType == iNODE_DEGREE_DIST)
  	setSize (new Dimension(800,400));
  else
  	setSize (new Dimension(400,400));
  setResizable(false);
  setLocation (new Point (MainWindow.iMapSize + 140 + 10 * (iGraphicType-1), 25 * (iGraphicType-1) ) );
  }



/**
 * This method process all the events produced by this class
 *
 *	@param evt This is the event received
 */
public void actionPerformed (ActionEvent evt) {
 
  if ("Save".equals (evt.getActionCommand())) {
  	BufferedImage oBImg = new BufferedImage (oVisor.getWidth(), oVisor.getHeight(), BufferedImage.TRANSLUCENT);
    Graphics2D oGraphics2D = oBImg.createGraphics();
    oVisor.paintAll (oGraphics2D);
    try {
      if (ImageIO.write(oBImg, "png", new File("./output/image"+iNumSaves+".png")))
      	iNumSaves++;
    } catch (IOException oException) {
    		oException.printStackTrace();
    }
  }
  
  if ("OK".equals (evt.getActionCommand()))
    setVisible(false);

}

  
}	// from the class
