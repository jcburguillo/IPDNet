package window;

import java.awt.*;
import java.awt.event.*;


public abstract class Visor extends Canvas implements MouseListener
{
private static final long serialVersionUID = 1L;
MainWindow oWindow;

public Visor(MainWindow oVentAux)
  {
  oWindow = oVentAux;
  setForeground (Color.black);
  setBackground (Color.white);
  setFont (new Font ("SansSerif", Font.PLAIN, 11));
  //setFont (new Font ("SansSerif", Font.BOLD, 11));
  addMouseListener(this);
  }

public void paint (Graphics g) {}

public void mousePressed (MouseEvent oME)  {}
public void mouseReleased (MouseEvent oME) {}
public void mouseClicked (MouseEvent oME)  {}
public void mouseEntered (MouseEvent oME)  {}
public void mouseExited (MouseEvent oME)   {}

}		// from Visor