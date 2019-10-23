/**
 * 
 */
package data.interfaces;

import java.util.Optional;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public interface Symbol {

  public String getLexeme();

  public Token getToken();

  public Scope getScope();

  public Optional<Type> getType();

  public void setType(Type type);

  public Optional<Integer> getStackAddr();

  public void setStackAddr(int stackAddr);

}
