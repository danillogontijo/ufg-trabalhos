package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class DeclProg implements AST {
	Bloco bloco;

	public void setSymtab(SymTab st) {
		bloco.setSymtab(st);
	}

}
