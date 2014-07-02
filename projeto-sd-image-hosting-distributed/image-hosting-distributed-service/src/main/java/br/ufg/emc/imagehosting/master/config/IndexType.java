package br.ufg.emc.imagehosting.master.config;

public enum IndexType {

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

	IndexType(String regex){
		this.regex = regex;
	}

	public String getRegex(){
		return this.regex;
	}

	public static IndexType getIndex(String str){
		for (final IndexType index : IndexType.values()) {
			if(str.matches(index.getRegex())){
				return index;
			}
		}

		return null;
	}

	public static IndexType fromOrdinal(final Integer ordinal) {
        for (final IndexType index : IndexType.values()) {
            if (index.ordinal() == ordinal) {
                return index;
            }
        }
        throw new RuntimeException("Ordinal IndexType inválido: " + ordinal);
    }

}
