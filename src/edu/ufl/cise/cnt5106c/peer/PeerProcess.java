package edu.ufl.cise.cnt5106c.peer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import edu.ufl.cise.cnt5106c.config.CommonProperties;
import edu.ufl.cise.cnt5106c.config.PeerInfo;
import edu.ufl.cise.cnt5106c.config.RemotePeerInfo;

public class PeerProcess {

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		final int peerId = Integer.parseInt(args[0]);
		String ipAddress = "localhost";
		int portNumber = 6008;
		boolean hasFile = false;
		Reader commonPropertiesReader = null;
		Reader peerInfoReader = null;
		PeerInfo peerInfo = new PeerInfo();
		CommonProperties commInfo = new CommonProperties();
		Vector<RemotePeerInfo> peersToConnectTo = new Vector<RemotePeerInfo>();
		try {
			commonPropertiesReader = new FileReader(CommonProperties.CONFIG_FILE_NAME);
			peerInfoReader = new FileReader(PeerInfo.CONFIG_FILE_NAME);
			commInfo.read(commonPropertiesReader);
			peerInfo.read(peerInfoReader);
			for (RemotePeerInfo peer : peerInfo.getPeerInfo()) {
				if (peerId == peer.getPeerId()) {
					ipAddress = peer.getPeerAddress();
					portNumber = peer.getPeerPort();
					hasFile = peer.isHasFile();
					break;
				} else {
					peersToConnectTo.add(peer);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return;
		} finally {
			try {
				commonPropertiesReader.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			try {
				peerInfoReader.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		Process peerProcess = new Process(peerId, ipAddress, portNumber, hasFile, commInfo);
		peerProcess.initialize(peerProcess);
//		Thread t = new Thread(peerProcess);
//		t.setName("peerProcess-" + peerId);
//		t.start();

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
