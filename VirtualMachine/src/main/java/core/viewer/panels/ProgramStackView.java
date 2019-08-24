/**
 *
 */
package core.viewer.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import core.engine.VirtualMachineEngine;
import data.impl.BreakPointList_Impl;
import data.impl.ProgramStack_Impl;
import data.interfaces.BreakPointList;
import data.interfaces.ProgramStack;
import data.interfaces.RefreshableView;
import utils.InterpreterUtils;

/**
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class ProgramStackView extends RefreshableView {
  private static final long serialVersionUID = -4550622448784509545L;
  private JTable programTable;
  private DefaultTableModel model;
  private JScrollPane scrollTable;

  /**
   * Create the panel.
   */
  public ProgramStackView() {
    initComponents();
    initListeners();
  }


  @Override
  public void refresh() {
    model.getDataVector().removeAllElements();
    addContent(ProgramStack_Impl.getInstance().getInstructions());
    //  TODO -- Highlight actual line
    //  TODO -- Highlight Breakpoints
  }

  public void addContent(List<String> programStack) {
    for (int line = 0; line < programStack.size(); line++) {
      model.addRow(InterpreterUtils.getFormattedRow(programStack.get(line), line));
    }
  }

  private void initListeners() {
    programTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getSource();
        Point point = mouseEvent.getPoint();
        int rowNumber = table.rowAtPoint(point);
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
          BreakPointList_Impl.getInstance().toggleBreak(rowNumber);
          //  This panel Correlates to the Breakpoint's One, So Update them all
          VirtualMachineEngine.getInstance().updateViewer();
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

    initTable();

    JLabel tittle = new JLabel("Instru\u00E7\u00F5es a serem executadas pela MV");
    tittle.setHorizontalAlignment(SwingConstants.CENTER);
    tittle.setFont(new Font("Tahoma", Font.PLAIN, 18));
    GridBagConstraints gbc_tittle = new GridBagConstraints();
    gbc_tittle.insets = new Insets(0, 0, 5, 0);
    gbc_tittle.gridx = 0;
    gbc_tittle.gridy = 0;
    add(tittle, gbc_tittle);
    GridBagConstraints gbc_programTable = new GridBagConstraints();
    gbc_programTable.fill = GridBagConstraints.BOTH;
    gbc_programTable.gridx = 0;
    gbc_programTable.gridy = 1;
    scrollTable = new JScrollPane(programTable);
    add(scrollTable, gbc_programTable);
  }

  private void initTable() {
    //  FIXME Replicar -- START
    model = new DefaultTableModel();
    model.addColumn("I");
    model.addColumn("Instru\u00E7\u00E3o");
    model.addColumn("Atributo #1");
    model.addColumn("Atributo #2");
    model.addColumn("Coment\u00E1rios");
    programTable = new JTable(model) {
      private static final long serialVersionUID = -6483103102847896595L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      };
    };
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 4555190414235458436L;

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        Color color = Color.WHITE;
        int fontStyle = Font.PLAIN; //  FIXME -- Not Changing Font Style

        BreakPointList breakpoints = BreakPointList_Impl.getInstance();
        ProgramStack programStack = ProgramStack_Impl.getInstance();

        this.setValue(table.getValueAt(row, 0));
        if (breakpoints.hasBreak(row)) {
          color = new Color(255, 204, 51);
          fontStyle = Font.BOLD;
        }
        if (programStack.getCounter() == row) {
          color = Color.GREEN;
          fontStyle = Font.ITALIC;
        }
        this.setValue(table.getValueAt(row, 0));
        this.setFont(this.getFont().deriveFont(fontStyle));
        setBackground(color);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      }
      
    };
    //  FIXME Replicar -- END
    programTable.getTableHeader().setReorderingAllowed(false);
    programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    defaultRenderer.setHorizontalAlignment(JLabel.CENTER);

    programTable.getColumnModel().getColumn(0).setResizable(false);
    programTable.getColumnModel().getColumn(0).setMinWidth(60);
    programTable.getColumnModel().getColumn(0).setMaxWidth(60);
    programTable.getColumnModel().getColumn(0).setCellRenderer(defaultRenderer);
    programTable.getColumnModel().getColumn(1).setResizable(false);
    programTable.getColumnModel().getColumn(1).setMinWidth(150);
    programTable.getColumnModel().getColumn(1).setMaxWidth(150);
    programTable.getColumnModel().getColumn(1).setCellRenderer(defaultRenderer);
    programTable.getColumnModel().getColumn(2).setResizable(false);
    programTable.getColumnModel().getColumn(2).setMinWidth(150);
    programTable.getColumnModel().getColumn(2).setMaxWidth(150);
    programTable.getColumnModel().getColumn(2).setCellRenderer(defaultRenderer);
    programTable.getColumnModel().getColumn(3).setResizable(false);
    programTable.getColumnModel().getColumn(3).setMinWidth(150);
    programTable.getColumnModel().getColumn(3).setMaxWidth(150);
    programTable.getColumnModel().getColumn(3).setCellRenderer(defaultRenderer);
    programTable.getColumnModel().getColumn(4).setResizable(false);
    programTable.getColumnModel().getColumn(4).setCellRenderer(defaultRenderer);
  }
}
