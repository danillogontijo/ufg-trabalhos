%{
/* Secao prologo*/
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "tipos.h"
extern char * yytext;
extern int yylex();
extern int numLinha;
extern FILE* yyin;
extern int erroOrigem;
void yyerror( char const *s);


Toperador* CriaOperador(TespecieOperador tipoOperador,  int linha, Toperador* filho1, Toperador* filho2, Toperador* filho3, char* lexema);
Toperador* CriaVarEFunc(TespecieOperador tipoOperador, int linha, Toperador* filho1, Toperador* filho2, Toperador* filho3, TVar tipoVar, char* lexema);
void percorreArvore(Toperador* raiz);
void obtemEspecieNoEnumLin(Toperador* no, char* nomeOperador);
void imprimeNo(Toperador* no);

Toperador* raiz;
char nomeOperador[200];
%}

%union{
    Toperador* op;
}


%start  program /* Inidica que o simbolo incial da gramatica e programm */
%type<op> program DeclFuncVar DeclProg DeclVar DeclFunc ListaParametros ListaParametrosCont Bloco ListaDeclVar Tipo ListaComando Comando Expr AssignExpr
%type<op> CondExpr OrExpr AndExpr EqExpr DesigExpr AddExpr MulExpr UnExpr LValueExpr PrimExpr ListExpr


%token<op> PROGRAMA ENQUANTO OU E ESCREVA SE ENTAO SENAO IDENTIFICADOR LEQ GEQ 
%token<op> INTCONST CADEIACHAR INT CAR RETORNE NOVALINHA EXECUTE LEIA 
%token<op> '[' ']' '(' ')' '<' ':' '/' '>' '+' ',' '{' '}' '*' '-' '%' ';' '!' '='

%%

program: 	DeclFuncVar DeclProg { raiz=CriaOperador(Raiz, $2->linha, $1, $2, NULL, NULL);}
		;

DeclFuncVar:	Tipo IDENTIFICADOR DeclVar ';' DeclFuncVar {printf("montando descfuncvar"); $$=CriaVarEFunc(Variavel, $2->linha, $3, $5, NULL, $1->tipoVariavel, $2->lexema); }
				|Tipo IDENTIFICADOR '[' INTCONST ']' DeclVar ';' DeclFuncVar  {printf("montando descfuncvar"); $$=CriaVarEFunc(Variavel, $2->linha, $6, $8, NULL, $1->tipoVariavel, $2->lexema); }
				|Tipo IDENTIFICADOR DeclFunc DeclFuncVar {printf("montando descfuncvar"); $$=CriaVarEFunc(Funcao, $2->linha, $3, $4, NULL, $1->tipoVariavel, $2->lexema); }
				| {$$=NULL;}
		;

DeclProg:	PROGRAMA Bloco {printf("montando Programa"); $$=CriaOperador(Programa, $1->linha, $2, NULL, NULL, NULL);}
		;

DeclVar:	',' IDENTIFICADOR DeclVar {printf("montando DeclVar"); $$ = CriaOperador(Variavel, $2->linha, $3, NULL, NULL, $2->lexema);}
			|',' IDENTIFICADOR '[' INTCONST ']' DeclVar {printf("montando DeclVar"); $$ = CriaOperador(Variavel, $2->linha, $3, NULL, NULL, $2->lexema);}
			| {$$=NULL;}
		;
DeclFunc:	'(' ListaParametros ')' Bloco {printf("montando DeclFunc"); $$ = CriaOperador(ListaParametros, $2->linha, $2, $3, NULL, NULL);}
		;

ListaParametros: ListaParametrosCont {$$=$1;}
				| {$$=NULL;}
		;

