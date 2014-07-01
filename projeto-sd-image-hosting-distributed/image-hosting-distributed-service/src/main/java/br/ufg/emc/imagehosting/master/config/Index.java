package br.ufg.emc.imagehosting.master.config;

public enum Index {
	
	NUMBER("^[0-9].*$"),
	A("^[aA].*$"),
	B("^[bB].*$"),
	C("^[cC].*$"),
	D("^[dD].*$"),
	E("^[eE].*$"),
	F("^[fF].*$"),
	G("^[gG].*$"),
	H("^[hH].*$"),
	I("^[iI].*$"),
	J("^[jJ].*$"),
	K("^[kK].*$"),
	L("^[lL].*$"),
	M("^[mM].*$"),
	N("^[nN].*$"),
	O("^[oO].*$"),
	P("^[pP].*$"),
	Q("^[qQ].*$"),
	R("^[rR].*$"),
	S("^[sS].*$"),
	T("^[tT].*$"),
	U("^[uU].*$"),
	V("^[vV].*$"),
	W("^[wW].*$"),
	X("^[xX].*$"),
	Y("^[yY].*$"),
	Z("^[zZ].*$"),
	SPECIALS("^[^\\w].*$");
	
	private String regex;
	
	Index(String regex){
		this.regex = regex;
	}
	
	public String getRegex(){
		return this.regex;
	}
	
	public Index getIndex(String str){
		Index[] indexes = Index.values();
		for (Index index : indexes) {
			if(str.matches(index.getRegex())){
				return index;
			}
		}
		
		return null;
	}

}
