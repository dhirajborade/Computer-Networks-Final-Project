package edu.ufl.cise.cnt5106c.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

/**
 * @author Prajakta Karandikar
 *
 */

public class CommonProperties {

	public static final String CONFIG_FILE_NAME = "Common.cfg";
	private static int numberOfPreferredNeighbors;
	private static int unchokingInterval;
	private static int optimisticUnchokingInterval;
	private static String fileName;
	private static int fileSize;
	private static int pieceSize;

	public void read(Reader reader) throws FileNotFoundException, IOException, ParseException {
		try {
			BufferedReader in = new BufferedReader(new FileReader("Common.cfg"));
			for (String line; (line = in.readLine()) != null;) {
				String[] config = line.split(" ");
				String name = config[0];
				String value = config[1];

				if (name.equals("NumberOfPreferredNeighbors")) {
					numberOfPreferredNeighbors = Integer.parseInt(value) + 1;
				} else if (name.equals("UnchokingInterval")) {
					unchokingInterval = Integer.parseInt(value);
				} else if (name.equals("OptimisticUnchokingInterval")) {
					optimisticUnchokingInterval = Integer.parseInt(value);
				} else if (name.equals("FileName")) {
					fileName = value;
				} else if (name.equals("FileSize")) {
					fileSize = Integer.parseInt(value);
				} else if (name.equals("PieceSize")) {
					pieceSize = Integer.parseInt(value);
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the numberOfPreferredNeighbors
	 */
	public static int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	/**
	 * @param numberOfPreferredNeighbors
	 *            the numberOfPreferredNeighbors to set
	 */
	public static void setNumberOfPreferredNeighbors(int numberOfPreferredNeighbors) {
		CommonProperties.numberOfPreferredNeighbors = numberOfPreferredNeighbors;
	}

	/**
	 * @return the unchokingInterval
	 */
	public static int getUnchokingInterval() {
		return unchokingInterval;
	}

	/**
	 * @param unchokingInterval
	 *            the unchokingInterval to set
	 */
	public static void setUnchokingInterval(int unchokingInterval) {
		CommonProperties.unchokingInterval = unchokingInterval;
	}

	/**
	 * @return the optimisticUnchokingInterval
	 */
	public static int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	/**
	 * @param optimisticUnchokingInterval
	 *            the optimisticUnchokingInterval to set
	 */
	public static void setOptimisticUnchokingInterval(int optimisticUnchokingInterval) {
		CommonProperties.optimisticUnchokingInterval = optimisticUnchokingInterval;
	}

	/**
	 * @return the fileName
	 */
	public static String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public static void setFileName(String fileName) {
		CommonProperties.fileName = fileName;
	}

	/**
	 * @return the fileSize
	 */
	public static int getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public static void setFileSize(int fileSize) {
		CommonProperties.fileSize = fileSize;
	}

	/**
	 * @return the pieceSize
	 */
	public static int getPieceSize() {
		return pieceSize;
	}

	/**
	 * @param pieceSize
	 *            the pieceSize to set
	 */
	public static void setPieceSize(int pieceSize) {
		CommonProperties.pieceSize = pieceSize;
	}

}
