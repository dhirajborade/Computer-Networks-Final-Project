/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

/**
 * @author Dhiraj Borade
 *
 */
public class PiecePayLoad extends PayLoad {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5795021442144294811L;
	// 4-byte piece index field we will index from -2,147,483,648 to 2,147,483,647
	private byte[] content = null;
	private int index = 0;

	public PiecePayLoad(byte[] content, int index) {
		this.index = index;
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	public int getIndex() {
		return index;
	}
}
