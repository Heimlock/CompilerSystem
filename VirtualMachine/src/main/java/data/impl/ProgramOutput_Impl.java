/**
 * 
 */
package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.interfaces.ProgramOutput;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 18 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class ProgramOutput_Impl implements ProgramOutput {
  private static ProgramOutput_Impl instance = null;
  private List<Integer> outputData;

  public static ProgramOutput_Impl getInstance() {
    if (instance == null) {
      instance = new ProgramOutput_Impl();
    }
    return instance;
  }

  private ProgramOutput_Impl() {
    outputData = new ArrayList<>();
  }

  @Override
  public void push(int value) {
    outputData.add(value);
  }

  @Override
  public List<Integer> getAll() {
    return Collections.unmodifiableList(outputData);
  }

  @Override
  public void reset() {
    outputData.clear();
  }

}
