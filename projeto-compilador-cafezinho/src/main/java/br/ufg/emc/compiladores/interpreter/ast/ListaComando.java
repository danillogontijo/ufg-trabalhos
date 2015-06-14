package br.ufg.emc.compiladores.interpreter.ast;

import br.ufg.emc.compiladores.interpreter.symtab.SymTab;

public class ListaComando implements AST {
	
	ListaComando listaComando;

	public void setSymtab(SymTab st, boolean isInput, int index) {
//		boolean isNew = st.enter(ident.toString(), new STElocalvar(ident.toString(),
//				isInput, index));
//
//		if (!isNew)
//			System.err.println("Variable " + ident + " defined twice!");
//		if (listaComando != null)
//			listaComando.setSymtab(st, isInput, index + 1);
	}

}
