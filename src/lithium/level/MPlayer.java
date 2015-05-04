package lithium.level;

import java.net.*;

import carbonserver.HeaderData;

public class MPlayer extends Player {
	
	public InetAddress 	clientAddress;
	public int 			clientPort;
	
	public MPlayer(HeaderData header, byte[] data) {
		clientAddress 	= header.ip;
		clientPort 		= header.port;
		value 			= data[0];
	}
	
	public byte[] getData() {
		byte[] arr = { value };
		return arr;
	}
	
	public void retrieveData(byte[] data) {
		value = data[0];
	}
	
}