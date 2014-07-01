package br.ufg.emc.imagehosting.rmi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClientTest {

	public static void main(String argv[]) throws Exception {

		String returnFromServer;

		int porta = 9000;
		String servidor = "localhost";

		System.out.println("Conectando ao servidor " + servidor + ":" + porta);

		Socket clientSocket = new Socket(servidor, porta);

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		returnFromServer = inFromServer.readLine();

		System.out.println("Recebido do servidor: " + returnFromServer);

		clientSocket.close();

	}

}
