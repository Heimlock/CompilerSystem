package core.engine.operations;

import core.engine.VirtualMachineEngine;
import data.impl.MemoryStack_Impl;
import data.impl.ProgramOutput_Impl;
import data.impl.ProgramStack_Impl;
import data.impl.UserInput_Impl;
import data.interfaces.MemoryStack;
import data.interfaces.ProgramStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class ControlOperations {
  private static ProgramStack program = ProgramStack_Impl.getInstance();
  private static MemoryStack memory = MemoryStack_Impl.getInstance();

  protected static void start() {
    MemoryStack_Impl.getInstance().reset();
    ProgramOutput_Impl.getInstance().reset();
    UserInput_Impl.getInstance().reset();
  }

  protected static void hlt() {
    VirtualMachineEngine.getInstance().setRunning(Boolean.FALSE);
  }

  /*
   * i:=M[s]; s:=s - 1
   */
  protected static void returns() {
    int a = memory.pop();
    program.setCounter(a);
  }

  protected static void returnF(int m, int n) {
    int result = memory.pop();
    if (m != -1 && n != -1) {
      Operations.DALLOC.compute(m, n);
    }
    returns();
    memory.push(result);
  }
}
