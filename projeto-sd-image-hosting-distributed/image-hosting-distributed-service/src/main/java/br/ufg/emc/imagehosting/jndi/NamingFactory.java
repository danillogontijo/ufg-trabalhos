package br.ufg.emc.imagehosting.jndi;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class NamingFactory {

	private static final Map<String, Object> objects = new HashMap<String, Object>();

	public static void registry(String name, Object object){
		objects.put(name, object);
	}

	public static Object get(String name) throws RemoteException{
		Object skeleton = objects.get(name);
		if(skeleton == null){
			throw new RemoteException("Naming not find.");
		}
		return skeleton;
	}

}
