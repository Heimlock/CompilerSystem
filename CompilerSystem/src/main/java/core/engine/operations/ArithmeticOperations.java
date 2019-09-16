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
public class ArithmeticOperations {
  private static MemoryStack memory = MemoryStack_Impl.getInstance();
  private static int a, b, result;

  private static void before() {
    a = memory.pop(); // M[S]
    b = memory.pop(); // M[S-1]
  }

  private static void after() {
    memory.push(result);
  }

  /*
   * M[s-1]:=M[s-1] + M[s]; s:=s - 1
   */
  protected static void add() {
    before();
    result = b + a;
    after();
  }

  /*
   * M[s-1]:=M[s-1] - M[s]; s:=s - 1
   */
  protected static void sub() {
    before();
    result = b - a;
    after();
  }

  /*
   * M[s-1]:=M[s-1] * M[s]; s:=s - 1
   */
  protected static void mult() {
    before();
    result = b * a;
    after();
  }

  /*
   * M[s-1]:=M[s-1] / M[s]; s:=s - 1
   */
  protected static void divi() {
    before();
    result = b / a;
    after();
  }

  /*
   * M[s]:= -M[s]
   */
  protected static void inv() {
    result = -memory.pop();
    after();
  }
}
