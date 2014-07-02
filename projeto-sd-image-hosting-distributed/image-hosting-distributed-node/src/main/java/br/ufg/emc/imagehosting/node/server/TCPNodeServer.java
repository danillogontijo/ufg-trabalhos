package br.ufg.emc.imagehosting.node.server;

import java.util.List;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.jndi.InitialContext;
import br.ufg.emc.imagehosting.master.config.Params;
import br.ufg.emc.imagehosting.service.remote.ClusterService;
import br.ufg.emc.imagehosting.service.stub.ProxyMasterService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;



public class TCPNodeServer extends Base{

	private final Node node;
	private static TCPNodeServer serverNode = FactoryUtil.getFactory().get(TCPNodeServer.class);
	private final String naming = "masterService";

	public TCPNodeServer(){
		this.node = new Node(getValue(Params.HOST)+":"+getValue(Params.PORT));
		init();
	}

	private void init(){
		this.node.setName(getValue(Params.NAME));
		this.node.setIp(getValue(Params.HOST));
		this.node.setPort(getIntValue(Params.PORT));
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

		System.out.println("Starting "+ serverNode.node.getIp() +" in port: " + port);

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

	/**
	 * Realiza o registro do node nos masters
	 *
	 * @throws RemoteException
	 */
	private static void registryNode() throws RemoteException{
		List<String> masters = serverNode.getListValues("masters");
		for (String master : masters) {
			if(ping(master)){
				System.out.println("Registry node on server master: " + master);
				ClusterService<Node> masterService = new ProxyMasterService(master);
				masterService.registryNode(serverNode.node);
			}else{
				System.err.println("Master is NOT alive: " + master);
			}
		}
	}

	private static boolean ping(String host){
		return NetworkUtil.ping(host);
	}


}
