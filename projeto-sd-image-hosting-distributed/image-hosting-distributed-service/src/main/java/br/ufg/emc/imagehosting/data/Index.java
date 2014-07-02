package br.ufg.emc.imagehosting.data;

import java.util.LinkedHashSet;
import java.util.Map;

import br.ufg.emc.imagehosting.common.data.DTO;
import br.ufg.emc.imagehosting.master.config.IndexType;

public class Index extends DTO {

	private static final long serialVersionUID = 1L;

	private IndexType indexType;
	private Map<IndexType, LinkedHashSet<Node>> indexMap;

	public Map<IndexType, LinkedHashSet<Node>> getIndexMap() {
		return indexMap;
	}
	public void setIndexMap(Map<IndexType, LinkedHashSet<Node>> indexMap) {
		this.indexMap = indexMap;
	}
	public IndexType getIndexType() {
		return indexType;
	}
	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

}
