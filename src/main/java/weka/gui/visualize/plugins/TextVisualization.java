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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import weka.core.Utils;
import weka.gui.ExtensionFileFilter;

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

  /** the text area with the confusion matrix. */
  protected JTextArea m_TextArea;

  /** the file chooser for saving the content. */
  protected JFileChooser m_FileChooser;

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  public String getMenuItemText() {
    return "Text";
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
   * Returns the file chooser to use, creates one if necessary.
   * 
   * @return		the file chooser
   */
  protected JFileChooser getFileChooser() {
    ExtensionFileFilter	filter;
    
    if (m_FileChooser == null) {
      m_FileChooser = new JFileChooser();
      filter = new ExtensionFileFilter("txt", "Text files (*.txt)");
      m_FileChooser.addChoosableFileFilter(filter);
      m_FileChooser.setFileFilter(filter);
    }
    
    return m_FileChooser;
  }
  
  /**
   * Saves the current content to the specified file.
   * 
   * @param file	the file to save the content to
   * @return		true if successfully written
   */
  protected boolean save(File file) {
    boolean		result;
    BufferedWriter	writer;
    
    writer = null;
    try {
      result = true;
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(m_TextArea.getText());
      writer.newLine();
      writer.flush();
    }
    catch (Exception e) {
      System.err.println("Failed to write content to " + file + "!");
      e.printStackTrace();
      result = false;
    }
    finally {
      if (writer != null) {
	try {
	  writer.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
    }
    
    return result;
  }
  
  /**
   * Returns the "save as" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  @Override
  protected JMenuItem getSaveAsMenuItem(final JFrame frame) {
    JMenuItem	result;
    
    result = new JMenuItem("Save as...");
    result.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	JFileChooser fileChooser = getFileChooser();
	int retVal = fileChooser.showSaveDialog(m_TextArea);
	if (retVal != JFileChooser.APPROVE_OPTION)
	  return;
	if (!save(fileChooser.getSelectedFile()))
	  JOptionPane.showMessageDialog(m_TextArea, "Failed to save content to " + fileChooser.getSelectedFile() + "!");
      }
    });
    
    return result;
  }

  /**
   * Method for generating indices for the confusion matrix.
   * <p/>
   * Taken from weka.classifiers.evaluation.Evaluation#num2ShortID
   *
   * @param num integer to format
   * @param IDChars the characters to use
   * @param IDWidth the width of the entry
   * @return the formatted integer as a string
   */
  protected String num2ShortID(int num, char[] IDChars, int IDWidth) {

    char ID[] = new char[IDWidth];
    int i;

    for (i = IDWidth - 1; i >= 0; i--) {
      ID[i] = IDChars[num % IDChars.length];
      num = num / IDChars.length - 1;
      if (num < 0) {
        break;
      }
    }
    for (i--; i >= 0; i--) {
      ID[i] = ' ';
    }

    return new String(ID);
  }

  /**
   * Generates the textual representation of the matrix.
   * <p/>
   * Adapted from {@link weka.classifiers.evaluation.Evaluation#toSummaryString(String, boolean)}
   * 
   * @param matrix	the matrix to use
   * @return		the generated representation
   */
  protected String doGenerate(ConfusionMatrix matrix) {
    StringBuilder result = new StringBuilder();
    char[] IDChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    int IDWidth;
    boolean fractional = false;

    // Find the maximum value in the matrix
    // and check for fractional display requirement
    double maxval = 0;
    for (int i = 0; i < matrix.getNumClasses(); i++) {
      for (int j = 0; j < matrix.getNumClasses(); j++) {
        double current = matrix.getMatrix()[i][j];
        if (current < 0) {
          current *= -10;
        }
        if (current > maxval) {
          maxval = current;
        }
        double fract = current - Math.rint(current);
        if (!fractional && ((Math.log(fract) / Math.log(10)) >= -2)) {
          fractional = true;
        }
      }
    }

    IDWidth = 1 + Math.max(
      (int) (Math.log(maxval) / Math.log(10) + (fractional ? 3 : 0)),
      (int) (Math.log(matrix.getNumClasses()) / Math.log(IDChars.length)));
    for (int i = 0; i < matrix.getNumClasses(); i++) {
      result.append(" ");
      if (fractional) {
        result.append(num2ShortID(i, IDChars, IDWidth - 3));
        result.append("   ");
      }
      else {
        result.append(num2ShortID(i, IDChars, IDWidth));
      }
    }
    result.append("   <-- classified as [incorr/correct/total]\n");
    for (int i = 0; i < matrix.getNumClasses(); i++) {
      for (int j = 0; j < matrix.getNumClasses(); j++) {
        result.append(" ");
        result.append(
          Utils.doubleToString(matrix.getMatrix()[i][j], IDWidth, (fractional ? 2 : 0)));
      }
      result.append(" | ");
      result.append(num2ShortID(i, IDChars, IDWidth));
      result.append(" = ");
      result.append(matrix.getLabels()[i]);
      result.append(" [");
      result.append(Utils.doubleToString(matrix.getIncorrect(i), 3));
      result.append("/");
      result.append(Utils.doubleToString(matrix.getCorrect(i), 3));
      result.append("/");
      result.append(Utils.doubleToString(matrix.getTotal(i), 3));
      result.append("]");
      result.append("\n");
    }
    return result.toString();
  }
  
  /**
   * Generates the visualization.
   * 
   * @param matrix	the matrix to visualize
   * @return		the panel with the visualization
   */
  @Override
  public JPanel generate(ConfusionMatrix matrix) {
    JPanel		result;
    
    result = new JPanel(new BorderLayout());

    m_TextArea = new JTextArea();
    m_TextArea.setFont(new Font("monospaced", Font.PLAIN, 12));
    m_TextArea.setEditable(false);
    m_TextArea.setText(doGenerate(matrix));
    
    result.add(createScrollPane(m_TextArea), BorderLayout.CENTER);

    return result;
  }
}
