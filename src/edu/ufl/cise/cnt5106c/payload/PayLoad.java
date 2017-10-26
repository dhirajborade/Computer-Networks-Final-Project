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

	public void setMsgLength(int length) {
		msgLength = length;
	}

	public int getMsgLength() {
		return msgLength;
	}

}
