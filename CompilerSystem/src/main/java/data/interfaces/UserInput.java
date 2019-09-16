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
public interface UserInput {
  void add(int value);

  int getNextValue();

  int getCounter();

  List<Integer> getAll();

  void changeValue(int oldValue, int newValue);

  void remove(int index);

  void reset();
}
