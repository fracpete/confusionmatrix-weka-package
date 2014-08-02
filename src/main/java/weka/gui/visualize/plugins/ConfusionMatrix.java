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
import weka.core.Utils;

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
  protected double[][] m_Matrix;
  
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
    m_Matrix = new double[m_ClassAttribute.numValues()][m_ClassAttribute.numValues()];
    for (Prediction pred: m_Predictions)
      m_Matrix[(int) pred.actual()][(int) pred.predicted()] += pred.weight();
  }
  
  /**
   * Returns the class attribute.
   * 
   * @return		the attribute
   */
  public Attribute getClassAttribute() {
    return m_ClassAttribute;
  }
  
  /**
   * Returns the matrix.
   * 
   * @return		the matrix
   */
  public double[][] getMatrix() {
    return m_Matrix;
  }
  
  /**
   * Returns the number of classes.
   * 
   * @return		the number of classes
   */
  public int getNumClasses() {
    return m_ClassAttribute.numValues();
  }
  
  /**
   * Returns the class labels.
   * 
   * @return		the labels
   */
  public String[] getLabels() {
    return m_Labels;
  }
  
  /**
   * Returns the total count for the specified class label.
   * 
   * @param index	the 0-based class label
   * @return		the count
   */
  public double getTotal(int index) {
    return Utils.sum(m_Matrix[index]);
  }
  
  /**
   * Returns the total count for all class labels.
   * 
   * @return		the count
   */
  public double getTotal() {
    double	result;
    int		i;
    
    result = 0;
    for (i = 0; i < getNumClasses(); i++)
      result += getTotal(i);
    
    return result;
  }
  
  /**
   * Returns the correct count for the specified class label.
   * 
   * @param index	the 0-based class label
   * @return		the count
   */
  public double getCorrect(int index) {
    return m_Matrix[index][index];
  }
  
  /**
   * Returns the correct count for all class labels.
   * 
   * @return		the count
   */
  public double getCorrect() {
    double	result;
    int		i;
    
    result = 0;
    for (i = 0; i < getNumClasses(); i++)
      result += getCorrect(i);
    
    return result;
  }

  /**
   * Returns the incorrect (= misclassified) count for the specified class label.
   * 
   * @param index	the 0-based class label
   * @return		the count
   */
  public double getIncorrect(int index) {
    return getTotal(index) - getCorrect(index);
  }
  
  /**
   * Returns the incorrect count for all class labels.
   * 
   * @return		the count
   */
  public double getIncorrect() {
    double	result;
    int		i;
    
    result = 0;
    for (i = 0; i < getNumClasses(); i++)
      result += getIncorrect(i);
    
    return result;
  }
  
  /**
   * Returns the maximum count in the matrix.
   * 
   * @return		the count
   */
  public double getMax() {
    double	result;
    int		i;
    int		n;
    
    result = 0;
    for (i = 0; i < getNumClasses(); i++) {
      for (n = 0; n < getNumClasses(); n++)
	result = Math.max(result, m_Matrix[i][n]);
    }
    
    return result;
  }
  
  /**
   * Returns the minimum count in the matrix.
   * 
   * @return		the count
   */
  public double getMin() {
    double	result;
    int		i;
    int		n;
    
    result = 0;
    for (i = 0; i < getNumClasses(); i++) {
      for (n = 0; n < getNumClasses(); n++)
	result = Math.min(result, m_Matrix[i][n]);
    }
    
    return result;
  }
}
