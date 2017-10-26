package edu.ufl.cise.cnt5106c.message;

import java.io.Serializable;


public class Message implements Serializable {

	private static final long serialVersionUID = -2925592711545151885L;

	// Message Length in bytes excluding the length of message length field
	
	private int mLength;
	
	// Message Type
	private MessageType mType;
	
	// Message payload
	Payload mPayload;
	
	public Message(){
		
	}
	
	public Message(MessageType type, Payload payload){
		mType = type;
		mPayload = payload;
		
		if(payload == null)
			{
			mLength  = 0;
			}
		else{
			mLength = payload.getMsgLength()+1;
		}
	}
	
	public MessageType getMsgType(){
		return mType;
	}
}


