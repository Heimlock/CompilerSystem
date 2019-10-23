/**
 * 
 */
package core.syntactic.analyzer;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntaticAnalyzerException extends Exception {
  private static final long serialVersionUID = -3147739644658623882L;

  /**
   * @param message
   */
  public SyntaticAnalyzerException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public SyntaticAnalyzerException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public SyntaticAnalyzerException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public SyntaticAnalyzerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
