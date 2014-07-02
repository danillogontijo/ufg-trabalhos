package br.ufg.emc.imagehosting.common.data;

import java.util.Arrays;

public class Image extends DTO {

	private static final long serialVersionUID = 1L;

	public Image(String id) {
		super(id);
	}

	public Image() {
		super("Image");
	}

	private String filename;
	private byte[] file;

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

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(filename).append("]")
		.append("[").append(super.getNaming()).append("]")
		.append("[").append(getMethodName()).append("]");

		return sb.toString();
	}
}
