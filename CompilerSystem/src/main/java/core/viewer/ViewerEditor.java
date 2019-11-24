/**
 *
 */
package core.viewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 9 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class ViewerEditor extends JFrame {
  private static final long serialVersionUID = -6343319701690480782L;

  public ViewerEditor() {
    initComponents();
  }

  private void initComponents() {
    setBounds(100, 100, 944, 450);
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    getContentPane().setLayout(gridBagLayout);

    JPanel panel = new JPanel();
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.fill = GridBagConstraints.BOTH;
    gbc_panel.gridx = 1;
    gbc_panel.gridy = 1;
    getContentPane().add(panel, gbc_panel);
  }
}
