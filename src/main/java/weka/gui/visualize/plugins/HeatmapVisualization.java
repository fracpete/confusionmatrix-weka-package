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
 * HeatmapVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Visualizes the confusion matrix as heatmap.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class HeatmapVisualization
  extends AbstractConfusionMatrixVisualization {

  /** for serialization. */
  private static final long serialVersionUID = 6739403493248911860L;

  /** the size of a single cell. */
  public final static int CELL_SIZE = 10;

  /** the number of colors. */
  public final static int NUM_COLORS = 256;
  
  /** the generated panel. */
  protected HeatmapPanel m_Heatmap;

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  public String getMenuItemText() {
    return "Heatmap";
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
   * Returns the "save as" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  @Override
  protected JMenuItem getSaveAsMenuItem(JFrame frame) {
    JMenuItem	result;
    
    result = new JMenuItem("Save as...");
    result.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	if (m_Heatmap == null)
	  return;
	m_Heatmap.saveComponent();
      }
    });

    return result;
  }

  /**
   * Performs the actual generation.
   *
   * @param first	the first color
   * @param second	the second color
   * @param num		the number of colors to generate
   * @return		the generated colors
   */
  protected Color[] generateColors(Color first, Color second, int num) {
    Color[]	result;
    int		red1;
    int		red2;
    int		redNew;
    int		green1;
    int		green2;
    int		greenNew;
    int		blue1;
    int		blue2;
    int		blueNew;
    int		i;
    double	step;

    result = new Color[num];
    red1   = first.getRed();
    green1 = first.getGreen();
    blue1  = first.getBlue();

    red2   = second.getRed();
    green2 = second.getGreen();
    blue2  = second.getBlue();

    step   = 1.0 / num;

    for (i = 0; i < num; i++) {
      redNew   = (int) (red1   + ((red2   < red1)   ? -i : i) * step * Math.abs(red2   - red1));
      greenNew = (int) (green1 + ((green2 < green1) ? -i : i) * step * Math.abs(green2 - green1));
      blueNew  = (int) (blue1  + ((blue2  < blue1)  ? -i : i) * step * Math.abs(blue2  - blue1));

      result[i] = new Color(redNew, greenNew, blueNew);
    }

    return result;
  }

  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  @Override
  public JPanel generate(ConfusionMatrix matrix) {
    BufferedImage	image;
    int			i;
    int			n;	
    double		min;
    double		max;
    double		binWidth;
    Color[]		colors;
    int			bin;
    Graphics		g;
    
    // create heatmap image
    image    = new BufferedImage(matrix.getNumClasses() * CELL_SIZE, matrix.getNumClasses() * CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
    g        = image.createGraphics();
    min      = matrix.getMin();
    max      = matrix.getMax();
    binWidth = (max - min) / NUM_COLORS;
    colors   = generateColors(Color.BLACK, Color.WHITE, NUM_COLORS);
    for (i = 0; i < matrix.getNumClasses(); i++) {
      for (n = 0; n < matrix.getNumClasses(); n++) {
	bin = (int) Math.floor((matrix.getMatrix()[i][n] - min) / binWidth);
	// max belongs in the top-most bin
	if (bin == NUM_COLORS)
	  bin--;
	g.setColor(colors[bin]);
	g.fillRect(n * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
    }
    g.dispose();

    // create panel
    m_Heatmap = new HeatmapPanel(image);
    
    return m_Heatmap;
  }
}
