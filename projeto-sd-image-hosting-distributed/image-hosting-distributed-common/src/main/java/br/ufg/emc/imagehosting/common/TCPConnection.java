package br.ufg.emc.imagehosting.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPConnection {

	private int port = 9000;
	private String server = "localhost";
	private Socket clientSocket;

	public TCPConnection(String host){
		String[] ar = host.split(":");
		this.server = ar[0];
		this.port = Integer.parseInt(ar[1]);
	}

	public TCPConnection(String host, int port){
		this.server = host;
		this.port = port;
	}

	public void open() throws RemoteException {

		System.out.println("Openning connection " + server + ":" + port);

		try {
			clientSocket = new Socket(server, port);
		} catch (UnknownHostException e) {
			throw new RemoteException("Can't open tcp connection", e);
		} catch (IOException e) {
			throw new RemoteException("Can't open tcp connection", e);
		}
	}

	public Object lookup(String naming) throws RemoteException{
		return send(naming);
	}

	public Object send(Object object) throws RemoteException {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Object objreturn = null;
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(object);

			// Read and display the response message sent by server application
			ois = new ObjectInputStream(clientSocket.getInputStream());
			objreturn = (Object) ois.readObject();

		} catch (ClassNotFoundException e) {
			throw new RemoteException("Class not found: ", e);
		} catch (IOException e) {
			throw new RemoteException("Error to return data: ", e);
		} finally{
			try {
				if(ois != null){
					ois.close();
				}
				if(oos != null){
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println("Received from server: " + objreturn);
		}
		return objreturn;
	}

	public void close() throws RemoteException{
		try {
			clientSocket.close();
		} catch (IOException e) {
			throw new RemoteException("I cannot close conn.", e);
		}
	}

}
