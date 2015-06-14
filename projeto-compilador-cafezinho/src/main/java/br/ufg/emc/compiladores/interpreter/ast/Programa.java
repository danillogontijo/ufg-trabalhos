package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class Programa implements AST {
	DeclFuncVar declFuncVar;
	DeclProg declProg;

	SymTab globalvar; // table of input variables
	SymTab scope; // table of functions

	public void setSymtabs() { 
		globalvar = new SymTab(); 
		declFuncVar.setSymtab(globalvar, true, 0);
		scope = new SymTab(globalvar);
		declProg.setSymtab(scope);
	}
}
