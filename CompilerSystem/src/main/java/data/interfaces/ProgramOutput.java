/**
 * 
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 18 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public interface ProgramOutput {
  void push(int value);

  List<Integer> getAll();

  void reset();
}
