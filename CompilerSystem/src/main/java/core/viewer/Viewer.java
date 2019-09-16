/**
 *
 */
package core.viewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.viewer.panels.BreakPointsPanelView;
import core.viewer.panels.ControlPanelView;
import core.viewer.panels.InputPanelView;
import core.viewer.panels.MemoryStackView;
import core.viewer.panels.MenuBar;
import core.viewer.panels.OutputPanelView;
import core.viewer.panels.ProgramStackView;
import data.interfaces.RefreshableView;

/**
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class Viewer extends JFrame {
  private static final long serialVersionUID = -5606629778426340157L;

  private JPanel contentPane;
  private JMenuBar menuBar = new MenuBar();
  private RefreshableView programStack;
  private RefreshableView memoryStack;
  private RefreshableView inputPanel;
  private RefreshableView outputPanel;
  private RefreshableView breakPointsPanel;
  private RefreshableView controlPanel;

  public Viewer() {
    setJMenuBar(menuBar);
    initComponents();
    setVisible(true);
  }

  public void refresh() {
    programStack.refresh();
    memoryStack.refresh();
    inputPanel.refresh();
    outputPanel.refresh();
    breakPointsPanel.refresh();
    controlPanel.refresh();
  }

  private void initComponents() {
    try {
      String icon = "./assets/icon.png"; //  TODO -- Unify Data Resource Strings
      ImageIcon img = new ImageIcon(this.getClass().getResource(icon));
      this.setIconImage(img.getImage());
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Maquina Virtual");

    setBounds(100, 100, 1280, 800);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 0, 300, 300, 300, 30, 275, 0, 0 };
    gbl_contentPane.rowHeights = new int[] { 0, 460, 0, 70, 70, 70, 0, 0 };
    gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    contentPane.setLayout(gbl_contentPane);

    programStack = new ProgramStackView();
    GridBagConstraints gbc_table = new GridBagConstraints();
    gbc_table.insets = new Insets(0, 0, 5, 5);
    gbc_table.gridwidth = 3;
    gbc_table.fill = GridBagConstraints.BOTH;
    gbc_table.gridx = 1;
    gbc_table.gridy = 1;
    contentPane.add(programStack, gbc_table);

    memoryStack = new MemoryStackView();
    GridBagConstraints gbc_memoryStack = new GridBagConstraints();
    gbc_memoryStack.insets = new Insets(0, 0, 5, 5);
    gbc_memoryStack.fill = GridBagConstraints.BOTH;
    gbc_memoryStack.gridx = 5;
    gbc_memoryStack.gridy = 1;
    contentPane.add(memoryStack, gbc_memoryStack);

    inputPanel = new InputPanelView();
    GridBagConstraints gbc_inputPanel = new GridBagConstraints();
    gbc_inputPanel.gridheight = 3;
    gbc_inputPanel.insets = new Insets(0, 0, 5, 5);
    gbc_inputPanel.fill = GridBagConstraints.BOTH;
    gbc_inputPanel.gridx = 1;
    gbc_inputPanel.gridy = 3;
    contentPane.add(inputPanel, gbc_inputPanel);

    outputPanel = new OutputPanelView();
    GridBagConstraints gbc_outputPanel = new GridBagConstraints();
    gbc_outputPanel.gridheight = 3;
    gbc_outputPanel.insets = new Insets(0, 0, 5, 5);
    gbc_outputPanel.fill = GridBagConstraints.BOTH;
    gbc_outputPanel.gridx = 2;
    gbc_outputPanel.gridy = 3;
    contentPane.add(outputPanel, gbc_outputPanel);

    breakPointsPanel = new BreakPointsPanelView();
    GridBagConstraints gbc_breakPointsPanel = new GridBagConstraints();
    gbc_breakPointsPanel.gridheight = 3;
    gbc_breakPointsPanel.insets = new Insets(0, 0, 5, 5);
    gbc_breakPointsPanel.fill = GridBagConstraints.BOTH;
    gbc_breakPointsPanel.gridx = 3;
    gbc_breakPointsPanel.gridy = 3;
    contentPane.add(breakPointsPanel, gbc_breakPointsPanel);

    controlPanel = new ControlPanelView();
    GridBagConstraints gbc_controlPanel = new GridBagConstraints();
    gbc_controlPanel.insets = new Insets(0, 0, 5, 5);
    gbc_controlPanel.gridheight = 3;
    gbc_controlPanel.fill = GridBagConstraints.BOTH;
    gbc_controlPanel.gridx = 5;
    gbc_controlPanel.gridy = 3;
    contentPane.add(controlPanel, gbc_controlPanel);
  }
}
