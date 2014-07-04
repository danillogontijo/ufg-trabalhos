package br.ufg.emc.imagehosting.data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.ufg.emc.imagehosting.common.data.DTO;

public class Node extends DTO{

	private static final long serialVersionUID = 1L;

	private String name;
	private String ip;
	private final Set<Node> nodesReplications = new LinkedHashSet<Node>();
	private List<Node> nodes = new ArrayList<Node>();
	private boolean alive = true;
	private int port = 9001;
	private Index index;

	public Node(String id) {
		super(id);
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
	public void setIp(String ip) {
		this.ip = ip;
	}

	public Set<Node> getNodesReplications() {
		return nodesReplications;
	}

	public void addReplication(Node node){
		this.nodesReplications.add(node);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((super.getId() == null) ? 0 : super.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if (obj != null && obj instanceof Node){

			if (this == obj)
				return true;

			Node node = (Node) obj;
			return node.getId().equals(super.getId()) ? true : false;
		}
		return false;
	}

	public String toString(){
		return "Node id:"+super.getId();
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

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Node> getAllNodes(){
		List<Node> all = new ArrayList<Node>(nodesReplications);
		all.add(this);
		return all;
	}

}
