package lithium.level;

import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import carbon.CarbonClient;
import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	public int			modelIndex;
	
	public Vector3f 	pos;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
		modelIndex 		= 0;
		pos 			= new Vector3f();
		retrieveData(data);
	}
	
	public byte[] getData() {
		byte[] sum;
		sum = CarbonClient.addArrays(getByteArray(pos.x), getByteArray(pos.y));
		sum = CarbonClient.addArrays(sum, getByteArray(pos.z));
		return sum;
	}
	
	public void retrieveData(byte[] data) {
		if (data == null) return;
		pos.x = getFloat(Arrays.copyOfRange(data, 0, 4));
		pos.y = getFloat(Arrays.copyOfRange(data, 4, 8));
		pos.z = getFloat(Arrays.copyOfRange(data, 8, 12));
	}
	
	public static byte[] getByteArray(float f) {
		return java.nio.ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
	}
	
	public static float getFloat(byte[] b) {
		return java.nio.ByteBuffer.allocate(Float.BYTES).put(b).getFloat(0);
	}
	
}
