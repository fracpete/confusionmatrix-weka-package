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
 * HeatmapVisualizationPlugin.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.Dimension;

/**
 * Heatmap visualization of a confusion matrix.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class HeatmapVisualizationPlugin
  extends AbstractConfusionMatrixVisualizationPlugin {

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  protected String getMenuItemText() {
    return "Confusion matrix (heatmap)";
  }
  
  /**
   * Returns the initial size of the frame.
   * 
   * @return		the dimensions
   */
  @Override
  protected Dimension getFrameDimension() {
    return new Dimension(600, 400);
  }

  /**
   * Returns the visualization.
   * 
   * @param matrix	the matrix to use for the visualization
   * @return		the visualization
   */
  @Override
  protected HeatmapVisualization getVisualization() {
    return new HeatmapVisualization();
  }
}
