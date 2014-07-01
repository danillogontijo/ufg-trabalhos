package br.ufg.emc.imagehosting.common;

import java.io.Serializable;

public interface ImageService extends Serializable{

	public void upload(Image image) throws RemoteException;

	public Image download(Image image) throws RemoteException;

}
