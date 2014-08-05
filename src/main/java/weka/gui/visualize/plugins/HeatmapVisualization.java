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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

  /** the panel with the options. */
  protected JPanel m_PanelOptions;
  
  /** the button for selecting the first color. */
  protected JButton m_ButtonFirst;
  
  /** the first color. */
  protected Color m_ColorFirst = Color.BLACK;
  
  /** the button for selecting the second color. */
  protected JButton m_ButtonSecond;
  
  /** the second color. */
  protected Color m_ColorSecond = Color.WHITE;
  
  /** the spinner for the size of the squares. */
  protected JSpinner m_SpinnerSize;
  
  /** the size of the squares. */
  protected int m_Size = CELL_SIZE;
  
  /** the spinner for the number of colors. */
  protected JSpinner m_SpinnerNumColors;
  
  /** the number of colors. */
  protected int m_NumColors = NUM_COLORS;
  
  /** the underlying matrix. */
  protected ConfusionMatrix m_Matrix;
  
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
    return new Dimension(800, 400);
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
   * Returns the "print" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  @Override
  protected JMenuItem getPrintMenuItem(final JFrame frame) {
    return null;
  }

  /**
   * Updates the image using the current parameters.
   */
  protected void update() {
    m_Heatmap.setImage(generateImage());
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
   * Generates the heatmap image.
   * 
   * @return		the image
   */
  protected BufferedImage generateImage() {
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
    image    = new BufferedImage(m_Matrix.getNumClasses() * m_Size, m_Matrix.getNumClasses() * m_Size, BufferedImage.TYPE_INT_ARGB);
    g        = image.createGraphics();
    min      = m_Matrix.getMin();
    max      = m_Matrix.getMax();
    binWidth = (max - min) / m_NumColors;
    colors   = generateColors(m_ColorFirst, m_ColorSecond, m_NumColors);
    for (i = 0; i < m_Matrix.getNumClasses(); i++) {
      for (n = 0; n < m_Matrix.getNumClasses(); n++) {
	bin = (int) Math.floor((m_Matrix.getMatrix()[i][n] - min) / binWidth);
	// max belongs in the top-most bin
	if (bin == m_NumColors)
	  bin--;
	g.setColor(colors[bin]);
	g.fillRect(n * m_Size, i * m_Size, m_Size, m_Size);
      }
    }
    g.dispose();
    
    return image;
  }
  
  /**
   * Generates the heatmap panel.
   * 
   * @return		the panel
   */
  protected HeatmapPanel generateHeatmap() {
    m_Heatmap = new HeatmapPanel(generateImage());
    return m_Heatmap;
  }

  /**
   * Generats the options panel.
   * 
   * @return		the panel
   */
  protected JPanel generateOptions() {
    JPanel	result;
    JPanel	options;
    JPanel	option;
    JLabel	label;
    
    result = new JPanel(new BorderLayout());
    options = new JPanel(new GridLayout(4, 1));
    result.add(options, BorderLayout.NORTH);
    
    // first color
    option = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_ButtonFirst = new JButton("First color");
    m_ButtonFirst.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	Color chosen = JColorChooser.showDialog(m_ButtonFirst, "Select first color", m_ColorFirst);
	if (chosen != null)
	  m_ColorFirst = chosen;
	update();
      }
    });
    option.add(m_ButtonFirst);
    options.add(option);
    
    // second color
    option = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_ButtonSecond = new JButton("Second color");
    m_ButtonSecond.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	Color chosen = JColorChooser.showDialog(m_ButtonSecond, "Select second color", m_ColorSecond);
	if (chosen != null)
	  m_ColorSecond = chosen;
	update();
      }
    });
    option.add(m_ButtonSecond);
    options.add(option);
    
    // size
    option = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_SpinnerSize = new JSpinner();
    m_SpinnerSize.setPreferredSize(new Dimension(50, 20));
    ((SpinnerNumberModel) m_SpinnerSize.getModel()).setMinimum(1);
    ((SpinnerNumberModel) m_SpinnerSize.getModel()).setMaximum(1000);
    ((SpinnerNumberModel) m_SpinnerSize.getModel()).setValue(CELL_SIZE);
    m_SpinnerSize.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
	m_Size = (Integer) ((SpinnerNumberModel) m_SpinnerSize.getModel()).getValue();
	update();
      }
    });
    label = new JLabel("Size of squares");
    label.setLabelFor(m_SpinnerSize);
    option.add(m_SpinnerSize);
    options.add(option);
    
    // number of colors
    option = new JPanel(new FlowLayout(FlowLayout.LEFT));
    m_SpinnerNumColors = new JSpinner();
    m_SpinnerNumColors.setPreferredSize(new Dimension(50, 20));
    ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).setMinimum(1);
    ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).setMaximum(256);
    ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).setValue(NUM_COLORS);
    m_SpinnerNumColors.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
	m_NumColors = (Integer) ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).getValue();
	update();
      }
    });
    label = new JLabel("# of colors");
    label.setLabelFor(m_SpinnerNumColors);
    option.add(m_SpinnerNumColors);
    options.add(option);
    
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
    JPanel	result;
    
    m_Matrix = matrix;
    result   = new JPanel(new BorderLayout());
    result.add(createScrollPane(generateHeatmap()), BorderLayout.CENTER);
    result.add(createScrollPane(generateOptions()), BorderLayout.EAST);
    
    return result;
  }
}
