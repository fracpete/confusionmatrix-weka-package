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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

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
   * Returns the visualization to use.
   * 
   * @return		the visualization
   */
  protected abstract AbstractConfusionMatrixVisualization getVisualization();
  
  /**
   * Returns the initial size of the frame.
   * 
   * @return		the dimensions
   */
  protected abstract Dimension getFrameDimension();
  
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
	AbstractConfusionMatrixVisualization visualization = getVisualization();
	final JFrame jf = new JFrame(classAtt.name() + " - " + getMenuItemText());
	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	jf.setSize(getFrameDimension());
	jf.getContentPane().setLayout(new BorderLayout());
	jf.getContentPane().add(visualization.generate(matrix), BorderLayout.CENTER);
	jf.setLocationRelativeTo(null);
	jf.setJMenuBar(visualization.getMenuBar(jf));
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
