package edu.ufl.cise.cnt5106c.start;

/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import edu.ufl.cise.cnt5106c.config.PeerInfo;
import edu.ufl.cise.cnt5106c.config.RemotePeerInfo;

/*
 * The StartRemotePeers class begins remote peer processes. 
 * It reads configuration file PeerInfo.cfg and starts remote peer processes.
 * You must modify this program a little bit if your peer processes are written in C or C++.
 * Please look at the lines below the comment saying IMPORTANT.
 */
public class StartRemotePeers {

	public Vector<RemotePeerInfo> peerInfoVector;

	public void getConfiguration(String[] inputArgs) {
		peerInfoVector = new Vector<RemotePeerInfo>();
		final String configFile = (inputArgs.length == 0 ? PeerInfo.CONFIG_FILE_NAME : inputArgs[0]);
		FileReader inputFileReader = null;
		try {
			inputFileReader = new FileReader(configFile);
			BufferedReader inputBufferedReader = new BufferedReader(inputFileReader);
			PeerInfo peerInfo = new PeerInfo();
			peerInfo.read(inputBufferedReader);
			peerInfoVector = peerInfo.getPeerInfo();
			inputBufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputFileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			StartRemotePeers myStart = new StartRemotePeers();
			myStart.getConfiguration(args);

			// get current path
			String path = System.getProperty("user.dir");

			// start clients at remote hosts
			for (int i = 0; i < myStart.peerInfoVector.size(); i++) {
				RemotePeerInfo pInfo = (RemotePeerInfo) myStart.peerInfoVector.elementAt(i);
				System.out.println("Start remote peer " + pInfo.getPeerId() + " at " + pInfo.getPeerAddress());
				Runtime.getRuntime().exec(
						"ssh " + pInfo.getPeerAddress() + " cd " + path + "; java PeerProcess " + pInfo.getPeerId());
			}
			System.out.println("Starting all remote peers has done.");

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
