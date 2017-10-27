/**
 * 
 */
package edu.ufl.cise.cnt5106c.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import edu.ufl.cise.cnt5106c.file.FileManager;
import edu.ufl.cise.cnt5106c.file.FileUtilities;
import edu.ufl.cise.cnt5106c.logger.Logger;
import edu.ufl.cise.cnt5106c.message.Message;
import edu.ufl.cise.cnt5106c.message.MessageType;
import edu.ufl.cise.cnt5106c.payload.BitFieldPayLoad;
import edu.ufl.cise.cnt5106c.payload.HavePayLoad;
import edu.ufl.cise.cnt5106c.payload.PayLoad;
import edu.ufl.cise.cnt5106c.payload.PiecePayLoad;
import edu.ufl.cise.cnt5106c.payload.RequestPayLoad;
import edu.ufl.cise.cnt5106c.peer.Peer;
import edu.ufl.cise.cnt5106c.peer.PeerManager;

/**
 * @author Dhiraj Borade
 *
 */
public class ConnectionHandler extends Thread {

	private Peer hostPeer;
	private Peer neighboringPeer;
	private ObjectInputStream streamIn;
	private ObjectOutputStream streamOut;
	private PeerManager pManager;
	private int piecesDownloaded;
	private ObjectInputStream hostPeerStreamIn;

	public ConnectionHandler() {

	}

