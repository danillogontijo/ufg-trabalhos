package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class Programa implements AST {
	DeclFuncVar declFuncVar;
	DeclProg declProg;

	SymTab globalvar; // table of input variables
	SymTab scope; // table of functions
	
	public Programa(DeclFuncVar declFuncVar, DeclProg declProg){
		this.declFuncVar = declFuncVar;
		this.declProg = declProg;
	}

	public Programa(Object declFuncVar, Object declProg) {
		this.declFuncVar = (DeclFuncVar) declFuncVar;
		this.declProg = (DeclProg) declProg;
	}

	public void setSymtabs() { 
		globalvar = new SymTab(); 
		declFuncVar.setSymtab(globalvar, true, 0);
		scope = new SymTab(globalvar);
		declProg.setSymtab(scope);
	}
}
