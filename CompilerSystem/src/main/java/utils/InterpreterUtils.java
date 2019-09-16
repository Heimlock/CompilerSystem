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
public abstract class InterpreterUtils {

  public static int[] getArgs(String instruction) {
    List<Integer> args = new ArrayList<>();
    int[] result = null;
    String[] values = instruction.split(" "); // FIXME Parametrizar
    for (int i = 1; i < values.length; i++) {
      if (StringUtils.isNumeric(values[i])) {
        args.add(Integer.valueOf(values[i]));
      }
    }
    result = args.size() != 0 ? args.stream().filter(t -> t != null).mapToInt(t -> t).toArray() : null;
    return result;
  }

  public static String getLabel(String instruction) {
    String[] result = instruction.split(" "); // FIXME Parametrizar
    return result[1];
  }

  public static Operations getOperation(String instruction) {
    String[] values = instruction.split(" "); // FIXME Parametrizar
    Operations operation = Operations.getOperation(values[0]);
    if (operation == null) {
      //  TODO -- Throw Error
    }
    return operation;
  }

  public static Object[] getFormattedRow(String instruction, int line) {
    List<String> result = new ArrayList<>();
    result.add(String.format("%04d", line));
    for (String value : instruction.split(" ")) { // FIXME Parametrizar
      result.add(value);
    }
    for (int i = result.size(); i < 4; i++) {
      result.add("");
    }
    return result.toArray();
  }

  public static Object[] getFormattedRow(String... values) {
    List<String> result = new ArrayList<>();
    for (String value : values) {
      result.add(value);
    }
    return result.toArray();
  }
}
