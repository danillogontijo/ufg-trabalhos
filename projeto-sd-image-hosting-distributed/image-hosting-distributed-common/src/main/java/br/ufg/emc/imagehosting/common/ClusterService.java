package br.ufg.emc.imagehosting.common;

public interface ClusterService extends ImageService {
	
	public void add(Node node) throws RemoteException;
	
}
