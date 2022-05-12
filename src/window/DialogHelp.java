package window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
  * This class offers a window to put strings and an OK button to leave.
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
public class DialogHelp extends JDialog implements KeyListener
{
private static final String sCELLNET_HELP = ""
		+ "\t\t\t HELP CONTENTS\n\n"
		
		+ "  1. Introducing the IPDNet simulator\n"
		+ "  2. IPDNet simulation modes\n"
		+ "  3. Main window appearance\n"
		+ "  4. Main window menus\n"
		+ "  5. References\n\n\n"
		
		
		
		
		
		+ "  1. Introducing the IPDNet simulator\n\n"

		+ "  IPDNet is a free to use open-source Java-based software for fast prototyping, developed by Juan C. Burguillo and licensed under the GNU Lesser General Public License (LGPL). This software is a spin-off from the CellNet simulation framework [1], that was used for the simulations presented in [2].\n\n"
		
		+ "  IPDNet works in two modes: i) using a graphical user interface (GUI) for doing micro-simulations or ii) using a batch mode for doing macro-simulations. IPDNet also provides support for:\n"
		+ "    - Visualizing the whole set of cells and their state along each simulation iteration.\n"
		+ "    - Visualising the simulation results in real time by means of a set of graphical windows to show the simulation result.\n"
		+ "    - Importing network data to reuse particular network structures to run simulations.\n"
		+ "    - Exporting network data, to save a particular network structure. The format used for the exported files is compatible with popular network analysers such as Pajek or Gephi.\n\n\n"

		
		
		
		+ "  2. IPDNet simulation modes\n\n"

		+ "  IPDNet provides a user-friendly access to two different in-built modes, ready to be executed with a single mouse click. These modes are shortly described next:\n\n"

		+ "  2.1 IPD Net: Iterated Prisoner’s Dilemma (IPD) with Probabilistic Memory One strategies (PMO) over the Network: this is a version of the IPD over Networks [2,3] based also on the works [4,5,6,7], that uses PMO strategies to describe if a player plays cooperatively after playing CC, CD, DC or DD in the previous round. The game has four possible displays to describe the strategies playing the game. These four possible displays and the colours used are:\n\n"
		+ "      - View Probabilities:   P1 (red)   P2 (orange)   P3 (blue)   P4 (green)\n\n"
		+ "      - View Basic Strats:    All D (black)     d.Spiteful (red)             d.TFT (cyan)       d.Pavlov (yellow)\n\n"
		+ "\t                All C (white)     c.Spiteful (orange)        c.TFT (blue)        c.Pavlov (green)\n\n"
		+ "\t                Other-strategy (gray)\n\n"
		+ "      - View ZD Strats:       ZD (black)     Equalizer (blue)     Extortioner (red)\n\n"
		+ "      - View P(C) > 0.5:      Avg. Cooperator (green)             Avg. Defector (black)\n\n"
		+ "      - View SOM:              Frequent BMU (lighter)                Few times BMU (darker)\n\n\n"
		
		+ "  2.2 Cluster Net: this is an extension of the IPD Net scenario where, after stability, a cluster seed is introduced in the population to start a cluster. This cluster joins to its team any other agent that imitates its behaviour. The cluster uses a machine learning architecture, based on Self-Organising Maps (SOM), Quality Learning Automata (QLA) and Evolutionary Game Theory (EGT) to play adaptive strategies in order to spread through the population and eventually dominate it.\n\n\n"
		
		
		+ "  3. Main window appearance\n\n"

		+ "  On the left side of the figure appears the main window that provides a menu bar, a control section (on the left side with several buttons), a status bar with information (at the bottom of the window), and the main grid full of coloured cells; which can be linked among them either spatially (as a lattice) or using any complex topology (small-world, scale free, random network, etc.).\n\n\n"


		
		
		+ "  4. Main window menus\n\n"

		+ "  The File menu contains several options to load/save the simulator state and topology. Besides it also allows to import/export the network topology to an external file in a format to be processed by external network tools (Pajek, Gephi, etc.).\n\n"

		+ "  The Mode menu allows to execute the two modes available on IPDNet and described above.\n\n"

		+ "  The Options menu provides access to the general configuration, the network parameters and the payoff matrix. Besides, the Options menu also gives access to the particular parameters used to configure each individual simulation mode.\n\n"

		+ "  The Window menu gives access to several data graphical windows to present, dynamically and on real time, the data generated by the simulator.\n\n"

		+ "  The help menu gives access to the verbose mode (on/off), the simulation Help (containing this text) and the About information.\n\n\n"


		
		
		+ "  5. References\n\n"

		+ "  [1] Self-organizing Coalitions for Managing Complexity by J.C. Burguillo. Springer ECC series (Emergence, Complexity and Computation 29). ©Springer International Publishing AG 2018. https://doi.org/10.1007/978-3-319-69898-4\n"

		+ "  [2] J.C. Burguillo. Self-organizing Coalitions for Managing Complexity. Springer ECC series (Emergence, Complexity and Computation 29). Springer International Publishing AG 2018. https://doi.org/10.1007/978-3-319-69898-4\n"
		
		+ "  [3] R.Axelrod, The evolution of Cooperation, Basic Books. NewYork, 1984.\n"
		
		+ "  [4] W.H. Press, F.J. Dyson, Iterated prisoner's dilemma contains strategies that dominate any evolutionary opponent, Proceedings of the National Academy of Sciences 109 (26) (2012) 10409–10413.\n"
		
		+ "  [5] C.Adami, A.Hintze,et al., Winning isn't everything: Evolutionary stability of zero determinant strategies, Nature Communications 4 (10.1038).\n"

		+ "  [6] P. Mathieu, J.P. Delahaye, Experimental criteria to identify efficient probabilistic memory-one strategies for the iterated prisoners dilemma, Simulation Modelling Practice and Theory 97 (2019) 101946.\n"

		+ "  [7] N.E. Glynatsi, V.A. Knight, A meta analysis of tournaments and an evaluation of performance in the iterated prisoner’s dilemma, arXiv preprint arXiv:2001.05911.\n"
		
		;


JTextArea oJTextArea = new JTextArea (sCELLNET_HELP);




/**
  * This is the constructor
  *
  * @param	oParent   Pointer to the object that creates this window
  * @param	sTit      Title of this window
  * @param	bBool     Tells if it is modal (true) or not
  */
public DialogHelp (JFrame oParent, String sTit, boolean bBool)
  {
  super (oParent, sTit, bBool);

  setLayout (new GridLayout(1,1));
  
  oJTextArea.setEditable (false);
  oJTextArea.setLineWrap (true);
  oJTextArea.setWrapStyleWord (true);
  oJTextArea.addKeyListener(this);
  add (new JScrollPane (oJTextArea));
  
  setSize(new Dimension(700, 900));
  setLocation (new Point (300, 100));
  setResizable(true);
  setVisible(true);
  }


public void keyTyped (KeyEvent e) {}
public void keyReleased (KeyEvent e) {}
public void keyPressed (KeyEvent e) {
	if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
		dispose();
}

}	// de la clase DialogOK






