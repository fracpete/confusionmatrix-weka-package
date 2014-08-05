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
 * ConfusionMatrixVisualizationPlugin.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;
import weka.core.ClassDiscovery;

/**
 * Menu for confusion matrix visualizations.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ConfusionMatrixVisualizationPlugin
  implements VisualizePlugin {
  
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
    JMenu					result;
    Vector<String>				plugins;
    AbstractConfusionMatrixVisualization	visualization;
    
    if (!classAtt.isNominal()) {
      System.err.println("Class is not nominal: " + classAtt.name());
      return null;
    }
    
    result  = null;
    plugins = ClassDiscovery.find(AbstractConfusionMatrixVisualization.class, getClass().getPackage().getName());
    for (String plugin: plugins) {
      try {
	visualization = (AbstractConfusionMatrixVisualization) Class.forName(plugin).newInstance();
	if (result == null)
	  result = new JMenu("Confusion matrix");
	result.add(visualization.getVisualizeMenuItem(preds, classAtt));
      }
      catch (Exception e) {
	System.err.println("Failed to process confusion matrix plugin: " + plugin);
	e.printStackTrace();
      }
    }
    
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