	/**
	 * @param hostPeer
	 * @param neighboringPeer
	 * @param streamIn
	 * @param streamOut
	 * @param pManager
	 * @param piecesDownloaded
	 * @param hostPeerStreamIn
	 */
	public ConnectionHandler(Peer hostPeer, Peer neighboringPeer, ObjectInputStream streamIn,
			ObjectOutputStream streamOut, Socket socketPort, PeerManager pManager, int piecesDownloaded) {
		super();
		this.hostPeer = hostPeer;
		this.neighboringPeer = neighboringPeer;
		this.streamIn = streamIn;
		this.streamOut = streamOut;
		this.pManager = pManager;
		this.piecesDownloaded = 0;
		try {
			if (neighboringPeer.getHostSocket() == null) {
				this.hostPeerStreamIn = null;
			} else {
				this.hostPeerStreamIn = new ObjectInputStream(neighboringPeer.getHostSocket().getInputStream());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * @return the neighboringPeer
	 */
	public Peer getNeighboringPeer() {
		return neighboringPeer;
	}

	/**
	 * @param neighboringPeer
	 *            the neighboringPeer to set
	 */
	public void setNeighboringPeer(Peer neighboringPeer) {
		this.neighboringPeer = neighboringPeer;
	}

	/**
	 * @return the streamIn
	 */
	public ObjectInputStream getStreamIn() {
		return streamIn;
	}

	/**
	 * @param streamIn
	 *            the streamIn to set
	 */
	public void setStreamIn(ObjectInputStream streamIn) {
		this.streamIn = streamIn;
	}

	/**
	 * @return the streamOut
	 */
	public ObjectOutputStream getStreamOut() {
		return streamOut;
	}

	/**
	 * @param streamOut
	 *            the streamOut to set
	 */
	public void setStreamOut(ObjectOutputStream streamOut) {
		this.streamOut = streamOut;
	}

	/**
	 * @return the pManager
	 */
	public PeerManager getpManager() {
		return pManager;
	}

	/**
	 * @param pManager
	 *            the pManager to set
	 */
	public void setpManager(PeerManager pManager) {
		this.pManager = pManager;
	}

	/**
	 * @return the piecesDownloaded
	 */
	public int getPiecesDownloaded() {
		return piecesDownloaded;
	}

	/**
	 * @param piecesDownloaded
	 *            the piecesDownloaded to set
	 */
	public void setPiecesDownloaded(int piecesDownloaded) {
		this.piecesDownloaded = piecesDownloaded;
	}

	/**
	 * @return the hostPeerStreamIn
	 */
	public ObjectInputStream getHostPeerStreamIn() {
		return hostPeerStreamIn;
	}

	/**
	 * @param hostPeerStreamIn
	 *            the hostPeerStreamIn to set
	 */
	public void setHostPeerStreamIn(ObjectInputStream hostPeerStreamIn) {
		this.hostPeerStreamIn = hostPeerStreamIn;
	}

	public void sendMessage(Message msg) {
		try {
			streamOut.writeObject(msg);
			streamOut.reset();
			streamOut.flush();
		} catch (ConnectException e) {
			neighboringPeer.setPeerUp(false);
		} catch (IOException e) {
			System.out.println(hostPeer.getPeerId() + ": Error sending message to " + neighboringPeer.getPeerId());
			e.printStackTrace();
		}
	}

	public void receiveMessage() {
		// Waits on input stream of the connection to read incoming messages
		new Thread() {
			public void run() {
				Message receivedMsg = null;
				// flag to check choke and unchoke status
				boolean unChokeFlag = false;
				while (true) {
					try {
						receivedMsg = (Message) hostPeerStreamIn.readObject();
						System.out.println("Received message type: " + receivedMsg.getMsgType() + " from: "
								+ neighboringPeer.getPeerId());
						if (receivedMsg != null) {
							switch (receivedMsg.getMsgType()) {
							case UNCHOKE: {
								Logger.peerUnchoked(neighboringPeer.getPeerId());
								unChokeFlag = true;
								sendRequest();
								break;
							}
							case CHOKE:
								Logger.peerChoked(neighboringPeer.getPeerId());
								unChokeFlag = false;
								break;
							case HAVE: {
								HavePayLoad have = (HavePayLoad) (receivedMsg.getMsgPayload());
								FileUtilities.updateBitfield(have.getIndex(), neighboringPeer.getBitField());
								Logger.haveFileMsgRecieved(neighboringPeer.getPeerId(), have.getIndex());
								// Check whether the piece is interesting and send interested message
								if (!FileManager.isInteresting(have.getIndex())) {
									System.out.println("Peer " + neighboringPeer.getPeerId()
											+ " contains interesting file pieces");
									Message interested = new Message(MessageType.INTERESTED, null);
									sendMessage(interested);
								}
								break;
							}
							case REQUEST: {
								int requestedIndex = ((RequestPayLoad) receivedMsg.getMsgPayload()).getIndex();
								byte[] pieceContent = FileManager.get(requestedIndex).getContent();
								int pieceIndex = FileManager.get(requestedIndex).getIndex();
								Message pieceToSend = new Message(MessageType.PIECE,
										new PiecePayLoad(pieceContent, pieceIndex));
								sendMessage(pieceToSend);
								break;
							}
							case INTERESTED:
								pManager.add(neighboringPeer);
								Logger.interestedMsgRecieved(neighboringPeer.getPeerId());
								break;
							case NOT_INTERESTED:
								pManager.remove(neighboringPeer);
								Logger.notInterestedMsgRecieved(neighboringPeer.getPeerId());
								break;
							case BITFIELD: {
								BitFieldPayLoad in_payload = (BitFieldPayLoad) (receivedMsg.getMsgPayload());
								// setting bitfield for the neighboring peer
								neighboringPeer.setBitField(in_payload.getBitfield());
								if (!FileManager.compareBitfields(in_payload.getBitfield(), hostPeer.getBitField())) {
									System.out.println("Peer " + neighboringPeer.getPeerId()
											+ " does not contain any interesting file pieces");
									Message notInterested = new Message(MessageType.NOT_INTERESTED, null);
									sendMessage(notInterested);
									break;
								}
								System.out.println(
										"Peer " + neighboringPeer.getPeerId() + " contains interesting file pieces");
								Message interested = new Message(MessageType.INTERESTED, null);
								sendMessage(interested);
								// No need to add peers that you are interested in.

								break;
							}
							case PIECE: {

								try {
									FileManager.store((PiecePayLoad) receivedMsg.getMsgPayload());
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

								hostPeer.setBitField(FileManager.getBitField());

								pManager.sendHaveAll(((PiecePayLoad) receivedMsg.getMsgPayload()).getIndex());
								piecesDownloaded++;
								Logger.fileDownloading(neighboringPeer.getPeerId(),
										((PiecePayLoad) receivedMsg.getMsgPayload()).getIndex(),
										FileManager.getNoOfPiecesAvailable());
								if (FileManager.getNooffilepieces() == FileManager.getNoOfPiecesAvailable())
									Logger.fileDownloadCompleted();

								if (unChokeFlag)
									sendRequest();
								break;
							}
							}
						}
					} catch (SocketException e) {
						neighboringPeer.setPeerUp(false);
					} catch (ClassNotFoundException | IOException e) {

						System.out.println(
								hostPeer.getPeerId() + ": Error recieving message from " + neighboringPeer.getPeerId());
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			/**
			 * Sends request message with piece index to neighbor
			 */
			void sendRequest() {
				int pieceIdx = FileManager.requestPiece(neighboringPeer.getBitField(), hostPeer.getBitField(),
						neighboringPeer.getPeerId());
				if (pieceIdx == -1) {
					System.out
							.println("No more interesting pieces to request from peer " + neighboringPeer.getPeerId());
					Message not_interested = new Message(MessageType.NOT_INTERESTED, null);
					sendMessage(not_interested);
					return;
				}
				PayLoad requestPayload = new RequestPayLoad(pieceIdx);
				Message msgRequest = new Message(MessageType.REQUEST, requestPayload);
				try {
					streamOut.writeObject(msgRequest);
					streamOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	public void run() {
		receiveMessage();
		FileManager.checker();
		neighboringPeer.setDownloadSpeed(piecesDownloaded);
	}

	/**
	 * After p seconds of time interval this will be called to reset download rate
	 */
	public void resetPiecesDownloaded() {
		piecesDownloaded = 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
