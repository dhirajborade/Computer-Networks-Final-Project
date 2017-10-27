package edu.ufl.cise.cnt5106c.config;

/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RemotePeerInfo {
	private final String peerId;
	private final String peerAddress;
	private final String peerPort;
	private final boolean hasFile;
	private AtomicInteger bytesDownloadedFrom;
	private BitSet receivedParts;
	private final AtomicBoolean interested;

	/**
	 * @param peerId
	 */
	public RemotePeerInfo(int peerId) {
		this(Integer.toString(peerId), "127.0.0.1", "0", false);
	}

	/**
	 * @param peerId
	 * @param peerAddress
	 * @param peerPort
	 * @param hasFile
	 * @param bytesDownloadedFrom
	 * @param receivedParts
	 * @param interested
	 */
	public RemotePeerInfo(String peerId, String peerAddress, String peerPort, boolean hasFile) {
		super();
		this.peerId = peerId;
		this.peerAddress = peerAddress;
		this.peerPort = peerPort;
		this.hasFile = hasFile;
		this.bytesDownloadedFrom = new AtomicInteger(0);
		this.receivedParts = new BitSet();
		this.interested = new AtomicBoolean(false);
	}

	/**
	 * @return the bytesDownloadedFrom
	 */
	public AtomicInteger getBytesDownloadedFrom() {
		return bytesDownloadedFrom;
	}

	/**
	 * @param bytesDownloadedFrom
	 *            the bytesDownloadedFrom to set
	 */
	public void setBytesDownloadedFrom(AtomicInteger bytesDownloadedFrom) {
		this.bytesDownloadedFrom = bytesDownloadedFrom;
	}

	/**
	 * @return the receivedParts
	 */
	public BitSet getReceivedParts() {
		return receivedParts;
	}

	/**
	 * @param receivedParts
	 *            the receivedParts to set
	 */
	public void setReceivedParts(BitSet receivedParts) {
		this.receivedParts = receivedParts;
	}

	/**
	 * @return the peerId
	 */
	public int getPeerId() {
		return Integer.parseInt(peerId);
	}

	/**
	 * @return the peerAddress
	 */
	public String getPeerAddress() {
		return peerAddress;
	}

	/**
	 * @return the peerPort
	 */
	public int getPeerPort() {
		return Integer.parseInt(peerPort);
	}

	/**
	 * @return the hasFile
	 */
	public boolean isHasFile() {
		return hasFile;
	}

	/**
	 * @return the interested
	 */
	public boolean isInterested() {
		return interested.get();
	}

	public void setInterested() {
		interested.set(true);
	}

	public void setNotIterested() {
		interested.set(false);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof RemotePeerInfo) {
			return (((RemotePeerInfo) obj).peerId.equals(this.peerId));
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(this.peerId);
		return hash;
	}

	@Override
	public String toString() {
		return new StringBuilder(this.peerId).append(" Address: ").append(peerAddress).append(" Port: ")
				.append(this.peerPort).append(" Has File: ").append(this.hasFile).toString();
	}

	public static Collection<Integer> toIdSet(Collection<RemotePeerInfo> peers) {
		Set<Integer> peerIDs = new HashSet<>();
		for (RemotePeerInfo peer : peers) {
			peerIDs.add(peer.getPeerId());
		}
		return peerIDs;
	}
}
