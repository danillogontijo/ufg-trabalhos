package br.ufg.emc.compiladores.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//import java_cup.runtime.Symbol;
//import br.ufg.emc.compiladores.lexico.CafezinhoLex;
import br.ufg.emc.compiladores.sintatico.byacc.Parser;
//import br.ufg.emc.compiladores.sintatico.cup.parser;

public class Cafezinho {

	static boolean do_debug_parse = true;

	public static void main(String[] args) throws IOException {

		if(args.length == 0){
			System.out.println("Syntax: java -jar cafezinho.jar teste.z byacc");
			System.exit(0);
		}

		String filename = args[0];
		char syntactic_program = 'b';
		if(args.length == 2){
			syntactic_program = args[1].charAt(0);
		}

		switch (syntactic_program) {
		case 'b':
				Parser.main(args);
			break;

		case 'c':
//				useCup(filename);
			break;
		default:
			break;
		}
	}

//	public static void useCup(String file) throws FileNotFoundException{
//
//		parser parser_obj = new parser(
//				new CafezinhoLex(new FileReader(file)));
//		Symbol parse_tree = null;
//		try {
//			if (do_debug_parse)
//				parse_tree = parser_obj.debug_parse();
//			else
//				parse_tree = parser_obj.parse();
//			System.out.println("Entrada correcta");
//		} catch (Exception e) {
//			System.out.println("Entrada invalida");
//		}
//	}

}
