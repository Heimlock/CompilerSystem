/**
 * 
 */
package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.interfaces.UserInput;

/**
 * @author Felipe / 15 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class UserInput_Impl implements UserInput {
  private static UserInput_Impl instance = null;
  private List<Integer> userInputValues;
  private Integer readCounter;

  public static UserInput_Impl getInstance() {
    if (instance == null) {
      instance = new UserInput_Impl();
    }
    return instance;
  }

  private UserInput_Impl() {
    this.userInputValues = new ArrayList<>();
    reset();
  }

  /* (non-Javadoc)
   * @see data.interfaces.UserInput#add(int)
   */
  @Override
  public void add(int value) {
    this.userInputValues.add(value);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.UserInput#getNextValue()
   */
  @Override
  public int getNextValue() {
    Integer result = null;
    if (this.userInputValues.size() > this.readCounter) {
      result = this.userInputValues.get(this.readCounter++);
    } else {
      //FIXME Throw Error
    }
    return result;
  }

  /* (non-Javadoc)
   * @see data.interfaces.UserInput#getCounter()
   */
  @Override
  public int getCounter() {
    return this.readCounter;
  }

  /* (non-Javadoc)
   * @see data.interfaces.UserInput#getAll()
   */
  @Override
  public List<Integer> getAll() {
    return Collections.unmodifiableList(userInputValues);
  }


  @Override
  public void changeValue(int oldIndex, int newValue) {
    userInputValues.set(oldIndex, newValue);
  }

  @Override
  public void remove(int index) {
    userInputValues.remove(index);
  }


  /*
   * (non-Javadoc)
   * @see data.interfaces.UserInput#reset()
   */
  @Override
  public void reset() {
    readCounter = 0;
  }

}
