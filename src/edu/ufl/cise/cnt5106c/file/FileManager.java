/**
 * 
 */
package edu.ufl.cise.cnt5106c.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Hashtable;

import edu.ufl.cise.cnt5106c.config.CommonProperties;
import edu.ufl.cise.cnt5106c.payload.PiecePayLoad;

/**
 * @author Dhiraj Borade
 *
 */
public class FileManager {

	/** File pieces owned by the peer */
	private static boolean[] filePiecesOwned;
	/**
	 * Table to keep track of requested file pieces at any instant so that redundant
	 * requests are not made
	 */
	private static Hashtable<Integer, Integer> requestedPieces = new Hashtable<Integer, Integer>();

	/** Number of file pieces the file can be broken into */
	private static final int noOfFilePieces = (int) Math
			.ceil((double) CommonProperties.getFileSize() / CommonProperties.getPieceSize());
	/** File pieces available by the peer */
	private static int noOfPiecesAvailable = 0;
	private static String directory = null;
	private static String fileName = CommonProperties.getFileName();
	private static int fileSize = CommonProperties.getFileSize();
	private static File file = null;

	/**
	 * Constructor for the instance of FileManager
	 * 
	 * @param peerId
	 *            specifies the peerid for the file manager
	 * @param hasFile
	 *            specifies if the peer has the file
	 */
	public FileManager(int peerId, boolean hasFile) {
		directory = "peer_" + peerId + "/";

		filePiecesOwned = new boolean[noOfFilePieces];

		if (hasFile) {
			Arrays.fill(filePiecesOwned, true);
			noOfPiecesAvailable = noOfFilePieces;
		}

		File folder = new File(directory);

		if (!folder.exists()) {
			folder.mkdirs();
		}

		file = new File(directory + fileName);

		if (!file.exists()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fos.write(new byte[fileSize]);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return the filePiecesOwned
	 */
	public static boolean[] getFilePiecesOwned() {
		return filePiecesOwned;
	}

	/**
	 * @param filePiecesOwned
	 *            the filePiecesOwned to set
	 */
	public static void setFilePiecesOwned(boolean[] filePiecesOwned) {
		FileManager.filePiecesOwned = filePiecesOwned;
	}

	/**
	 * @return the requestedPieces
	 */
	public static Hashtable<Integer, Integer> getRequestedPieces() {
		return requestedPieces;
	}

	/**
	 * @param requestedPieces
	 *            the requestedPieces to set
	 */
	public static void setRequestedPieces(Hashtable<Integer, Integer> requestedPieces) {
		FileManager.requestedPieces = requestedPieces;
	}

	/**
	 * @return the noOfPiecesAvailable
	 */
	public static int getNoOfPiecesAvailable() {
		return noOfPiecesAvailable;
	}

	/**
	 * @param noOfPiecesAvailable
	 *            the noOfPiecesAvailable to set
	 */
	public static void setNoOfPiecesAvailable(int noOfPiecesAvailable) {
		FileManager.noOfPiecesAvailable = noOfPiecesAvailable;
	}

	/**
	 * @return the directory
	 */
	public static String getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public static void setDirectory(String directory) {
		FileManager.directory = directory;
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
		FileManager.fileName = fileName;
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
		FileManager.fileSize = fileSize;
	}

	/**
	 * @return the file
	 */
	public static File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public static void setFile(File file) {
		FileManager.file = file;
	}

	/**
	 * @return the nooffilepieces
	 */
	public static int getNooffilepieces() {
		return noOfFilePieces;
	}

	/**
	 * @param index
	 *            The index of the piece we need to check
	 * @return true if the piece is not available
	 */
	public static boolean isInteresting(int index) {
		return filePiecesOwned[index] ? true : false;
	}

	/**
	 * @return true if the file has all the file pieces/complete file
	 */
	public static synchronized boolean hasCompleteFile() {
		return noOfPiecesAvailable == noOfFilePieces ? true : false;
	}

	/**
	 * Creates bitfield first time during initialization for the peer
	 * 
	 * @param pieces
	 *            boolean representation of pieces available
	 * @return byte format of the pieces available
	 * @throws Exception
	 */
	public static synchronized byte[] getBitField() throws Exception {
		int size = (int) Math.ceil((double) noOfFilePieces / 8);
		byte[] bitfield = new byte[size];
		int counter = 0;
		int indexI = 0;
		while (indexI < noOfFilePieces) {
			int temp;
			if (noOfFilePieces > indexI + 8) {
				temp = indexI + 8;
			} else {
				temp = noOfFilePieces;
			}
			bitfield[counter++] = FileUtilities.boolToByte(Arrays.copyOfRange(filePiecesOwned, indexI, temp));
			indexI = indexI + 8;
		}
		return bitfield;
	}

	/**
	 * @param index
	 *            index of the file piece requested
	 * @return the file piece that was requested
	 */
	public static synchronized PiecePayLoad get(int index) {
		try {
			FileInputStream fis = new FileInputStream(file);
			int loc = CommonProperties.getPieceSize() * index;
			fis.skip(loc);
			int contentSize = CommonProperties.getPieceSize();
			if (fileSize - loc < CommonProperties.getPieceSize())
				contentSize = fileSize - loc;
			byte[] content = new byte[contentSize];
			fis.read(content);
			fis.close();
			return new PiecePayLoad(content, index);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * @param piece
	 *            The file piece that needs to be added to the file
	 * @throws Exception
	 *             If unable to add the file piece to the file
	 */
	public static synchronized void store(PiecePayLoad piece) throws Exception {
		int loc = CommonProperties.getPieceSize() * piece.getIndex();
		RandomAccessFile fos = null;
		try {
			fos = new RandomAccessFile(file, "rw");
			fos.seek(loc);
			fos.write(piece.getContent());
			fos.close();

			noOfPiecesAvailable++;
			filePiecesOwned[piece.getIndex()] = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compares bitfields of two peers to decide interesting or not interesting
	 * 
	 * @param neighborBitfield
	 *            Neighboring peer's bitfield
	 * @param bitfield
	 *            Host peer's bitfield
	 * @return
	 */
	public static boolean compareBitfields(byte[] neighborBitfield, byte[] bitfield) {
		boolean flag = false;
		int size = (int) Math.ceil((double) noOfFilePieces / 8);
		byte[] interesting = new byte[size];
		if (neighborBitfield == null) {
			return flag;
		}
		int indexI = 0;
		while (indexI < bitfield.length) {
			interesting[indexI] = (byte) ((bitfield[indexI] ^ neighborBitfield[indexI]) & neighborBitfield[indexI]);
			if (interesting[indexI] != 0) {
				flag = true;
			}
			indexI++;
		}
		return flag;
	}

	/**
	 * Randomly requests a file piece to a neighbor which it does not have and has
	 * not been requested to other peers
	 * 
	 * @param neighborBitfield
	 *            Neighboring peer's bitfield
	 * @param bitfield
	 *            Host peer's bitfield
	 * @return
	 */
	public static int requestPiece(byte[] neighborBitfield, byte[] bitfield, int nPID) {
		int size = (int) Math.ceil((double) noOfFilePieces / 8);
		byte[] interesting = new byte[size];
		boolean[] interestingPieces = new boolean[noOfFilePieces];
		int finLength;

		finLength = size > 1 ? noOfFilePieces % (8) : noOfFilePieces;

		int start, end;

		int indexI = 0, indexJ = 0;
		while (indexI < bitfield.length) {
			interesting[indexI] = (byte) ((bitfield[indexI] ^ neighborBitfield[indexI]) & neighborBitfield[indexI]);
			start = indexI == size - 1 ? 8 - finLength : 0;
			end = indexI == size - 1 ? finLength : 8;
			boolean[] x = FileUtilities.byteToBoolean(interesting[indexI]);
			System.arraycopy(x, start, interestingPieces, indexJ, end);
			indexJ = indexJ + 8 < noOfFilePieces ? indexJ + 8 : noOfFilePieces - finLength;
			indexI++;
		}
		int indexK = 0;
		while (indexK < noOfFilePieces) {
			if (interestingPieces[indexK] == true && !requestedPieces.containsKey(indexK)) {
				requestedPieces.put(indexK, indexK);
				return indexK;
			}
			indexK++;
		}
		return -1;
	}

	public static void checker() {

		(new Thread() {
			@Override
			public void run() {
				try {
					do {
						Thread.sleep(60000);
						for (Integer ind : requestedPieces.keySet()) {
							if (!filePiecesOwned[ind])
								requestedPieces.remove(ind);
						}
					} while (noOfPiecesAvailable < noOfFilePieces);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
