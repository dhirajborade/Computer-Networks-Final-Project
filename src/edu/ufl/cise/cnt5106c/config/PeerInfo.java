package edu.ufl.cise.cnt5106c.config;

import java.util.Collection;
import java.util.Vector;

public class PeerInfo {

	public static final String CONFIG_FILE_NAME = "PeerInfo.cfg";
	private final Vector<RemotePeerInfo> peerInfoVector = new Vector<RemotePeerInfo>();;

	public Collection<RemotePeerInfo> getPeerInfo() {
		return new Vector<>(peerInfoVector);
	}
}