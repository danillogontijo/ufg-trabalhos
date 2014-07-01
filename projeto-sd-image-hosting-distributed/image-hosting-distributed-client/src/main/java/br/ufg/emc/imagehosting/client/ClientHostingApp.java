package br.ufg.emc.imagehosting.client;

import java.util.List;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.util.FileUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;

/**
 * Client hosting image
 *
 * @author Danillo Gontijo - 095907
 *
 */
public class ClientHostingApp extends Base {

	private final ImageService imageService;
	private final String naming = "masterService";
	private final Image image;

	public ClientHostingApp(String host){
		imageService = new ProxyImageService(host);
		image = new Image();
		image.setNaming(naming);
	}

	public Image getImage(){
		return this.image;
	}

	public Image getImageFromLocation(String location) throws RemoteException{
		String[] split = location.split("/");
		String filename = split[split.length-1];

		image.setFilename(filename);

		byte[] data = FileUtil.getFile(location);
		image.setFile(data);

		return image;
	}

	public void saveImageToLocation(String location) throws RemoteException{
		try {
			FileUtil.saveFile(location, image.getFile());
		} catch (RemoteException e) {
			throw new RemoteException("Can't save data into: " + location);
		}
	}

	public static void main(String[] args) throws RemoteException {

		ClientHostingApp client;

		if(args.length == 0){
			client = new ClientHostingApp("localhost:9000");

			args = new String[4];
			args[0] = "";
			args[1] = "upload";
			args[2] = "d:/image.jpg";

			args[1] = "download";
			args[2] = "c:/temp/image-downloaded.jpg";
			args[3] = "image.jpg";
		}else if(args[0].equals("help")){
			System.out.println("Use: [host:port] [upload|download] [image_location] [image_name (only for download)]");
			return;
		}else{
			client = new ClientHostingApp(args[0]);
			if(!NetworkUtil.ping(args[0])){
				List<String> servers = client.getListValues("masters");
				client = null;
				for (String server : servers) {
					if(NetworkUtil.ping(server)){
						client = new ClientHostingApp(server);
					}
				}
			}
			
			if(client == null){
				throw new RemoteException("There is no server active");
			}
		}

		String command = args[1];
		String imageLocation = args[2];

		Image image;

		switch (command) {
		case "upload":
			image = client.getImageFromLocation(imageLocation);
			image.setMethodName("upload");
			client.imageService.upload(image);
			break;

		case "download":
			String imagename = args[3];
			image = client.getImage();
			image.setFilename(imagename);
			image.setMethodName("download");
			image = client.imageService.download(image);
			client.getImage().setFile(image.getFile());
			client.saveImageToLocation(imageLocation);
			break;

		default:
			System.out.println("Command is not valid.");
			System.out.println("Use: [host:port] [upload|download] [image_location] [image_name (only for download)]");
			break;
		}
	}

}
