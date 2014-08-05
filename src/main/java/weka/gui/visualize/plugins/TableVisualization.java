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
 * TableVisualization.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import weka.core.Utils;
import weka.gui.ExtensionFileFilter;
import weka.gui.JTableHelper;

/**
 * Generates a simple representation using a table.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TableVisualization
  extends AbstractConfusionMatrixVisualization {

  /** for serialization. */
  private static final long serialVersionUID = -6139634408453147499L;

  /** the table with the confusion matrix. */
  protected JTable m_Table;

  /** the model for the table. */
  protected DefaultTableModel m_Model;

  /** the file chooser for saving the content. */
  protected JFileChooser m_FileChooser;

  /**
   * Returns the text for the menu item.
   * 
   * @return		the text
   */
  @Override
  public String getMenuItemText() {
    return "Table";
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
      filter = new ExtensionFileFilter("csv", "CSV files (*.csv)");
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
    int			i;
    int			n;
    Object		cell;
    
    writer = null;
    try {
      result = true;
      writer = new BufferedWriter(new FileWriter(file));
      // header
      for (n = 0; n < m_Model.getColumnCount(); n++) {
	if (n > 0)
	  writer.write(",");
	writer.write(Utils.quote(m_Model.getColumnName(n)));
      }
      writer.newLine();
      // data
      for (i = 0; i < m_Model.getRowCount(); i++) {
	for (n = 0; n < m_Model.getColumnCount(); n++) {
	  if (n > 0)
	    writer.write(",");
	  cell = m_Model.getValueAt(i, n);
	  if (cell != null)
	    writer.write(Utils.quote(cell.toString()));
	}
	writer.newLine();
      }
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
   * Returns the "print" menu item.
   * 
   * @param frame	the frame
   * @return		the generate menu item, null if not available
   */
  @Override
  protected JMenuItem getPrintMenuItem(final JFrame frame) {
    JMenuItem	result;
    
    result = new JMenuItem("Print...");
    result.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	try {
	  m_Table.print();
	}
	catch (Exception ex) {
	  JOptionPane.showMessageDialog(m_Table, "Failed to print!\n" + ex);
	  ex.printStackTrace();
	}
      }
    });
    
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
	int retVal = fileChooser.showSaveDialog(m_Table);
	if (retVal != JFileChooser.APPROVE_OPTION)
	  return;
	if (!save(fileChooser.getSelectedFile()))
	  JOptionPane.showMessageDialog(m_Table, "Failed to save content to " + fileChooser.getSelectedFile() + "!");
      }
    });
    
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
    JPanel		result;
    Vector<String>	cols;
    int			i;
    int			n;
    
    result = new JPanel(new BorderLayout());

    m_Model = new DefaultTableModel(matrix.getNumClasses(), matrix.getNumClasses() + 4);
    cols = new Vector<String>();
    cols.addAll(Arrays.asList(matrix.getLabels()));
    cols.add("<-- classified as");
    cols.add("incorrect");
    cols.add("correct");
    cols.add("total");
    m_Model.setColumnIdentifiers(cols);
    
    for (i = 0; i < matrix.getNumClasses(); i++)
      m_Model.setValueAt(matrix.getLabels()[i], i, matrix.getNumClasses());

    for (i = 0; i < matrix.getNumClasses(); i++) {
      for (n = 0; n < matrix.getNumClasses(); n++)
	m_Model.setValueAt(matrix.getMatrix()[i][n], i, n);
      m_Model.setValueAt(matrix.getIncorrect(i), i, matrix.getNumClasses() + 1);
      m_Model.setValueAt(matrix.getCorrect(i), i, matrix.getNumClasses() + 2);
      m_Model.setValueAt(matrix.getTotal(i), i, matrix.getNumClasses() + 3);
    }
    
    m_Table = new JTable(m_Model);
    m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JTableHelper.setOptimalColumnWidth(m_Table);
    
    result.add(createScrollPane(m_Table), BorderLayout.CENTER);

    return result;
  }
}
