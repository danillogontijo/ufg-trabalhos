package br.ufg.emc.imagehosting.common;

import java.io.Serializable;

public abstract class DTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String naming;

	public String getNaming() {
		return naming;
	}

	public void setNaming(String naming) {
		this.naming = naming;
	}

}
