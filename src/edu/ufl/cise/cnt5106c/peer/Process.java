/**
 * 
 */
package edu.ufl.cise.cnt5106c.peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import edu.ufl.cise.cnt5106c.config.CommonProperties;
import edu.ufl.cise.cnt5106c.connection.ConnectionHandler;
import edu.ufl.cise.cnt5106c.file.FileManager;
import edu.ufl.cise.cnt5106c.file.FileUtilities;
import edu.ufl.cise.cnt5106c.handshake.HandShake;
import edu.ufl.cise.cnt5106c.logger.Logger;
import edu.ufl.cise.cnt5106c.message.Message;
import edu.ufl.cise.cnt5106c.message.MessageType;
import edu.ufl.cise.cnt5106c.payload.BitFieldPayLoad;

/**
 * @author Dhiraj Borade
 *
 */
public class Process extends Peer implements Runnable {

	// Stores the peer process objects in a map
	private static HashMap<Integer, Peer> peers = new HashMap<Integer, Peer>();
	private final PeerManager pManager;
	private final CommonProperties commInfo;
	// Server socket for this peer
	private ServerSocket sSocket;

	public Process(String pid, String hName, String portno, String present, CommonProperties commInfo) {
		super(pid, hName, portno, present);
		new Logger(Integer.parseInt(pid));
		this.commInfo = commInfo;
		pManager = new PeerManager(sSocket, new Peer(), peers);
	}

	public Process(int pid, String hName, int portno, boolean present, CommonProperties commInfo) {
		super(pid, hName, portno, present);
		new Logger(pid);
		this.commInfo = commInfo;
		pManager = new PeerManager(sSocket, new Peer(), peers);
	}

	/**
	 * @return the commInfo
	 */
	public CommonProperties getCommInfo() {
		return commInfo;
	}

	/**
	 * All the peers will be interested initially except the peer with the complete
	 * file, so need to send handshake messages to neighboring peers to check their
	 * bitfields, essentially broadcasting handshake messages
	 */
	public void startSender() {
		long timeout = 60000;
		new Thread() {
			public void run() {
				while (true) {
					try {
						// Sending Handshake message to all other peers
						for (Peer pNeighbor : peers.values()) {
							if (!pNeighbor.isPeerUp()) {
								Socket s = new Socket(pNeighbor.getHostName(), pNeighbor.getPortNumber());
								ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
								out.flush();
								System.out.println("Handshake Message sent from peer " + getPeerId() + " to peer "
										+ pNeighbor.getPeerId());
								Logger.makeconnection(pNeighbor.getPeerId());
								out.writeObject(new HandShake(getPeerId()));
								out.flush();
								out.reset();
								pNeighbor.setHostSocket(s);
								pNeighbor.setPeerUp(true);
							}
						}
						Thread.sleep(timeout);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (ConnectException e) {
						// System.out.println("Peer not accepting connections");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * Starts listening on a socket for handshake message, calls establishConnection
	 * method internally
	 */
	public void startServer() {

		(new Thread() {
			@Override
			public void run() {
				while (!sSocket.isClosed()) {
					try {
						establishConnection();
					} catch (SocketException e) {
						System.out.println("Peer " + getPeerId() + " is shutting down");
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	/**
	 * Establishes connection between host peer and neighboring peer *
	 * 
	 * @return ConnectionHandler object
	 * @throws Exception
	 */
	public ConnectionHandler establishConnection() throws Exception {

		Socket lSocket = sSocket.accept();
		ObjectOutputStream out = new ObjectOutputStream(lSocket.getOutputStream());
		out.flush();
		ObjectInputStream in = new ObjectInputStream(lSocket.getInputStream());

		// Receiving Handshake message
		HandShake incoming = (HandShake) in.readObject();
		if (peers.get(incoming.getPeerID()) == null || !incoming.getPeerMsgHeader().equals(HandShake.getHeader())) {
			System.out.println("Error performing Handshake : PeerId or Header unknown");
		}
		System.out.println(
				"Received Handshake Message : " + incoming.getPeerID() + " Header - " + incoming.getPeerMsgHeader());
		Logger.connected(incoming.getPeerID());

		// No need to send Handshake message here again, since all peers will send
		// handshake messages to
		// neighboring peers in startSender method as well as there is no place we are
		// receiving second handshake

		// Creating connection irrespective of peers being interested
		Peer neighbor = peers.get(incoming.getPeerID());
		ConnectionHandler conn = new ConnectionHandler(this, neighbor, in, out, lSocket, pManager, 0);
		conn.start();
		neighbor.setConnHandler(conn);

		// Sending Bitfield message
		BitFieldPayLoad out_payload = new BitFieldPayLoad(FileManager.getBitField());
		conn.sendMessage(new Message(MessageType.BITFIELD, out_payload));
		System.out.println("Sending Bitfield Message from: " + getPeerId() + " to: " + incoming.getPeerID());

		return conn;
	}

	public void terminator() {

		(new Thread() {
			@Override
			public void run() {
				try {

					int peerWithFile = 0;
					int end = peers.size() + 1;
					HashMap<Integer, Peer> temp = peers;
					int pieces = (int) Math
							.ceil((double) CommonProperties.getFileSize() / CommonProperties.getPieceSize());
					while (peerWithFile < end) {
						Thread.sleep(CommonProperties.getUnchokingInterval() * 2000);
						if (FileManager.hasCompleteFile())
							peerWithFile++;

						for (Integer ind : temp.keySet()) {
							Peer peer = temp.get(ind);
							if (peer.getBitField() != null && FileUtilities.checkComplete(peer.getBitField(), pieces)) {
								peerWithFile++;
							}

						}
					}
					System.out.println("Shutting Down");
					System.exit(0);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// public static void getConfiguration(String[] ar) {
	// String st;
	// try {
	// String hostname = InetAddress.getLocalHost().getHostName();
	// String FileName = "PeerInfo.cfg";
	// BufferedReader in = new BufferedReader(new FileReader(FileName));
	//
	// PeerProcess hostPeer = null;
	//
	// while ((st = in.readLine()) != null) {
	// String[] tokens = st.split("\\s+");
	// if (ar[0].equalsIgnoreCase(tokens[0])) {
	// hostPeer = new PeerProcess(tokens[0], tokens[1], tokens[2], tokens[3]);
	// } else {
	// Peer peer = new Peer(tokens[0], tokens[1], tokens[2], tokens[3]);
	// peers.put(Integer.parseInt(tokens[0]), peer);
	// }
	// }
	// in.close();
	//
	// new Thread(hostPeer).start();
	// } catch (Exception ex) {
	// System.out.println(ex.toString());
	// }
	// }

	public void initialize(Process peerProcess) {
		Thread t = new Thread(peerProcess);
		t.setName("peerProcess-" + this.getPeerId());
		t.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// getConfiguration(args);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Starting peer " + getPeerId());

		new FileManager(getPeerId(), isFilePresent());

		try {
			setBitField(FileManager.getBitField());
			sSocket = new ServerSocket(this.getPortNumber());
			System.out.println("Server socket created for peer " + getHostName());
		} catch (Exception e) {
			System.out.println("Error opening socket");
			e.printStackTrace();
		}
		startServer();
		startSender();

		pManager.setSocketPort(sSocket);
		pManager.start();
		// terminator();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					sSocket.close();
					for (Peer p : peers.values()) {
						p.getHostSocket().close();
					}
				} catch (IOException e) {
					System.out.println("Error closing socket of peer " + getPeerId());
					e.printStackTrace();
				}
			}
		}));
	}

}
