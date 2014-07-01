package br.ufg.emc.imagehosting.node.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;

import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.ImageService;
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
        	ImageService imageService;

            // Read a message sent by client application
            ois = new ObjectInputStream(clientSocket.getInputStream());
            Image objClient = (Image) ois.readObject();

            System.out.println("Receiving from cliente: " + objClient);

            if (objClient instanceof Image){
            	imageService = lookup(objClient.getNaming());

            	// Invoke method by reflection
            	Method method = imageService.getClass().getMethod(objClient.getMethodName(), Image.class);
            	Object objReturn = method.invoke(imageService, objClient);

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

    private ImageService lookup(String jndiname) throws RemoteException{
    	return (ImageService) context.lookup(jndiname);
    }
}