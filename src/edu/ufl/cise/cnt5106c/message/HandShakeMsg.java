package edu.ufl.cise.cnt5106c.message;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class HandShakeMsg implements Serializable  {
	
	private static final long serialVersionUID = -8034174401138365022L;

	static final String Header = "P2PFILESHARINGPROJ";
	private final byte[] zeroBits = new byte[10];
	private final byte[] peerID = new byte[4];
	
	
	public int getPeerId() {
        return ByteBuffer.wrap(peerID).order(ByteOrder.BIG_ENDIAN).getInt();
    }
	
	public String getHeader(){
		return Header;
	}
	
	private HandShakeMsg (byte[] peerId) {
        if (peerId.length > 4) {
            throw new ArrayIndexOutOfBoundsException("peerId max length is 4, while "
                    + Arrays.toString (peerId) + "'s length is "+ peerID.length);
        }
        int i = 0;
        for (byte b : peerId) {
            peerID[i++] = b;    
        }
    }
	
	@Override
    public String toString()
    {
        String msgString = "Header :"+Header+"\n";
        msgString = msgString+"Peer ID: "+peerID; 
        return msgString;
    }
	
	public void SendHandShake (OutputStream out) throws IOException 
	{  
		ObjectOutputStream ostream = new ObjectOutputStream(out);  			  
		ostream.writeObject(this);
		System.out.println("sending handshake message with peer" + this.peerID);
	}
	
	//return value could be changed to HandShakeMsg if header is also needed
	public HandShakeMsg ReceiveHandShake (InputStream in) throws IOException 
	{
		try 
		{
			ObjectInputStream istream = new ObjectInputStream(in);  
			HandShakeMsg Response = (HandShakeMsg)istream.readObject();  
			if (Response != null) 
			{		
				return Response;	
			}
			else {
				return null;
			}	
		} 
		catch (ClassNotFoundException ex) {
			System.out.println(ex);
		}
		finally {
			//close streams
		}
		return null;
	}
}
