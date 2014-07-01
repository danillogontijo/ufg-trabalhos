import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import br.ufg.emc.imagehosting.common.Node;
import br.ufg.emc.imagehosting.common.RemoteException;
import br.ufg.emc.imagehosting.master.config.Config;
import br.ufg.emc.imagehosting.master.config.Index;
import br.ufg.emc.imagehosting.service.Indexer;


public class Teste {
	
	public static void main(String[] args) throws RemoteException, IOException {
		String text = "faaf";
		text = "$faaf";
		text = "9faaf";
		text = "Fdfasd";
		text = "*dfasd";
		
		System.out.println(text.matches(Index.F.getRegex()));
		System.out.println(text.matches(Index.SPECIALS.getRegex()));
		System.out.println(text.matches(Index.NUMBER.getRegex()));
		
		Node n1 = new Node("1.1.1.1");
		Node n2 = new Node("1.1.1.2");
		Node n3 = new Node("1.1.1.3");
		
		Config.addNode(n1);
		Config.addNode(n2);
		Config.addNode(n3);
		
		System.out.println(n3.equals(n2));
		
		System.out.println(Config.getSizeNodes());
		System.out.println(Config.getNodes().size());
//		System.out.println(Config.removeNode(n1).getIp());
//		System.out.println(Config.removeNode(n2).getIp());
		
		Indexer i = new Indexer();
		Node node = i.index("9este");
		System.out.println(node);
		
		 InetAddress inet;

		    inet = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
		    System.out.println("Sending Ping Request to " + inet);
		    System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

		    inet = InetAddress.getByAddress(new byte[] { (byte) 173, (byte) 194, 32, 38 });
		    System.out.println("Sending Ping Request to " + inet);
		    System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
		
	}

}
