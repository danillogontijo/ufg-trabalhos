package br.ufg.emc.imagehosting.service.stub;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ClusterService;
import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.TCPConnection;

public class ProxyMasterService extends Base implements ClusterService {

	private static final long serialVersionUID = 1L;

	private final TCPConnection conn;

	public ProxyMasterService(String host){
		conn = new TCPConnection(host);
	}
	
	public ProxyMasterService(String host, int port){
		conn = new TCPConnection(host, port);
	}
	
	public ProxyMasterService(){
		conn = new TCPConnection(getValue("host"), getIntValue("port"));
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
	public void add(Node node) throws RemoteException {
		conn.open();
		conn.send(node);
		conn.close();
	}

}
