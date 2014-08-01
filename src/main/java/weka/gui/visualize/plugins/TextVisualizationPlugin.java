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
 * TextVisualizationPlugin.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import javax.swing.JPanel;

/**
 * Text visualization plugin.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 * @see TextVisualization
 */
public class TextVisualizationPlugin
  extends AbstractConfusionMatrixVisualizationPlugin {

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  protected String getMenuItemText() {
    return "Confusion matrix (text)";
  }

  /**
   * Returns the visualization.
   * 
   * @param matrix	the matrix to use for the visualization
   * @return		the visualization
   */
  @Override
  protected JPanel getVisualization(ConfusionMatrix matrix) {
    return new TextVisualization().generate(matrix);
  }
}
