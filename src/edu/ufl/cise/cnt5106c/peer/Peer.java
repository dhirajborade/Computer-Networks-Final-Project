package edu.ufl.cise.cnt5106c.peer;

import java.net.Socket;

public class Peer {
	
	private int peerId;
	private String hostname;
	private int portNo;
	private boolean hasFile;
	private Socket peerSocket;
	private HostInfo hostinfo;
	
	public Socket getPeerSocket() {
		return peerSocket;
	}
	public void setPeerSocket(Socket peerSocket) {
		this.peerSocket = peerSocket;
	}
	public int getPeerId() {
		return peerId;
	}
	public String getHostname() {
		return hostname;
	}
	public int getPortNo() {
		return portNo;
	}
	
	public boolean isHasFile() {
		return hasFile;
	}

	public Peer(int peerId, String hostname, int portNo, boolean hasFile)
	{
		this.peerId = peerId;
		this.hostname =hostname;
		this.portNo=portNo;
		this.hasFile=hasFile; 
	}
	
	
}
