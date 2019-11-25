/**
 *
 */
package data.interfaces;

import java.util.List;

import core.generator.CodeGeneratorException;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public interface PostfixNotation extends GenerateCode {
  //  Debug Function
  public List<Token> convert() throws CodeGeneratorException;

  public Type getType();

  public void clear();
}
