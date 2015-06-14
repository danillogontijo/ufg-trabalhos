package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymtabEntry;

/**
 * Symbol table entry for functions.
 * 
 * Contains arity and reference to location of definition
 */ 

public class STEfun extends SymtabEntry {
  int arity;
  DeclProg dekl; // location of definition
  
  public STEfun(String f, DeclProg d, int a) { 
    super(f);
    dekl=d;
    arity=a;
  }
  
  public int kind() { 
    return SymtabEntry.FUN; 
  }

  public String toString() { 
    return "function    "+name+", arity "+arity; 
  }

  public int arity() { 
    return arity; 
  }

  public DeclProg getDekl() { 
    return dekl; 
  }
}