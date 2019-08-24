/**
 * 
 */
package data.impl;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import data.interfaces.BreakPointList;

/**
 * @author Felipe / 15 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class BreakPointList_Impl implements BreakPointList {
  private static BreakPointList_Impl instance;
  private SortedSet<Integer> breakpoints;
  private Boolean skipAll;

  public static BreakPointList_Impl getInstance() {
    if (instance == null) {
      instance = new BreakPointList_Impl();
    }
    return instance;
  }

  private BreakPointList_Impl() {
    this.breakpoints = new TreeSet<>();
    this.skipAll = Boolean.FALSE;
  }

  /* (non-Javadoc)
   * @see data.interfaces.BreakPointList#addBreak(int)
   */
  @Override
  public void addBreak(int line) {
    if (hasBreak(line)) {
      //  TODO -- Throw Error
    } else {
      this.breakpoints.add(line);
    }
  }

  /* (non-Javadoc)
   * @see data.interfaces.BreakPointList#removeBreak(int)
   */
  @Override
  public void removeBreak(int line) {
    if (hasBreak(line)) {
      this.breakpoints.remove(line); //  TODO -- Verify if it works
    } else {
      //  TODO -- Throw Error
    }
  }

  /* (non-Javadoc)
   * @see data.interfaces.BreakPointList#hasBreak(int)
   */
  @Override
  public Boolean hasBreak(int line) {
    return breakpoints.contains(line) && !this.skipAll;
  }

  /* (non-Javadoc)
   * @see data.interfaces.BreakPointList#getAll()
   */
  @Override
  public List<Integer> getAll() {
    return breakpoints.stream().collect(Collectors.toList());
  }

  @Override
  public void setSkipAll(Boolean state) {
    this.skipAll = state;
  }

  @Override
  public void toggleBreak(int line) {
    if (hasBreak(line)) {
      removeBreak(line);
    } else {
      addBreak(line);
    }
  }

}
