package br.ufg.emc.imagehosting.master.server;


import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.jndi.InitialContext;
import br.ufg.emc.imagehosting.util.FactoryUtil;

public class TCPMasterServer extends Base{

	private static TCPMasterServer serverMaster = FactoryUtil.getFactory().get(TCPMasterServer.class);
	
	public static void main(String[] args) {

		InitialContext context = new InitialContext();
		context.bind("masterService");

		int port = serverMaster.getIntValue("port");
		
		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}

		System.out.println("Starting sever in port: " + port);

		ThreadPooledServer server = new ThreadPooledServer(port);
		new Thread(server).start();

		try {
		    Thread.sleep(2000 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();

	}


}
