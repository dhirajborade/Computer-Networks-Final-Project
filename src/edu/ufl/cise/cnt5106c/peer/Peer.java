/**
 * 
 */
package edu.ufl.cise.cnt5106c.peer;

import java.net.Socket;

import edu.ufl.cise.cnt5106c.connection.ConnectionHandler;

/**
 * @author Dhiraj Borade
 *
 */
public class Peer {

	private int peerId;
	private String hostname;
	private int portNo;
	private boolean filePresent;
	private byte[] bitfield;
	private boolean unchoked;
	private long downloadSpeed;
	private ConnectionHandler conn;
	private Socket hostSocket;
	private boolean peerUp;

	public Peer() {
	}

	public Peer(String pid, String hName, String portno, String present) {
		this.setPeerId(Integer.parseInt(pid));
		this.setHostname(hName);
		this.setPortNo(Integer.parseInt(portno));
		if (Short.parseShort(present) == 0)
			this.setFilePresent(false);
		else
			this.setFilePresent(true);
	}

	public Peer(int pid, String hName, int portno, boolean present) {
		this.setPeerId(pid);
		this.setHostname(hName);
		this.setPortNo(portno);
		this.setFilePresent(present);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getPeerId() {
		return peerId;
	}

	/**
	 * @return the peerUp
	 */
	public boolean isPeerUp() {
		return peerUp;
	}

	/**
	 * @param peerUp
	 *            the peerUp to set
	 */
	public void setPeerUp(boolean peerUp) {
		this.peerUp = peerUp;
	}

	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public boolean getFilePresent() {
		return filePresent;
	}

	public void setFilePresent(boolean filePresent) {
		this.filePresent = filePresent;
	}

	public void setBitfield(byte[] _bf) {
		bitfield = _bf;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void unChoke(boolean state) {
		unchoked = state;
	}

	public boolean isUnchoked() {
		return unchoked;
	}

	public void setDownloadSpeed(long ds) {
		downloadSpeed = ds;
	}

	public long getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setConnHandler(ConnectionHandler c) {
		conn = c;
	}

	public ConnectionHandler getConn() {
		return conn;
	}

	public Peer getInstance() {
		return this;
	}

	/**
	 * @return the hostSocket
	 */
	public Socket getHostSocket() {
		return hostSocket;
	}

	/**
	 * @param hostSocket
	 *            the hostSocket to set
	 */
	public void setHostSocket(Socket hostSocket) {
		this.hostSocket = hostSocket;
	}

}
