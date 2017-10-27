/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

/**
 * @author Dhiraj Borade
 *
 */
public class HavePayLoad extends PayLoad {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3777628630171683471L;

	private int index; // 4-byte piece index field we will index from -2,147,483,648 to 2,147,483,647

	/**
	 * @param index
	 */
	public HavePayLoad(int index) {
		super();
		this.index = index;
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
