/**
 * 
 */
package data.interfaces;

import java.util.Arrays;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 19 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public enum Type {
  Inteiro,
  Booleano,
  ;

  private Type() {
  }

  public static Type getType(String typeString) {
    Type result = Arrays.asList(values()).stream().filter(type -> type.toString().toLowerCase().equals(typeString)).findFirst().orElse(null);
    return result;
  }
}