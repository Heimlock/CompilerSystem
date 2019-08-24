/**
 *
 */
package utils;

import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;

/**
 * @author Felipe / 15 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public abstract class StringUtils {

  public static Integer[] getArgs(String instruction) {
    List<Integer> args = new ArrayList<>();
    String[] values = instruction.split(" "); // FIXME Parametrizar
    for (int i = 1; i < values.length; i++) {
      args.add(Integer.valueOf(values[i]));
    }
    return (Integer[]) args.toArray();
  }

  public static Operations getOperation(String instruction) {
    String[] values = instruction.split(" "); // FIXME Parametrizar
    Operations operation = Operations.getOperation(values[0]);
    if (operation == null) {
      //  TODO -- Throw Error
    }
    return operation;
  }

  public static boolean isNumeric(final String str) {
    // null or empty
    if (str == null || str.length() == 0) {
      return false;
    }
    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }
}
