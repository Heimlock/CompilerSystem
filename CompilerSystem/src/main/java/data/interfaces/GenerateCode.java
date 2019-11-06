/**
 *
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public interface GenerateCode {
  public void addToken(Token token);

  public List<String> generate();
}
