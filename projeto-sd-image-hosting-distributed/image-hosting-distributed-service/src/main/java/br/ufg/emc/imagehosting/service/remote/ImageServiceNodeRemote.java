package br.ufg.emc.imagehosting.service.remote;

import org.apache.commons.lang3.StringUtils;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.data.Image;
import br.ufg.emc.imagehosting.master.config.IndexType;
import br.ufg.emc.imagehosting.service.IndexerService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.FileUtil;

/**
 * Classe de servico do Node
 *
 * @author danilo.gontijo
 *
 */
public class ImageServiceNodeRemote extends Base implements ImageService<Image> {

	private static final long serialVersionUID = 1L;

	private String FOLDER = "c:/temp/";

	private IndexerService indexer;

	public ImageServiceNodeRemote(){
		this.indexer = FactoryUtil.getFactory().get(IndexerService.class);
		String folder = getValue("localStorage");
		if(StringUtils.isNotBlank(folder)){
			FOLDER = folder;
		}
	}

	/**
	 * Salva a imagem no pasta configurada do node
	 */
	public void upload(Image image) throws RemoteException {
		String folderFull = folder(image);

		FileUtil.saveFile(folderFull+image.getFilename(), image.getFile());
		System.out.println("File saved: "+folderFull+image.getFilename());
	}

	/**
	 * Traz o arquivo de imagem do local armazendo no node
	 */
	public Image download(Image image) throws RemoteException {
		try{
			byte[] data = FileUtil.getFile(folder(image)+image.getFilename());
			image.setFile(data);
			System.out.println("Returning file: "+folder(image)+image.getFilename());
		}catch(RemoteException e){
			System.out.println(e.getMessage());
			image.setException(e);
		}

		return image;
	}

	private String folder(Image image) throws RemoteException{
		IndexType indexType = indexer.getIndexTypeFromName(image.getFilename());
		return FOLDER + indexType.name() + "/";
	}

}
