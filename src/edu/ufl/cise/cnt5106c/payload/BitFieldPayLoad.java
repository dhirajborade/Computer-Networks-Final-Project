/**
 * 
 */
package edu.ufl.cise.cnt5106c.payload;

/**
 * @author Dhiraj Borade
 *
 */
public class BitFieldPayLoad extends PayLoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052696107658542567L;

	// Representing file pieces present in the corresponding bits
	// A four byte field
	private byte[] bitfield;

	public BitFieldPayLoad(byte[] bitfield) {
		this.bitfield = bitfield;
	}

	public byte[] getBitfield() {
		return bitfield;
	}
}
