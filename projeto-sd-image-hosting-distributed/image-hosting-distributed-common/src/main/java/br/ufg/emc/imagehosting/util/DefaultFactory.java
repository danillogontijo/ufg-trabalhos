package br.ufg.emc.imagehosting.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public class DefaultFactory implements Factory {

	protected DefaultFactory() {

	}

	private final Map<Class<?>, Object> instancias = new HashMap<Class<?>, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {

		synchronized (clazz) {
			try {
				if (instancias.get(clazz) == null) {

					Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();

					if (!declaredConstructor.isAccessible()) {
						declaredConstructor.setAccessible(true);
					}
					T newInstance = declaredConstructor.newInstance();
					instancias.put(clazz, newInstance);
					// seria necessário setAccessible(false) aqui ???
					return newInstance;
				} else {
					return (T) instancias.get(clazz);
				}

			} catch (Exception e) {
				throw new RuntimeException(String.format("Nao foi possivel gerar instancia da classe %s", clazz.getName()), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> clazz, Object... params) {
		try {
			Constructor<?> constructor = null;
			if (params.length == 0) {
				constructor = clazz.getDeclaredConstructor();
			} else {
				for (Constructor<?> c : clazz.getDeclaredConstructors()) {
					boolean suitable = c.getParameterTypes().length > 0;
					int i = 0;
					for (Class<?> clsParam : c.getParameterTypes()) {
						suitable &= clsParam.isInstance(params[i++]);
					}
					if (suitable) {
						constructor = c;
						break;
					}
				}
			}
			if (constructor == null) {
				throw new IllegalArgumentException(String.format("Nao encontrado construtor em %s para os parametros informados %s",
						clazz.getName(), ArrayUtils.toString(params, "null")));
			}

			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			T newInstance = (T) constructor.newInstance(params);
			instancias.put(clazz, newInstance);
			return newInstance;
		} catch (Exception e) {
			throw new RuntimeException(String.format("Nao foi possivel gerar instancia da classe %s com os parametros %s",
					clazz.getName(), ArrayUtils.toString(params, "null")), e);
		}
	}

}
