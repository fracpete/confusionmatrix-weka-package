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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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

  /** the size of a single cell. */
  public final static int CELL_SIZE = 25;

  /** the number of colors. */
  public final static int NUM_COLORS = 256;

  /** the underlying matrix. */
  protected ConfusionMatrix m_Matrix;

  /** the image to display. */
  protected BufferedImage m_Image;
  
  /** the size of the squares. */
  protected int m_SizeSquares = CELL_SIZE;
  
  /** the number of colors. */
  protected int m_NumColors = NUM_COLORS;
  
  /** the first color. */
  protected Color m_ColorFirst = Color.WHITE;
  
  /** the second color. */
  protected Color m_ColorSecond = Color.BLACK;
  
  /**
   * Initializes the panel.
   * 
   * @param img	the image to display
   */
  public HeatmapPanel(ConfusionMatrix matrix) {
    m_Matrix = matrix;
    setImage(generateImage());
    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
	int x = e.getX() / m_SizeSquares;
	int y = e.getY() / m_SizeSquares;
	String tiptext = null;
	if ((x < m_Matrix.getNumClasses()) && (y < m_Matrix.getNumClasses())) {
	  tiptext = "act: " + m_Matrix.getLabels()[y] 
	      + ", pred: " + m_Matrix.getLabels()[x] 
		  + ", count: " + m_Matrix.getMatrix()[y][x];
	}
	setToolTipText(tiptext);
      }
    });
  }

  /**
   * Sets the first color to use.
   * 
   * @param value	the color
   */
  public void setFirstColor(Color value) {
    m_ColorFirst = value;
    update();
  }
  
  /**
   * Returns the first color.
   * 
   * @return		the color
   */
  public Color getFirstColor() {
    return m_ColorFirst;
  }

  /**
   * Sets the second color to use.
   * 
   * @param value	the color
   */
  public void setSecondColor(Color value) {
    m_ColorSecond = value;
    update();
  }
  
  /**
   * Returns the second color.
   * 
   * @return		the color
   */
  public Color getSecondColor() {
    return m_ColorSecond;
  }

  /**
   * Sets the size of the squares.
   * 
   * @param value	the size of the squares
   */
  public void setSizeSquares(int value) {
    if ((value >= 1) && (value <= 1000)) {
      m_SizeSquares = value;
      update();
    }
    else {
      System.err.println("Size of squares must satisfy: 1 <= x <= 1000");
    }
  }
  
  /**
   * Returns the size of the squares.
   * 
   * @return		the size of the squares
   */
  public int getSizeSquares() {
    return m_SizeSquares;
  }

  /**
   * Sets the number of colors to use.
   * 
   * @param value	the number of colors
   */
  public void setNumColors(int value) {
    if ((value >= 1) && (value <= 256)) {
      m_NumColors = value;
      update();
    }
    else {
      System.err.println("Number of colors must satisfy: 1 <= x <= 256");
    }
  }
  
  /**
   * Returns the number of colors to use.
   * 
   * @return		the number of colors
   */
  public int getNumColors() {
    return m_NumColors;
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
    image    = new BufferedImage(m_Matrix.getNumClasses() * m_SizeSquares, m_Matrix.getNumClasses() * m_SizeSquares, BufferedImage.TYPE_INT_ARGB);
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
	g.fillRect(n * m_SizeSquares, i * m_SizeSquares, m_SizeSquares, m_SizeSquares);
      }
    }
    g.dispose();
    
    return image;
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
   * Updates the image using the current parameters.
   */
  protected void update() {
    setImage(generateImage());
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