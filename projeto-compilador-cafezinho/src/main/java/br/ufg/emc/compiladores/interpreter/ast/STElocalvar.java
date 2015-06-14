package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymtabEntry;

public class STElocalvar extends SymtabEntry {
	
	  boolean is_input;           
	  int index;                  

	  public STElocalvar(String v, boolean ii, int ind) {
	    super(v);
	    is_input=ii;
	    index=ind;
	  }

	  public int kind() {
	    return SymtabEntry.VAR; 
	  }

	  public String toString() {
	    if (is_input) 
	      return "input var "+name+"  ("+index+")";
	    else 
	      return "parameter "+name+"  ("+index+")";
	  }

	  public int getIndex() {
	    return index; 
	  }

	  public boolean isInput() {
	    return is_input; 
	  }
	}