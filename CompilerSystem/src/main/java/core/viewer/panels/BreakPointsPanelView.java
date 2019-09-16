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
import data.impl.BreakPointList_Impl;
import data.impl.ProgramStack_Impl;
import data.interfaces.RefreshableView;
import utils.InterpreterUtils;
import utils.StringUtils;

public class BreakPointsPanelView extends RefreshableView {
  private static final long serialVersionUID = 287537261117362179L;

  private final JFrame mainFrame = VirtualMachineEngine.getInstance().getViewer();
  private final String NEW_INPUT = "<< Nova Entrada >>";

  private JTable breakTable;
  private DefaultTableModel model;
  private JScrollPane scrollTable;

  /**
   * Create the panel.
   */
  public BreakPointsPanelView() {
    initComponents();
    initListeners();
  }

  @Override
  public void refresh() {
    model.getDataVector().removeAllElements();
    BreakPointList_Impl.getInstance().getAll().forEach(line -> {
      model.addRow(InterpreterUtils.getFormattedRow(String.format("%04d", line)));
    });
    model.addRow(InterpreterUtils.getFormattedRow(NEW_INPUT));
  }

  private void initListeners() {
    breakTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getSource();
        Point point = mouseEvent.getPoint();
        int rowNumber = table.rowAtPoint(point);
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && table.getRowCount() > 0) {
          String cellText = (String) table.getModel().getValueAt(rowNumber, 0);
          if (cellText != null && !cellText.isEmpty()) {
            if (cellText.equals(NEW_INPUT)) {
              Log.log(Level.INFO, "Add Input");
              String value = JOptionPane.showInputDialog(mainFrame, "Entre com a Linha do BreakPoint", "Novo BreakPoint", JOptionPane.QUESTION_MESSAGE);
              Log.log(Level.INFO, String.format("New Input Value %s", value));

              try {
                if (StringUtils.isNumeric(value)) {
                  Integer newBreak = Integer.valueOf(value);
                  if (newBreak >= 0 && newBreak < ProgramStack_Impl.getInstance().getInstructions().size()) {
                    BreakPointList_Impl.getInstance().addBreak(newBreak);
                  } else {
                    //  FIXME -- Throw Error
                    JOptionPane.showMessageDialog(mainFrame, "Linha fora dos Limites", "BreakPoint Error", JOptionPane.ERROR_MESSAGE);
                  }
                } else {
                  //  FIXME -- Throw Error
                  JOptionPane.showMessageDialog(mainFrame, "Linha fora dos Limites", "BreakPoint Error", JOptionPane.ERROR_MESSAGE);
                }
              } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Valor Invalido", "BreakPoint Error", JOptionPane.ERROR_MESSAGE);
              }
            } else {
              Log.log(Level.INFO, "remove Break");
              Log.log(Level.INFO, String.format("Row #%02d - RowValue: %s", rowNumber, cellText));
              Integer line = Integer.valueOf((String) table.getModel().getValueAt(rowNumber, 0));
              BreakPointList_Impl.getInstance().removeBreak(line);
            }
            //  This panel Correlates to the ProgramStack's One, So Update them all
            VirtualMachineEngine.getInstance().updateViewer();
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

    JLabel tittle = new JLabel("Break Point's");
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
    scrollTable = new JScrollPane(breakTable);
    add(scrollTable, gbc_InputTable);
  }

  private void initTable() {
    //  FIXME Replicar -- START
    model = new DefaultTableModel();
    model.addColumn("Linha");
    breakTable = new JTable(model) {
      private static final long serialVersionUID = -6483103102847896595L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      };
    };
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
    //  FIXME Replicar -- END
    breakTable.getTableHeader().setReorderingAllowed(false);
    breakTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);

    for (int column = 0; column < breakTable.getColumnCount(); column++) {
      breakTable.getColumnModel().getColumn(column).setResizable(false);
      breakTable.getColumnModel().getColumn(column).setCellRenderer(defaultRenderer);
    }
  }
}
