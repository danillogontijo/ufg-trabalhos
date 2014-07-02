package br.ufg.emc.imagehosting.client;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.util.PropertiesUtil;

public class Teste extends Base{

	public static void main(String[] args) throws RemoteException {
		String[] ar = {"localhost:9000","upload","/Users/danilo.gontijo/Documents/mysavedplaces_closed.png"};

		ClientHostingApp.main(ar);

	}

}
