package edu.ufl.cise.cnt5106c.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Vector;

/**
 * @author Prajakta Karandikar
 *
 */

public class PeerInfo {

	public static final String CONFIG_FILE_NAME = "PeerInfo.cfg";
	private final String COMMENT_CHAR = "#";
	private final Vector<RemotePeerInfo> peerInfoVector = new Vector<RemotePeerInfo>();

	public void read(Reader reader) throws FileNotFoundException, IOException, ParseException {
		BufferedReader in = new BufferedReader(reader);
		int i = 0;
		for (String line; (line = in.readLine()) != null;) {
			line = line.trim();
			if ((line.length() <= 0) || (line.startsWith(COMMENT_CHAR))) {
				continue;
			}
			String[] tokens = line.split("\\s+");
			if (tokens.length != 4) {
				throw new ParseException(line, i);
			}
			final boolean peerHasFile = (tokens[3].trim().compareTo("1") == 0);
			peerInfoVector
					.addElement(new RemotePeerInfo(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), peerHasFile));
			i++;
		}
	}

	public Vector<RemotePeerInfo> getPeerInfo() {
		return new Vector<RemotePeerInfo>(peerInfoVector);
	}
}