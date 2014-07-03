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

	/**
	 * Adiciona um node caso ele ainda nao exista.
	 * <p>
	 * Caso o node já exista, ele é retornado,
	 * caso contrário é retornado NULL.
	 *
	 * @param node
	 */
	public static Node addNode(Node node){
		if(!nodes.containsKey(node.getId())){
			nodes.put(node.getId(), node);
			sizeNodes++;
		}else{
			return nodes.get(node);
		}

		return null;
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
		if(!masters.containsKey(node.getId())){
			masters.put(node.getIp(), node);
		}
	}

	public static Node getMaster(String key){
		return masters.get(key);
	}

	public static Node removeMaster(Node node){
		Node masterRemoved = masters.remove(node.getId());
		return masterRemoved;
	}

	public static Map<String, Node> getMasters(){
		return masters;
	}

	public static List<Node> getMastersList(){
		return new ArrayList<Node>(masters.values());
	}

	public static Node removeNode(Node node){
		Node nodeRemoved = nodes.remove(node.getId());
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
