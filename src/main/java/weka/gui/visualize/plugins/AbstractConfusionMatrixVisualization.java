/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * AbstractConfusionMatrixVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;

/**
 * Ancestor for confusion matrix visualizations.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractConfusionMatrixVisualization 
  implements Serializable {

  /** for serialization. */
  private static final long serialVersionUID = 8547782264818608668L;

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  public abstract String getMenuItemText();
  
  /**
   * Returns the initial size of the frame.
   * 
   * @return		the dimensions
   */
  protected abstract Dimension getFrameDimension();

  /**
   * Generates a {@link JScrollPane} with proper scroll settings.
   * 
   * @param view	the component to add to the pane
   * @return		the scroll pane
   */
  protected JScrollPane createScrollPane(Component view) {
    JScrollPane		result;
    
    result = new JScrollPane(view);
    result.getVerticalScrollBar().setUnitIncrement(20);
    result.getHorizontalScrollBar().setUnitIncrement(20);
    result.getVerticalScrollBar().setBlockIncrement(100);
    result.getHorizontalScrollBar().setBlockIncrement(100);
    result.setWheelScrollingEnabled(true);
    
    return result;
  }
  
  /**
   * Returns the "save as" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  protected abstract JMenuItem getSaveAsMenuItem(final JFrame frame);
  
  /**
   * Generates the menubar for the frame.
   * 
   * @param frame	the frame
   * @return		the generate menu
   */
  public JMenuBar getMenuBar(final JFrame frame) {
    JMenuBar 	menubar;
    JMenu 	menu;
    JMenuItem 	menuitem;
    
    menubar = new JMenuBar();
    menu    = new JMenu("File");
    menubar.add(menu);
    
    menuitem = getSaveAsMenuItem(frame);
    if (menuitem != null) {
      menu.add(menuitem);
      menu.addSeparator();
    }

    menuitem = new JMenuItem("Close");
    menuitem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	frame.setVisible(false);
	frame.dispose();
      }
    });
    menu.add(menuitem);
    
    return menubar;
  }
  
  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  public abstract JPanel generate(ConfusionMatrix matrix);
  
  /**
   * Get a JMenu or JMenuItem which contain action listeners that perform the
   * visualization, using some but not necessarily all of the data. Exceptions
   * thrown because of changes in Weka since compilation need to be caught by
   * the implementer.
   * 
   * @see NoClassDefFoundError
   * @see IncompatibleClassChangeError
   * 
   * @param preds predictions
   * @param classAtt class attribute
   * @return menuitem for opening visualization(s), or null to indicate no
   *         visualization is applicable for the input
   */
  public JMenuItem getVisualizeMenuItem(final ArrayList<Prediction> preds, final Attribute classAtt) {
    JMenuItem	result;
    
    if (!classAtt.isNominal()) {
      System.err.println("Class is not nominal: " + classAtt.name());
      return null;
    }
    
    result = new JMenuItem(getMenuItemText());
    result.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	ConfusionMatrix matrix = new ConfusionMatrix(preds, classAtt);
	final JFrame jf = new JFrame(classAtt.name() + " - " + getMenuItemText());
	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	jf.setSize(getFrameDimension());
	jf.getContentPane().setLayout(new BorderLayout());
	jf.getContentPane().add(generate(matrix), BorderLayout.CENTER);
	jf.setLocationRelativeTo(null);
	jf.setJMenuBar(getMenuBar(jf));
	jf.setVisible(true);
      }
    });
    
    return result;
  }
}
