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

	public static int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	public static int getUnchokingInterval() {
		return unchokingInterval;
	}

	public static int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	public static String getFileName() {
		return fileName;
	}

	public static int getFileSize() {
		return fileSize;
	}

	public static int getPieceSize() {
		return pieceSize;
	}
}
