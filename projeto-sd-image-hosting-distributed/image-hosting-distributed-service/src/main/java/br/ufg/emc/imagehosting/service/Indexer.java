package br.ufg.emc.imagehosting.service;

import java.util.ArrayList;
import java.util.List;

import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.master.config.Config;
import br.ufg.emc.imagehosting.master.config.Index;

public class Indexer {
	
	public Node index(String filename) throws RemoteException{
		Index index = null;
		
		Index[] indexes = Index.values();
		for (Index aux : indexes) {
			if(filename.matches(aux.getRegex())){
				index = aux;
			}
		}
		
		return getBalanced(index);
	}
	
	private Node getBalanced(Index index) throws RemoteException{
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
