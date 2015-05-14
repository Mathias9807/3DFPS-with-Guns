package lithium.level;

import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	public int			modelIndex;
	
	public byte value;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
		modelIndex 		= 0;
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
