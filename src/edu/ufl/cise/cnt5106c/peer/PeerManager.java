/**
 * 
 */
package edu.ufl.cise.cnt5106c.peer;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import edu.ufl.cise.cnt5106c.config.CommonProperties;
import edu.ufl.cise.cnt5106c.file.FileManager;
import edu.ufl.cise.cnt5106c.logger.Logger;
import edu.ufl.cise.cnt5106c.message.Message;
import edu.ufl.cise.cnt5106c.message.MessageType;
import edu.ufl.cise.cnt5106c.payload.HavePayLoad;

/**
 * @author Meghana Ranganath
 *
 */
public class PeerManager extends Thread {

	private ServerSocket socketPort;
	private Peer hostPeer;

	// Stores the peer process objects in a map
	private HashMap<Integer, Peer> peers;
	private static ArrayList<Peer> interestedPeers = new ArrayList<Peer>();
	private static ArrayList<Peer> kNeighborPeers;
	private static Peer optimizedUnchokedPeer;

	public PeerManager() {

	}

	/**
	 * @param sSocket
	 * @param peers
	 * @param hostPeer
	 */
	public PeerManager(ServerSocket sSocket, Peer hostPeer, HashMap<Integer, Peer> peers) {
		super();
		this.socketPort = sSocket;
		this.peers = peers;
		this.hostPeer = hostPeer;
	}

	/**
	 * @return the socketPort
	 */
	public ServerSocket getSocketPort() {
		return socketPort;
	}

	/**
	 * @param socketPort
	 *            the socketPort to set
	 */
	public void setSocketPort(ServerSocket socketPort) {
		this.socketPort = socketPort;
	}

	/**
	 * @return the hostPeer
	 */
	public Peer getHostPeer() {
		return hostPeer;
	}

	/**
	 * @param hostPeer
	 *            the hostPeer to set
	 */
	public void setHostPeer(Peer hostPeer) {
		this.hostPeer = hostPeer;
	}

	/**
	 * @return the peers
	 */
	public HashMap<Integer, Peer> getPeers() {
		return peers;
	}

	/**
	 * @param peers
	 *            the peers to set
	 */
	public void setPeers(HashMap<Integer, Peer> peers) {
		this.peers = peers;
	}

	/**
	 * @return the interestedPeers
	 */
	public static ArrayList<Peer> getInterestedPeers() {
		return interestedPeers;
	}

	/**
	 * @param interestedPeers
	 *            the interestedPeers to set
	 */
	public static void setInterestedPeers(ArrayList<Peer> interestedPeers) {
		PeerManager.interestedPeers = interestedPeers;
	}

	/**
	 * @return the kNeighborPeers
	 */
	public static ArrayList<Peer> getkNeighborPeers() {
		return kNeighborPeers;
	}

	/**
	 * @param kNeighborPeers
	 *            the kNeighborPeers to set
	 */
	public static void setkNeighborPeers(ArrayList<Peer> kNeighborPeers) {
		PeerManager.kNeighborPeers = kNeighborPeers;
	}

	/**
	 * @return the optimizedUnchokedPeer
	 */
	public static Peer getOptimizedUnchokedPeer() {
		return optimizedUnchokedPeer;
	}

	/**
	 * @param optimizedUnchokedPeer
	 *            the optimizedUnchokedPeer to set
	 */
	public static void setOptimizedUnchokedPeer(Peer optimizedUnchokedPeer) {
		PeerManager.optimizedUnchokedPeer = optimizedUnchokedPeer;
	}

	public void add(Peer intPeers) {
		interestedPeers.add(intPeers);
	}

	public void remove(Peer intPeers) {
		interestedPeers.remove(intPeers);
	}

