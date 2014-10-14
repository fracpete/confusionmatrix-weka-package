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
 * HeatmapVisualizationScaled.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import javax.swing.JPanel;

/**
 * Visualizes the confusion matrix as heatmap. Scales the rows by the sum
 * of instances that have this class label. Useful when class distributions
 * are skewed.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class HeatmapVisualizationScaled
  extends HeatmapVisualization {
  
  /** for serialization. */
  private static final long serialVersionUID = 1251550468136172540L;

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  public String getMenuItemText() {
    return "Heatmap (scaled)";
  }

  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  @Override
  public JPanel generate(ConfusionMatrix matrix) {
    matrix = matrix.clone();
    matrix.scaleRows();
    return super.generate(matrix);
  }
}
