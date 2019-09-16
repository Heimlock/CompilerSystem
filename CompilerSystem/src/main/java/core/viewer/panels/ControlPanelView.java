/**
 * 
 */
package core.viewer.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import core.engine.VirtualMachineEngine;
import data.impl.MemoryStack_Impl;
import data.impl.ProgramStack_Impl;
import data.interfaces.MemoryStack;
import data.interfaces.ProgramStack;
import data.interfaces.RefreshableView;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 1 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class ControlPanelView extends RefreshableView {
  private static final long serialVersionUID = 6895258125928686061L;

  private JLabel lblProgramCounterValue;
  private JLabel lblStackCounterValue;
  private JLabel lblRunningState;
  private JLabel lblRunningStateValue;

  private ProgramStack program;
  private MemoryStack memory;
  private VirtualMachineEngine vmEngine;

  /**
   * Create the panel.
   */
  public ControlPanelView() {
    program = ProgramStack_Impl.getInstance();
    memory = MemoryStack_Impl.getInstance();
    vmEngine = VirtualMachineEngine.getInstance();
    initComponents();
  }

  private void initComponents() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 35, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 37, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel lblProgramCounter = new JLabel("Program Counter");
    GridBagConstraints gbc_lblProgramCounter = new GridBagConstraints();
    gbc_lblProgramCounter.insets = new Insets(0, 0, 5, 5);
    gbc_lblProgramCounter.gridx = 1;
    gbc_lblProgramCounter.gridy = 1;
    add(lblProgramCounter, gbc_lblProgramCounter);

    lblProgramCounterValue = new JLabel("XX");
    GridBagConstraints gbc_lblProgramCounterValue = new GridBagConstraints();
    gbc_lblProgramCounterValue.insets = new Insets(0, 0, 5, 5);
    gbc_lblProgramCounterValue.gridx = 2;
    gbc_lblProgramCounterValue.gridy = 1;
    add(lblProgramCounterValue, gbc_lblProgramCounterValue);

    JLabel lblStackCounter = new JLabel("Stack Counter");
    GridBagConstraints gbc_lblStackCounter = new GridBagConstraints();
    gbc_lblStackCounter.insets = new Insets(0, 0, 5, 5);
    gbc_lblStackCounter.gridx = 1;
    gbc_lblStackCounter.gridy = 2;
    add(lblStackCounter, gbc_lblStackCounter);

    lblStackCounterValue = new JLabel("XX");
    GridBagConstraints gbc_lblStackCounterValue = new GridBagConstraints();
    gbc_lblStackCounterValue.insets = new Insets(0, 0, 5, 5);
    gbc_lblStackCounterValue.gridx = 2;
    gbc_lblStackCounterValue.gridy = 2;
    add(lblStackCounterValue, gbc_lblStackCounterValue);

    lblRunningState = new JLabel("Running State");
    GridBagConstraints gbc_lblRunningState = new GridBagConstraints();
    gbc_lblRunningState.insets = new Insets(0, 0, 5, 5);
    gbc_lblRunningState.gridx = 1;
    gbc_lblRunningState.gridy = 3;
    add(lblRunningState, gbc_lblRunningState);

    lblRunningStateValue = new JLabel("Stopped");
    GridBagConstraints gbc_lblRunningStateValue = new GridBagConstraints();
    gbc_lblRunningStateValue.insets = new Insets(0, 0, 5, 5);
    gbc_lblRunningStateValue.gridx = 2;
    gbc_lblRunningStateValue.gridy = 3;
    add(lblRunningStateValue, gbc_lblRunningStateValue);

  }

  @Override
  public void refresh() {
    lblProgramCounterValue.setText(String.format("%d", program.getCounter()));
    lblStackCounterValue.setText(String.format("%d", memory.getCounter()));
    lblRunningStateValue.setText(vmEngine.isRunning() ? "Running" : "Stopped");
  }

}
