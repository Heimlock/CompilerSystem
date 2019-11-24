/**
 *
 */
package core.generator;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 23 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class CodeGeneratorException extends Exception {
  private static final long serialVersionUID = 9188485974263971917L;

  /**
   * @param message
   */
  public CodeGeneratorException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public CodeGeneratorException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public CodeGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public CodeGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
