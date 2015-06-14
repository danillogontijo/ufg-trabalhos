/* Analisador Lexico */
package br.ufg.emc.compiladores.lexico;
import br.ufg.emc.compiladores.sintatico.byacc.Parser;
import static br.ufg.emc.compiladores.sintatico.byacc.Parser.*;
import br.ufg.emc.compiladores.sintatico.byacc.ParserVal;

%%

%public
%standalone

%unicode
%byaccj
%line
%column

%debug

%{
  private Parser yyparser;

  StringBuffer string = new StringBuffer();
  
  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
  
  public String getLine(){
  	return yyline;
  }

%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
Comment = {TraditionalComment}

Identifier = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*

/* string literals */
StringCharacter = [^\r\n\"\\]

%state STRING

%%

<YYINITIAL> {
	/* keywords */
	"programa" { return PROGRAMA; }
	"car" { return CAR; }
	"int" { return INT; }
	"retorne" { return RETORNE; }
	"leia" { return LEIA; }
	"escreva" { return ESCREVA; }
	"novalinha" { return NOVALINHA; }
	"se" { return SE; }
	"entao" { return ENTAO; }
	"senao" { return SENAO; }
	"enquanto" { return ENQUANTO; }
	"execute" { return EXECUTE; }

	/* operadores */
	"=" { return IGUAL; }
	">" { return MAIORQUE; }
	">=" { return MAIORIGUAL; }
	"<" { return MENORQUE; }
	"<=" { return MENORIGUAL; }
	"+" { return PLUS; }
	"-" { return MINUS; }
	"*" { return MULT; }
	"/" { return DIV; }
	"%" { return MOD; }

	"!" { return NOT; }

	"&&" { return E; }
	"||" { return OU; }

	"?" { return INTERROGACAO; }
	":" { return COLON; }

	"[" { return LBRACK; }
	"]" { return RBRACK; }
	"{" { return LBRACE; }
	"}" { return RBRACE; }
	"(" { return LPAREN; }
	")" { return RPAREN; }

	"," { return COMMA; }
	";" { return SEMICOLON; }

	 /* string literal */
	 \" { yybegin(STRING); string.setLength(0); }

	/* numeric literals */
	{DecIntegerLiteral}  { yyparser.yylval = new ParserVal(Integer.parseInt(yytext()));
         return INTEGER_LITERAL; }

  	/* comments */
  	{Comment}                      { /* ignore */ }

  	/* whitespace */
  	{WhiteSpace}                   { /* ignore */ }

  	/* identifiers */
  	{Identifier}                   {yyparser.yylval = new ParserVal(yytext()); return ID; }
}

  	<STRING> {
		  \"                             { yybegin(YYINITIAL); yyparser.yylval = new ParserVal(string.toString()); return STRING_LITERAL;  }

		  {StringCharacter}+             { string.append( yytext() ); }

		  /* escape sequences */
		  "\\b"                          { string.append( '\b' ); }
		  "\\t"                          { string.append( '\t' ); }
		  "\\n"                          { string.append( '\n' ); }
		  "\\f"                          { string.append( '\f' ); }
		  "\\r"                          { string.append( '\r' ); }
		  "\\\""                         { string.append( '\"' ); }
		  "\\'"                          { string.append( '\'' ); }
		  "\\\\"                         { string.append( '\\' ); }

		  /* error cases */
		  {LineTerminator}               { throw new RuntimeException("CADEIA DE CARACTERES OCUPA MAIS DE UMA LINHA"); }
	}

	/* error fallback */
	[^]    { System.err.println("ERRO: CARACTER INVALIDO '"+yytext()+"'. Linha " + (yyline+1) + "\n"); return -1; }