ListaParametrosCont:	Tipo IDENTIFICADOR {printf("montando ListaParametrosCont"); $$=CriaVarEFunc(Parametro, $2->linha, NULL, NULL, NULL, $1->tipoVariavel, $2->lexema); }
						|Tipo IDENTIFICADOR '[' ']' {printf("montando ListaParametrosCont"); $$=CriaVarEFunc(Parametro, $2->linha, NULL, NULL, NULL, $1->tipoVariavel, $2->lexema); }
						|Tipo IDENTIFICADOR ',' ListaParametrosCont {printf("montando ListaParametrosCont"); $$=CriaVarEFunc(Parametro, $2->linha, $4, NULL, NULL, $1->tipoVariavel, $2->lexema); }
						|Tipo IDENTIFICADOR '[' ']' ',' ListaParametrosCont {printf("montando ListaParametrosCont"); $$=CriaVarEFunc(Parametro, $2->linha, $6, NULL, NULL, $1->tipoVariavel, $2->lexema); }
		;
			
Bloco: 	'{' ListaDeclVar  ListaComando '}' {printf("montando Bloco"); $$=CriaOperador(Bloco, $1->linha, $2, $3, NULL, NULL); }
			|'{' ListaDeclVar '}' {printf("montando Bloco"); $$=CriaOperador(Bloco, $1->linha, $2, NULL, NULL, NULL); }
		;

ListaDeclVar:	Tipo IDENTIFICADOR DeclVar ';' ListaDeclVar {printf("montando ListaDeclVar"); $$=CriaVarEFunc(Variavel, $2->linha, $3, $5, NULL, $1->tipoVariavel, $2->lexema);}
				|Tipo IDENTIFICADOR '[' INTCONST ']' DeclVar ';'  {printf("montando ListaDeclVar"); $$=CriaVarEFunc(Variavel, $2->linha, $4, $6, NULL, $1->tipoVariavel, $2->lexema);}
				| {$$=NULL;}
			;

Tipo:	INT {printf("montando tipo"); CriaVarEFunc(Ti, $1->linha, NULL, NULL, NULL, Tint, NULL);}
		|CAR {printf("montando tipo"); CriaVarEFunc(Ti, $1->linha, NULL, NULL, NULL, Tchar, NULL);}
	;

ListaComando:	Comando {printf("montando ListaComando"); $$=CriaOperador(ListaComando, $1->linha, $1, NULL, NULL, NULL);}
				| Comando ListaComando {printf("montando ListaComando"); $$=CriaOperador(ListaComando, $1->linha, $1, $2, NULL, NULL);}
			;

Comando: 	';' {$$=$1}
			|Expr ';' {$$=$1}
			|RETORNE Expr ';' {printf("montando Comando"); $$=CriaOperador(Retorne, $1->linha, $2, NULL, NULL, NULL);}
			|LEIA LValueExpr ';' {printf("montando Comando"); $$=CriaOperador(Leia, $1->linha, $2, NULL, NULL, NULL);}
			|ESCREVA Expr ';' {printf("montando Comando"); $$=CriaOperador(Escreva, $1->linha, $2, NULL, NULL, NULL);}
			|ESCREVA CADEIACHAR ';' {printf("montando Comando");$$=CriaOperador(Escreva, $1->linha, NULL, NULL, NULL, $2->lexema);}
			|NOVALINHA ';' {printf("montando Comando");$$=CriaOperador(Novalinha, $1->linha, NULL, NULL, NULL, NULL);}
			|SE '(' Expr ')' ENTAO Comando {printf("montando Comando");$$=CriaOperador(Se, $1->linha, $3, $6, NULL, NULL);}
			|SE '(' Expr ')' ENTAO Comando SENAO Comando {printf("montando Comando");$$=CriaOperador(Se, $1->linha, $3, $6, $8, NULL);}
			|ENQUANTO '(' Expr ')' EXECUTE Comando {printf("montando Comando");$$=CriaOperador(Enquanto, $1->linha, $3, $6, NULL, NULL);}
			|Bloco {printf("montando Comando"); $$=CriaOperador(Bloco, $1->linha, $1, NULL, NULL, NULL);}
		;

