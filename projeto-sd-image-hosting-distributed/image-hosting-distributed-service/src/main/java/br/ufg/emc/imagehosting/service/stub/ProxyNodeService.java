package br.ufg.emc.imagehosting.service.stub;

import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.TCPConnection;

public class ProxyNodeService implements ImageService {

	private static final long serialVersionUID = 1L;

	private final TCPConnection conn;
	private final String naming = "nodeService";

	public ProxyNodeService(String host){
		conn = new TCPConnection(host);
	}
	
	public ProxyNodeService(String host, int port){
		conn = new TCPConnection(host, port);
	}

	@Override
	public void upload(Image image) throws RemoteException {
		image.setNaming(naming);
		conn.open();
		conn.send(image);
		conn.close();
	}

	@Override
	public Image download(Image image) throws RemoteException {
		image.setNaming(naming);
		conn.open();
		image = (Image) conn.send(image);
		conn.close();

		return image;
	}

}
