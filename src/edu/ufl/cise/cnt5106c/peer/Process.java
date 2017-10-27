/**
 * 
 */
package edu.ufl.cise.cnt5106c.peer;

import java.net.ServerSocket;
import java.util.HashMap;

import edu.ufl.cise.cnt5106c.logger.Logger;

/**
 * @author Dhiraj Borade
 *
 */
public class Process extends Peer implements Runnable {
	
	//Stores the peer process objects in a map
		private static HashMap<Integer,Peer> peers = new HashMap<Integer,Peer>();
		
		private final PeerManager pManager;
		
		//Server socket for this peer
		private ServerSocket sSocket;
		
		public Process(String pid, String hName, String portno, String present){
			super(pid,hName,portno,present);
			new Logger(Integer.parseInt(pid));
			pManager = new PeerManager(sSocket, this.getInstance(), peers);
		}
		
		public Process(int pid, String hName, int portno, boolean present){
			super(pid,hName,portno,present);
			new Logger(pid);
			pManager = new PeerManager(sSocket, this.getInstance(), peers);
		}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
