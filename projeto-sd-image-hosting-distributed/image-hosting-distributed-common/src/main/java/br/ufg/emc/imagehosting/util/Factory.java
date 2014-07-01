package br.ufg.emc.imagehosting.util;

public interface Factory {

	public <T> T get(Class<T> clazz);

	public <T> T create(Class<T> clazz, Object... params);

}
