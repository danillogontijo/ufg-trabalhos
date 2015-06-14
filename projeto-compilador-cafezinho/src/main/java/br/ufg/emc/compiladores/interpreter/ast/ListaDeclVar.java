package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class ListaDeclVar implements AST {

	ListaDeclVar parlist; // rest of the liste (optional null)
	Tipo ident; // identifier

	public ListaDeclVar(ListaDeclVar p, Tipo i) {
		parlist = p;
		ident = i;
	}

	public ListaDeclVar(Tipo i) {
		parlist = null;
		ident = i;
	}

	public String toString() {
		if (parlist != null)
			return parlist + "," + ident;
		else
			return ident.toString();
	}

	public void setSymtab(SymTab st, boolean isInput, int index) {
		boolean isNew = st.enter(ident.toString(), new STElocalvar(ident.toString(),
				isInput, index));

		if (!isNew)
			System.err.println("Variable " + ident + " defined twice!");
		if (parlist != null)
			parlist.setSymtab(st, isInput, index + 1);
	}

}
