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
public class MemoryOperations {
  private static MemoryStack memory = MemoryStack_Impl.getInstance();

  /*
   * S:=s + 1 ; M [s]: = k
   */
  protected static void ldc(int k) {
    memory.push(k);
  }

  /*
   * S:=s + 1 ; M[s]:=M[n]
   */
  protected static void ldv(int n) {
    memory.push(memory.getData(n));
  }

  /*
   * M[n]:=M[s]; s:=s-1
   */
  protected static void str(int n) {
    int a = memory.pop();
    memory.putData(n, a);
  }

  /*
   * Para k:=0 até n-1 faca
   * {s:=s + 1; M[s]:=M[m+k]}
   */
  protected static void alloc(int m, int n) {
    int k, value;
    int s;
    for (k = 0; k <= n - 1; k++) {
      memory.push(0);
      s = memory.getCounter() - 1;
      value = memory.getData(m + k);
      memory.putData(s, value);
    }
  }

  /*
   * Para k:=n-1 até 0 faca
   * {M[m+k]:=M[s]; s:=s - 1}
   */
  protected static void dalloc(int m, int n) {
    int k, value;
    for (k = n - 1; k >= 0; k--) {
      value = memory.pop();
      memory.putData(m + k, value);
    }
  }
}
