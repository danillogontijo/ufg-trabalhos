package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymtabEntry;

/**
 * Symbol table entry for global variables.
 * 
 * Contains index in the parameter list and a flag if it
 * is an input variable.
 */ 
public class STEglobalvar extends SymtabEntry {
  boolean is_array;
  int index;                 

  public STEglobalvar(String v, boolean ii, int ind) {
    super(v);
    is_array=ii;
    index=ind;
  }

  public int kind() {
    return SymtabEntry.VAR; 
  }

  public String toString() {
    if (is_array) 
      return "input var "+name+"  ("+index+")";
    else 
      return "parameter "+name+"  ("+index+")";
  }

  public int getIndex() {
    return index; 
  }

  public boolean isInput() {
    return is_array; 
  }
}