Expr:	AssignExpr {$$=$1;}
	;

AssignExpr:	CondExpr {$$=$1;}
				|LValueExpr '=' AssignExpr {printf("montando AssignExpr");$$=CriaOperador(Assign, $1->linha, $1, $3, NULL, NULL);}
		;

CondExpr:	OrExpr { $$ = $1;}
			|OrExpr '?' Expr ':' CondExpr {printf("montando CondExpr");$$=CriaOperador(If, $1->linha, $1, $3, $5, NULL); }
		;

OrExpr:	OrExpr OU AndExpr {printf("montando OrExpr");$$=CriaOperador(Ou, $1->linha, $1, $3, NULL, NULL); }
			|AndExpr {$$=$1;}
		;

AndExpr:	AndExpr E EqExpr {printf("montando AndExpr");$$=CriaOperador(And, $1->linha, $1, $3, NULL, NULL);}
			|EqExpr {$$=$1;}
		;

EqExpr:	EqExpr '=' '=' DesigExpr {printf("montando EqExpr");$$=CriaOperador(Igual, $1->linha, $1, $4, NULL, NULL);}
			|EqExpr '!' '=' DesigExpr {printf("montando EqExpr");$$=CriaOperador(Diferente, $1->linha, $1, $4, NULL, NULL);}
			|DesigExpr {$$=$1;}
		;

DesigExpr:	DesigExpr '<' AddExpr {printf("montando DesigExpr");$$=CriaOperador(Menor, $1->linha, $1, $3, NULL, NULL);}
			|DesigExpr '>' AddExpr {printf("montando DesigExpr");$$=CriaOperador(Maior, $1->linha, $1, $3, NULL, NULL);}
			|DesigExpr GEQ AddExpr {printf("montando DesigExpr");$$=CriaOperador(MaiorQ, $1->linha, $1, $3, NULL, NULL);}
			|DesigExpr LEQ AddExpr {printf("montando DesigExpr");$$=CriaOperador(MenorQ, $1->linha, $1, $3, NULL, NULL);}
			|AddExpr {$$=$1;}
		;

AddExpr: 	AddExpr '+' MulExpr {printf("montando AddExpr");$$=CriaOperador(Mais, $1->linha, $1, $3, NULL, NULL);}
			|AddExpr '-' MulExpr {printf("montando AddExpr");$$=CriaOperador(Menos, $1->linha, $1, $3, NULL, NULL);}
			|MulExpr {$$=$1;}
		;

MulExpr:	MulExpr '*' UnExpr {printf("montando MulExpr");$$=CriaOperador(Mult, $1->linha, $1, $3, NULL, NULL);}
			|MulExpr '/' UnExpr {printf("montando MulExpr");$$=CriaOperador(Div, $1->linha, $1, $3, NULL, NULL);}
			|MulExpr '%' UnExpr {printf("montando MulExpr");$$=CriaOperador(Resto, $1->linha, $1, $3, NULL, NULL);}
			|UnExpr {$$=$1;}
		;

UnExpr:	'-' PrimExpr {printf("montando UnExpr");$$=CriaOperador(Menos, $1->linha, $2, NULL, NULL, NULL);}
			|'!' PrimExpr {printf("montando UnExpr");$$=CriaOperador(Diferente, $1->linha, $2, NULL, NULL, NULL);}
			|PrimExpr {$$=$1;}
		;

LValueExpr:	IDENTIFICADOR '[' Expr ']' {printf("montando LValueExpr");$$=CriaOperador(Array, $1->linha, $3, NULL, NULL, $1->lexema);}
				|IDENTIFICADOR {$$=$1;}
			;

