package edu.ufl.cise.cnt5106c.peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HostInfo extends Thread {

	private Peer hostPeer;
	private Peer neighbor;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private ObjectInputStream hostInputStream;
	private int pieces;
	
	ArrayList<Peer> interestedPeers = new ArrayList<Peer>();


	public Peer getHostPeer() {
		return hostPeer;
	}

	public void setHostPeer(Peer hostPeer) {
		this.hostPeer = hostPeer;
	}

	public Peer getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(Peer neighbor) {
		this.neighbor = neighbor;
	}

	
	public ObjectInputStream getServerInputStream() {
		return serverInputStream;
	}

	public void setServerInputStream(ObjectInputStream serverInputStream) {
		this.serverInputStream = serverInputStream;
	}

	public ObjectOutputStream getServerOutputStream() {
		return serverOutputStream;
	}

	public void setServerOutputStream(ObjectOutputStream serverOutputStream) {
		this.serverOutputStream = serverOutputStream;
	}

	public ObjectInputStream getHostInputStream() {
		return hostInputStream;
	}

	public void setHostInputStream(ObjectInputStream hostInputStream) {
		this.hostInputStream = hostInputStream;
	}

	public int getPieces() {
		return pieces;
	}

	public void setPieces(int pieces) {
		this.pieces = pieces;
	}

	public PeerInfo getPeerInfo() {
		return peerInfo;
	}

	public void setPeerInfo(PeerInfo peerInfo) {
		this.peerInfo = peerInfo;
	}
	
	public HostInfo(Peer hostPeer,Peer neighbor,ObjectInputStream input, ObjectOutputStream output, Socket socket, PeerInfo peerInfo) 
	{
		this.setHostPeer(hostPeer);
		this.setNeighbor(neighbor);
		this.setServerInputStream(input);
		this.setServerOutputStream(output);
		this.setPeerInfo(peerInfo);
		this.setPieces(0);
		if(neighbor.getPeerSocket()==null){
			this.setHostInputStream(null);
		}
		else{
			try {
				hostInputStream=new ObjectInputStream(neighbor.getPeerSocket().getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void message(){
		new Thread(){
			public void run(){
				Message message= null;//message 
				
				message= hostInputStream.readObject();
				
				if(message!=null){
					if(message.getMsgType().equals(HAVE)){
						HavePayload payload = (HavePayload)(message.payload);
						//TO BE IMPLEMENTED
					}
					
					else if(message.getMsgType().equals(CHOKE)){
						//TO BE IMPLEMENTED 
					}
					
					else if(message.getMsgType().equals(UNCHOKE)){
						//TO BE IMPLEMENTED
					}
					else if(message.getMsgType().equals(REQUEST)){
						int requestedIndex = ((RequestPayload)recv.mPayload).getIndex();
						byte [] pieceContent = FileManager.get(requestedIndex).getContent();
						int pieceIndex = FileManager.get(requestedIndex).getIndex();
						Message pieceToSend = new Message(MessageType.PIECE, new PiecePayload(pieceContent, pieceIndex));
						serverOutputStream.writeObject(message);
						serverOutputStream.reset();
						serverOutputStream.flush();
					}
					else if(message.getMsgType().equals(BITFIELD)){
						//TO BE IMPLEMENTED
					}
					else if(message.getMsgType().equals(PIECE)){
						//TO BE IMPLEMENTED
					}
					else if(message.getMsgType().equals(INTERESTED)){
						interestedPeers.add(neighbor);
					}
					else if(message.getMsgType().equals(NOT_INTERESTED)){
						interestedPeers.remove(neighbor);
					}
				}
				
			}
		};
	}
	
	
}
