package core.viewer.panels;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import core.engine.VirtualMachineEngine;
import core.engine.file.FileInterpreter;

public class MenuBar extends JMenuBar {
  private static final long serialVersionUID = -8230365528787030614L;

  private static Logger Log = Logger.getGlobal();
  //	SubMenus
  private JMenu fileMenu;
  private JMenu execMenu;
  private JMenu breakPointMenu;
  private JMenu aboutMenu;
  //	Itens
  private JMenuItem openFileAction;
  private JMenuItem runProgramAction;
  private JMenuItem stopProgramAction;
  private JMenuItem stepBreakAction;
  private JMenuItem resumeAction;
  private JMenuItem skipAllBreakAction;
  //  Debug Actions
  private JMenu debugMenu;
  private JMenuItem updateDebugAction;

  public MenuBar() {
    initComponents();
    initMnemonics();
    initListeners();
  }

  private void initComponents() {
    fileMenu = new JMenu("Arquivos");
    execMenu = new JMenu("Executar");
    breakPointMenu = new JMenu("BreakPoints");
    aboutMenu = new JMenu("Sobre");

    openFileAction = new JMenuItem("Abrir Arquivo");

    runProgramAction = new JMenuItem("Iniciar Execução");
    stopProgramAction = new JMenuItem("Parar Execução");

    resumeAction = new JMenuItem("Resume");
    stepBreakAction = new JMenuItem("Step");
    skipAllBreakAction = new JMenuItem("Skip All");
  }

  private void initMnemonics() {
    //	Mnemônicas
    fileMenu.setMnemonic(KeyEvent.VK_T);
    execMenu.setMnemonic(KeyEvent.VK_O);

    openFileAction.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));

    runProgramAction.setAccelerator(KeyStroke.getKeyStroke("F4"));
    stopProgramAction.setAccelerator(KeyStroke.getKeyStroke("F12"));

    stepBreakAction.setAccelerator(KeyStroke.getKeyStroke("F6"));
    resumeAction.setAccelerator(KeyStroke.getKeyStroke("F8"));

    //	Adding Itens
    fileMenu.add(openFileAction);
    fileMenu.add(runProgramAction);

    execMenu.add(runProgramAction);
    execMenu.add(stopProgramAction);

    breakPointMenu.add(resumeAction);
    breakPointMenu.add(stepBreakAction);
    breakPointMenu.add(skipAllBreakAction);

    add(fileMenu);
    add(execMenu);
    add(breakPointMenu);
    add(aboutMenu);

    //  Debug --  Start
    debugMenu = new JMenu("Debug");
    updateDebugAction = new JMenuItem("Update ViewerVirtualMachine");
    updateDebugAction.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
    updateDebugAction.addActionListener(evt -> {
      Log.log(Level.INFO, "updateDebugAction");
      VirtualMachineEngine.getInstance().updateViewer();
    });
    add(debugMenu);
    debugMenu.add(updateDebugAction);
    //  Debug --  End
  }

  private void initListeners() {
    VirtualMachineEngine vmEngine = VirtualMachineEngine.getInstance();

    openFileAction.addActionListener(evt -> {
      Log.log(Level.INFO, "openFileAction");
      try {
        VirtualMachineEngine.getInstance().reset();
        FileInterpreter interpreter = new FileInterpreter();
        interpreter.parseOperations();
        VirtualMachineEngine.getInstance().updateViewer();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    runProgramAction.addActionListener(evt -> {
      Log.log(Level.INFO, "runProgramAction");
      vmEngine.setRunning(Boolean.TRUE);
      vmEngine.reset();
    });

    stopProgramAction.addActionListener(evt -> {
      Log.log(Level.INFO, "stopProgramAction");
      vmEngine.setRunning(Boolean.FALSE);
    });

    stepBreakAction.addActionListener(evt -> {
      Log.log(Level.INFO, "stepBreakAction");
      vmEngine.doCycle();
    });

    resumeAction.addActionListener(evt -> {
      Log.log(Level.INFO, "resumeAction");
      vmEngine.setRunning(Boolean.TRUE);
    });

    skipAllBreakAction.addActionListener(evt -> {
      Log.log(Level.INFO, "skipAllBreakAction");
      vmEngine.toggleBreak();
    });
  }
}