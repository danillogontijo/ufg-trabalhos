package br.ufg.emc.imagehosting.service.remote;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ClusterService;
import br.ufg.emc.imagehosting.common.Image;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.master.config.Config;
import br.ufg.emc.imagehosting.service.Indexer;
import br.ufg.emc.imagehosting.service.stub.ProxyNodeService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;

public class ImageServiceMasterRemote extends Base implements ClusterService {

	private static final long serialVersionUID = 1L;

	private ImageService service;
	private Indexer indexer;
	
	public ImageServiceMasterRemote(){
		this.indexer = FactoryUtil.getFactory().get(Indexer.class);
	}

	public void upload(Image image) throws RemoteException {
		boolean existsNodeActivated = false;
		Node node = indexer.index(image.getFilename());
		service = new ProxyNodeService(node.getIp(), node.getPort());
		if(ping(node)){
			service.upload(image);
			existsNodeActivated = true;
		}else{
			for (Node n : node.getNodesReplications()) {
				if(ping(n)){
					service.upload(image);
					existsNodeActivated = true;
				}
			}
		}
		if(!existsNodeActivated){
			throw new RemoteException("No node active.");
		}
	}

	public Image download(Image image) throws RemoteException {
		Node node = indexer.index(image.getFilename());
		service = new ProxyNodeService(node.getIp(), node.getPort());
		if(ping(node)){
			return service.download(image);
		}else{
			for (Node n : node.getNodesReplications()) {
				if(ping(n)){
					return service.download(image);
				}
			}
		}
		throw new RemoteException("No node active.");
	}
	
	private boolean ping(Node node){
		boolean alive = NetworkUtil.ping(node.getIp(), node.getPort());
		if(alive){
			System.out.println("Node ["+node.getIp()+"] is alive.");
		}else{
			System.err.println("Node ["+node.getIp()+"] is NOT alive.");
		}
		node.setAlive(alive);
		
		return alive;
	}

	/**
	 * Adiciona um node e seus nodes para replicacao de dados.
	 * <p>
	 * Faz uma iteracao de todos os nodos gerenciados pelo
	 * master, e ao encontrar aquele com menor quantidade de
	 * nodo na replicacao é adicionado em sua lista, até atingir
	 * a quantidade configurada.
	 * <p>
	 * A quantidade de replicacao nao pode ser maior do que a 
	 * quantidade de nodes existente no master. Caso isso 
	 * aconteca o valor default sera a quantidade de nodes
	 * do proprio master.
	 */
	@Override
	public void add(Node node) throws RemoteException {
		int sizeReplication = getIntValue("sizeReplication");
		Map<String, Node> nodes = Config.getNodes();
		
		if(sizeReplication > nodes.size()){
			sizeReplication = nodes.size();
		}
		
		int tmp = 0;
		String key = "";
		while(sizeReplication != 0){
			for(Node nodeSystem : nodes.values()){
				int nodeSizeReplication = nodeSystem.getNodesReplications().size();
				if(StringUtils.isBlank(key)){
					tmp = nodeSizeReplication;
					key = nodeSystem.getIp();
				}else if(nodeSizeReplication < tmp){
					tmp = nodeSizeReplication;
					key = nodeSystem.getIp();
				}
			}
			
			Node replication = Config.getNode(key);
			node.addReplication(replication);
			
			sizeReplication--;
		}
		
		Config.addNode(node);
	}

}
