/**
 * 
 */
package data.interfaces;

import java.util.Optional;

enum Scope {
  Variable,
  Procedure
}

enum Type {
  Inteiro,
  Booleano
}

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public interface Symbol {

  public String getLexeme();

  public Optional<Scope> getScope();

  public Optional<Type> getType();

  public Integer getStackAddr();

  public void setStackAddr(Integer stackAddr);

}
