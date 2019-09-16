/**
 *
 */
package core.viewer.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.impl.MemoryStack_Impl;
import data.interfaces.RefreshableView;
import utils.InterpreterUtils;

/**
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class MemoryStackView extends RefreshableView {
  private static final long serialVersionUID = -2748121729478531602L;
  private JTable memoryTable;
  private DefaultTableModel model;
  private JScrollPane scrollTable;

  /**
   * Create the panel.
   */
  public MemoryStackView() {
    initComponents();
  }

  @Override
  public void refresh() {
    model.getDataVector().removeAllElements();
    List<Integer> memoryStack = MemoryStack_Impl.getInstance().getAll();
    for (int line = 0; line < memoryStack.size(); line++) {
      model.addRow(InterpreterUtils.getFormattedRow(String.valueOf(line), memoryStack.get(line).toString()));
    }
  }

  private void initComponents() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);
    
    initTable();

    JLabel tittle = new JLabel("Conte\u00FAdo da Pilha");
    tittle.setHorizontalAlignment(SwingConstants.CENTER);
    tittle.setFont(new Font("Tahoma", Font.PLAIN, 18));
    GridBagConstraints gbc_tittle = new GridBagConstraints();
    gbc_tittle.insets = new Insets(0, 0, 5, 0);
    gbc_tittle.gridx = 0;
    gbc_tittle.gridy = 0;
    add(tittle, gbc_tittle);
    GridBagConstraints gbc_memoryTable = new GridBagConstraints();
    gbc_memoryTable.fill = GridBagConstraints.BOTH;
    gbc_memoryTable.gridx = 0;
    gbc_memoryTable.gridy = 1;
    scrollTable = new JScrollPane(memoryTable);
    add(scrollTable, gbc_memoryTable);
  }

  private void initTable() {
    //  FIXME Replicar -- START
    model = new DefaultTableModel();
    model.addColumn("Endere\u00E7o (S)");
    model.addColumn("Valor");
    memoryTable = new JTable(model) {
      private static final long serialVersionUID = -6483103102847896595L;
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      };
    };
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
    //  FIXME Replicar -- END
    memoryTable.getTableHeader().setReorderingAllowed(false);
    memoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);

    for (int column = 0; column < memoryTable.getColumnCount(); column++) {
      memoryTable.getColumnModel().getColumn(column).setResizable(false);
      memoryTable.getColumnModel().getColumn(column).setCellRenderer(defaultRenderer);
    }
  }

}
