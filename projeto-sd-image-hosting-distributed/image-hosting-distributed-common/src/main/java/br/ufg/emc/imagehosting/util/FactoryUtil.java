package br.ufg.emc.imagehosting.util;

public abstract class FactoryUtil {

	private static Factory instance;
	private static Object lock = new Object();

	public static void setup(Factory factory) {
		// TODO: como implementar esse controle sem prejudicar os testes
		// unit_rios?
		// synchronized (lock) {
		// if (instance == null) {
		// instance = factory;
		// } else {
		// throw new IllegalStateException("factory ja inicializado.");
		// }
		// }

		instance = factory;
	}

	public static Factory getFactory() {
		synchronized (lock) {
			if (instance == null) {
				setup(new DefaultFactory());
			}
		}
		return instance;
	}

}
