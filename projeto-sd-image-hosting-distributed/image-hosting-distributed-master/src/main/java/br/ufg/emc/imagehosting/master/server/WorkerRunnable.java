package br.ufg.emc.imagehosting.master.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;

import br.ufg.emc.imagehosting.common.ClusterService;
import br.ufg.emc.imagehosting.common.DTO;
import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.jndi.InitialContext;

public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    private InitialContext context = new InitialContext();

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
    	ObjectInputStream ois = null;

        try {
        	ClusterService clusterService;

            // Read a message sent by client application
            ois = new ObjectInputStream(clientSocket.getInputStream());
            DTO objClient = (DTO) ois.readObject();

            System.out.println("Receiving from cliente: " + objClient);

            if (objClient instanceof Image){
            	clusterService = lookup(objClient.getNaming());

            	// Invoke method by reflection
            	Method method = clusterService.getClass().getMethod(((Image)objClient).getMethodName(), Image.class);
            	Object objReturn = method.invoke(clusterService, objClient);

            	if(objReturn != null){
                    // Send a response information to the client application
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(objReturn);
                    oos.close();
            	}else{
            		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject("SUCESS!");
                    oos.close();
            	}

            }else if (objClient instanceof Node){
            	clusterService = lookup(objClient.getNaming());
            	try {
					clusterService.add((Node)objClient);
					ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject("SUCESS!");
                    oos.close();
				} catch (br.ufg.emc.imagehosting.common.RemoteException e) {
					e.printStackTrace();
					throw new RemoteException(e.getMessage());
				}
            }else{
            	throw new RemoteException("Object is not valid");
            }

        } catch (IOException e) {
            System.err.println("IOException: " + e);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(ois != null){
					ois.close();
				}
			} catch (IOException e) {
				System.err.println("Cannot close Inputstream: " + e.getMessage());
			}
		}
    }

    private ClusterService lookup(String jndiname) throws RemoteException{
    	return (ClusterService) context.lookup(jndiname);
    }
}