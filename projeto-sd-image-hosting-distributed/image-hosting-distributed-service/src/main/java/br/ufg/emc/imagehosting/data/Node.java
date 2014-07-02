package br.ufg.emc.imagehosting.data;

import java.util.ArrayList;
import java.util.List;

import br.ufg.emc.imagehosting.common.data.DTO;

public class Node extends DTO{

	private static final long serialVersionUID = 1L;

	private String name;
	private final String ip;
	private final List<Node> nodesReplications = new ArrayList<Node>();
	private boolean alive = true;
	private int port = 9001;
	private Index index;

	public Node(String ip){
		this.ip = ip;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}

	public List<Node> getNodesReplications() {
		return nodesReplications;
	}

	public void addReplication(Node node){
		this.nodesReplications.add(node);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if (obj != null && obj instanceof Node){

			if (this == obj)
				return true;

			Node node = (Node) obj;
			return node.getIp().equals(this.ip) ? true : false;
		}
		return false;
	}

	public String toString(){
		return "Node ip:"+this.ip;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

}
