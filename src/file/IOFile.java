package file;

import games.*;
import games.IPDNet.*;
import window.MainWindow;

import java.io.*;
import java.awt.FileDialog;
import java.util.Vector;
import java.util.StringTokenizer;



/**
  * This class allows to load / save the matrix to a file (serialized) and also
  * to load / save complex networks in the Pajek format
  * 
  * @author  Ana Peleteiro & Juan C. Burguillo
  * @version 2.0
  */
public class IOFile
{


/**
  * Reading the matrix (serialized) from a file
  */
public static void vLoadMatrix (MainWindow oMainWindow, Cell[][] oMatAux) {
  FileInputStream oIn = null;
  ObjectInputStream oFileIn = null;

  FileDialog oFD = new FileDialog (oMainWindow, "Load Matrix", FileDialog.LOAD);
  oFD.setFile ("*.mtx");
  oFD.setVisible(true);
  String sFich = oFD.getFile();
  String sDir = oFD.getDirectory();

  if (sFich == null) return;
  sDir = sDir.concat (sFich);

  try {
	oIn = new FileInputStream (sDir);
	oFileIn = new ObjectInputStream (oIn);
		
	Game.oCellMatrix = (Cell[][]) oFileIn.readObject ();
	oFileIn.close();
  }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
  catch (ClassNotFoundException e) {e.printStackTrace();}
}

	

/**
  * Saving the games matrix (serialized) into a file
  */
public static void vSaveMatrix (MainWindow oMainWindow, Cell[][] oMatAux) {
  int iAux;
  FileOutputStream oOut = null;
  ObjectOutputStream oFileOut = null;

  FileDialog oFD = new FileDialog (oMainWindow, "Save Matrix", FileDialog.SAVE);
  oFD.setFile ("Game.mtx");
  oFD.setVisible(true);
  String sFich = oFD.getFile();
  String sDir = oFD.getDirectory();
  if (sFich == null) return;
  String sAux = new String (sFich);

  iAux = sAux.indexOf ('.');
  if (iAux >= 0)
  sFich = sAux.substring (0, iAux);						// removing the ".*"
  sFich = sFich.concat (".mtx");

  sDir = sDir.concat (sFich);

  try {
    oOut = new FileOutputStream (sDir);
    oFileOut = new ObjectOutputStream (oOut);
    oFileOut.writeObject (Game.oCellMatrix);
    oFileOut.flush();
    oFileOut.close();
  }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
}








/**
  * Reading the cluster strat (serialized) from a file
  */
public static void vLoadStrat (MainWindow oMainWindow) {
  FileInputStream oIn = null;
  ObjectInputStream oFileIn = null;

  FileDialog oFD = new FileDialog (oMainWindow, "Load Strat", FileDialog.LOAD);
  oFD.setFile ("*.ser");
  oFD.setVisible(true);
  String sFich = oFD.getFile();
  String sDir = oFD.getDirectory();

  if (sFich == null) return;
  sDir = sDir.concat (sFich);

  try {
		oIn = new FileInputStream (sDir);
		oFileIn = new ObjectInputStream (oIn);
			
		GameIPDNet.oClusterStratPMO = (Strat_PMO) oFileIn.readObject ();
		oFileIn.close();
  }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
  catch (ClassNotFoundException e) {e.printStackTrace();}
}

	

/**
  * Saving the cluster strat (serialized) into a file
  */
public static void vSaveStrat (MainWindow oMainWindow) {
  int iAux;
  FileOutputStream oOut = null;
  ObjectOutputStream oFileOut = null;

  FileDialog oFD = new FileDialog (oMainWindow, "Save Strat", FileDialog.SAVE);
  oFD.setFile ("Strat.ser");
  oFD.setVisible(true);
  String sFich = oFD.getFile();
  String sDir = oFD.getDirectory();
  if (sFich == null) return;
  String sAux = new String (sFich);

  iAux = sAux.indexOf ('.');
  if (iAux >= 0)
  sFich = sAux.substring (0, iAux);						// removing the ".*"
  sFich = sFich.concat (".ser");

  sDir = sDir.concat (sFich);

  try {
    oOut = new FileOutputStream (sDir);
    oFileOut = new ObjectOutputStream (oOut);
    oFileOut.writeObject (GameIPDNet.oClusterStratPMO);
    oFileOut.flush();
    oFileOut.close();
  }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
}









/**
  * Saving the cluster strat (serialized) directly into a file
  */
public static void vSaveStrat (String sFileName) {
  int iAux;
  FileOutputStream oOut = null;
  ObjectOutputStream oFileOut = null;

  String sDir = "./output/" + sFileName;

  try {
    oOut = new FileOutputStream (sDir);
    oFileOut = new ObjectOutputStream (oOut);
    oFileOut.writeObject (GameIPDNet.oClusterStratPMO);
    oFileOut.flush();
    oFileOut.close();
  }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
}













/**
  * This method imports complex networks from a file with Pajek notation
  *
  *	@param oMainWindow Reference to the main window needed for the FileDialog
  *	@param oMatAux This is the reference to Game.oCellMatrix
  */  
public static void vImportNetwork (MainWindow oMainWindow, Cell[][] oMatAux) {

  FileDialog oFD = new FileDialog (oMainWindow, "Import Network", FileDialog.LOAD);
  oFD.setFile ("*.net");
  oFD.setVisible(true);
  String sFich = oFD.getFile();
  String sDir = oFD.getDirectory();

  if (sFich == null) return;
  sDir = sDir.concat (sFich);
  
  File oFile = new File (sDir);
  FileInputStream oFIS = null;
  BufferedInputStream oBIS = null;
  // DataInputStream oDIS = null;
  BufferedReader oDIS = null;

  try {
    oFIS = new FileInputStream(oFile);
    oBIS = new BufferedInputStream(oFIS);                 // Here BufferedInputStream is added for fast reading.
    // oDIS = new DataInputStream(oBIS);
    oDIS = new BufferedReader(new InputStreamReader(oBIS));


    if ((!oFile.exists()) || (!oFile.canRead()))
      System.err.println("There has been an exception !");



          // Reading the first two lines that have only text
    String sLine = oDIS.readLine();
    sLine = oDIS.readLine();

          // This will be used in the future
//    StringTokenizer oST = new StringTokenizer(sLine);
//    String sNOfNodes = "";
//    while (oST.hasMoreTokens())
//      sNOfNodes = oST.nextToken();

    int iContador = 1;
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else {
          oMatAux[x][y].vSetIdPajek(iContador);
          iContador++;
        }


      // Reading the file until we'll find the arcs
    while (!sLine.equals("*Arcs"))
      sLine = oDIS.readLine();

    
      // Reseting the neighborhoods of the cells
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else {
          Vector ovNeighbors = new Vector(1, 1);
          oMatAux[x][y].vSetNeighbors(ovNeighbors);
        }


    sLine = oDIS.readLine();
    String sPartition = "";
    while (!sPartition.equals("*Partition")) {
      StringTokenizer oArcs = new StringTokenizer(sLine);
      String sPajekIdMio = oArcs.nextToken();
      int iCell = Integer.parseInt(sPajekIdMio);
      Cell oCell = null;
      for (int y = 0; y < Game.iCellV; y++)
        for (int x = 0; x < Game.iCellH; x++)
          if (oMatAux[x][y] == null)
            continue;
          else if (oMatAux[x][y].iGetIdPajek() == iCell)
              oCell = oMatAux[x][y];

      String sVecino = oArcs.nextToken();
      int iVecino = Integer.parseInt(sVecino);

        // Searching the cell with a certain Pajek identifier
      for (int y = 0; y < Game.iCellV; y++)
        for (int x = 0; x < Game.iCellH; x++)
          if (oMatAux[x][y] == null)
            continue;
          else  if (oMatAux[x][y].iGetIdPajek() == iVecino)
              oCell.vAddNeighbor (oMatAux[x][y]);

      sLine = oDIS.readLine();
      String sLine2 = sLine;
      StringTokenizer oSTFinal = new StringTokenizer (sLine2);
      sPartition = oSTFinal.nextToken();

    }   // del while



      // Reading the next lines: Vertices 16
    sLine = oDIS.readLine();

     for (int y = 0; y < Game.iCellV; y++) {
        for (int x = 0; x < Game.iCellH; x++) {
          sLine = oDIS.readLine();
          int iAction=Integer.parseInt(sLine);
           oMatAux[x][y].vSetNewAction(iAction);
           oMatAux[x][y].vUpdateAction();
        }
     }
      // Reading the lines Vector y vertices
    sLine = oDIS.readLine();
    sLine = oDIS.readLine();

    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++) {
        sLine = oDIS.readLine();
        double dGanancia=Double.parseDouble(sLine);
        oMatAux[x][y].vSetPayoff (dGanancia);
      }

    oFIS.close();
    oBIS.close();
    oDIS.close();
    }
  catch (FileNotFoundException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
  }
    


	


/**
  * This method allows to store complex networks in the Pajek notation
  *
  *	@param oMainWindow This is a reference to the main window needed for FileDialog
  *	@param oMatAux This is a reference to Game.oCellMatrix
  */  
public static void vExportNetwork (MainWindow oMainWindow, Cell[][] oMatAux) {

  Cell oVecino;
  String sDir="";
  String sFich="";

  FileDialog oFD = new FileDialog (oMainWindow, "Export Network", FileDialog.SAVE);
  oFD.setFile ("Network.net");
  oFD.setVisible(true);
  sFich = oFD.getFile();
  sDir = oFD.getDirectory();
  if (sFich == null) return;
 
  
  sDir = sDir.concat (sFich);
  try {
    FileWriter oFW = new FileWriter (sDir);
    BufferedWriter oBufWriter = new BufferedWriter (oFW);

    oBufWriter.write ("*Network Neighborhood.net\n");
    oBufWriter.write ("*Vertices " + Game.iTotNumCells + "\n");
    
    int iContador=1;
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else {
          oBufWriter.write (iContador+" ");
          oBufWriter.write ("\"" + oMatAux[x][y].sGetID() + "\"\n");
          oMatAux[x][y].vSetIdPajek (iContador);
          iContador++;
        }


    oBufWriter.write ("*Arcs\n");
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else {
           Vector ovNeighbors = oMatAux[x][y].oVGetNeighbors();
           for (int i=0; i<ovNeighbors.size(); i++) {
           oVecino = (Cell) ovNeighbors.elementAt(i);
           oBufWriter.write (oMatAux[x][y].iGetIdPajek()+" ");
           oBufWriter.write (oVecino.iGetIdPajek() + "\n");
           }
        }


    oBufWriter.write ("*Partition TypeOfPlayer.clu\n");
    oBufWriter.write ("*Vertices " + Game.iTotNumCells + "\n");
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else
          oBufWriter.write(oMatAux[x][y].iGetAction() + "\n");

    oBufWriter.write("*Vector OutputValues.vec\n");
    oBufWriter.write("*Vertices " + Game.iTotNumCells + "\n");
    for (int y = 0; y < Game.iCellV; y++)
      for (int x = 0; x < Game.iCellH; x++)
        if (oMatAux[x][y] == null)
          continue;
        else
          oBufWriter.write(oMatAux[x][y].dGetOutput4ExternalTool() + "\n");

    oBufWriter.close();
    oFW.close();
    }
  catch (Exception e) {System.err.println("Error: " + e.getMessage());}
}



/**
 * This method read a training or testing file with one float value per position.
 * @return 	outputs the file name.
 */
public static String sReadDataFile (MainWindow oMainWindow) {
  String sFile=null;
   
  FileDialog oFD = new FileDialog (oMainWindow, "Load File", FileDialog.LOAD);
  oFD.setDirectory ("./input");
  oFD.setVisible (true);
  sFile = oFD.getFile();															// Selected File
  //String sPath = oFD.getDirectory();								  // Selected folder
  String sPath = new String ("./input/" + sFile);					// Otherwise, I construct the path

  return sPath;
}




/**
 * This method read a training or testing file with one float value per position.
 * @return 	It returns the file name.
 */
public static Vector ovReadDataFileNOWindow (String sFile) {
  boolean bExit=false, bMatlab = false, bTextFile=false;
  int iIndex, iCounter=0;
  String sAux, sAux2, sCad, sError=null;
  FileReader oFile=null;
  BufferedReader oFileBuffer=null;
  Vector ovSamples = new Vector<Double> (1,1);
  
  try {
    if (sFile == null) return null;										// If there is no file, returns
    if (sFile.indexOf(".mat") > -1)										// If it is a Matlab file									
      bMatlab = true;
    else if (sFile.indexOf(".txt") > -1)							// If it is a text file									
      bTextFile = true;

    oFile = new FileReader (sFile);										// Building the file reader
    oFileBuffer = new BufferedReader(oFile);					// Creating a buffer for such file

    while (true) {
      sCad = oFileBuffer.readLine();									// Reading a whole line
      if (sCad == null) break;												// End of file
      sCad = sCad.trim();															// Deleting spaces before and after the numbers
      if (sCad.startsWith ("*")) break;								// End of file with symbol *
      if (sCad.startsWith ("%")) continue;						// Comments
      if (sCad.equals ("")) continue;									// Empty lines
      
      bExit = false;
  	  while (true) {																	// Processing several numbers in a line
				iIndex = sCad.indexOf (" ");									// First position with " "
				if (iIndex > -1) {														// If it finds " "
		  		sAux = sCad.substring (0, iIndex);							// Get the head-string
				  sCad = sCad.substring (iIndex+1);								// sCad points to the tail-string
				  sCad = sCad.trim();															// Deleting spaces before and after the numbers
				}
				else {																				// If there is no " " -> last
				  sAux = sCad;
				  bExit = true;
				}
	
			if ( sAux.indexOf("e") > 0 ) {
			  iIndex = sAux.indexOf("e+0");
			  if (iIndex < 0)
			  	iIndex = sAux.indexOf("e-0");
			  sAux2 = sAux.substring(0, iIndex+2);
			  sAux2 += sAux.substring(iIndex+3);					// This is for skipping a '0' when having "e+001" --> gives a conversion error !!!
			  sAux = sAux2;
			}
			

			ovSamples.add (new Double (Float.valueOf(sAux)));
			iCounter++;
			if (bExit) break;
			}
    }
    
  
  oFileBuffer.close();
  oFile.close();
  }
  
  catch (FileNotFoundException oFNFE)
  	{sError = "File not Found: " + sFile;}
  catch (NumberFormatException oNFE)
  	{sError = "Wrong Number Format in File: "+ sFile;}
  catch (IOException oFNFE) {
		if (oFNFE instanceof EOFException)
		  sError = null;
		else
		  sError = "I/O Exception Reading the File: " + sFile ;
	}

  if (sError != null) {
		System.out.println (sError);
		return null;
  }
  

	System.out.println ("File read: " + sFile + "      Samples: " + iCounter);
	
  return ovSamples;
}



public static Vector ovScaleInputValues (Vector ovSamples) {
  double dVal, dMax;
  Double oDouble;
  Vector ovScaledSamples = new Vector<Double> (1,1);

  dMax=0;
  for (int i=0; i<ovSamples.size(); i++) {
		oDouble = (Double) ovSamples.elementAt(i);
		dVal = oDouble.doubleValue();
		if (dMax < Math.abs(dVal))
		  dMax = Math.abs(dVal);
  }
  
  for (int i=0; i<ovSamples.size(); i++) {
		oDouble = (Double) ovSamples.elementAt(i);
		dVal = oDouble.doubleValue();
		oDouble = new Double (dVal / dMax);
		ovScaledSamples.add (oDouble);
  }
  
  return ovScaledSamples;
}




}	// from the class IOFile

