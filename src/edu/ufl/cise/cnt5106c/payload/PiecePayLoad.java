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

	/**
	 * @param content
	 * @param index
	 */
	public PiecePayLoad(byte[] content, int index) {
		super();
		this.content = content;
		this.index = index;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

}
