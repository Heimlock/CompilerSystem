/**
 *
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public interface ProgramStack {

  public int getCounter();

  public List<String> getInstructions();

  public void setCounter(int value);

  public String getInstruction(int index);

  public String nextInstruction();

  public int getLabel(String label);

  public void putData(int index, String value);

  public void push(String value);
}
