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
	public final String peerId;
	public final String peerAddress;
	public final String peerPort;
	public final boolean hasFile;
	public AtomicInteger bytesDownloadedFrom;
	public BitSet receivedParts;
	private final AtomicBoolean interested;

	public RemotePeerInfo(int peerId) {
		this(Integer.toString(peerId), "127.0.0.1", "0", false);
	}

	public RemotePeerInfo(String peerId, String peerAddress, String peerPort, boolean hasFile) {
		this.peerId = peerId;
		this.peerAddress = peerAddress;
		this.peerPort = peerPort;
		this.hasFile = hasFile;
		this.bytesDownloadedFrom = new AtomicInteger(0);
		this.receivedParts = new BitSet();
		this.interested = new AtomicBoolean(false);
	}

	public int getPeerId() {
		return Integer.parseInt(this.peerId);
	}

	public int getPort() {
		return Integer.parseInt(this.peerPort);
	}

	public String getPeerAddress() {
		return this.peerAddress;
	}

	public boolean hasFile() {
		return this.hasFile;
	}

	public boolean isInterested() {
		return this.interested.get();
	}

	public void setInterested() {
		this.interested.set(true);
	}

	public void setNotIterested() {
		this.interested.set(false);
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
		Set<Integer> ids = new HashSet<>();
		for (RemotePeerInfo peer : peers) {
			ids.add(peer.getPeerId());
		}
		return ids;
	}
}
