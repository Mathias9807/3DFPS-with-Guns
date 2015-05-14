package lithium.level;

import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	
	public byte value;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
		retrieveData(data);
	}
	
	public byte[] getData() {
		byte[] arr = { value };
		return arr;
	}
	
	public void retrieveData(byte[] data) {
		if (data == null) return;
		value = data[0];
	}
	
}
