package br.ufg.emc.compiladores.interpreter.symtab;

/**
 * Symbol table entry for names, there are subclasses for
 * variables and functions.
 * 
 * Defines constants UNKNOWN, VAR und FUN as kinds of
 * symbol table entries.
 */ 
public class SymtabEntry {
  public String name;

  public SymtabEntry(String v) {
    name=v; 
  }

  public int kind() {
    return UNKNOWN; 
  }

  public String toString() {
    return("unknown "+name); 
  }

  protected static final int UNKNOWN = 12;
  protected static final int VAR = 13; //car e int
  protected static final int FUN = 14; //comandos
}

