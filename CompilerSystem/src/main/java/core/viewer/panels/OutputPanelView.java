package core.viewer.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.impl.ProgramOutput_Impl;
import data.interfaces.RefreshableView;
import utils.InterpreterUtils;

public class OutputPanelView extends RefreshableView {
  private static final long serialVersionUID = -2962934083177007985L;
  private JTable outputTable;
  private DefaultTableModel model;
  private JScrollPane scrollTable;

  /**
   * Create the panel.
   */
  public OutputPanelView() {
    initComponents();
  }

  @Override
  public void refresh() {
    model.getDataVector().removeAllElements();
    ProgramOutput_Impl.getInstance().getAll().forEach(line -> {
      model.addRow(InterpreterUtils.getFormattedRow(String.format("%04d", line)));
    });
  }

  private void initComponents() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel tittle = new JLabel("Janela de Sa\u00EDda");
    tittle.setFont(new Font("Tahoma", Font.PLAIN, 18));
    tittle.setHorizontalAlignment(SwingConstants.CENTER);
    GridBagConstraints gbc_tittle = new GridBagConstraints();
    gbc_tittle.anchor = GridBagConstraints.SOUTH;
    gbc_tittle.insets = new Insets(0, 0, 5, 0);
    gbc_tittle.gridx = 0;
    gbc_tittle.gridy = 0;
    add(tittle, gbc_tittle);

    initTable();

    GridBagConstraints gbc_InputTable = new GridBagConstraints();
    gbc_InputTable.fill = GridBagConstraints.BOTH;
    gbc_InputTable.gridx = 0;
    gbc_InputTable.gridy = 1;
    scrollTable = new JScrollPane(outputTable);
    add(scrollTable, gbc_InputTable);

  }

  private void initTable() {
    //  FIXME Replicar -- START
    model = new DefaultTableModel();
    model.addColumn("Valor");
    outputTable = new JTable(model) {
      private static final long serialVersionUID = -6483103102847896595L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      };
    };
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
    //  FIXME Replicar -- END
    outputTable.getTableHeader().setReorderingAllowed(false);
    outputTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);

    for (int column = 0; column < outputTable.getColumnCount(); column++) {
      outputTable.getColumnModel().getColumn(column).setResizable(false);
      outputTable.getColumnModel().getColumn(column).setCellRenderer(defaultRenderer);
    }
  }
}
