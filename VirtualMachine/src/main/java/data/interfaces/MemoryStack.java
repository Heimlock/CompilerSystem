/**
 * 
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 10 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public interface MemoryStack {
  public int getCounter();

  public void setCounter(int value);

  public int getData(int index);

  public void putData(int index, int value);

  public void push(int data);

  public int pop();

  public List<Integer> getAll();

  public void reset();
}
