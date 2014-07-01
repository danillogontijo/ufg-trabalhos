package br.ufg.emc.imagehosting.common;

import java.util.Arrays;

public class Image extends DTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String filename;
	private String methodName;
	private byte[] file;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		if (file == null) {
			this.file = new byte[0];
		} else {
			this.file = Arrays.copyOf(file, file.length);
		}
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(filename).append("]")
		.append("[").append(super.getNaming()).append("]")
		.append("[").append(methodName).append("]");

		return sb.toString();
	}
}
