package core.engine.operations;

import data.impl.MemoryStack_Impl;
import data.interfaces.MemoryStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 11 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class ComparisonOperations {
  private static MemoryStack memory = MemoryStack_Impl.getInstance();
  private static int a, b;
  private static boolean result;

  private static void before() {
    a = memory.pop(); // M[S]
    b = memory.pop(); // M[S-1]
  }

  private static void after() {
    memory.push(result ? 1 : 0);
  }

  /*
   * se M[s-1] < M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void cme() {
    before();
    result = b < a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }

  /*
   * se M[s-1] > M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void cma() {
    before();
    result = b > a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }

  /*
   * se M[s-1] = M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void ceq() {
    before();
    result = b == a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }

  /*
   * se M[s-1] != M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void cdif() {
    before();
    result = b != a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }

  /*
   * se M[s-1] <= M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void cmeq() {
    before();
    result = b <= a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }

  /*
   * se M[s-1] >= M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
   */
  protected static void cmaq() {
    before();
    result = b >= a ? Boolean.TRUE : Boolean.FALSE;
    after();
  }
}
