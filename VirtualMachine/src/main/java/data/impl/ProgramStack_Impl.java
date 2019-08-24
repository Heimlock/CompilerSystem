package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.engine.operations.Operations;
import data.interfaces.ProgramStack;
import utils.InterpreterUtils;

public class ProgramStack_Impl implements ProgramStack {
  private static ProgramStack_Impl instance;
  private Integer programCounter;
  private List<String> stack;

  public static ProgramStack_Impl getInstance() {
    if (instance == null) {
      instance = new ProgramStack_Impl();
    }
    return instance;
  }

  private ProgramStack_Impl() {
    this.programCounter = 0;
    this.stack = new ArrayList<>();
  }

  @Override
  public int getCounter() {
    return programCounter;
  }

  @Override
  public List<String> getInstructions() {
    return Collections.unmodifiableList(stack);
  }

  @Override
  public void setCounter(int value) {
    this.programCounter = value;
  }

  @Override
  public String getInstruction(int index) {
    return this.stack.get(index);
  }

  @Override
  public String nextInstruction() {
    return this.stack.get(programCounter++);
  }

  @Override
  public void putData(int index, String value) {
    try {
      this.stack.set(index, value);
    } catch (IndexOutOfBoundsException e) {
      this.stack.add(index, value);
    }
  }

  @Override
  public void push(String value) {
    this.stack.add(value);
  }

  @Override
  public int getLabel(String label) {
    Integer row = null;
    Operations operation;
    String instruction;

    for (int index = 0; index < stack.size(); index++) {
      instruction = stack.get(index);
      operation = InterpreterUtils.getOperation(instruction);
      if (operation.equals(Operations.NULL) && InterpreterUtils.getLabel(instruction).equals(label)) {
        row = index;
        break;
      }
    }
    return row;
  }
}