PrimExpr:	IDENTIFICADOR '(' ListExpr ')' {printf("montando PrimExpr");$$=CriaOperador(Call, $1->linha, $3, NULL, NULL, $1->lexema);}
			|IDENTIFICADOR '(' ')' {printf("montando PrimExpr");$$=CriaOperador(Call, $1->linha, NULL, NULL, NULL, $1->lexema);}
			|IDENTIFICADOR '[' Expr ']' {printf("montando PrimExpr");$$=CriaOperador(Array, $1->linha, $3, NULL, NULL, $1->lexema);}
			|IDENTIFICADOR {$$=$1;}
			|CADEIACHAR {$$=$1;}
			|INTCONST	{$$=$1;}
			|'(' Expr ')' {$$=$1;}
		;

ListExpr:	AssignExpr {$$=$1;}
			|ListExpr ',' AssignExpr {printf("montando ListExpr");$$=CriaOperador(Lexpr, $1->linha, $3, NULL, NULL, NULL);}
		;	

	
%%

int main (int argc, char** argv)
{
   // if(argc != 2) yyerror("Uso correto: ./simpleLang nome_arq_entrada");

   yyin = fopen("/Users/Gabriel/Downloads/exemploSint/expressao1.z", "r");

   if(!yyin) yyerror("arquivo não pode ser aberto\n");

   yyparse();
   percorreArvore(raiz);
    printf("\n");
    return 0;
}

void yyerror( char const *s) {
    if(erroOrigem==0) /*Erro lexico*/
    {
        printf("%s na linha %d - token: %s\n", s, numLinha, yytext);
    }
    else
    {
        printf("Erro sintatico proximo a %s ", yytext);
        printf(" - linha: %d \n", numLinha);
        erroOrigem=1;
    }
    exit(1);
}

void percorreArvore(Toperador* raiz){
    if(raiz){
        imprimeNo(raiz);
        // printf("%s ", nomeOperador);
        percorreArvore(raiz->filho1);
        percorreArvore(raiz->filho2);
        percorreArvore(raiz->filho3);
        }
}



Toperador* CriaVarEFunc(TespecieOperador tipoOperador, int linha, Toperador* filho1, Toperador* filho2, Toperador* filho3, TVar tipoVar, char* lexema){
    
    Toperador* aux = CriaOperador(tipoOperador, linha, filho1, filho2, filho3, lexema);
    if (aux){
        aux->tipoVariavel=tipoVar;
        return(aux);
    }
    return(NULL);
}


Toperador* CriaOperador(TespecieOperador tipoOp, int linha, Toperador* filho1, Toperador* filho2, Toperador* filho3, char* lexema){
    Toperador* aux = (Toperador*) malloc(sizeof(Toperador));
    if (aux){
        aux->tipoOperador=tipoOp;
        aux->linha=linha;
        aux->filho1=filho1;
        aux->filho2=filho2;
        aux->filho3=filho3;
        aux->tipoVariavel=None;
        if(lexema){
            aux->lexema= (char*)malloc(strlen(lexema)+1);
            strcpy(aux->lexema, lexema);
        }
        imprimeNo(aux);
        return(aux);
    }
    return(NULL);
}

