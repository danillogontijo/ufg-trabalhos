package br.ufg.emc.imagehosting.service.remote;

import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.service.GenericService;
import br.ufg.emc.imagehosting.data.Node;

public interface ClusterService<T extends Node> extends GenericService {

	public void addNode(T node) throws RemoteException;
	public void addMaster(T node) throws RemoteException;
	public void registryNode(T node) throws RemoteException;
	public void refreshIndex(T node) throws RemoteException;
	public void refreshNodes(T node) throws RemoteException;
	public T synchronizeMaster(T node) throws RemoteException;
	public void synchronizeIndex(T node) throws RemoteException;

}
