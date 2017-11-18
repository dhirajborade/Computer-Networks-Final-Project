/**
 * 
 */
package edu.ufl.cise.cnt5106c.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Dhiraj Borade
 *
 */
public class Logger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	private static PrintWriter logfile;
	private static int peerID;

	public Logger(int pID) {
		try {
			peerID = pID;
			logfile = new PrintWriter("log_peer_" + pID + ".log");

		} catch (FileNotFoundException e) {
			System.out.println("Not able to create log writer");
		}
	}

	public static void makeconnection(int pID) {
		try {
			logfile.println("Peer " + peerID + " makes a connection to Peer " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void connect(int pID) {
		try {
			logfile.println("Peer " + peerID + " is connected from Peer " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void connected(int pID) {
		try {
			logfile.println("Peer " + peerID + " is connected from Peer " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void preferredNeighbors(ArrayList<Integer> neighbors) {
		try {
			logfile.println("Peer " + peerID + " has the preferred neighbors " + neighbors);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void optUnchoke(int pID) {
		try {
			logfile.println("Peer " + peerID + " has the optimistically-unchocked neighbor " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void peerUnchoked(int pID) {
		try {
			logfile.println("Peer " + peerID + " is unchoked by " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void peerChoked(int pID) {
		try {
			logfile.println("Peer " + peerID + " is choked by " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void haveFileMsgRecieved(int pID, int index) {
		try {
			logfile.println("Peer " + peerID + " received a \'have\' message from " + pID + "for the piece " + index);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void notInterestedMsgRecieved(int pID) {
		try {
			logfile.println("Peer " + peerID + " received a \'not interested\' message from " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void interestedMsgRecieved(int pID) {
		try {
			logfile.println("Peer " + peerID + " received a \'interested\' message from " + pID);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fileDownloading(int pID, int index, int count) {
		try {
			logfile.println("Peer " + peerID + " has downloaded the piece " + index + " from " + pID
					+ ".\nNow the number of pieces it has is " + count);
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fileDownloadCompleted() {
		try {
			logfile.println("Peer " + peerID + " has downloaded the complete file.");
			logfile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
