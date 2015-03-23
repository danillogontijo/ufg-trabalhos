/* Analisador Lexico */
import java_cup.runtime.*;

package br.ufg.emc.compiladores.lexico

%%

%class Cafezinho
%state COMMENT

%line
%column
%debug
%unicode
%cup

%{
StringBuffer string = new StringBuffer();
private Symbol symbol(int type) {
return new Symbol(type, yyline, yycolumn);
}
private Symbol symbol(int type, Object value) {
return new Symbol(type, yyline, yycolumn, value);
}
%}


DIGIT=[0-9]
ALPHA=[A-Za-z]
ID=[_a-zA-Z][_a-zA-Z0-9]*
COMMENT_TEXT=([^*/\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])*

%%

/* keywords */
<YYINITIAL> "programa" { return symbol(sym.PROGRAMA); }
<YYINITIAL> "car" { return symbol(sym.CAR); }
<YYINITIAL> "int" { return symbol(sym.INT); }
<YYINITIAL> "retorne" { return symbol(sym.RETORNE); }
<YYINITIAL> "leia" { return symbol(sym.LEIA); }
<YYINITIAL> "escreva" { return symbol(sym.ESCREVA); }
<YYINITIAL> "novalinha" { return symbol(sym.NOVALINHA); }
<YYINITIAL> "se" { return symbol(sym.SE); }
<YYINITIAL> "entao" { return symbol(sym.ENTAO); }

<YYINITIAL> "senao" { return symbol(sym.SENAO); }
<YYINITIAL> "enquanto" { return symbol(sym.ENQUANTO); }
<YYINITIAL> "execute" { return symbol(sym.EXECUTE); }


<COMMENT> {
  "/*" { comment_count++; }
  "*/" { if (--comment_count == 0) yybegin(YYINITIAL); }
  {COMMENT_TEXT} { }
}

