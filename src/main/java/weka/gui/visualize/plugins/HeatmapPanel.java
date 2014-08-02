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
 * HeatmapPanel.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import weka.gui.visualize.PrintablePanel;

/**
 * Panel for displaying a heatmap image.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class HeatmapPanel 
  extends PrintablePanel {
  
  /** for serialization. */
  private static final long serialVersionUID = 4727155732097501101L;
  
  /** the image to display. */
  protected BufferedImage m_Image;
  
  /**
   * Initializes the panel.
   * 
   * @param img	the image to display
   */
  public HeatmapPanel(BufferedImage img) {
    setImage(img);
  }
  
  /**
   * Sets the image to display.
   * 
   * @param value	the image
   */
  public void setImage(BufferedImage value) {
    m_Image = value;
    if (m_Image == null) {
      setSize(new Dimension(600, 400));
      setMinimumSize(new Dimension(600, 400));
      setPreferredSize(new Dimension(600, 400));
    }
    else {
      setSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
      setMinimumSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
      setPreferredSize(new Dimension(m_Image.getWidth(), m_Image.getHeight()));
    }
    repaint();
  }
  
  /**
   * Returns the current image.
   * 
   * @return		the image, null if not available
   */
  public BufferedImage getImage() {
    return m_Image;
  }
  
  /**
   * Paints the component.
   * 
   * @param g		the graphics context
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // clear
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // paint image
    if (m_Image != null)
      g.drawImage(m_Image, 0, 0, null);
  }
}