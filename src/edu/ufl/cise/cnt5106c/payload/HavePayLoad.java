/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

import edu.ufl.cise.cnt5106c.message.PayloadTemp;

/**
 * @author Dhiraj Borade
 *
 */
public class HavePayLoad extends PayloadTemp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3777628630171683471L;

	private int index; // 4-byte piece index field we will index from -2,147,483,648 to 2,147,483,647

	public HavePayLoad(int idx) {
		this.index = idx;
	}

	public int getIndex() {
		return this.index;
	}
}
