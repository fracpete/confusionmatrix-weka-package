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
 * AbstractConfusionMatrixVisualizationPlugin.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;

/**
 * Ancestor for confusion matrix visualizations.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractConfusionMatrixVisualizationPlugin
  implements VisualizePlugin {

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  protected abstract String getMenuItemText();
  
  /**
   * Returns the visualization.
   * 
   * @param matrix	the matrix to use for the visualization
   * @return		the visualization
   */
  protected abstract JPanel getVisualization(ConfusionMatrix matrix);
  
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
  public JMenuItem getVisualizeMenuItem(ArrayList<Prediction> preds, Attribute classAtt) {
    JMenuItem			result;
    final ConfusionMatrix	matrix;
    
    if (!classAtt.isNominal()) {
      System.err.println("Class is not nominal: " + classAtt.name());
      return null;
    }
    
    matrix = new ConfusionMatrix(preds, classAtt);
    result = new JMenuItem(getMenuItemText());
    result.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	JPanel panel = getVisualization(matrix);
	final JFrame jf = new JFrame(getMenuItemText());
	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	jf.setSize(800, 600);
	jf.getContentPane().setLayout(new BorderLayout());
	jf.getContentPane().add(panel, BorderLayout.CENTER);
	jf.setLocationRelativeTo(null);
	JMenuBar menubar = new JMenuBar();
	JMenu menu = new JMenu("File");
	menubar.add(menu);
	JMenuItem menuitem = new JMenuItem("Close");
	menuitem.addActionListener(new ActionListener() {
	  @Override
	  public void actionPerformed(ActionEvent e) {
	    jf.setVisible(false);
	    jf.dispose();
	  }
	});
	menu.add(menuitem);
	jf.setJMenuBar(menubar);
	jf.setVisible(true);
      }
    });
    
    return result;
  }

  /**
   * Get the minimum version of Weka, inclusive, the class is designed to work
   * with. eg: <code>3.5.0</code>
   */
  public String getMinVersion() {
    return "3.7.9";
  }

  /**
   * Get the maximum version of Weka, exclusive, the class is designed to work
   * with. eg: <code>3.6.0</code>
   */
  public String getMaxVersion() {
    return "3.8.0";
  }

  /**
   * Get the specific version of Weka the class is designed for. eg:
   * <code>3.5.1</code>
   */
  public String getDesignVersion() {
    return "3.7.11";
  }

}
