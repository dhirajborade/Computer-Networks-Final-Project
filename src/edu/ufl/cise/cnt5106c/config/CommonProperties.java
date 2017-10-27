package edu.ufl.cise.cnt5106c.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommonProperties {

	private static int numberOfPreferredNeighbors;
	private static int unchokingInterval;
	private static int optimisticUnchokingInterval;
	private static String fileName;
	private static int fileSize;
	private static int pieceSize;

	static {
		try {
			BufferedReader in = new BufferedReader(new FileReader("Common.cfg"));
			String line;
			for (line = in.readLine(); line != null;) {
				String[] config = line.split(" ");
				String name = config[0];
				String value = config[1];

				if (name == "NumberOfPreferredNeighbors") {
					numberOfPreferredNeighbors = Integer.parseInt(value) + 1;
				} else if (name == "UnchokingInterval") {
					unchokingInterval = Integer.parseInt(value);
				} else if (name == "OptimisticUnchokingInterval") {
					optimisticUnchokingInterval = Integer.parseInt(value);
				} else if (name == "FileName") {
					fileName = value;
				} else if (name == "FileSize") {
					fileSize = Integer.parseInt(value);
				} else if (name == "PieceSize") {
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
