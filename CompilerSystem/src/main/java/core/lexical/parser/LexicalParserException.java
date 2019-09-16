/**
 * 
 */
package core.lexical.parser;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 15 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class LexicalParserException extends Exception {
  private static final long serialVersionUID = -3147739644658623882L;

  /**
   * @param message
   */
  public LexicalParserException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public LexicalParserException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public LexicalParserException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public LexicalParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
