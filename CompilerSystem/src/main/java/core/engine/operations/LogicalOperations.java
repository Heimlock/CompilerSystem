package core.engine.operations;

import data.impl.MemoryStack_Impl;
import data.interfaces.MemoryStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class LogicalOperations {
  private static MemoryStack memory = MemoryStack_Impl.getInstance();
  private static boolean a, b, result;

  private static void before() {
    a = memory.pop() == 1 ? Boolean.TRUE : Boolean.FALSE; // M[S]
    b = memory.pop() == 1 ? Boolean.TRUE : Boolean.FALSE; // M[S-1]
  }

  private static void after() {
    memory.push(result ? 1 : 0);
  }

  /*
   * M[s-1]:=M[s-1] && M[s]; s:=s - 1
   */
  protected static void and() {
    before();
    result = b && a;
    after();
  }

  /*
   * M[s-1]:=M[s-1] || M[s]; s:=s - 1
   */
  protected static void or() {
    before();
    result = b || a;
    after();
  }

  /*
   * M[s]:=1 - M[s]
   */
  protected static void neg() {
    a = memory.pop() == 1 ? Boolean.TRUE : Boolean.FALSE; // M[S]
    result = !a;
    after();
  }
}
