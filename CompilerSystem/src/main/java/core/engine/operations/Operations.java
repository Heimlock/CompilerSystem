package core.engine.operations;

import static data.OperationType.Arithmetic;
import static data.OperationType.Comparison;
import static data.OperationType.Control;
import static data.OperationType.IO;
import static data.OperationType.Jump;
import static data.OperationType.Logical;
import static data.OperationType.Memory;

import java.util.Optional;

import data.OperationType;
import data.interfaces.DoOperation;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public enum Operations implements DoOperation {
  //  Control
  START(Control, (data) -> ControlOperations.start()),
  HLT(Control, (data) -> ControlOperations.hlt()),
  NULL(Control),

  //	Memory
  LDC(Memory, (data) -> MemoryOperations.ldc(data[0])),
  LDV(Memory, (data) -> MemoryOperations.ldv(data[0])),
  STR(Memory, (data) -> MemoryOperations.str(data[0])),
  ALLOC(Memory, (data) -> MemoryOperations.alloc(data[0], data[1])),
  DALLOC(Memory, (data) -> MemoryOperations.dalloc(data[0], data[1])),

  //	Arithmetic
  ADD(Arithmetic, (data) -> ArithmeticOperations.add()),
  SUB(Arithmetic, (data) -> ArithmeticOperations.sub()),
  MULT(Arithmetic, (data) -> ArithmeticOperations.mult()),
  DIVI(Arithmetic, (data) -> ArithmeticOperations.divi()),
  INV(Arithmetic, (data) -> ArithmeticOperations.inv()),

  //	Logical
  AND(Logical, (data) -> LogicalOperations.and()),
  OR(Logical, (data) -> LogicalOperations.or()),
  NEG(Logical, (data) -> LogicalOperations.neg()),

  //	Comparison
  CME(Comparison, (data) -> ComparisonOperations.cme()),
  CMA(Comparison, (data) -> ComparisonOperations.cma()),
  CEQ(Comparison, (data) -> ComparisonOperations.ceq()),
  CDIF(Comparison, (data) -> ComparisonOperations.cdif()),
  CMEQ(Comparison, (data) -> ComparisonOperations.cmeq()),
  CMAQ(Comparison, (data) -> ComparisonOperations.cmaq()),

  //	Jump
  JMP(Jump, (data) -> JumpOperations.jmp(data[0])),
  JMPF(Jump, (data) -> JumpOperations.jmpf(data[0])),
  CALL(Jump, (data) -> JumpOperations.call(data[0])),
  RETURN(Jump, (data) -> JumpOperations.returns()),
  RETURNF(Jump, (data) -> JumpOperations.returnF(data[0], data[1])),

  //	I/O
  RD(IO, (data) -> InputOutputOperations.rd()),
  PRN(IO, (data) -> InputOutputOperations.prn()),
  ;

  private OperationType type;
  private Optional<DoOperation> operation;

  private Operations(OperationType type) {
    this.type = type;
    this.operation = Optional.empty();
  }

  private Operations(OperationType type, DoOperation operation) {
    this.type = type;
    this.operation = Optional.of(operation);
  }

  public OperationType getType() {
    return this.type;
  }

  public static Operations getOperation(String name) {
    Operations result = null;
    for (Operations item : values()) {
      if (name.equals(item.name())) {
        result = item;
      }
    }
    return result;
  }

  @Override
  public void compute(int... data) {
    this.operation.ifPresent((e) -> e.compute(data));
  }
}
