package edu.ufl.cise.cnt5106c.message;

import java.io.Serializable;

import edu.ufl.cise.cnt5106c.payload.PayLoad;

/**
 * @author Prajakta Karandikar
 *
 */

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2925592711545151885L;

	// Message Length in bytes excluding the length of message length field
	private int msgLength;
	private MessageType msgType;
	private PayLoad msgPayload;

	public Message() {

	}

	public Message(MessageType type, PayLoad payload) {
		this.msgType = type;
		this.msgPayload = payload;
		if (payload == null) {
			this.msgLength = 0;
		} else {
			this.msgLength = payload.getMsgLength() + 1;
		}
	}

	public int getMsgLength() {
		return this.msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public MessageType getMsgType() {
		return this.msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	public PayLoad getMsgPayload() {
		return this.msgPayload;
	}

	public void setMsgPayload(PayLoad msgPayload) {
		this.msgPayload = msgPayload;
	}
}