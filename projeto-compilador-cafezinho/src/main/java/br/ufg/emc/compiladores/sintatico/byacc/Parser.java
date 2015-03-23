//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "sintatico.y"
	package br.ufg.emc.compiladores.sintatico.byacc;
	import java.io.*;
//#line 21 "Parser.java"

import jflex.br.ufg.emc.compiladores.lexico.Yylex;




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt;
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
public ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short CAR=257;
public final static short INT=258;
public final static short LBRACK=259;
public final static short RBRACK=260;
public final static short SEMICOLON=261;
public final static short COMMA=262;
public final static short LBRACE=263;
public final static short RBRACE=264;
public final static short LPAREN=265;
public final static short RPAREN=266;
public final static short COLON=267;
public final static short PROGRAMA=268;
public final static short RETORNE=269;
public final static short LEIA=270;
public final static short ESCREVA=271;
public final static short NOVALINHA=272;
public final static short SE=273;
public final static short ENTAO=274;
public final static short SENAO=275;
public final static short INTERROGACAO=276;
public final static short ENQUANTO=277;
public final static short EXECUTE=278;
public final static short OU=279;
public final static short E=280;
public final static short MENORQUE=281;
public final static short MAIORQUE=282;
public final static short MAIORIGUAL=283;
public final static short MENORIGUAL=284;
public final static short IGUAL=285;
public final static short DIFERENTE=286;
public final static short PLUS=287;
public final static short MINUS=288;
public final static short MULT=289;
public final static short NOT=290;
public final static short DIV=291;
public final static short MOD=292;
public final static short INTEGER_LITERAL=293;
public final static short CAR_LITERAL=294;
public final static short STRING_LITERAL=295;
public final static short ID=296;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    4,    4,    4,    4,
    5,    7,    7,    8,    8,    8,    8,    6,    6,    9,
    9,    9,    3,    3,   10,   10,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   14,   15,   12,
   16,   16,   17,   17,   18,   18,   19,   19,   20,   20,
   20,   21,   21,   21,   21,   21,   22,   22,   22,   23,
   23,   23,   23,   24,   24,   24,   13,   13,   25,   25,
   25,   25,   25,   25,   25,   26,   26,
};
final static short yylen[] = {                            2,
    2,    5,    8,    4,    0,    2,    2,    3,    6,    0,
    4,    0,    1,    2,    4,    4,    6,    4,    3,    0,
    5,    8,    1,    1,    1,    2,    1,    2,    3,    3,
    3,    3,    2,    1,    1,    6,    1,    6,    3,    1,
    1,    3,    1,    5,    3,    1,    3,    1,    3,    3,
    1,    3,    3,    3,    3,    1,    3,    3,    1,    3,
    3,    3,    1,    2,    2,    1,    4,    1,    4,    3,
    4,    1,    1,    1,    3,    1,    3,
};
final static short yydefred[] = {                         0,
   24,   23,    0,    0,    0,    0,    1,    0,    0,    6,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    7,
    0,    0,   13,    0,    4,    0,   27,   19,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   74,   73,    0,
   37,    0,    0,    0,    0,    0,   35,   40,   41,    0,
    0,    0,    0,    0,    0,   63,   66,    0,    0,    8,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,   33,    0,    0,    0,   64,   65,    0,    0,   18,
   26,   28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   11,    0,    0,   75,   29,    0,   30,   32,
   31,    0,    0,    0,    0,   70,   76,    0,   42,   39,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   60,   61,   62,    0,    0,    0,   16,    0,   21,
    0,    0,    0,    0,    0,    0,   69,    0,    3,    9,
    0,    0,   67,    0,    0,   71,   77,   44,   17,    0,
   38,   36,   22,
};
final static short yydgoto[] = {                          3,
    4,    7,    5,   14,   15,   41,   22,   23,   17,   42,
   43,   44,   45,   46,   47,   48,   49,   50,   51,   52,
   53,   54,   55,   56,   57,  118,
};
final static short yysindex[] = {                      -174,
    0,    0,    0, -255, -265, -219,    0, -239, -174,    0,
 -244, -243, -174, -197, -174, -228, -236, -181, -203,    0,
 -192, -158,    0, -174,    0, -196,    0,    0, -173, -173,
 -184, -193, -147, -139, -119, -115, -115,    0,    0, -235,
    0, -146, -100, -113, -117, -101,    0,    0,    0, -170,
 -105, -264,  -84, -189, -125,    0,    0,  -82, -111,    0,
 -143, -219,    0, -110,  -85,  -77,  -66,  -57,  -58,  -46,
  -40,    0, -173, -173, -227,    0,    0, -173, -248,    0,
    0,    0, -173, -100, -173, -141, -141, -141, -141, -141,
 -141, -141, -141, -141, -141, -141, -141, -141,  -39,  -76,
  -37, -174,    0,  -30, -174,    0,    0, -173,    0,    0,
    0,  -35,  -34, -173,  -27,    0,    0, -223,    0,    0,
  -32, -105, -264,  -84,  -84, -189, -189, -189, -189, -125,
 -125,    0,    0,    0, -174,  -82,  -26,    0,  -82,    0,
  -12,  -19,  -22,   -3,    0, -173,    0, -141,    0,    0,
 -174,  -10,    0, -100, -100,    0,    0,    0,    0, -174,
    0,    0,    0,
};
final static short yyrindex[] = {                        -7,
    0,    0,    0,    0,    0,    0,    0,    1, -183,    0,
    0,    1,   -6,    0,   -7,    0,    0,    0,    1,    0,
    0,    0,    0,   -7,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -75,
    0,    0,    2,    0,    0, -134,    0,    0,    0, -191,
  278,  249,  199,  112,   25,    0,    0,    1,    0,    0,
   -2,    0,    0,    0,    0,    0,    0,    4,    0,    0,
    0,    0,    0,    0,  -42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -183,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  286,  257,  220,  228,  123,  150,  161,  188,   54,
   83,    0,    0,    0,   -7,    1,    3,    0,    1,    0,
    0,    0,    0,    0,   -8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -183,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -13,    0,   -9,  -11,    0,   -1,    0,  -86,  -93,  224,
  -81,  -23,  232,    0,    0,  -69,  122,    0,  196,  201,
   52,  136,   50,   36,  121,    0,
};
final static int YYTABLESIZE=565;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   20,   25,  120,   21,   10,   66,   67,   60,   71,  117,
   63,  140,    6,  119,   65,  138,   29,  116,   12,   11,
   88,   89,   12,   78,   27,   13,    9,   28,   29,   79,
    8,  114,   30,   31,   32,   33,   34,   79,  146,   36,
   35,   37,  147,    9,   38,   39,   99,   40,   18,  112,
  113,   36,   19,   37,  115,   59,   38,   39,   12,   40,
  103,  121,   64,   24,  159,   12,  163,   26,   43,   43,
   43,   29,  161,  162,   43,   43,  157,   20,   58,   20,
   20,   20,    1,    2,  141,   20,   20,   20,   20,   20,
  144,   29,   21,   20,   36,   16,   37,   94,   95,   38,
   39,   70,   40,   61,   20,   85,   20,   62,   86,   20,
   20,   68,   20,   72,   36,  101,   37,   80,  102,   38,
   39,  149,   40,   29,  150,   73,   34,  152,   34,   34,
   34,  132,  133,  134,   34,   34,   34,   34,   34,  124,
  125,   21,   34,  130,  131,   74,   36,   82,   37,   29,
   16,   38,   39,   34,   75,   34,   76,   77,   34,   34,
   27,   34,    9,   96,   29,   97,   98,   83,   30,   31,
   32,   33,   34,   84,   87,  105,   35,   38,   39,   12,
   75,  100,  104,  136,   72,   72,   72,   36,  106,   37,
   72,   72,   38,   39,  107,   40,   90,   91,   92,   93,
   72,  108,  109,   72,   72,   72,   72,   72,   72,   68,
   72,   72,   72,   72,  110,   72,   72,   72,   72,   72,
  111,  135,  137,   72,   72,  126,  127,  128,  129,  139,
  142,  143,  145,   72,  148,  151,   72,   72,   72,   72,
   72,   72,   72,   72,   72,   72,   72,  153,   72,   72,
  160,   71,   71,   71,  154,  155,  156,   71,   71,   12,
    5,   10,   69,   14,   68,   25,   81,   71,   15,  158,
   71,   71,   71,   71,   71,   71,   67,   71,   71,   71,
   71,  122,   71,   71,   59,   59,   59,  123,    0,    0,
   59,   59,    0,    0,    0,    0,    0,    0,    0,    0,
   59,    0,    0,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   57,   57,   57,    0,    0,    0,   57,
   57,    0,    0,    0,    0,    0,    0,    0,    0,   57,
    0,    0,   57,   57,   57,   57,   57,   57,   57,   57,
   57,   57,   58,   58,   58,    0,    0,    0,   58,   58,
    0,    0,    0,    0,    0,    0,    0,    0,   58,    0,
    0,   58,   58,   58,   58,   58,   58,   58,   58,   58,
   58,   56,   56,   56,    0,    0,    0,   56,   56,    0,
    0,    0,   52,   52,   52,    0,    0,   56,   52,   52,
   56,   56,   56,   56,   56,   56,   56,   56,   52,    0,
    0,   52,   52,   52,   52,   52,   52,   52,   52,   53,
   53,   53,    0,    0,    0,   53,   53,    0,    0,    0,
   54,   54,   54,    0,    0,   53,   54,   54,   53,   53,
   53,   53,   53,   53,   53,   53,   54,    0,    0,   54,
   54,   54,   54,   54,   54,   54,   54,   55,   55,   55,
    0,    0,    0,   55,   55,    0,    0,    0,   51,   51,
   51,    0,    0,   55,   51,   51,   55,   55,   55,   55,
   55,   55,   55,   55,   51,    0,    0,   51,   51,   49,
   49,   49,    0,   51,   51,   49,   49,   50,   50,   50,
    0,    0,    0,   50,   50,   49,    0,    0,   49,   49,
    0,    0,    0,   50,   49,   49,   50,   50,   48,   48,
   48,    0,   50,   50,   48,   48,   47,   47,   47,    0,
    0,    0,   47,   47,   48,    0,    0,   48,   48,    0,
    0,    0,   47,    0,    0,   47,   47,   46,   46,   46,
    0,    0,    0,   46,   46,   45,   45,   45,    0,    0,
    0,   45,   45,   46,    0,    0,   46,    0,    0,    0,
    0,   45,    0,    0,   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   12,   15,   84,   13,    6,   29,   30,   19,   32,   79,
   24,  105,  268,   83,   26,  102,  265,  266,  262,  259,
  285,  286,  262,  259,  261,  265,  263,  264,  265,  265,
  296,  259,  269,  270,  271,  272,  273,  265,  262,  288,
  277,  290,  266,  263,  293,  294,   58,  296,  293,   73,
   74,  288,  296,  290,   78,  259,  293,  294,  262,  296,
   62,   85,  259,  261,  151,  262,  160,  296,  260,  261,
  262,  265,  154,  155,  266,  267,  146,  261,  260,  263,
  264,  265,  257,  258,  108,  269,  270,  271,  272,  273,
  114,  265,  102,  277,  288,  105,  290,  287,  288,  293,
  294,  295,  296,  296,  288,  276,  290,  266,  279,  293,
  294,  296,  296,  261,  288,  259,  290,  264,  262,  293,
  294,  135,  296,  265,  136,  265,  261,  139,  263,  264,
  265,   96,   97,   98,  269,  270,  271,  272,  273,   88,
   89,  151,  277,   94,   95,  265,  288,  261,  290,  265,
  160,  293,  294,  288,  296,  290,   36,   37,  293,  294,
  261,  296,  263,  289,  265,  291,  292,  285,  269,  270,
  271,  272,  273,  275,  280,  261,  277,  293,  294,  262,
  296,  293,  293,  260,  260,  261,  262,  288,  266,  290,
  266,  267,  293,  294,  261,  296,  281,  282,  283,  284,
  276,  259,  261,  279,  280,  281,  282,  283,  284,  285,
  286,  287,  288,  289,  261,  291,  292,  260,  261,  262,
  261,  261,  260,  266,  267,   90,   91,   92,   93,  260,
  266,  266,  260,  276,  267,  262,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  288,  289,  260,  291,  292,
  261,  260,  261,  262,  274,  278,  260,  266,  267,  266,
  268,  261,   31,  266,  261,  264,   43,  276,  266,  148,
  279,  280,  281,  282,  283,  284,  285,  286,  287,  288,
  289,   86,  291,  292,  260,  261,  262,   87,   -1,   -1,
  266,  267,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  276,   -1,   -1,  279,  280,  281,  282,  283,  284,  285,
  286,  287,  288,  260,  261,  262,   -1,   -1,   -1,  266,
  267,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,
   -1,   -1,  279,  280,  281,  282,  283,  284,  285,  286,
  287,  288,  260,  261,  262,   -1,   -1,   -1,  266,  267,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,   -1,
   -1,  279,  280,  281,  282,  283,  284,  285,  286,  287,
  288,  260,  261,  262,   -1,   -1,   -1,  266,  267,   -1,
   -1,   -1,  260,  261,  262,   -1,   -1,  276,  266,  267,
  279,  280,  281,  282,  283,  284,  285,  286,  276,   -1,
   -1,  279,  280,  281,  282,  283,  284,  285,  286,  260,
  261,  262,   -1,   -1,   -1,  266,  267,   -1,   -1,   -1,
  260,  261,  262,   -1,   -1,  276,  266,  267,  279,  280,
  281,  282,  283,  284,  285,  286,  276,   -1,   -1,  279,
  280,  281,  282,  283,  284,  285,  286,  260,  261,  262,
   -1,   -1,   -1,  266,  267,   -1,   -1,   -1,  260,  261,
  262,   -1,   -1,  276,  266,  267,  279,  280,  281,  282,
  283,  284,  285,  286,  276,   -1,   -1,  279,  280,  260,
  261,  262,   -1,  285,  286,  266,  267,  260,  261,  262,
   -1,   -1,   -1,  266,  267,  276,   -1,   -1,  279,  280,
   -1,   -1,   -1,  276,  285,  286,  279,  280,  260,  261,
  262,   -1,  285,  286,  266,  267,  260,  261,  262,   -1,
   -1,   -1,  266,  267,  276,   -1,   -1,  279,  280,   -1,
   -1,   -1,  276,   -1,   -1,  279,  280,  260,  261,  262,
   -1,   -1,   -1,  266,  267,  260,  261,  262,   -1,   -1,
   -1,  266,  267,  276,   -1,   -1,  279,   -1,   -1,   -1,
   -1,  276,   -1,   -1,  279,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=296;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"CAR","INT","LBRACK","RBRACK","SEMICOLON","COMMA","LBRACE",
