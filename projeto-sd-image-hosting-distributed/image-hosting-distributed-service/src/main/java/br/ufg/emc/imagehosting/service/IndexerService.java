package br.ufg.emc.imagehosting.service;

import java.util.ArrayList;
import java.util.List;

import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.data.IndexTable;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.master.config.Config;
import br.ufg.emc.imagehosting.master.config.IndexType;

public class IndexerService {

	public IndexType getIndexTypeFromName(String filename) throws RemoteException{
		for (final IndexType aux : IndexType.values()) {
			if(filename.matches(aux.getRegex())){
				return aux;
			}
		}

		throw new RemoteException("IndexType nao encontrado.");
	}

	public Node index(String filename) throws RemoteException{
		IndexType index = getIndexTypeFromName(filename);

		return getBalanced(index);
	}

	public Node getFromIndexTable(String filename){
		return IndexTable.getFirstNode(IndexType.getIndex(filename));
	}

	private Node getBalanced(IndexType index) throws RemoteException{
		int sizeNodes = Config.getSizeNodes();

		if(sizeNodes == 0){
			throw new RemoteException("Nenhum node configurado.");
		}

		int offset = Config.SIZE_INDEX / sizeNodes;
		List<Node> nodes = new ArrayList<Node>(Config.getNodes().values());
		for(int i=0; i<sizeNodes; i++){
			if(index.ordinal() < ((i+1) * offset) + 1){
				return nodes.get(i);
			}
		}

		throw new RemoteException("Erro fatal na indexacao.");
	}

}
