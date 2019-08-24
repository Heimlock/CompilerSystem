/**
 * 
 */
package core.engine;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import core.engine.file.FileInterpreter;
import core.engine.operations.Operations;
import core.viewer.Viewer;
import data.OperationType;
import data.impl.BreakPointList_Impl;
import data.impl.MemoryStack_Impl;
import data.impl.ProgramOutput_Impl;
import data.impl.ProgramStack_Impl;
import data.impl.UserInput_Impl;
import data.interfaces.BreakPointList;
import data.interfaces.MemoryStack;
import data.interfaces.ProgramOutput;
import data.interfaces.ProgramStack;
import data.interfaces.UserInput;
import utils.InterpreterUtils;

/**
 * @author 16116469 / 15 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class VirtualMachineEngine {
  private static VirtualMachineEngine instance = null;

  private BreakPointList breakpoints;
  private UserInput userInput;
  private ProgramOutput programOutput;
  private MemoryStack memoryStack;
  private ProgramStack programStack;

  private Boolean running; //  Controls BreakPoints
  private Boolean ignoreBreaks; //  Controls BreakPoints

  private Viewer viewerInstance;

  public static VirtualMachineEngine getInstance() {
    if (instance == null) {
      instance = new VirtualMachineEngine();
    }
    return instance;
  }

  private VirtualMachineEngine() {
    //  Sigletons
    this.memoryStack = MemoryStack_Impl.getInstance();
    this.programStack = ProgramStack_Impl.getInstance();
    this.userInput = UserInput_Impl.getInstance();
    this.programOutput = ProgramOutput_Impl.getInstance();
    this.breakpoints = BreakPointList_Impl.getInstance();

    //  Flags
    this.running = Boolean.FALSE;
    this.ignoreBreaks = Boolean.FALSE;
  }

  public void doCycle() {
    String instruction = programStack.nextInstruction();
    Operations operation = InterpreterUtils.getOperation(instruction);

    //  Do Instruction
    if (operation.getType() == OperationType.Jump) {
      operation.compute(programStack.getLabel(InterpreterUtils.getLabel(instruction)));
    } else {
      operation.compute(InterpreterUtils.getArgs(instruction));
    }

    //  Stop if it has a BreakPoint
    if (breakpoints.hasBreak(programStack.getCounter()) || ignoreBreaks) {
      setRunning(Boolean.FALSE);
    }

    //  TODO Update Viewer
    viewerInstance.refresh();
  }

  public Boolean isRunning() {
    return this.running;
  }

  public void setRunning(Boolean state) {
    this.running = state;
  }

  public void toggleBreak() {
    this.ignoreBreaks = !this.ignoreBreaks;
  }

  public boolean getIgnoreBreaks() {
    return this.ignoreBreaks;
  }

  public void setViewerInstance(Viewer viewer) {
    this.viewerInstance = viewer;
  }

  public void updateViewer() {
    this.viewerInstance.refresh();
  }

  public void reset() {
    this.memoryStack.reset();
    this.programStack.setCounter(0);
    //    this.userInput.reset();
    this.programOutput.reset();
  }

  public JFrame getViewer() {
    return viewerInstance;
  }

  public void stub() {
    try {
      FileInterpreter file = new FileInterpreter("./assets/testeAssembly.obj");
      file.parseOperations();
      setRunning(Boolean.TRUE);

      userInput.add(8);
      //      for (int i = 0; i < 10; i++) {
      //        memoryStack.push(i);
      //        userInput.add(i);
      //        programOutput.push(i);
      //      }
      VirtualMachineEngine.getInstance().updateViewer();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
