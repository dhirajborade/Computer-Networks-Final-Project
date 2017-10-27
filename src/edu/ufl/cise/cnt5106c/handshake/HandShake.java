/**
 * 
 */
package edu.ufl.cise.cnt5106c.handshake;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author Dhiraj Borade
 *
 */
public class HandShake implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1482860868859618509L;
	private static final String Header = "P2PFILESHARINGPROJ";
	private final String peerMsgHeader;
	private int peerID;

	/**
	 * @param peerID
	 */
	public HandShake(int peerID) {
		super();
		this.peerID = peerID;
		this.peerMsgHeader = getHeader();
	}

	/**
	 * @return the peerID
	 */
	public int getPeerID() {
		return peerID;
	}

	/**
	 * @param peerID
	 *            the peerID to set
	 */
	public void setPeerID(int peerID) {
		this.peerID = peerID;
	}

	/**
	 * @return the peerMsgHeader
	 */
	public String getPeerMsgHeader() {
		return peerMsgHeader;
	}

	/**
	 * @return the header
	 */
	public static String getHeader() {
		return Header;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("[Header :").append(getHeader()).append("]\n").append("[Peer ID: ").append(this.peerID).append("]")
				.toString();
	}

	public void SendHandShake(OutputStream out) throws IOException {
		ObjectOutputStream opStream = new ObjectOutputStream(out);
		opStream.writeObject(this);
		System.out.println("Sending handshake message to peer " + this.peerID);
	}

	// return value could be changed to HandShakeMsg if header is also needed
	public int ReceiveHandShake(InputStream in) throws IOException {
		try {
			ObjectInputStream ipStream = new ObjectInputStream(in);
			HandShake Response = (HandShake) ipStream.readObject();
			return Response != null ? Response.peerID : -1;
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		return -1;
	}
}
