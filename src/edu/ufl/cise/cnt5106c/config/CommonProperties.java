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
			while ((line = in.readLine()) != null) {
				String[] config = line.split(" ");
				String name = config[0];
				String value = config[1];

				switch (name) {
				case "NumberOfPreferredNeighbors":
					numberOfPreferredNeighbors = Integer.parseInt(value) + 1;
					break;
				case "UnchokingInterval":
					unchokingInterval = Integer.parseInt(value);
					break;
				case "OptimisticUnchokingInterval":
					optimisticUnchokingInterval = Integer.parseInt(value);
					break;
				case "FileName":
					fileName = value;
					break;
				case "FileSize":
					fileSize = Integer.parseInt(value);
					break;
				case "PieceSize":
					pieceSize = Integer.parseInt(value);
					break;
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
