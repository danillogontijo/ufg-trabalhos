package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class Bloco implements AST {

	ListaDeclVar listaDeclVar;
	ListaComando listaComando;
	
	public String toString() {
		return "";
	}

	SymTab localvars; // symbol table of the local variables
	SymTab comandos; // symbol table of the commands
	
	public void setSymtab(SymTab st) {
		localvars = new SymTab(st);
		listaComando.setSymtab(localvars, false, 0);
	}

}
