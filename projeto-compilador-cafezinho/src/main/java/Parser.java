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






//#line 1 "sintatico.y"

	/*package br.ufg.emc.compiladores.sintatico.byacc;
*/
	import java.io.*;

import br.ufg.emc.compiladores.lexico.*;
import br.ufg.emc.compiladores.interpreter.ast.*;
import br.ufg.emc.compiladores.interpreter.symtab.*;
//#line 24 "Parser.java"




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
ParserVal yylval;//the 'lval' (result) I got from yylex()
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
    0,    1,    1,    1,    1,    2,    3,    3,    3,    3,
    5,    7,    7,    8,    8,    8,    8,    6,    6,   11,
   11,   11,    4,    4,   10,   10,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   12,   14,   14,
   15,   15,   16,   16,   17,   17,   18,   18,   18,   19,
   19,   19,   19,   19,   20,   20,   20,   21,   21,   21,
   21,   22,   22,   22,   13,   13,   23,   23,   23,   23,
   23,   23,   23,   24,   24,
};
final static short yylen[] = {                            2,
    2,    5,    8,    4,    0,    2,    2,    3,    6,    0,
    4,    0,    1,    2,    4,    4,    6,    4,    3,    0,
    5,    8,    1,    1,    1,    2,    1,    2,    3,    3,
    3,    3,    2,    6,    8,    6,    1,    1,    1,    3,
    1,    5,    3,    1,    3,    1,    3,    3,    1,    3,
    3,    3,    3,    1,    3,    3,    1,    3,    3,    3,
    1,    2,    2,    1,    4,    1,    4,    3,    4,    1,
    1,    1,    3,    1,    3,
};
final static short yydefred[] = {                         0,
   24,   23,    0,    0,    0,    0,    1,    0,    0,    6,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    7,
    0,    0,   13,    0,    4,    0,   27,   19,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   72,   71,    0,
   37,    0,    0,    0,    0,   38,   39,    0,    0,    0,
    0,    0,    0,   61,   64,    0,    0,    8,    0,    0,
    2,    0,    0,    0,    0,    0,    0,    0,    0,   33,
    0,    0,    0,   62,   63,    0,    0,   26,   18,   28,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   11,
    0,    0,   73,   29,    0,   30,   32,   31,    0,    0,
    0,    0,   68,   74,    0,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   58,   59,   60,
    0,    0,    0,   16,    0,   21,    0,    0,    0,    0,
    0,    0,   67,    0,    3,    9,    0,    0,   65,    0,
    0,   69,   75,   42,   17,    0,    0,   36,   22,    0,
   35,
};
final static short yydgoto[] = {                          3,
    4,    7,   14,    5,   15,   41,   22,   23,   42,   43,
   17,   44,   45,   46,   47,   48,   49,   50,   51,   52,
   53,   54,   55,  115,
};
final static short yysindex[] = {                      -237,
    0,    0,    0, -263, -267, -209,    0, -176, -237,    0,
 -231, -245, -237, -192, -237, -198, -238, -173, -199,    0,
 -166, -125,    0, -237,    0, -163,    0,    0, -171, -171,
 -162,  -94, -106, -108, -104, -229, -229,    0,    0, -243,
    0, -121, -101,  -99, -119,    0,    0, -174, -112, -248,
 -155, -244, -146,    0,    0,  -92, -117,    0, -127, -209,
    0, -105,  -66,  -63,  -55,  -51,  -52,  -40,  -33,    0,
 -171, -171, -235,    0,    0, -171,  -61,    0,    0,    0,
 -171, -171,  -31,  -31,  -31,  -31,  -31,  -31,  -31,  -31,
  -31,  -31,  -31,  -31,  -31,  -20,  -18,   -6, -237,    0,
   -2, -237,    0,    0, -171,    0,    0,    0,   -5,    3,
 -171,    4,    0,    0, -181,    0,   -7, -112, -248, -155,
 -155, -244, -244, -244, -244, -146, -146,    0,    0,    0,
 -237,  -92,    8,    0,  -92,    0,   11,    1,   -4,   17,
    0, -171,    0,  -31,    0,    0, -237,   15,    0, -121,
 -121,    0,    0,    0,    0, -237,    5,    0,    0, -121,
    0,
};
final static short yyrindex[] = {                        10,
    0,    0,    0,    0,    0,    0,    0,   18, -193,    0,
    0,   18,   32,    0,   10,    0,    0,    0,   18,    0,
    0,    0,    0,   10,    0,   18,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -102,
    0,   19,    0,    0,    0,    0,    0, -220,  259,  230,
  180,   93,    6,    0,    0,   18,    0,    0,   33,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,  -69,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -193,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  267,  238,  201,
  209,  104,  131,  142,  169,   35,   64,    0,    0,    0,
   10,   18,   34,    0,   18,    0,    0,    0,    0,    0,
  -36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -193, -157,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  -13,    0,  -11,   -9,    0,   -3,    0,  -86, -132,  242,
  -90,  -23,  272,  -67,  160,    0,  222,  223,  -12,  149,
   62,   16,   55,    0,
};
final static int YYTABLESIZE=546;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   20,   25,   10,   21,    6,   64,   65,   58,   69,  114,
   61,  136,  134,  116,   63,   76,   12,  157,  158,    1,
    2,   77,   27,  111,    9,   28,   29,  161,    8,   77,
   30,   31,   32,   33,   34,   29,   85,   86,   35,   41,
   41,   41,   91,   92,   96,   41,   41,  109,  110,   36,
   19,   37,  112,    9,   38,   39,  100,   40,  117,   57,
  155,   18,   12,   38,   39,  159,   73,   20,   24,   20,
   20,   20,  120,  121,  153,   20,   20,   20,   20,   20,
  142,  137,   11,   20,  143,   12,   56,  140,   13,   21,
   74,   75,   16,   29,   20,   62,   20,   26,   12,   20,
   20,   82,   20,   34,   83,   34,   34,   34,  128,  129,
  130,   34,   34,   34,   34,   34,   36,  145,   37,   34,
  146,   38,   39,  148,   40,   87,   88,   89,   90,   59,
   34,   98,   34,   66,   99,   34,   34,   21,   34,   27,
   60,    9,   93,   29,   94,   95,   16,   30,   31,   32,
   33,   34,  126,  127,   70,   35,   71,   70,   70,   70,
   72,   80,   79,   70,   70,   81,   36,   84,   37,   12,
   29,   38,   39,   70,   40,   97,   70,   70,   70,   70,
   70,   70,   66,   70,   70,   70,   70,  101,   70,   70,
   70,   70,   70,   36,  102,   37,   70,   70,   38,   39,
   68,   40,  103,   29,  113,  104,   70,  105,  106,   70,
   70,   70,   70,   70,   70,   70,   70,   70,   70,   70,
  107,   70,   70,   69,   69,   69,   36,  108,   37,   69,
   69,   38,   39,   29,   40,  122,  123,  124,  125,   69,
  131,  132,   69,   69,   69,   69,   69,   69,   65,   69,
   69,   69,   69,  133,   69,   69,   36,  135,   37,  144,
  138,   38,   39,  141,   73,   57,   57,   57,  139,  147,
  149,   57,   57,  151,  150,  156,  152,    5,   10,  160,
   66,   57,   25,   78,   57,   57,   57,   57,   57,   57,
   57,   57,   57,   57,   55,   55,   55,   12,   14,   15,
   55,   55,   67,  154,  118,    0,  119,    0,    0,    0,
   55,    0,    0,   55,   55,   55,   55,   55,   55,   55,
   55,   55,   55,   56,   56,   56,    0,    0,    0,   56,
   56,    0,    0,    0,    0,    0,    0,    0,    0,   56,
    0,    0,   56,   56,   56,   56,   56,   56,   56,   56,
   56,   56,   54,   54,   54,    0,    0,    0,   54,   54,
    0,    0,    0,   50,   50,   50,    0,    0,   54,   50,
   50,   54,   54,   54,   54,   54,   54,   54,   54,   50,
    0,    0,   50,   50,   50,   50,   50,   50,   50,   50,
   51,   51,   51,    0,    0,    0,   51,   51,    0,    0,
    0,   52,   52,   52,    0,    0,   51,   52,   52,   51,
   51,   51,   51,   51,   51,   51,   51,   52,    0,    0,
   52,   52,   52,   52,   52,   52,   52,   52,   53,   53,
   53,    0,    0,    0,   53,   53,    0,    0,    0,   49,
   49,   49,    0,    0,   53,   49,   49,   53,   53,   53,
   53,   53,   53,   53,   53,   49,    0,    0,   49,   49,
   47,   47,   47,    0,   49,   49,   47,   47,   48,   48,
   48,    0,    0,    0,   48,   48,   47,    0,    0,   47,
   47,    0,    0,    0,   48,   47,   47,   48,   48,   46,
   46,   46,    0,   48,   48,   46,   46,   45,   45,   45,
    0,    0,    0,   45,   45,   46,    0,    0,   46,   46,
    0,    0,    0,   45,    0,    0,   45,   45,   44,   44,
   44,    0,    0,    0,   44,   44,   43,   43,   43,    0,
    0,    0,   43,   43,   44,    0,    0,   44,    0,    0,
    0,    0,   43,    0,    0,   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          9,
   12,   15,    6,   13,  268,   29,   30,   19,   32,   77,
   24,  102,   99,   81,   26,  259,  262,  150,  151,  257,
  258,  265,  261,  259,  263,  264,  265,  160,  296,  265,
  269,  270,  271,  272,  273,  265,  285,  286,  277,  260,
  261,  262,  287,  288,   56,  266,  267,   71,   72,  288,
  296,  290,   76,  263,  293,  294,   60,  296,   82,  259,
  147,  293,  262,  293,  294,  156,  296,  261,  261,  263,
  264,  265,   85,   86,  142,  269,  270,  271,  272,  273,
  262,  105,  259,  277,  266,  262,  260,  111,  265,   99,
   36,   37,  102,  265,  288,  259,  290,  296,  262,  293,
  294,  276,  296,  261,  279,  263,  264,  265,   93,   94,
   95,  269,  270,  271,  272,  273,  288,  131,  290,  277,
  132,  293,  294,  135,  296,  281,  282,  283,  284,  296,
  288,  259,  290,  296,  262,  293,  294,  147,  296,  261,
  266,  263,  289,  265,  291,  292,  156,  269,  270,  271,
  272,  273,   91,   92,  261,  277,  265,  260,  261,  262,
  265,  261,  264,  266,  267,  285,  288,  280,  290,  262,
  265,  293,  294,  276,  296,  293,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  288,  289,  293,  291,  292,
  260,  261,  262,  288,  261,  290,  266,  267,  293,  294,
  295,  296,  266,  265,  266,  261,  276,  259,  261,  279,
  280,  281,  282,  283,  284,  285,  286,  287,  288,  289,
  261,  291,  292,  260,  261,  262,  288,  261,  290,  266,
  267,  293,  294,  265,  296,   87,   88,   89,   90,  276,
  261,  260,  279,  280,  281,  282,  283,  284,  285,  286,
  287,  288,  289,  260,  291,  292,  288,  260,  290,  267,
  266,  293,  294,  260,  296,  260,  261,  262,  266,  262,
  260,  266,  267,  278,  274,  261,  260,  268,  261,  275,
  261,  276,  264,   42,  279,  280,  281,  282,  283,  284,
  285,  286,  287,  288,  260,  261,  262,  266,  266,  266,
  266,  267,   31,  144,   83,   -1,   84,   -1,   -1,   -1,
  276,   -1,   -1,  279,  280,  281,  282,  283,  284,  285,
  286,  287,  288,  260,  261,  262,   -1,   -1,   -1,  266,
  267,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,
   -1,   -1,  279,  280,  281,  282,  283,  284,  285,  286,
  287,  288,  260,  261,  262,   -1,   -1,   -1,  266,  267,
   -1,   -1,   -1,  260,  261,  262,   -1,   -1,  276,  266,
  267,  279,  280,  281,  282,  283,  284,  285,  286,  276,
   -1,   -1,  279,  280,  281,  282,  283,  284,  285,  286,
  260,  261,  262,   -1,   -1,   -1,  266,  267,   -1,   -1,
   -1,  260,  261,  262,   -1,   -1,  276,  266,  267,  279,
  280,  281,  282,  283,  284,  285,  286,  276,   -1,   -1,
  279,  280,  281,  282,  283,  284,  285,  286,  260,  261,
  262,   -1,   -1,   -1,  266,  267,   -1,   -1,   -1,  260,
  261,  262,   -1,   -1,  276,  266,  267,  279,  280,  281,
  282,  283,  284,  285,  286,  276,   -1,   -1,  279,  280,
  260,  261,  262,   -1,  285,  286,  266,  267,  260,  261,
  262,   -1,   -1,   -1,  266,  267,  276,   -1,   -1,  279,
  280,   -1,   -1,   -1,  276,  285,  286,  279,  280,  260,
  261,  262,   -1,  285,  286,  266,  267,  260,  261,  262,
   -1,   -1,   -1,  266,  267,  276,   -1,   -1,  279,  280,
   -1,   -1,   -1,  276,   -1,   -1,  279,  280,  260,  261,
  262,   -1,   -1,   -1,  266,  267,  260,  261,  262,   -1,
   -1,   -1,  266,  267,  276,   -1,   -1,  279,   -1,   -1,
   -1,   -1,  276,   -1,   -1,  279,
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
"Comando : SE LPAREN Expr RPAREN ENTAO Comando",
"Comando : SE LPAREN Expr RPAREN ENTAO Comando SENAO Comando",
"Comando : ENQUANTO LPAREN Expr RPAREN EXECUTE Comando",
"Comando : Bloco",
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

