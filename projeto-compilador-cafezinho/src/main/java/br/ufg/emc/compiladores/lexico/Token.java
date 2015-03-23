package br.ufg.emc.compiladores.lexico;

import java.util.Formatter;

public class Token {

	public enum T {
		CAR { public String toString() { return "CAR"; } },
		ID { public String toString() { return "ID"; } },
		INT { public String toString() { return "INT"; } },
		PROGRAMA { public String toString() { return "PROGRAMA"; } },
		RETORNE { public String toString() { return "RETORNE"; } },
		LEIA { public String toString() { return "LEIA"; } },
		ESCREVA { public String toString() { return "ESCREVA"; } },
		NOVALINHA { public String toString() { return "NOVALINHA"; } },
		EXECUTE { public String toString() { return "EXECUTE"; } },
		SE { public String toString() { return "SE"; } },
		ENTAO { public String toString() { return "ENTAO"; } },
		SENAO { public String toString() { return "SENAO"; } },
		ENQUANTO { public String toString() { return "ENQUANTO"; } },
		DIGIT { public String toString() { return "DIGIT"; } },

		COMMA { public String toString() { return "COMMA"; } },
		SEMICOLON { public String toString() { return "SEMICOLON"; } },
		COLON { public String toString() { return "COLON"; } },

		AND { public String toString() { return "AND"; } },
		OR { public String toString() { return "OR"; } },

		LPAREN { public String toString() { return "LPAREN"; } },
		RPAREN { public String toString() { return "RPAREN"; } },
		LBRACK { public String toString() { return "LBRACK"; } },
		RBRACK { public String toString() { return "RBRACK"; } },
		LBRACE { public String toString() { return "LBRACE"; } },
		RBRACE { public String toString() { return "RBRACE"; } },

		EQEQ { public String toString() { return "EQEQ"; } },
		NEQ { public String toString() { return "NEQ"; } },
		NOT { public String toString() { return "NOT"; } },

		/*Operadores*/
		EQ { public String toString() { return "EQ"; } },
		LT { public String toString() { return "LT"; } },
		GT { public String toString() { return "GT"; } },
		LTE { public String toString() { return "LTE"; } },
		GTE { public String toString() { return "GTE"; } },

		MULT { public String toString() { return "MULT"; } },
		DIV { public String toString() { return "DIV"; } },
		MOD { public String toString() { return "MOD"; } },
		PLUS { public String toString() { return "PLUS"; } },
		MINUS { public String toString() { return "MINUS"; } },

		STR { public String toString() { return "STR"; } },
		EOF { public String toString() { return "EOF"; } }
	}

	public T type;
	public Object val;
	public int line;
	public int col;

	public Token(T type, int line, int col) {
		this.type = type;
		this.line = line;
		this.col = col;
	}

	public Token(T type, Object val, int line, int col) {
		this.type = type;
		this.val = val;
		this.line = line;
		this.col = col;
	}

	public String toString() {
		Formatter out = new Formatter();
		out.format("(%4d,%4d) %s", line, col, type);
		if (val != null){
			out.format(" [%s]", val);
		}
		String str = out.toString();
		out.close();
		return str;
	}

	public int getOrdinal(){
		return this.type.ordinal();
	}
}