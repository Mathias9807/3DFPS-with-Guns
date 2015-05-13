package lithium.level;

import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	
	public byte value;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
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
