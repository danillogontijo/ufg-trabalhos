package br.ufg.emc.imagehosting.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkUtil {
	
	public static boolean ping(String host, int port){
		 SocketAddress sockaddr = new InetSocketAddress(host, port);
         Socket socket = new Socket();
         boolean online = true;
         try {
             socket.connect(sockaddr, 10000);
         }
         catch (IOException IOException) {
             online = false;
         }finally{
        	 try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
         
         return online;
	}
	
	public static boolean ping(String host){
		String[] ar = host.split(":");
		return NetworkUtil.ping(ar[0], Integer.parseInt(ar[1]));
	}

}
