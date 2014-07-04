package br.ufg.emc.imagehosting.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.ufg.emc.imagehosting.common.RemoteException;

public class FileUtil {

	public static byte[] getFile(String localfile) throws RemoteException{

		byte[] encoded = null;

		FileInputStream input;
		try {
			input = new FileInputStream(new File(localfile));
			encoded = IOUtils.toByteArray(input);
		} catch (FileNotFoundException e) {
			throw new RemoteException("File not found: " + localfile, e);
		} catch (IOException e) {
			throw new RemoteException("Can't get data", e);
		}

		return encoded;
	}

	public static void saveFile(String localtosave, byte[] data) throws RemoteException{
		try {
			FileUtils.writeByteArrayToFile(new File(localtosave), data);
		} catch (IOException e) {
			throw new RemoteException("Can't write data", e);
		}
	}

}
