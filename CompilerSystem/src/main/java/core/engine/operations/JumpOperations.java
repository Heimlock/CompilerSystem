package core.engine.operations;

import data.impl.MemoryStack_Impl;
import data.impl.ProgramStack_Impl;
import data.interfaces.MemoryStack;
import data.interfaces.ProgramStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class JumpOperations {
  private static ProgramStack program = ProgramStack_Impl.getInstance();
  private static MemoryStack memory = MemoryStack_Impl.getInstance();

  /*
   * i:= t
   */
  protected static void jmp(int t) {
    program.setCounter(t);
  }

  /*
   * se M[s] = 0 então i:=t senão i:=i + 1;
   * s:=s-1
   */
  protected static void jmpf(int t) {
    int a = memory.pop();
    if (a == 0) {
      program.setCounter(t);
    } else {
      // TODO -- Avaliar se necessaria alguma acao 
    }
  }

  /*
   * S:=s + 1; M[s]:=i + 1; i:=t
   */
  protected static void call(int t) {
    memory.push(program.getCounter() + 1);
    program.setCounter(t);
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
    Operations.DALLOC.compute(m, n);
    returns();
    memory.push(result);
  }
}
