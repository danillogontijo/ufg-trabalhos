package br.ufg.emc.imagehosting.common;

import br.ufg.emc.imagehosting.common.data.Image;
import br.ufg.emc.imagehosting.common.service.GenericService;

public interface ImageService<T extends Image> extends GenericService{

	public void upload(T image) throws RemoteException;

	public T download(T image) throws RemoteException;

}
