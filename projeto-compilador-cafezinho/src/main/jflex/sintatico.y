%{
	package br.ufg.emc.compiladores.sintatico.byacc;
	import java.io.*;
	import br.ufg.emc.compiladores.lexico.*;
%}


%token CAR INT

%token LBRACK RBRACK
%token SEMICOLON COMMA LBRACE RBRACE LPAREN RPAREN COLON

%token PROGRAMA RETORNE LEIA ESCREVA NOVALINHA
%token SE ENTAO SENAO INTERROGACAO ENQUANTO EXECUTE
%token OU E
%token MENORQUE MAIORQUE MAIORIGUAL MENORIGUAL IGUAL DIFERENTE
%token PLUS MINUS MULT NOT DIV MOD

%token <ival> INTEGER_LITERAL;
%token <oval> CAR_LITERAL;
%token <sval> STRING_LITERAL;
%token <sval> ID; // name

%%

Programa : DeclFuncVar DeclProg ;

DeclFuncVar : Tipo ID DeclVar SEMICOLON DeclFuncVar
            | Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON DeclFuncVar
            | Tipo ID DeclFunc DeclFuncVar
            | /*cadeia vazia*/ ;

DeclProg : PROGRAMA Bloco ;

DeclVar : COMMA DeclVar
		  | COMMA ID DeclVar
          | COMMA ID LBRACK INTEGER_LITERAL RBRACK DeclVar
          | /*cadeia vazia*/ ;

DeclFunc : LPAREN ListaParametros RPAREN Bloco ;

ListaParametros : /*cadeia vazia*/
                  | ListaParametrosCont ;

ListaParametrosCont : Tipo ID
                      | Tipo ID LBRACK RBRACK
                      | Tipo ID COMMA ListaParametrosCont
                      | Tipo ID LBRACK RBRACK COMMA ListaParametrosCont ;

Bloco : LBRACE ListaDeclVar ListaComando RBRACE
        | LBRACE ListaDeclVar RBRACE ;

ListaDeclVar : /*cadeia vazia*/
               | Tipo ID DeclVar SEMICOLON ListaDeclVar
               | Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON ListaDeclVar ;

Tipo : INT
       | CAR ;

ListaComando : Comando
               | Comando ListaComando ;

Comando : SEMICOLON
           | Expr SEMICOLON
           | RETORNE Expr SEMICOLON
           | LEIA LValueExpr SEMICOLON
           | ESCREVA Expr SEMICOLON
           | ESCREVA STRING_LITERAL SEMICOLON
           | NOVALINHA SEMICOLON
           | SE LPAREN Expr RPAREN ENTAO Comando
           | SE LPAREN Expr RPAREN ENTAO Comando SENAO Comando
           | ENQUANTO LPAREN Expr RPAREN EXECUTE Comando
           | Bloco ;

Expr : AssingExpr ;

AssingExpr : CondExpr
             | LValueExpr IGUAL AssingExpr ;

CondExpr : OrExpr
           | OrExpr INTERROGACAO Expr COLON CondExpr ;

OrExpr : OrExpr OU AndExpr
         | AndExpr ;

AndExpr : AndExpr E EqExpr
          | EqExpr ;

EqExpr : EqExpr IGUAL DesigExpr
         | EqExpr DIFERENTE DesigExpr
         | DesigExpr ;

DesigExpr : DesigExpr MENORQUE AddExpr
            | DesigExpr MAIORQUE AddExpr
            | DesigExpr MAIORIGUAL AddExpr
            | DesigExpr MENORIGUAL AddExpr
            | AddExpr ;

AddExpr : AddExpr PLUS MulExpr
          | AddExpr MINUS MulExpr
          | MulExpr ;

MulExpr : MulExpr MULT UnExpr
          | MulExpr DIV UnExpr
          | MulExpr MOD UnExpr
          | UnExpr ;

UnExpr : MINUS PrimExpr
          | NOT PrimExpr
          | PrimExpr ;

LValueExpr : ID LBRACK Expr RBRACK
             | ID ;

PrimExpr : ID LPAREN ListExpr RPAREN
            | ID LPAREN RPAREN
            | ID LBRACK Expr RBRACK
            | ID
            | CAR_LITERAL
            | INTEGER_LITERAL
            | LPAREN Expr RPAREN ;

ListExpr : AssingExpr
           | ListExpr COMMA AssingExpr ;

%%

   private Yylex lexer;

   private int yylex () {
    int yyl_return = -1;
    try {
 //     yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex - Cafezinho");

    args = new String[1];
    args[0] = "src/test/resources/teste.z";

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    } else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    int ret = yyparser.yyparse();
    
    if(ret == 0){
    	System.out.println("\n Entrada Valida");
    }else{
    	System.err.println("\n Entrada Invalida");
    }

    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
  }