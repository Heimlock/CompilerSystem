/**
 *
 */
package core.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class CodeGenerator {
  private static CodeGenerator instance = null;
  private List<String> generatedProgram;
  private String filename;
  private final static String BASE_OUTPUT_FILE = "./assets/output/";

  public static CodeGenerator getInstance() {
    if (instance == null) {
      instance = new CodeGenerator();
    }
    return instance;
  }

  private CodeGenerator() {
    this.generatedProgram = new ArrayList<>();
  }

  public void addProgramLine(String line) {
    this.generatedProgram.add(line);
  }

  public void addProgramLines(List<String> line) {
    this.generatedProgram.addAll(line);
  }

  public List<String> getGeneratedProgram() {
    return this.generatedProgram;
  }

  public void clear() {
    this.generatedProgram.clear();
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void saveToFile() {
    BufferedWriter writer;
    try {
      writer = new BufferedWriter(new FileWriter(String.format("%s%s.obj", BASE_OUTPUT_FILE, filename)));
      for (String line : generatedProgram) {
        writer.write(String.format("%s\n", line));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
