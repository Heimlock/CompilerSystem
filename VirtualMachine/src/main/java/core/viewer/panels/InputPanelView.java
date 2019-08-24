/**
 *
 */
package core.viewer.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import core.engine.VirtualMachineEngine;
import data.impl.UserInput_Impl;
import data.interfaces.RefreshableView;
import utils.InterpreterUtils;
import utils.StringUtils;

/**
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class InputPanelView extends RefreshableView {
  private static final long serialVersionUID = -2748662461659075299L;
  private JTable inputTable;
  private DefaultTableModel model;
  private JScrollPane scrollTable;

  private final JFrame mainFrame = VirtualMachineEngine.getInstance().getViewer();
  private final String NEW_INPUT = "<< Nova Entrada >>";

  /**
   * Create the panel.
   */
  public InputPanelView() {
    initComponents();
    initListeners();
  }

  @Override
  public void refresh() {
    model.getDataVector().removeAllElements();
    UserInput_Impl.getInstance().getAll().forEach(line -> {
      model.addRow(InterpreterUtils.getFormattedRow(String.format("%04d", line)));
    });
    model.addRow(InterpreterUtils.getFormattedRow(NEW_INPUT));
  }

  private void initListeners() {
    inputTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getSource();
        Point point = mouseEvent.getPoint();
        int rowNumber = table.rowAtPoint(point);
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
          String cellText = (String) table.getModel().getValueAt(rowNumber, 0);
          if (cellText != null && !cellText.isEmpty()) {
            if (cellText.equals(NEW_INPUT)) {
              Log.log(Level.INFO, "Add Input");
              String value = JOptionPane.showInputDialog(mainFrame, "Entre com o Valor da Nova Entrada", "Nova Entrada", JOptionPane.QUESTION_MESSAGE);
              if (StringUtils.isNumeric(value)) {
                Log.log(Level.INFO, String.format("New Input Value %s", value));
                UserInput_Impl.getInstance().add(Integer.valueOf(value));
              }
            } else {
              Log.log(Level.INFO, "Change or Delete Input");
              String newValue = JOptionPane.showInputDialog(mainFrame, "Alterar valor ou 'x' para Deletar.", cellText);
              Log.log(Level.INFO, String.format("Change or Delete Input Value %s", newValue));
              if (newValue.equals("x")) {
                UserInput_Impl.getInstance().remove(rowNumber);
              } else {
                UserInput_Impl.getInstance().changeValue(rowNumber, Integer.valueOf(newValue));
              }
            }
            refresh();
          }
        }
      }
    });
  }

  private void initComponents() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel tittle = new JLabel("Janela de Entrada");
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
    scrollTable = new JScrollPane(inputTable);
    add(scrollTable, gbc_InputTable);
  }

  private void initTable() {
    //  FIXME Replicar -- START
    model = new DefaultTableModel();
    model.addColumn("Valor");
    inputTable = new JTable(model) {
      private static final long serialVersionUID = -6483103102847896595L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      };
    };
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
    //  FIXME Replicar -- END
    inputTable.getTableHeader().setReorderingAllowed(false);
    inputTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);

    for (int column = 0; column < inputTable.getColumnCount(); column++) {
      inputTable.getColumnModel().getColumn(column).setResizable(false);
      inputTable.getColumnModel().getColumn(column).setCellRenderer(defaultRenderer);
    }
  }

}
