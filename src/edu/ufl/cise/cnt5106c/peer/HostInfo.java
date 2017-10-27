package edu.ufl.cise.cnt5106c.peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HostInfo extends Thread {

	private Peer hostPeer;
	private Peer neighbor;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private ObjectInputStream hostInputStream;
	private int pieces;
	private PeerInfo peerInfo;


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
	
	
	
	
}
