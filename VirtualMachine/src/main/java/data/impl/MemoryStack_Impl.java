package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.interfaces.MemoryStack;

public class MemoryStack_Impl implements MemoryStack {
  private static MemoryStack_Impl instance;
  private int memoryCounter;
  private List<Integer> stack;

  public static MemoryStack_Impl getInstance() {
    if (instance == null) {
      instance = new MemoryStack_Impl();
    }
    return instance;
  }

  private MemoryStack_Impl() {
    this.memoryCounter = 0;
    this.stack = new ArrayList<>();
  }

  @Override
  public int getCounter() {
    return memoryCounter;
  }

  @Override
  public void setCounter(int value) {
    this.memoryCounter = value;
  }

  @Override
  public int getData(int index) {
    return this.stack.get(index);
  }

  @Override
  public void putData(int index, int value) {
    try {
      this.stack.set(index, value);
    } catch (IndexOutOfBoundsException e) {
      push(value);
    }
  }

  @Override
  public void push(int data) {
    this.stack.add(data);
    this.memoryCounter++;
  }

  @Override
  public int pop() {
    Integer result = null;
    if (this.memoryCounter > 0 && this.memoryCounter <= this.stack.size()) {
      result = this.stack.remove(--this.memoryCounter);
    } else {
      //  TODO -- Throw Error
    }
    return result;
  }

  @Override
  public List<Integer> getAll() {
    return Collections.unmodifiableList(stack);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.MemoryStack#reset()
   */
  @Override
  public void reset() {
    memoryCounter = 0;
    stack.clear();
  }
}
