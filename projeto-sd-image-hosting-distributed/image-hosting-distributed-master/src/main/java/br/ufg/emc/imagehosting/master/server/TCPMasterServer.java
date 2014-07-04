package br.ufg.emc.imagehosting.master.server;


import java.util.List;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.jndi.InitialContext;
import br.ufg.emc.imagehosting.master.config.Params;
import br.ufg.emc.imagehosting.service.remote.ClusterService;
import br.ufg.emc.imagehosting.service.remote.ImageServiceMasterRemote;
import br.ufg.emc.imagehosting.service.stub.ProxyMasterService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;

public class TCPMasterServer extends Base{

	private final Node master;
	private static TCPMasterServer serverMaster = FactoryUtil.getFactory().get(TCPMasterServer.class);
	private static final String naming = "masterService";
	private static InitialContext context = new InitialContext();

	public TCPMasterServer(){
		this.master = new Node(getValue(Params.HOST)+":"+getValue(Params.PORT));
		init();
	}

	private void init(){
		this.master.setName(getValue(Params.NAME));
		this.master.setIp(getValue(Params.HOST));
		this.master.setPort(getIntValue(Params.PORT));
		this.master.setNaming(naming);
	}

	public static void main(String[] args) throws RemoteException {

		context.bind(naming);

		int port = serverMaster.master.getPort();

		registryMaster();

		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}

		System.out.println("Starting sever master in port: " + port);

		ThreadPooledServer server = new ThreadPooledServer(port);
		new Thread(server).start();

		try {
		    Thread.sleep(2000 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server" + serverMaster.master.getName());
		server.stop();

	}

	private static void registryMaster() throws RemoteException{
		List<String> masters = serverMaster.getListValues(Params.MASTERS);

		for (String master : masters) {
			if(NetworkUtil.ping(master)){
				System.out.println("Registry master on: " + master);
				ClusterService<Node> masterService = new ProxyMasterService(master);
				Node masterRemote = masterService.synchronizeMaster(serverMaster.master);
				ClusterService<Node> service = (ClusterService<Node>) context.lookup(naming);
				try{
					service.refreshIndex(masterRemote);
					service.refreshNodes(masterRemote);
				}catch(RemoteException e){
					System.out.println(e.getMessage());
				}
				break;
			}
			System.err.println("Master is NOT alive: " + master);

		}
	}


}