"RBRACE","LPAREN","RPAREN","COLON","PROGRAMA","RETORNE","LEIA","ESCREVA",
"NOVALINHA","SE","ENTAO","SENAO","INTERROGACAO","ENQUANTO","EXECUTE","OU","E",
"MENORQUE","MAIORQUE","MAIORIGUAL","MENORIGUAL","IGUAL","DIFERENTE","PLUS",
"MINUS","MULT","NOT","DIV","MOD","INTEGER_LITERAL","CAR_LITERAL",
"STRING_LITERAL","ID",
};
final static String yyrule[] = {
"$accept : Programa",
"Programa : DeclFuncVar DeclProg",
"DeclFuncVar : Tipo ID DeclVar SEMICOLON DeclFuncVar",
"DeclFuncVar : Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON DeclFuncVar",
"DeclFuncVar : Tipo ID DeclFunc DeclFuncVar",
"DeclFuncVar :",
"DeclProg : PROGRAMA Bloco",
"DeclVar : COMMA DeclVar",
"DeclVar : COMMA ID DeclVar",
"DeclVar : COMMA ID LBRACK INTEGER_LITERAL RBRACK DeclVar",
"DeclVar :",
"DeclFunc : LPAREN ListaParametros RPAREN Bloco",
"ListaParametros :",
"ListaParametros : ListaParametrosCont",
"ListaParametrosCont : Tipo ID",
"ListaParametrosCont : Tipo ID LBRACK RBRACK",
"ListaParametrosCont : Tipo ID COMMA ListaParametrosCont",
"ListaParametrosCont : Tipo ID LBRACK RBRACK COMMA ListaParametrosCont",
"Bloco : LBRACE ListaDeclVar ListaComando RBRACE",
"Bloco : LBRACE ListaDeclVar RBRACE",
"ListaDeclVar :",
"ListaDeclVar : Tipo ID DeclVar SEMICOLON ListaDeclVar",
"ListaDeclVar : Tipo ID LBRACK INTEGER_LITERAL RBRACK DeclVar SEMICOLON ListaDeclVar",
"Tipo : INT",
"Tipo : CAR",
"ListaComando : Comando",
"ListaComando : Comando ListaComando",
"Comando : SEMICOLON",
"Comando : Expr SEMICOLON",
"Comando : RETORNE Expr SEMICOLON",
"Comando : LEIA LValueExpr SEMICOLON",
"Comando : ESCREVA Expr SEMICOLON",
"Comando : ESCREVA STRING_LITERAL SEMICOLON",
"Comando : NOVALINHA SEMICOLON",
"Comando : if_then_statement",
"Comando : if_then_else_statement",
"Comando : ENQUANTO LPAREN Expr RPAREN EXECUTE Comando",
"Comando : Bloco",
"if_then_statement : SE LPAREN Expr RPAREN ENTAO Comando",
"if_then_else_statement : if_then_statement SENAO Comando",
"Expr : AssingExpr",
"AssingExpr : CondExpr",
"AssingExpr : LValueExpr IGUAL AssingExpr",
"CondExpr : OrExpr",
"CondExpr : OrExpr INTERROGACAO Expr COLON CondExpr",
"OrExpr : OrExpr OU AndExpr",
"OrExpr : AndExpr",
"AndExpr : AndExpr E EqExpr",
"AndExpr : EqExpr",
"EqExpr : EqExpr IGUAL DesigExpr",
"EqExpr : EqExpr DIFERENTE DesigExpr",
"EqExpr : DesigExpr",
"DesigExpr : DesigExpr MENORQUE AddExpr",
"DesigExpr : DesigExpr MAIORQUE AddExpr",
"DesigExpr : DesigExpr MAIORIGUAL AddExpr",
"DesigExpr : DesigExpr MENORIGUAL AddExpr",
"DesigExpr : AddExpr",
"AddExpr : AddExpr PLUS MulExpr",
"AddExpr : AddExpr MINUS MulExpr",
"AddExpr : MulExpr",
"MulExpr : MulExpr MULT UnExpr",
"MulExpr : MulExpr DIV UnExpr",
"MulExpr : MulExpr MOD UnExpr",
"MulExpr : UnExpr",
"UnExpr : MINUS PrimExpr",
"UnExpr : NOT PrimExpr",
"UnExpr : PrimExpr",
"LValueExpr : ID LBRACK Expr RBRACK",
"LValueExpr : ID",
"PrimExpr : ID LPAREN ListExpr RPAREN",
"PrimExpr : ID LPAREN RPAREN",
"PrimExpr : ID LBRACK Expr RBRACK",
"PrimExpr : ID",
"PrimExpr : CAR_LITERAL",
"PrimExpr : INTEGER_LITERAL",
"PrimExpr : LPAREN Expr RPAREN",
"ListExpr : AssingExpr",
"ListExpr : ListExpr COMMA AssingExpr",
};

//#line 133 "sintatico.y"

   private Yylex lexer;

   private int yylex () {
    int yyl_return = -1;
    try {
//      yylval = new ParserVal(lexer.yylex().sym);
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
	yydebug=true;
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex - Cafezinho");

//    args = new String[1];
//    args[0] = "src/test/resources/teste.z";

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

    yyparser.yyparse();

    System.out.println("\n Entrada Valida");

    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
  }
//#line 506 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop");
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
