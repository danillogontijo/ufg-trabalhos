import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.data.IndexTable;
import br.ufg.emc.imagehosting.data.Node;
import br.ufg.emc.imagehosting.service.remote.ImageServiceMasterRemote;


public class TesteIndex {

	public static void main(String[] args) throws RemoteException {
//		Node n1 = new Node("n1");
//		Node n2 = new Node("n2");
//		Node n3 = new Node("n3");
//
//		ImageServiceMasterRemote service = new ImageServiceMasterRemote();
//
//		service.addNode(n1);
//		service.synchronizeIndex(n1);
		System.out.println(IndexTable.getIndexMap());

//		service.addNode(n2);
//		service.synchronizeIndex(n2);
//		System.out.println(IndexTable.getIndexMap());
//
//		service.addNode(n3);
//		service.synchronizeIndex(n3);
//		System.out.println(IndexTable.getIndexMap());
	}

}
