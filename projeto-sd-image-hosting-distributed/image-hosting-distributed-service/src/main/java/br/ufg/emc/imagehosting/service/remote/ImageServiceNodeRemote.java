package br.ufg.emc.imagehosting.service.remote;

import org.apache.commons.lang3.StringUtils;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ClusterService;
import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.util.FileUtil;

public class ImageServiceNodeRemote extends Base implements ClusterService {

	private static final long serialVersionUID = 1L;
	
	private String FOLDER = "c://temp//";
	
	public ImageServiceNodeRemote(){
		String folder = getValue("localStorage");
		if(StringUtils.isNotBlank(folder)){
			FOLDER = folder;
		}
	}

	public void upload(Image image) throws RemoteException {
		FileUtil.saveFile(FOLDER+image.getFilename(), image.getFile());
		System.out.println("File saved: "+FOLDER+image.getFilename());
	}

	public Image download(Image image) throws RemoteException {
		byte[] data = FileUtil.getFile(FOLDER+image.getFilename());
		image.setFile(data);
		
		System.out.println("Returning file: "+FOLDER+image.getFilename());

		return image;
	}

	@Override
	public void add(Node node) throws RemoteException {
		throw new RemoteException("Not used.");
	}

}
