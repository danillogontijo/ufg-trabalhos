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
import br.ufg.emc.imagehosting.master.config.Params;
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

	/**
	 * Realiza o upload, replicando os dados nos nodes,
	 * adiciona os nodes na tabela de indice e, por final,
	 * faz a sincronizacao da tabela com os outros masters.
	 *
	 */
	public void upload(Image image) throws RemoteException {
		boolean existsNodeActivated = false;
		Node node = indexer.index(image.getFilename());
		service = new ProxyNodeService(node.getIp(), node.getPort());
		service.upload(image);
		existsNodeActivated = true;

		IndexType  indexType = IndexType.getIndex(image.getFilename());

		for (Node n : node.getNodesReplications()) {
			if(ping(n)){
				service = new ProxyNodeService(n.getIp(), n.getPort());
				service.upload(image);
				existsNodeActivated = true;
				// Adicionando node na tabela de indices
				IndexTable.addNode(indexType, n);
			}
		}

		// sincronizando a tabela de indices dos masters
		List<Node> masters = Config.getMastersList();
		for (Node master : masters) {
			synchronizeIndex(master);
		}

		if(!existsNodeActivated){
			throw new RemoteException("No node active.");
		}
	}

	public Image download(Image image) throws RemoteException {
		Node node = indexer.getFromIndexTable(image.getFilename());
		service = new ProxyNodeService(node.getIp(), node.getPort());
		if(ping(node)){
			return service.download(image);
		}else{
			for (Node n : node.getNodesReplications()) {
				if(ping(n)){
					service = new ProxyNodeService(n.getIp(), n.getPort());
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
	 * <p>
	 */
	@Override
	public void addNode(Node node) throws RemoteException {
		System.out.println("Adding node: " + node.toString());

		int sizeReplication = getIntValue("sizeReplication");
		Map<String, Node> nodes = Config.getNodes();

		if(sizeReplication > nodes.size()){
			sizeReplication = nodes.size();
		}

		while(sizeReplication != 0){
			Node replication = getBetterNodeToReplication(node);
			//se nao foi atingido o valor limite de replicacao
			if(node.getNodesReplications().size() < sizeReplication){
				node.addReplication(replication);
			}

			sizeReplication--;
		}

		Config.addNode(node);
	}

	private Node getBetterNodeToReplication(Node... ignores){
		Map<String, Node> nodes = Config.getNodes();

		int tmp = 0;
		String key = "";
		for(Node nodeSystem : nodes.values()){
			boolean isIgnore = false;
			for(Node ignore : ignores){
				if(nodeSystem.equals(ignore)){
					isIgnore = true;
					break;
				}
			}

			if(isIgnore){
				continue;
			}

			int nodeSizeReplication = nodeSystem.getNodesReplications().size();
			if(StringUtils.isBlank(key)){
				tmp = nodeSizeReplication;
				key = nodeSystem.getId();
			}else if(nodeSizeReplication < tmp){
				tmp = nodeSizeReplication;
				key = nodeSystem.getId();
			}
		}

		Node replication = Config.getNode(key);

		return replication;
	}

	/**
	 * Realiza a sincronizacao de dados com o novo Master.
	 * Diz que existe um novo Master ativo.
	 * Retornando um {@link Node} com a tabela de indice atual e a
	 * lista de nodes registrados para atualizacao do Master chamador.
	 *
	 */
	@Override
	public Node synchronizeMaster(Node node) throws RemoteException {
		System.out.println("Synchronizing master: " + node.toString());
		Map<String, Node> masters = Config.getMasters();
		for(Node masterSystem : masters.values()){
			if(ping(masterSystem)){
				ClusterService<Node> masterService = new ProxyMasterService(masterSystem.getIp(), masterSystem.getPort());
				masterService.addMaster(masterSystem);
			}else{
				masterSystem.setAlive(false);
			}
		}

		node.getNodes().addAll(Config.getNodesList());

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
		System.out.println("Adding new master: " + node.toString());
		Config.addMaster(node);
	}

	/**
	 * Atualiza a lista de index devido a inclusao de um novo node
	 */
	@Override
	public void refreshIndex(Node Node) throws RemoteException {
		System.out.println("Refreshing index...");

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

		System.out.println("Index table: " + IndexTable.getIndexMap());

	}

	/**
	 * Sincroniza todos indices de todos os nodes Master,
	 * incluindo o proprio da chamada.
	 */
	@Override
	public void synchronizeIndex(Node node) throws RemoteException {
		System.out.println("Synchronizing index: " + node.getIndex().toString());
		refreshIndex(node);
		Map<String, Node> masters = Config.getMasters();
		for(Node masterSystem : masters.values()){
			ClusterService<Node> masterService = new ProxyMasterService(masterSystem.getIp(), masterSystem.getPort());
			masterService.refreshIndex(masterSystem);
		}
	}

	/**
	 * Adiciona um novo Node no master.
	 * <p>
	 * Apos a adicao do novo Node eh feito o refresh da
	 * tabela de index.
	 * <p>
	 */
	@Override
	public void registryNode(Node node) throws RemoteException {
		addNode(node);
		refreshIndex(node);
		refreshNodes(node);
	}

	/**
	 * Atualiza os dados de replicacao dos Nodes.
	 */
	@Override
	public void refreshNodes(Node node) throws RemoteException {
		System.out.println("Refreshing nodes...");
		List<Node> nodes = Config.getNodesList();

		for (Node nodeSystem : nodes) {
			int nodeSystemReplicationSize = nodeSystem.getNodesReplications().size();
			int replicationSize = getIntValue(Params.SIZE_REPLICATION);
			if(replicationSize > nodes.size()-1){
				replicationSize = nodes.size()-1;
			}

			while(nodeSystemReplicationSize<replicationSize){
				Node replication = getBetterNodeToReplication(nodeSystem.getAllNodes().toArray(new Node[0]));
				if(!nodeSystem.equals(replication)){
					nodeSystem.getNodesReplications().add(replication);
				}
				nodeSystemReplicationSize = nodeSystem.getNodesReplications().size();
			}

			System.out.println(nodeSystem.getNodesReplications());
		}
	}

}
