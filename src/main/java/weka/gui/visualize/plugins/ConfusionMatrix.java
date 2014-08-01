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
 * ConfusionMatrix.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package weka.gui.visualize.plugins;

import java.io.Serializable;
import java.util.List;

import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;

/**
 * Represents a confusion matrix.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ConfusionMatrix
  implements Serializable {

  /** for serialization. */
  private static final long serialVersionUID = -2212913330894559303L;

  /** the predictions. */
  protected List<Prediction> m_Predictions;
  
  /** the class attribute. */
  protected Attribute m_ClassAttribute;
  
  /** the matrix. */
  protected int[][] m_Matrix;
  
  /** the labels. */
  protected String[] m_Labels;
  
  /**
   * Initializes the matrix.
   * 
   * @param preds	the predictions
   * @param classAtt	the class attribute
   */
  public ConfusionMatrix(List<Prediction> preds, Attribute classAtt) {
    super();
    m_Predictions    = preds;
    m_ClassAttribute = classAtt;
    initialize();
  }
  
  /**
   * Initializes the matrix.
   */
  protected void initialize() {
    int		i;
    
    // labels
    m_Labels = new String[m_ClassAttribute.numValues()];
    for (i = 0; i < m_ClassAttribute.numValues(); i++)
      m_Labels[i] = m_ClassAttribute.value(i);
    
    // matrix
    m_Matrix = new int[m_ClassAttribute.numValues()][m_ClassAttribute.numValues()];
    for (Prediction pred: m_Predictions)
      m_Matrix[(int) pred.actual()][(int) pred.predicted()] += pred.weight();
  }
  
  /**
   * Returns the matrix.
   * 
   * @return		the matrix
   */
  public int[][] getMatrix() {
    return m_Matrix;
  }
  
  /**
   * Returns the class labels.
   * 
   * @return		the labels
   */
  public String[] getLabels() {
    return m_Labels;
  }
}
