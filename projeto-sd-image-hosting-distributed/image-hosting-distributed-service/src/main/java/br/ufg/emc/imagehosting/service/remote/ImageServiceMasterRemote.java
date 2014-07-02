package br.ufg.emc.imagehosting.service.remote;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import br.ufg.emc.imagehosting.common.Base;
import br.ufg.emc.imagehosting.common.ImageService;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.common.data.Image;
import br.ufg.emc.imagehosting.data.Index;
import br.ufg.emc.imagehosting.data.IndexTable;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.master.config.Config;
import br.ufg.emc.imagehosting.master.config.IndexType;
import br.ufg.emc.imagehosting.service.IndexerService;
import br.ufg.emc.imagehosting.service.stub.ProxyMasterService;
import br.ufg.emc.imagehosting.service.stub.ProxyNodeService;
import br.ufg.emc.imagehosting.util.FactoryUtil;
import br.ufg.emc.imagehosting.util.NetworkUtil;

/**
 * Classe de servico do Master
 *
 * @author danilo.gontijo
 *
 */
public class ImageServiceMasterRemote extends Base implements ClusterService<Node>, ImageService<Image> {

	private static final long serialVersionUID = 1L;

	private ImageService<Image> service;
	private IndexerService indexer;

	public ImageServiceMasterRemote(){
		this.indexer = FactoryUtil.getFactory().get(IndexerService.class);
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
	public void addNode(Node node) throws RemoteException {
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

	/**
	 * Realiza a sincronizacao de dados com o novo Master.
	 * Diz que existe um novo Master ativo.
	 * Retornando um {@link Node} com a tabela de indice atual para atualizacao do
	 * Master chamador.
	 *
	 */
	@Override
	public Node synchronizeMaster(Node node) throws RemoteException {
		Map<String, Node> masters = Config.getMasters();
		for(Node masterSystem : masters.values()){
			ClusterService<Node> masterService = new ProxyMasterService(masterSystem.getIp(), masterSystem.getPort());
			masterService.addMaster(masterSystem);
		}

		addMaster(node);

		Index index = new Index();
		index.setIndexMap(IndexTable.getIndexMap());

		node.setIndex(index);

		return node;
	}

	/**
	 * Adiciona um node master a lista de masters
	 */
	@Override
	public void addMaster(Node node) throws RemoteException {
		Config.addMaster(node);
	}

	/**
	 * Atualiza a lista de index devido a inclusao de um novo node
	 */
	@Override
	public void refreshIndex(Node Node) throws RemoteException {
		int sizeNodes = Config.getSizeNodes();

		if(sizeNodes == 0){
			throw new RemoteException("Nenhum node configurado.");
		}

		List<Node> nodes = Config.getNodesList();

		int offset = 0;
		if(Config.SIZE_INDEX % sizeNodes == 0){
			offset = (Config.SIZE_INDEX / sizeNodes);
		}else{
			offset = (Config.SIZE_INDEX / sizeNodes) + 1;
		}

		for(int i=0; i<sizeNodes; i++){
			Node node = nodes.get(i);

			int sizeItems = (i+1) * offset;
			if(sizeItems > Config.SIZE_INDEX){
				sizeItems = Config.SIZE_INDEX;
			}

			for(int k=i*offset; k<sizeItems; k++){
				IndexTable.addNode(IndexType.fromOrdinal(k), node);
			}
		}

	}

	/**
	 * Sincroniza todos indices de todos os nodes Master,
	 * incluindo o proprio da chamada.
	 */
	@Override
	public void synchronizeIndex(Node node) throws RemoteException {
		refreshIndex(node);
		Map<String, Node> masters = Config.getMasters();
		for(Node masterSystem : masters.values()){
			ClusterService<Node> masterService = new ProxyMasterService(masterSystem.getIp(), masterSystem.getPort());
			masterService.refreshIndex(masterSystem);
		}
	}

}
