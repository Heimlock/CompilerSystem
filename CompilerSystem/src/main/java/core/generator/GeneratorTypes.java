/**
 *
 */
package core.generator;

import core.generator.specific.AssignmentGenerator;
import core.generator.specific.FunctionGenerator;
import core.generator.specific.IfGenerator;
import core.generator.specific.LoadGenerator;
import core.generator.specific.ProcedureGenerator;
import core.generator.specific.ReadGenerator;
import core.generator.specific.SubRotineGenerator;
import core.generator.specific.WhileGenerator;
import core.generator.specific.WriteGenerator;
import data.interfaces.GenerateCode;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public enum GeneratorTypes {
  If(new IfGenerator()), // Ok
  While(new WhileGenerator()),
  SubRotine(new SubRotineGenerator()),
  Procedure(new ProcedureGenerator()),
  Function(new FunctionGenerator()),
  Read(new ReadGenerator()), //  Ok
  Write(new WriteGenerator()), // Ok
  Assignment(new AssignmentGenerator()), //  Ok
  Load(new LoadGenerator()), //  Ok
  ;

  private GenerateCode generator;

  private GeneratorTypes() {
    this.generator = null;
  }

  private GeneratorTypes(GenerateCode generator) {
    this.generator = generator;
  }

  public GenerateCode getGenerator() {
    this.generator.clear();
    return this.generator;
  }
}