void obtemEspecieNoEnumLin(Toperador* no, char* nomeOperador){
    switch(no->tipoOperador){
    	case Raiz: //TODO definir Raiz
    	sprintf(nomeOperador, "Raiz - Lin: %d ", no->linha);
    	break;
    	case Programa: //TODO definir Raiz
    	sprintf(nomeOperador, "Funcao - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case Parametro: //TODO definir Raiz
    	sprintf(nomeOperador, "Funcao - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case ListaParametros:
    	sprintf(nomeOperador, "Funcao - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case Funcao:
    	sprintf(nomeOperador, "Funcao - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case Variavel:
    	sprintf(nomeOperador, "Variavel - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
        case If:
        sprintf(nomeOperador, "If - Lin: %d", no->linha);
        break;
        case Enquanto :
        sprintf(nomeOperador, "Enquanto - Lin: %d", no->linha);
        break;
        case Se:
        sprintf(nomeOperador, "Se - Lin: %d", no->linha);
        break;
        case Maior :
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Mais:
        sprintf(nomeOperador, "+ - Lin: %d", no->linha);
        break;
        case Mult:
        sprintf(nomeOperador, "* - Lin: %d", no->linha);
        break;
        case Menor:
        sprintf(nomeOperador, "< - Lin: %d", no->linha);
        break;
        case Igual:
        sprintf(nomeOperador, "= - Lin: %d", no->linha);
        break;
        case MenorQ:
        sprintf(nomeOperador, "<= - Lin: %d", no->linha);
        break;
		case YY:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Menos:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Div:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Resto:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Leia:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Lexpr:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Array:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Call:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case MaiorQ:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Bloco:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Ti:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case ListaComando:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Retorne:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Escreva:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Novalinha:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Assign:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Ou:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case And:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
        case Diferente:
        sprintf(nomeOperador, "%s - Lin: %d", no->lexema,no->linha);
        break;
    }
}

void imprimeNo(Toperador* no){
    switch(no->tipoOperador){
    	case Raiz: 
    	printf("Raiz - Lin: %d ", no->linha);
    	break;
    	case Programa: 
    	printf( "Programa - Lin: %d", no->linha);
    	break;
    	case Parametro: 
    	printf("Parametro - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case ListaParametros:
    	printf("ListaParametros - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case Funcao:
    	printf("Funcao - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
    	case Variavel:
    	printf("Variavel - Lin: %d nome: %s", no->linha, no->lexema);
    	break;
        case If:
        printf("If - Lin: %d", no->linha);
        break;
        case Enquanto :
        printf("Enquanto - Lin: %d", no->linha);
        break;
        case Se:
        printf("Se - Lin: %d", no->linha);
        break;
        case Maior :
        printf("> - Lin: %d", no->linha);
        break;
        case Mais:
        printf( "+ - Lin: %d", no->linha);
        break;
        case Mult:
        printf("* - Lin: %d", no->linha);
        break;
        case Menor:
        printf("< - Lin: %d", no->linha);
        break;
        case Igual:
        printf("= - Lin: %d", no->linha);
        break;
        case MenorQ:
        printf("<= - Lin: %d", no->linha);
        break;
		case YY:
        printf("YY - Lin: %d", no->linha);
        break;
        case Menos:
        printf("Menos - Lin: %d", no->linha);
        break;
        case Div:
        printf("Div - Lin: %d", no->linha);
        break;
        case Resto:
        printf("Resto - Lin: %d", no->linha);
        break;
        case Leia:
        printf("Leia - Lin: %d", no->linha);
        break;
        case Lexpr:
        printf("Lexpr - Lin: %d", no->linha);
        break;
        case Array:
        printf("Array - Lin: %d %s", no->linha, no->lexema);
        break;
        case Call:
        printf("Call - Lin: %d %s", no->linha,no->lexema);
        break;
        case MaiorQ:
        printf("MaiorQ - Lin: %d", no->linha);
        break;
        case Bloco:
        printf("Bloco - Lin: %d", no->linha);
        break;
        case Ti:
        printf("Ti - Lin: %d", no->linha);
        break;
        case ListaComando:
        printf("ListaComando - Lin: %d", no->linha);
        break;
        case Retorne:
        printf("Retorne - Lin: %d", no->linha);
        break;
        case Escreva:
        printf("Escreva - Lin: %d", no->linha);
        break;
        case Novalinha:
        printf("Novalinha - Lin: %d", no->linha);
        break;
        case Assign:
        printf("Assign - Lin: %d", no->linha);
        break;
        case Ou:
        printf("Ou - Lin: %d", no->linha);
        break;
        case And:
        printf("And - Lin: %d" ,no->linha);
        break;
        case Diferente:
        printf("Diferente - Lin: %d", no->linha);
        break;

    }
	printf("\n");

}