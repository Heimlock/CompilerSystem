/**
 * 
 */
package data.interfaces;

import java.util.List;

/**
 * @author Felipe / 15 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public interface BreakPointList {
  void addBreak(int line);

  void removeBreak(int line);

  Boolean hasBreak(int line);

  List<Integer> getAll();

  void setSkipAll(Boolean state);

  void toggleBreak(int line);
}
