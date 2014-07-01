package br.ufg.emc.imagehosting.client;

import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.TCPConnection;

public class ProxyImageService implements ImageService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final TCPConnection conn;

	public ProxyImageService(String host){
		conn = new TCPConnection(host);
	}

	@Override
	public void upload(Image image) throws RemoteException {
		conn.open();
		conn.send(image);
		conn.close();
	}

	@Override
	public Image download(Image image) throws RemoteException {
		conn.open();
		image = (Image) conn.send(image);
		conn.close();

		return image;
	}

}
