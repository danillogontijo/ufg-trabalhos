%{
	//package br.ufg.emc.compiladores.sintatico.byacc;
	import java.io.*;
	import br.ufg.emc.compiladores.lexico.*;
	import br.ufg.emc.compiladores.interpreter.ast.*;
        import br.ufg.emc.compiladores.interpreter.symtab.*;
%}

%start Programa

%token CAR INT

%token LBRACK RBRACK
%token SEMICOLON COMMA LBRACE RBRACE LPAREN RPAREN COLON

%token PROGRAMA RETORNE LEIA ESCREVA NOVALINHA
%token SE ENTAO SENAO INTERROGACAO ENQUANTO EXECUTE
%token OU E
%token MENORQUE MAIORQUE MAIORIGUAL MENORIGUAL IGUAL DIFERENTE
%token PLUS MINUS MULT NOT DIV MOD

%type <obj> Programa
%type <obj> DeclFuncVar
%type <obj> DeclProg
%type <obj> DeclVar

%type <sval> Tipo
%type <obj> DeclFunc
%type <obj> Bloco
%type <obj> ListaParametros
%type <obj> ListaParametrosCont
%type <obj> Comando
%type <obj> ListaComando
%type <obj> ListaDeclVar

%type <obj> Expr
%type <obj> LValueExpr
%type <obj> AssingExpr
%type <obj> CondExpr
%type <obj> OrExpr
%type <obj> AndExpr
%type <obj> EqExpr
%type <obj> DesigExpr
%type <obj> AddExpr
%type <obj> MulExpr
%type <obj> UnExpr
%type <obj> PrimExpr
%type <obj> ListExpr

%token <ival> INTEGER_LITERAL;
%token <obj> CAR_LITERAL;
%token <sval> STRING_LITERAL;
%token <sval> ID; // name

/*%union{
 Programa programa;
}*/

%%

Programa : DeclFuncVar DeclProg { $$ = new Programa($1,$2); }
;

DeclFuncVar : Tipo ID DeclVar SEMICOLON DeclFuncVar {$$=CriaVarEFunc(TypeOp.Variavel, $1, $2, $3, $5);}
            | Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON DeclFuncVar {$$=CriaVarEFunc(TypeOp.Variavel, $1, $2, $6, $8);}
            | Tipo ID DeclFunc DeclFuncVar {$$=CriaVarEFunc(TypeOp.Funcao, $1, $2, $3, $4);}
            | /*cadeia vazia*/ {$$=null;} 
;

DeclProg : PROGRAMA Bloco {$$=CriaOperador(TypeOp.Programa, null);}
;

DeclVar : COMMA DeclVar {$$ = CriaOperador(TypeOp.Variavel, $2, $2);}
	  | COMMA ID DeclVar {$$ = CriaOperador(TypeOp.Variavel, $2, $3);}
          | COMMA ID LBRACK INTEGER_LITERAL RBRACK DeclVar {$$ = CriaOperador(TypeOp.Variavel, $2, $6);}
          | /*cadeia vazia*/ {$$=null;}
; 

DeclFunc : LPAREN ListaParametros RPAREN Bloco {$$ = CriaOperador(TypeOp.ListaParametros, null, $2, $4);}
;

ListaParametros : /*cadeia vazia*/ {$$=null;}
                  | ListaParametrosCont {$$=$1;}
;

ListaParametrosCont : Tipo ID {$$=CriaVarEFunc(TypeOp.Parametro, $1, $2);}
                      | Tipo ID LBRACK RBRACK {$$=CriaVarEFunc(TypeOp.Parametro, $1, $2);}
                      | Tipo ID COMMA ListaParametrosCont {$$=CriaVarEFunc(TypeOp.Parametro, $1, $2, $4);}
                      | Tipo ID LBRACK RBRACK COMMA ListaParametrosCont {$$=CriaVarEFunc(TypeOp.Parametro, $1, $2, $6); } 
;

Bloco : LBRACE ListaDeclVar ListaComando RBRACE {$$=CriaOperador(TypeOp.Bloco, null, null, $2, $3);}
        | LBRACE ListaDeclVar RBRACE {$$=CriaOperador(TypeOp.Bloco, null, null, $2); }
;

ListaDeclVar : /*cadeia vazia*/ {$$=null;}
               | Tipo ID DeclVar SEMICOLON ListaDeclVar {$$=CriaVarEFunc(TypeOp.Variavel, $1, $2, $3, $5);}
               | Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON ListaDeclVar {$$=CriaVarEFunc(TypeOp.Variavel, $1, $2, $4, $6);}
;

Tipo : INT {CriaVarEFunc(TypeOp.Tipo, null, null, "Tint");}
       | CAR {CriaVarEFunc(TypeOp.Tipo, null, null, "Tcar");}
;

ListaComando : Comando {$$=CriaOperador(TypeOp.ListaComando, $1, null, $1);}
               | Comando ListaComando {$$=CriaOperador(TypeOp.ListaComando, null, $1, $2);}
;

