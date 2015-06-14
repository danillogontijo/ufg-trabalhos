package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class DeclFuncVar implements AST {

	DeclFuncVar declFuncVar;

	Tipo tipo;
	DeclVar declVar;
	DeclFunc declFunc;

	public String toString() {
		if (declFuncVar != null)
			return declFuncVar + "," + tipo;
		else
			return tipo.toString();
	}

	public void setSymtab(SymTab st, boolean isInput, int index) {
		boolean isNew = st.enter(tipo.toString(), new STEglobalvar(tipo.toString(), isInput, index));

		if (!isNew) {
			System.err.println("Variable " + tipo + " defined twice!");
		}

		if (declFuncVar != null)
			declFuncVar.setSymtab(st, isInput, index + 1);
	}

}
