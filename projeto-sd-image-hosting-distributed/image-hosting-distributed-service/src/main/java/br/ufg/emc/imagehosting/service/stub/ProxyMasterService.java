package br.ufg.emc.imagehosting.service.stub;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.TCPConnection;
import br.ufg.emc.imagehosting.common.data.Image;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.master.config.Params;
import br.ufg.emc.imagehosting.service.remote.ClusterService;

public class ProxyMasterService extends Base implements ClusterService<Node>, ImageService<Image> {

	private static final long serialVersionUID = 1L;

	private final TCPConnection conn;

	public ProxyMasterService(String host){
		conn = new TCPConnection(host);
	}

	public ProxyMasterService(String host, int port){
		conn = new TCPConnection(host, port);
	}

	public ProxyMasterService(){
		conn = new TCPConnection(getValue(Params.HOST), getIntValue(Params.PORT));
	}

	@Override
	public void upload(Image image) throws RemoteException {
		throw new RemoteException("Not used.");
	}

	@Override
	public Image download(Image image) throws RemoteException {
		throw new RemoteException("Not used.");
	}

	@Override
	public void addNode(Node node) throws RemoteException {
		node.setMethodName("addNode");
		conn.open();
		conn.send(node);
		conn.close();
	}


	@Override
	public void addMaster(Node node) throws RemoteException {
		node.setMethodName("addMaster");
		conn.open();
		conn.send(node);
		conn.close();
	}

	@Override
	public Node synchronizeMaster(Node node) throws RemoteException {
		node.setMethodName("synchronizeMaster");
		conn.open();
		node = (Node) conn.send(node);
		conn.close();

		return node;
	}

	@Override
	public void refreshIndex(Node node) throws RemoteException {
		node.setMethodName("refreshIndex");
		conn.open();
		conn.send(node);
		conn.close();
	}

	@Override
	public void synchronizeIndex(Node node) throws RemoteException {
		node.setMethodName("synchronizeIndex");
		conn.open();
		conn.send(node);
		conn.close();
	}

	@Override
	public void registryNode(Node node) throws RemoteException {
		node.setMethodName("registryNode");
		conn.open();
		conn.send(node);
		conn.close();
	}

	@Override
	public void refreshNodes(Node node) throws RemoteException {
		node.setMethodName("refreshNodes");
		conn.open();
		conn.send(node);
		conn.close();
	}
}
