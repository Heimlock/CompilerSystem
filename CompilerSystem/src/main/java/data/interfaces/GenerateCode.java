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
public interface GenerateCode {
  public void addToken(Token token) throws CodeGeneratorException;

  public default void addBlock(List<String> generatedBlock) {
    throw new UnsupportedOperationException("Operation Not Overwritten!");
  }

  public List<String> generate() throws CodeGeneratorException;

  public void clear();
}