	public void kPreferredPeers() {

		long timeout = CommonProperties.getUnchokingInterval() * 1000;
		new Thread() {
			public void run() {
				try {
					// reselecting k preferred peers in time intervals of 'UnchokingInterval' from
					// config

					synchronized (interestedPeers) {
						System.out.println("Finding k preferred peers");
						if (interestedPeers.size() != 0) {
							kNeighborPeers = new ArrayList<Peer>();
							// Sorts interested peers with respect to downloading rates only when host does
							// not have the complete file
							if (!FileManager.hasCompleteFile()) {
								interestedPeers.sort(new Comparator<Peer>() {
									Random r = new Random();

									@Override
									public int compare(Peer o1, Peer o2) {
										if (o1.getDownloadSpeed() == o2.getDownloadSpeed())
											return r.nextInt(2); // Randomly sequencing equal elements
										return (int) -(o1.getDownloadSpeed() - o2.getDownloadSpeed());
									}
								});
							}
							Iterator<Peer> it = interestedPeers.iterator();
							int indexJ = 0;
							while (indexJ < CommonProperties.getNumberOfPreferredNeighbors() && it.hasNext()) {
								Peer p = it.next();
								// chooses peer adds it to k preferred peers list and unchokes them
								p.getConnHandler().resetPiecesDownloaded();
								kNeighborPeers.add(p);
								if (!p.isUnChoked())
									unChokePeer(p);
								indexJ++;
							}
							ArrayList<Integer> preferredPeers = new ArrayList<Integer>();
							int indexI = 0;
							while (indexI < kNeighborPeers.size()) {
								preferredPeers.add(kNeighborPeers.get(indexI).getPeerId());
								indexI++;
							}
							Logger.preferredNeighbors(preferredPeers);
							chokePeers();
						}
					}
					Thread.sleep(timeout);

					while (!socketPort.isClosed()) {
						synchronized (interestedPeers) {
							System.out.println("Finding k preferred peers");
							if (interestedPeers.size() != 0) {
								kNeighborPeers = new ArrayList<Peer>();
								// Sorts interested peers with respect to downloading rates only when host does
								// not have the complete file
								if (!FileManager.hasCompleteFile()) {
									interestedPeers.sort(new Comparator<Peer>() {
										Random r = new Random();

										@Override
										public int compare(Peer o1, Peer o2) {
											if (o1.getDownloadSpeed() == o2.getDownloadSpeed())
												return r.nextInt(2); // Randomly sequencing equal elements
											return (int) -(o1.getDownloadSpeed() - o2.getDownloadSpeed());
										}
									});
								}
								Iterator<Peer> it = interestedPeers.iterator();
								int indexJ = 0;
								while (indexJ < CommonProperties.getNumberOfPreferredNeighbors() && it.hasNext()) {
									Peer p = it.next();
									// chooses peer adds it to k preferred peers list and unchokes them
									p.getConnHandler().resetPiecesDownloaded();
									kNeighborPeers.add(p);
									if (!p.isUnChoked())
										unChokePeer(p);
									indexJ++;
								}
								ArrayList<Integer> preferredPeers = new ArrayList<Integer>();
								int indexI = 0;
								while (indexI < kNeighborPeers.size()) {
									preferredPeers.add(kNeighborPeers.get(indexI).getPeerId());
									indexI++;
								}
								Logger.preferredNeighbors(preferredPeers);
								chokePeers();
							}
						}
						Thread.sleep(timeout);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * Optimistically unchokes a peer from interested peer list at regular intervals
	 */
	public void unChokeOptimisticPeer() {
		// time interval in seconds to find and unchoke next optimistic peer
		long timeout = CommonProperties.getOptimisticUnchokingInterval() * 1000;
		new Thread() {
			public void run() {
				try {
					// reselecting optimistic peer in time intervals of
					// 'OptimisticUnchokingInterval' from config

					synchronized (interestedPeers) {
						System.out.println("Finding Optimistic Peer");
						Peer p;
						Random r = new Random();
						Peer[] prs = interestedPeers.toArray(new Peer[interestedPeers.size()]);
						if (interestedPeers.size() != 0) {
							// Selects a choked interesting peer
							p = prs[r.nextInt(prs.length)];
							while (p.isUnChoked()) {
								p = prs[r.nextInt(prs.length)];
							}
							optimizedUnchokedPeer = p;
							unChokePeer(p);
							Logger.optUnchoke(p.getPeerId());
						}
					}
					Thread.sleep(timeout);

					while (!socketPort.isClosed()) {
						synchronized (interestedPeers) {
							System.out.println("Finding Optimistic Peer");
							Peer p;
							Random r = new Random();
							Peer[] prs = interestedPeers.toArray(new Peer[interestedPeers.size()]);
							if (interestedPeers.size() != 0) {
								// Selects a choked interesting peer
								p = prs[r.nextInt(prs.length)];
								while (p.isUnChoked()) {
									p = prs[r.nextInt(prs.length)];
								}
								optimizedUnchokedPeer = p;
								unChokePeer(p);
								Logger.optUnchoke(p.getPeerId());
							}
						}
						Thread.sleep(timeout);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * Unchokes a peer by sending unchoke message to the peer
	 * 
	 * @param p
	 *            peer to be unchoked
	 */
	public void unChokePeer(Peer p) {

		p.setUnChoked(true);
		// send unchoke message to peer p
		Message msgUnchoke = new Message(MessageType.UNCHOKE, null);
		p.getConnHandler().sendMessage(msgUnchoke);
		// log here or after receiving the message
		Logger.peerUnchoked(p.getPeerId());
	}

	/**
	 * Chokes all peers which are neither k preferred peers nor optimistically
	 * unchoked peer
	 */
	public void chokePeers() {
		// choke all other peers not in map kPeers
		Iterator<Entry<Integer, Peer>> itr = peers.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Integer, Peer> entry = itr.next();
			Peer temp = (Peer) entry.getValue();
			if (!kNeighborPeers.contains(temp) && temp != optimizedUnchokedPeer && temp.getConnHandler() != null) {
				temp.setUnChoked(false);
				Message chokeMsg = new Message(MessageType.CHOKE, null);
				temp.getConnHandler().sendMessage(chokeMsg);
				// TODO call method to stop sending data to neighbor
				Logger.peerChoked(temp.getPeerId());
			}
		}
	}

	public void run() {
		kPreferredPeers();
		unChokeOptimisticPeer();
	}

	/*
	 * public HashMap<Integer, Peer> getPeerList() { return peers; }
	 */

	public void sendHaveAll(int index) {
		Message have = new Message(MessageType.HAVE, new HavePayLoad(index));

		Iterator<Entry<Integer, Peer>> itr = peers.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Integer, Peer> entry = itr.next();
			Peer temp = entry.getValue();
			temp.getConnHandler().sendMessage(have);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
