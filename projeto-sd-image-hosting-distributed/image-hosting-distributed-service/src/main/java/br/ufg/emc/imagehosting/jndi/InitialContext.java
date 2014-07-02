package br.ufg.emc.imagehosting.jndi;

import java.rmi.RemoteException;

import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.service.remote.ClusterService;
import br.ufg.emc.imagehosting.service.remote.ImageServiceMasterRemote;
import br.ufg.emc.imagehosting.service.remote.ImageServiceNodeRemote;

public class InitialContext {

	public Object lookup(String jndiName) {
		
		switch (jndiName) {
		case "nodeService":
			System.out.println("Looking up a nodeService object");
			try {
				return (ClusterService) NamingFactory.get(jndiName);
			} catch (RemoteException e) {
				System.err.println("Not found service, use bind first");
			}
			break;
		case "masterService":
			System.out.println("Looking up a masterService object");
			try {
				return (ClusterService) NamingFactory.get(jndiName);
			} catch (RemoteException e) {
				System.err.println("Not found service, use bind first");
			}
			break;
		default:
			break;
		}
		
		return null;
	}
	
	public void bind(String jndiName) {
		
		ImageService service;
		
		switch (jndiName) {
		case "nodeService":
			System.out.println("Creating/binding a new nodeService object");

			service = new ImageServiceNodeRemote();
			NamingFactory.registry("nodeService", service);
			
			break;
		case "masterService":
			System.out.println("Creating/binding a new masterService object");

			service = new ImageServiceMasterRemote();
			NamingFactory.registry("masterService", service);
			
			break;

		default:
			break;
		}
		
	}

}
