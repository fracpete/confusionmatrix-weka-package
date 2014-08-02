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
 * AbstractConfusionMatrixVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Ancestor for confusion matrix visualizations.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractConfusionMatrixVisualization 
  implements Serializable {

  /** for serialization. */
  private static final long serialVersionUID = 8547782264818608668L;

  /**
   * Generates a {@link JScrollPane} with proper scroll settings.
   * 
   * @param view	the component to add to the pane
   * @return		the scroll pane
   */
  protected JScrollPane createScrollPane(Component view) {
    JScrollPane		result;
    
    result = new JScrollPane(view);
    result.getVerticalScrollBar().setUnitIncrement(20);
    result.getHorizontalScrollBar().setUnitIncrement(20);
    result.getVerticalScrollBar().setBlockIncrement(100);
    result.getHorizontalScrollBar().setBlockIncrement(100);
    result.setWheelScrollingEnabled(true);
    
    return result;
  }
  
  /**
   * Returns the "save as" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  protected abstract JMenuItem getSaveAsMenuItem(final JFrame frame);
  
  /**
   * Generates the menubar for the frame.
   * 
   * @param frame	the frame
   * @return		the generate menu
   */
  public JMenuBar getMenuBar(final JFrame frame) {
    JMenuBar 	menubar;
    JMenu 	menu;
    JMenuItem 	menuitem;
    
    menubar = new JMenuBar();
    menu    = new JMenu("File");
    menubar.add(menu);
    
    menuitem = getSaveAsMenuItem(frame);
    if (menuitem != null) {
      menu.add(menuitem);
      menu.addSeparator();
    }

    menuitem = new JMenuItem("Close");
    menuitem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	frame.setVisible(false);
	frame.dispose();
      }
    });
    menu.add(menuitem);
    
    return menubar;
  }
  
  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  public abstract JPanel generate(ConfusionMatrix matrix);
}
