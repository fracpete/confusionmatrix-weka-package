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
 * TextVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Generates a simple textual representation.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TextVisualization
  extends AbstractConfusionMatrixVisualization {

  /** for serialization. */
  private static final long serialVersionUID = -6139634408453147499L;

  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  @Override
  public JPanel generate(ConfusionMatrix matrix) {
    JPanel		result;
    JTextArea		textArea;
    JScrollPane 	pane;
    
    result = new JPanel(new BorderLayout());
    textArea = new JTextArea();
    textArea.setFont(new Font("monospace", Font.PLAIN, 12));
    textArea.setEditable(false);
    
    // TODO
    
    pane = new JScrollPane(textArea);
    pane.getVerticalScrollBar().setUnitIncrement(20);
    pane.getHorizontalScrollBar().setUnitIncrement(20);
    pane.getVerticalScrollBar().setBlockIncrement(100);
    pane.getHorizontalScrollBar().setBlockIncrement(100);
    pane.setWheelScrollingEnabled(true);
    
    result.add(pane, BorderLayout.CENTER);

    return result;
  }
}
