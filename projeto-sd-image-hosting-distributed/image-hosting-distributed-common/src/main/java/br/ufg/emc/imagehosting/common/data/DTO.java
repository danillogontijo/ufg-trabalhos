package br.ufg.emc.imagehosting.common.data;

import java.io.Serializable;

public abstract class DTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private final String id;
	private String naming;
	private String methodName;

	public DTO(String id){
		this.id = id;
	}

	public String getNaming() {
		return naming;
	}

	public void setNaming(String naming) {
		this.naming = naming;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getId() {
		return id;
	}

}