//#line 185 "sintatico.y"


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
    
	switch (typeop) {
	case Programa:
		return new Programa(filhos[0], filhos[1]);
	case Funcao:
		return new DeclFuncVar();
	case Variavel:
	
	case ListaParametros:
		
	default:
		break;
	}  
	  
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
//#line 539 "Parser.java"
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
case 1:
//#line 61 "sintatico.y"
{ yyval.obj = new Programa(val_peek(1).obj,val_peek(0).obj); }
break;
case 2:
//#line 64 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Variavel, val_peek(4).sval, val_peek(3).sval, val_peek(2).obj, val_peek(0).obj);}
break;
case 3:
//#line 65 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Variavel, val_peek(7).sval, val_peek(6).sval, val_peek(2).obj, val_peek(0).obj);}
break;
case 4:
//#line 66 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Funcao, val_peek(3).sval, val_peek(2).sval, val_peek(1).obj, val_peek(0).obj);}
break;
case 5:
//#line 67 "sintatico.y"
{yyval.obj=null;}
break;
case 6:
//#line 70 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Programa, null);}
break;
case 7:
//#line 73 "sintatico.y"
{yyval.obj = CriaOperador(TypeOp.Variavel, val_peek(0).obj, val_peek(0).obj);}
break;
case 8:
//#line 74 "sintatico.y"
{yyval.obj = CriaOperador(TypeOp.Variavel, val_peek(1).sval, val_peek(0).obj);}
break;
case 9:
//#line 75 "sintatico.y"
{yyval.obj = CriaOperador(TypeOp.Variavel, val_peek(4).sval, val_peek(0).obj);}
break;
case 10:
//#line 76 "sintatico.y"
{yyval.obj=null;}
break;
case 11:
//#line 79 "sintatico.y"
{yyval.obj = CriaOperador(TypeOp.ListaParametros, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 12:
//#line 82 "sintatico.y"
{yyval.obj=null;}
break;
case 13:
//#line 83 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 14:
//#line 86 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Parametro, val_peek(1).sval, val_peek(0).sval);}
break;
case 15:
//#line 87 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Parametro, val_peek(3).sval, val_peek(2).sval);}
break;
case 16:
//#line 88 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Parametro, val_peek(3).sval, val_peek(2).sval, val_peek(0).obj);}
break;
case 17:
//#line 89 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Parametro, val_peek(5).sval, val_peek(4).sval, val_peek(0).obj); }
break;
case 18:
//#line 92 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Bloco, null, null, val_peek(2).obj, val_peek(1).obj);}
break;
case 19:
//#line 93 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Bloco, null, null, val_peek(1).obj); }
break;
case 20:
//#line 96 "sintatico.y"
{yyval.obj=null;}
break;
case 21:
//#line 97 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Variavel, val_peek(4).sval, val_peek(3).sval, val_peek(2).obj, val_peek(0).obj);}
break;
case 22:
//#line 98 "sintatico.y"
{yyval.obj=CriaVarEFunc(TypeOp.Variavel, val_peek(7).sval, val_peek(6).sval, val_peek(4).ival, val_peek(2).obj);}
break;
case 23:
//#line 101 "sintatico.y"
{CriaVarEFunc(TypeOp.Tipo, null, null, "Tint");}
break;
case 24:
//#line 102 "sintatico.y"
{CriaVarEFunc(TypeOp.Tipo, null, null, "Tcar");}
break;
case 25:
//#line 105 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.ListaComando, val_peek(0).obj, null, val_peek(0).obj);}
break;
case 26:
//#line 106 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.ListaComando, null, val_peek(1).obj, val_peek(0).obj);}
break;
case 27:
//#line 109 "sintatico.y"
{yyval.obj=new Integer(SEMICOLON);}
break;
case 28:
//#line 110 "sintatico.y"
{yyval.obj=val_peek(1).obj;}
break;
case 29:
//#line 111 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Retorne, null, null, val_peek(1).obj);}
break;
case 30:
//#line 112 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Leia, null, null, val_peek(1).obj);}
break;
case 31:
//#line 113 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Escreva, null, null, val_peek(1).obj);}
break;
case 32:
//#line 114 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Escreva, null, val_peek(1).sval);}
break;
case 33:
//#line 115 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Novalinha, null, null);}
break;
case 34:
//#line 116 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Se, null, null, val_peek(3).obj, val_peek(0).obj);}
break;
case 35:
//#line 117 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Se, null, null, val_peek(5).obj, val_peek(2).obj, val_peek(0).obj);}
break;
case 36:
//#line 118 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Enquanto, null, null, val_peek(3).obj, val_peek(0).obj);}
break;
case 37:
//#line 119 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Bloco, null, null, val_peek(0).obj);}
break;
case 38:
//#line 122 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 39:
//#line 125 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 40:
//#line 126 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Assign, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 41:
//#line 129 "sintatico.y"
{ yyval.obj = val_peek(0).obj;}
break;
case 42:
//#line 130 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Se, null, null, val_peek(4).obj, val_peek(2).obj, val_peek(0).obj); }
break;
case 43:
//#line 133 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Ou, null, null, val_peek(2).obj, val_peek(0).obj); }
break;
case 45:
//#line 136 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.And, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 46:
//#line 137 "sintatico.y"
{ yyval.obj = val_peek(0).obj;}
break;
case 47:
//#line 140 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Igual, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 48:
//#line 141 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Diferente, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 49:
//#line 142 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 50:
//#line 145 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Menor, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 51:
//#line 146 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Maior, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 52:
//#line 147 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.MaiorQ, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 53:
//#line 148 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.MenorQ, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 54:
//#line 149 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 55:
//#line 152 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Mais, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 56:
//#line 153 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Menos, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 57:
//#line 154 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 58:
//#line 157 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Mult, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 59:
//#line 158 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Div, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 60:
//#line 159 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Resto, null, null, val_peek(2).obj, val_peek(0).obj);}
break;
case 61:
//#line 160 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 62:
//#line 163 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Menos, null, null, val_peek(0).obj);}
break;
case 63:
//#line 164 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Diferente, null, null, val_peek(0).obj);}
break;
case 64:
//#line 165 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 65:
//#line 168 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Array, null, val_peek(3).sval, val_peek(1).obj);}
break;
case 66:
//#line 169 "sintatico.y"
{yyval.obj=val_peek(0).sval;}
break;
case 67:
//#line 172 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Call, null, val_peek(3).sval, val_peek(1).obj);}
break;
case 68:
//#line 173 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Call, null, val_peek(2).sval);}
break;
case 69:
//#line 174 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Array, null, val_peek(3).sval, val_peek(1).obj);}
break;
case 70:
//#line 175 "sintatico.y"
{yyval.obj=val_peek(0).sval;}
break;
case 71:
//#line 176 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 72:
//#line 177 "sintatico.y"
{yyval.obj=val_peek(0).ival;}
break;
case 73:
//#line 178 "sintatico.y"
{yyval.obj=val_peek(1).obj;}
break;
case 74:
//#line 181 "sintatico.y"
{yyval.obj=val_peek(0).obj;}
break;
case 75:
//#line 182 "sintatico.y"
{yyval.obj=CriaOperador(TypeOp.Lexpr, null, null, val_peek(0).obj);}
break;
//#line 984 "Parser.java"
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