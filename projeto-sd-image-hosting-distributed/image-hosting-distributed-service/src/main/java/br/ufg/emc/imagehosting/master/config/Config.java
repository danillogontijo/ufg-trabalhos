package br.ufg.emc.imagehosting.master.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufg.emc.imagehosting.data.Node;

public class Config {

	private final static Map<String, Node> nodes = new LinkedHashMap<String, Node>();
	private final static Map<String, Node> masters = new LinkedHashMap<String, Node>();
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

	public synchronized static Node getNodeFromIndex(int index){
		List<Node> list = new ArrayList<Node>(nodes.values());

		return list.get(index);
	}

	public static List<Node> getNodesList(){
		return new ArrayList<Node>(nodes.values());
	}

	public static void addMaster(Node node){
		if(!masters.containsKey(node.getIp())){
			masters.put(node.getIp(), node);
		}
	}

	public static Node getMaster(String key){
		return masters.get(key);
	}

	public static Node removeMaster(Node node){
		Node masterRemoved = masters.remove(node.getIp());
		return masterRemoved;
	}

	public static Map<String, Node> getMasters(){
		return masters;
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
