package br.ufg.emc.imagehosting.master.config;

import java.util.LinkedHashMap;
import java.util.Map;

import br.ufg.emc.imagehosting.common.Node;

public class Config {
	
	private final static Map<String, Node> nodes = new LinkedHashMap<String, Node>();
	private static int sizeNodes = 0;
	public final static int SIZE_INDEX = 28;
	
	public static void addNode(Node node){
		if(!nodes.containsKey(node.getIp())){
			nodes.put(node.getIp(), node);
			sizeNodes++;
		}
	}
	
	public static Node getNode(String key){
		return nodes.get(key);
	}
	
	public static Node removeNode(Node node){
		Node nodeRemoved = nodes.remove(node.getIp());
		sizeNodes--;
		return nodeRemoved;
	}
	
	public static int getSizeNodes(){
		return sizeNodes;
	}
	
	public static Map<String,Node> getNodes(){
		return nodes;
	}

}
