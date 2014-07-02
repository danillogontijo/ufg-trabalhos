package br.ufg.emc.imagehosting.data;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import br.ufg.emc.imagehosting.master.config.IndexType;

/**
 * Tabela de indexacao
 *
 * @author danilo.gontijo
 *
 */
public class IndexTable {

	private static final Map<IndexType, LinkedHashSet<Node>> indexMap;

	static{
		indexMap = new LinkedHashMap<IndexType, LinkedHashSet<Node>>(28);
		IndexType[] indexes = IndexType.values();
		for (IndexType index : indexes) {
			indexMap.put(index, new LinkedHashSet<Node>());
		}
	}

	public static void addNodeList(Map<IndexType, LinkedHashSet<Node>> indexMap){
		IndexTable.indexMap.putAll(indexMap);
	}

	/**
	 *
	 * @param node
	 */
	public static void addNode(IndexType indexType, Node node){
		getListNode(indexType).add(node);
	}

	public static Node getFirstNode(IndexType indexType){
		LinkedHashSet<Node> set = IndexTable.indexMap.get(indexType);
		Node first = null;
		for (Node node : set) {
			first = node;
			break;
		}
		set.remove(first);
		set.add(first);
		return first;
	}

	public static LinkedHashSet<Node> getListNode(IndexType index){
		return IndexTable.indexMap.get(index);
	}

	public static Map<IndexType, LinkedHashSet<Node>> getIndexMap(){
		return indexMap;
	}

	public static void main(String[] args) {
		LinkedHashSet<Node> set = new LinkedHashSet<Node>();
		set.add(new Node("1"));
		set.add(new Node("2"));
		set.add(new Node("3"));
		indexMap.put(IndexType.A, set);

		System.out.println(getListNode(IndexType.A));

		Node first = getFirstNode(IndexType.A);
		System.out.println(first);

		System.out.println(getListNode(IndexType.A));
	}

}