Comando : SEMICOLON {$$=new Integer(SEMICOLON);}
           | Expr SEMICOLON {$$=$1;}
           | RETORNE Expr SEMICOLON {$$=CriaOperador(TypeOp.Retorne, null, null, $2);}
           | LEIA LValueExpr SEMICOLON {$$=CriaOperador(TypeOp.Leia, null, null, $2);}
           | ESCREVA Expr SEMICOLON {$$=CriaOperador(TypeOp.Escreva, null, null, $2);}
           | ESCREVA STRING_LITERAL SEMICOLON {$$=CriaOperador(TypeOp.Escreva, null, $2);}
           | NOVALINHA SEMICOLON {$$=CriaOperador(TypeOp.Novalinha, null, null);}
           | SE LPAREN Expr RPAREN ENTAO Comando {$$=CriaOperador(TypeOp.Se, null, null, $3, $6);}
           | SE LPAREN Expr RPAREN ENTAO Comando SENAO Comando {$$=CriaOperador(TypeOp.Se, null, null, $3, $6, $8);}
           | ENQUANTO LPAREN Expr RPAREN EXECUTE Comando {$$=CriaOperador(TypeOp.Enquanto, null, null, $3, $6);}
           | Bloco {$$=CriaOperador(TypeOp.Bloco, null, null, $1);}
;

Expr : AssingExpr {$$=$1;}
;

AssingExpr : CondExpr {$$=$1;}
             | LValueExpr IGUAL AssingExpr {$$=CriaOperador(TypeOp.Assign, null, null, $1, $3);}
;

CondExpr : OrExpr { $$ = $1;}
           | OrExpr INTERROGACAO Expr COLON CondExpr {$$=CriaOperador(TypeOp.Se, null, null, $1, $3, $5); }
;

OrExpr : OrExpr OU AndExpr {$$=CriaOperador(TypeOp.Ou, null, null, $1, $3); }
         | AndExpr ;

AndExpr : AndExpr E EqExpr {$$=CriaOperador(TypeOp.And, null, null, $1, $3);}
          | EqExpr { $$ = $1;}
;

EqExpr : EqExpr IGUAL DesigExpr {$$=CriaOperador(TypeOp.Igual, null, null, $1, $3);}
         | EqExpr DIFERENTE DesigExpr {$$=CriaOperador(TypeOp.Diferente, null, null, $1, $3);}
         | DesigExpr {$$=$1;}
;

DesigExpr : DesigExpr MENORQUE AddExpr {$$=CriaOperador(TypeOp.Menor, null, null, $1, $3);}
            | DesigExpr MAIORQUE AddExpr {$$=CriaOperador(TypeOp.Maior, null, null, $1, $3);}
            | DesigExpr MAIORIGUAL AddExpr {$$=CriaOperador(TypeOp.MaiorQ, null, null, $1, $3);}
            | DesigExpr MENORIGUAL AddExpr {$$=CriaOperador(TypeOp.MenorQ, null, null, $1, $3);}
            | AddExpr {$$=$1;}
;

AddExpr : AddExpr PLUS MulExpr {$$=CriaOperador(TypeOp.Mais, null, null, $1, $3);}
          | AddExpr MINUS MulExpr {$$=CriaOperador(TypeOp.Menos, null, null, $1, $3);}
          | MulExpr {$$=$1;}
;

MulExpr : MulExpr MULT UnExpr {$$=CriaOperador(TypeOp.Mult, null, null, $1, $3);}
          | MulExpr DIV UnExpr {$$=CriaOperador(TypeOp.Div, null, null, $1, $3);}
          | MulExpr MOD UnExpr {$$=CriaOperador(TypeOp.Resto, null, null, $1, $3);}
          | UnExpr {$$=$1;}
;

UnExpr : MINUS PrimExpr {$$=CriaOperador(TypeOp.Menos, null, null, $2);}
          | NOT PrimExpr {$$=CriaOperador(TypeOp.Diferente, null, null, $2);}
          | PrimExpr {$$=$1;}
;

LValueExpr : ID LBRACK Expr RBRACK {$$=CriaOperador(TypeOp.Array, null, $1, $3);}
             | ID {$$=$1;}
;

PrimExpr : ID LPAREN ListExpr RPAREN {$$=CriaOperador(TypeOp.Call, null, $1, $3);}
            | ID LPAREN RPAREN {$$=CriaOperador(TypeOp.Call, null, $1);}
            | ID LBRACK Expr RBRACK {$$=CriaOperador(TypeOp.Array, null, $1, $3);}
            | ID {$$=$1;}
            | CAR_LITERAL {$$=$1;}
            | INTEGER_LITERAL {$$=$1;}
            | LPAREN Expr RPAREN {$$=$2;}
;

ListExpr : AssingExpr {$$=$1;}
           | ListExpr COMMA AssingExpr {$$=CriaOperador(TypeOp.Lexpr, null, null, $3);}
;

%%

   private Yylex lexer;
   
   public SymTab symTab;

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

  public AST CriaVarEFunc(TypeOp typeop, Object typevar, String lexema, Object... filhos){
    
    System.err.println (typeop);
    
    return null;
  }

  public AST CriaOperador(TypeOp typeop, Object lexema, Object... filhos){
    
    System.err.println (typeop);
    
    return null;
  }

  public void yyerror (String error) {
    System.err.println ("Error: " + error + " in line " + lexer.getLine());
  }

  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }

  public enum TypeOp{

        Programa,        
        Variavel,
        Funcao,
        Parametro,
	ListaParametros,
	Lexpr, Bloco, Tipo, ListaComando, Retorne, Leia, 
	Novalinha, Escreva, Menos, Diferente, Igual, 
	Ou, And, Assign, Enquanto, Se, MenorQ, MaiorQ, 
	Menor, Mult, Div, Mais, Maior, Resto, Array, Call;
        
   }

  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex - Cafezinho");

	if ( args.length < 1 ) {
		args = new String[1];
		args[0] = "src/test/resources/teste.z";
	}

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
      System.out.println("====Fim====");
    }
  }