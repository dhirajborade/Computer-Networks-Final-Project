package edu.ufl.cise.cnt5106c.message;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2925592711545151885L;

	// Message Length in bytes excluding the length of message length field
	private int msgLength;
	private MessageType msgType;
	private PayloadTemp msgPayload;
	
	public Message() {
		
	}

	public Message(MessageType type, PayloadTemp payload) {
		this.msgType = type;
		this.msgPayload = payload;
		if (payload == null) {
			this.msgLength = 0;
		} else {
			this.msgLength = payload.getMsgLength() + 1;
		}
	}

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public MessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	public PayloadTemp getMsgPayload() {
		return msgPayload;
	}

	public void setMsgPayload(PayloadTemp msgPayload) {
		this.msgPayload = msgPayload;
	}
}