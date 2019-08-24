package core.engine.operations;

import data.impl.MemoryStack_Impl;
import data.impl.ProgramOutput_Impl;
import data.impl.UserInput_Impl;
import data.interfaces.MemoryStack;
import data.interfaces.ProgramOutput;
import data.interfaces.UserInput;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class InputOutputOperations {
  private static MemoryStack memory = MemoryStack_Impl.getInstance();
  private static ProgramOutput ouput = ProgramOutput_Impl.getInstance();
  private static UserInput input = UserInput_Impl.getInstance();

  protected static void rd() {
    Integer value = input.getNextValue();
    memory.push(value);
  }

  protected static void prn() {
    Integer value = memory.pop();
    ouput.push(value);
  }
}
