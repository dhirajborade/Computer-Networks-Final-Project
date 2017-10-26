package edu.ufl.cise.cnt5106c.message;

enum MessageType{
	
	CHOKE(0), UNCHOKE(1), INTERESTED(2), NOT_INTERESTED(3), HAVE(4),
	BITFIELD(5), REQUEST(6), PIECE(7);
	
	private byte val;
	
	MessageType(int val){
		this.val = (byte)val;
	}
	
	public byte getVal(){
		return val;
	 }
	}