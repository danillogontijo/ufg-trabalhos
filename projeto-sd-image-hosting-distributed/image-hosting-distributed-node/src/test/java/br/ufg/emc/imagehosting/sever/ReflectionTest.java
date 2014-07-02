package br.ufg.emc.imagehosting.sever;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.ufg.emc.imagehosting.common.data.Image;

public class ReflectionTest {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Image image = new Image();
		image.setFilename("nome do arquivo");

		Method m = image.getClass().getMethod("getFilename");
		Object obj = m.invoke(image);

		System.out.println(obj);
	}

}
