/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

/**
 * @author Dhiraj Borade
 *
 */
public class RequestPayLoad extends PayLoad {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9208727206802180498L;
	// 4-byte piece index field we will index from -2,147,483,648 to 2,147,483,647
	private int index;

	public RequestPayLoad(int idx) {
		this.index = idx;
	}

	public int getIndex() {
		return this.index;
	}
}
