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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  
  /** the generated panel. */
  protected HeatmapPanel m_Heatmap;

  /** the panel with the options. */
  protected JPanel m_PanelOptions;
  
  /** the button for selecting the first color. */
  protected JButton m_ButtonFirst;
  
  /** the button for selecting the second color. */
  protected JButton m_ButtonSecond;
  
  /** the spinner for the size of the squares. */
  protected JSpinner m_SpinnerSize;
  
  /** the spinner for the number of colors. */
  protected JSpinner m_SpinnerNumColors;
  
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
   * Generates the heatmap panel.
   * 
   * @return		the panel
   */
  protected HeatmapPanel generateHeatmap() {
    m_Heatmap = new HeatmapPanel(m_Matrix);
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
	Color chosen = JColorChooser.showDialog(m_ButtonFirst, "Select first color", m_Heatmap.getFirstColor());
	if (chosen != null)
	  m_Heatmap.setFirstColor(chosen);
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
	Color chosen = JColorChooser.showDialog(m_ButtonSecond, "Select second color", m_Heatmap.getSecondColor());
	if (chosen != null)
	  m_Heatmap.setSecondColor(chosen);
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
    ((SpinnerNumberModel) m_SpinnerSize.getModel()).setValue(m_Heatmap.getSizeSquares());
    m_SpinnerSize.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
	m_Heatmap.setSizeSquares((Integer) ((SpinnerNumberModel) m_SpinnerSize.getModel()).getValue());
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
    ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).setValue(m_Heatmap.getNumColors());
    m_SpinnerNumColors.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
	m_Heatmap.setNumColors((Integer) ((SpinnerNumberModel) m_SpinnerNumColors.getModel()).getValue());
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
