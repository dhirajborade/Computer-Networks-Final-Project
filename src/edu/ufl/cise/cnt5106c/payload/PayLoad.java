/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

import java.io.Serializable;

/**
 * @author Dhiraj Borade
 *
 */
public abstract class PayLoad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5424746124279275035L;
	private int msgLength = 0;

	/**
	 * 
	 */
	public PayLoad() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msgLength
	 */
	public PayLoad(int msgLength) {
		super();
		this.msgLength = msgLength;
	}

	/**
	 * @return the msgLength
	 */
	public int getMsgLength() {
		return msgLength;
	}

	/**
	 * @param msgLength
	 *            the msgLength to set
	 */
	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

}
