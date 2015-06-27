package br.ufg.emc.compiladores.interpreter.ast;

public class Tipo implements AST {
	
	Object type;
	
	public Tipo(Object type){
		this.type = type;
	}

	public String toString() {
		return "";
	}
	
}
