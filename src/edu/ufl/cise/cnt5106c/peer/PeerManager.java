/**
 * 
 */
package edu.ufl.cise.cnt5106c.peer;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.sun.corba.se.impl.orb.ORBConfiguratorImpl.ConfigParser;

import edu.ufl.cise.cnt5106c.config.CommonProperties;
import edu.ufl.cise.cnt5106c.file.FileManager;
import edu.ufl.cise.cnt5106c.logger.Logger;
import edu.ufl.cise.cnt5106c.message.Message;
import edu.ufl.cise.cnt5106c.message.MessageType;
import edu.ufl.cise.cnt5106c.payload.HavePayLoad;

/**
 * @author Dhiraj Borade
 *
 */
public class PeerManager extends Thread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
private static ServerSocket sSocket;
	
	//Stores the peer process objects in a map
	private static HashMap<Integer,Peer> peers;

	private static ArrayList<Peer> interested = new ArrayList<Peer>();
	
	private static ArrayList<Peer> kPeers;
	
	private static Peer optUnchokedPeer;
	
	private Peer hostPeer;
		
	public PeerManager(){}
	
	public PeerManager(ServerSocket s, Peer host, HashMap<Integer,Peer> prs){
		sSocket = s;
		hostPeer = host;
		peers = prs;
	}
	
	public void add(Peer i){
		interested.add(i);
	}
	
	public void remove(Peer i) 
	{
		interested.remove(i);
	}
	
	public void setSocket(ServerSocket s){
		sSocket = s;
	}
	
	public void kPreferredPeers(){
		
		long timeout = CommonProperties.getUnchokingInterval()*1000;
		new Thread(){
			public void run(){
				try{
					// reselecting k preferred peers in time intervals of 'UnchokingInterval' from config
					do{
						synchronized(interested){
							System.out.println("Finding k preferred peers");
							if(interested.size() != 0){
								kPeers = new ArrayList<Peer>();
								// Sorts interested peers with respect to downloading rates only when host does not have the complete file
								if(!FileManager.hasCompleteFile()){
									interested.sort(new Comparator<Peer>() {
										Random r = new Random();
										@Override
										public int compare(Peer o1, Peer o2) {
											if(o1.getDownloadSpeed() == o2.getDownloadSpeed())
												return r.nextInt(2); //Randomly sequencing equal elements
											return (int)-(o1.getDownloadSpeed()-o2.getDownloadSpeed());
										}
									});
								}
								Iterator<Peer> it = interested.iterator();
								for(int i=0; i<ConfigParser.getNumberOfPreferredNeighbors()
										&& it.hasNext();i++){
									Peer p = it.next();
									// chooses peer adds it to k preferred peers list and unchokes them
									p.getConn().resetPiecesDownloaded();
									kPeers.add(p);
									if(!p.isUnchoked())
										unchokePeer(p);
								}
								ArrayList<Integer> preferredPeers = new ArrayList<Integer>();
								for (int i = 0; i < kPeers.size(); i ++) {
									preferredPeers.add(kPeers.get(i).getPeerId());
								}
								Logger.preferredNeighbors(preferredPeers);
								chokePeers();
							}
						}
						Thread.sleep(timeout);
					}while(!sSocket.isClosed());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	/**
	 * Optimistically unchokes a peer from interested peer list at regular intervals
	 */
	public void optUnchokePeer(){
		//time interval in seconds to find and unchoke next optimistic peer
		long timeout = ConfigParser.getOptimisticUnchokingInterval()*1000;
		new Thread(){
			public void run(){
				try{
					// reselecting optimistic peer in time intervals of 'OptimisticUnchokingInterval' from config
					do{
						synchronized(interested){
							System.out.println("Finding optimistic peer");
							Peer p;
							Random r = new Random();
							Peer[] prs = interested.toArray(new Peer[interested.size()]);
							if(interested.size() != 0){
								do{
									p = prs[r.nextInt(prs.length)];
								}while(p.isUnchoked()); //Selects a choked interesting peer
								optUnchokedPeer = p;
								unchokePeer(p);
								Logger.optUnchoke(p.getPeerId());
							}
						}
						Thread.sleep(timeout);
					}while(!sSocket.isClosed());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	/**
	 * Unchokes a peer by sending unchoke message to the peer
	 * 
	 * @param p peer to be unchoked
	 */
	public void unchokePeer(Peer p){
		
		p.unChoke(true);
		//send unchoke message to peer p
		Message msgUnchoke = new Message(MessageType.UNCHOKE, null);
		p.getConn().sendMessage(msgUnchoke);
		// log here or after receiving the message
		Logger.unchoked(p.getPeerId());
	}
	
	/**
	 * Chokes all peers which are neither k preferred peers nor optimistically unchoked peer
	 */
	public void chokePeers(){
		//choke all other peers not in map kPeers
		Iterator itr = peers.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry entry = (Map.Entry)itr.next();
			Peer temp =(Peer)entry.getValue();
			if(!kPeers.contains(temp) && temp != optUnchokedPeer && temp.getConn() != null){
				temp.unChoke(false);
				Message chokeMsg = new Message(MessageType.CHOKE, null);
				temp.getConn().sendMessage(chokeMsg);
				// TODO call method to stop sending data to neighbor
				Logger.choked(temp.getPeerId());
			}
		}
	}
	
	public void run(){
		kPreferredPeers();
		optUnchokePeer();
	}
	
	/*public HashMap<Integer, Peer> getPeerList()
	{
		return peers;
	}*/
	
	public void sendHaveAll(int index)
	{
		Message have = new Message(MessageType.HAVE, new HavePayLoad(index));
	
		Iterator itr = peers.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry entry = (Map.Entry)itr.next();
			Peer temp =(Peer)entry.getValue();
			temp.getConn().sendMessage(have);
		}
	}

}
