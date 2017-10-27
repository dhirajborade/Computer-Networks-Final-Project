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
	private String hostName;
	private int portNumber;
	private boolean filePresent;
	private byte[] bitField;
	private boolean unChoked;
	private long downloadSpeed;
	private ConnectionHandler connHandler;
	private Socket hostSocket;
	private boolean peerUp;

	public Peer() {

	}

	/**
	 * @param peerId
	 * @param hostName
	 * @param portNumber
	 * @param filePresent
	 */
	public Peer(String peerId, String hostName, String portNumber, String filePresent) {
		super();
		this.peerId = Integer.parseInt(peerId);
		this.hostName = hostName;
		this.portNumber = Integer.parseInt(portNumber);
		this.filePresent = Short.parseShort(filePresent) == 0 ? false : true;
	}

	/**
	 * @param peerId
	 * @param hostName
	 * @param portNumber
	 * @param filePresent
	 */
	public Peer(int peerId, String hostName, int portNumber, boolean filePresent) {
		super();
		this.peerId = peerId;
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.filePresent = filePresent;
	}

	/**
	 * @return the peerId
	 */
	public int getPeerId() {
		return peerId;
	}

	/**
	 * @param peerId
	 *            the peerId to set
	 */
	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber
	 *            the portNumber to set
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return the filePresent
	 */
	public boolean isFilePresent() {
		return filePresent;
	}

	/**
	 * @param filePresent
	 *            the filePresent to set
	 */
	public void setFilePresent(boolean filePresent) {
		this.filePresent = filePresent;
	}

	/**
	 * @return the bitField
	 */
	public byte[] getBitField() {
		return bitField;
	}

	/**
	 * @param bitField
	 *            the bitField to set
	 */
	public void setBitField(byte[] bitField) {
		this.bitField = bitField;
	}

	/**
	 * @return the unChoked
	 */
	public boolean isUnChoked() {
		return unChoked;
	}

	/**
	 * @param unChoked
	 *            the unChoked to set
	 */
	public void setUnChoked(boolean unChoked) {
		this.unChoked = unChoked;
	}

	/**
	 * @return the downloadSpeed
	 */
	public long getDownloadSpeed() {
		return downloadSpeed;
	}

	/**
	 * @param downloadSpeed
	 *            the downloadSpeed to set
	 */
	public void setDownloadSpeed(long downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	/**
	 * @return the connHandler
	 */
	public ConnectionHandler getConnHandler() {
		return connHandler;
	}

	/**
	 * @param connHandler
	 *            the connHandler to set
	 */
	public void setConnHandler(ConnectionHandler connHandler) {
		this.connHandler = connHandler;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
