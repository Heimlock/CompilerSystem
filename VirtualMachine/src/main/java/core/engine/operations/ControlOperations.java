package core.engine.operations;

import core.engine.VirtualMachineEngine;
import data.impl.MemoryStack_Impl;
import data.impl.ProgramOutput_Impl;
import data.impl.UserInput_Impl;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class ControlOperations {

  protected static void start() {
    MemoryStack_Impl.getInstance().reset();
    ProgramOutput_Impl.getInstance().reset();
    UserInput_Impl.getInstance().reset();
  }

  protected static void hlt() {
    VirtualMachineEngine.getInstance().setRunning(Boolean.FALSE);
  }
}
