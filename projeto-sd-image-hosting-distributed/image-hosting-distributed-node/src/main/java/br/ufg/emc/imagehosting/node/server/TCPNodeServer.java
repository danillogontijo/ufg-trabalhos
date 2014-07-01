package br.ufg.emc.imagehosting.node.server;

import java.util.List;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ClusterService;
import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.jndi.InitialContext;
import br.ufg.emc.imagehosting.service.stub.ProxyMasterService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;



public class TCPNodeServer extends Base{
	
	private final Node node;
	private static TCPNodeServer serverNode = FactoryUtil.getFactory().get(TCPNodeServer.class);
	private final String naming = "masterService";
	
	public TCPNodeServer(){
		this.node = new Node(getValue("host"));
		init();
	}
	
	private void init(){
		this.node.setName(getValue("name"));
		this.node.setPort(getIntValue("port"));
		this.node.setNaming(naming);
	}

	public static void main(String[] args) throws RemoteException {
		
		InitialContext context = new InitialContext();
		context.bind("nodeService");
		
		registryNode();

		int port = serverNode.node.getPort();

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
	
	private static void registryNode() throws RemoteException{
		List<String> masters = serverNode.getListValues("masters");
		for (String master : masters) {
			if(ping(master)){
				System.out.println("Registry node on server master: " + master);
				ClusterService masterService = new ProxyMasterService(master);
				masterService.add(serverNode.node);
			}else{
				System.err.println("Master is NOT alive: " + master);
			}
		}
	}
	
	private static boolean ping(String host){
		String[] ar = host.split(":");
		return NetworkUtil.ping(ar[0], Integer.parseInt(ar[1]));
	}


}
